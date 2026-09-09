const FREE_VISION_MODEL = "google/gemma-4-26b-a4b-it:free";
const PAID_FALLBACK_MODEL = "google/gemini-2.5-flash";
const MAX_IMAGE_CHARS = 1_800_000;

const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
  "Access-Control-Allow-Headers": "Content-Type, Authorization",
};

function jsonResponse(body: unknown, status: number): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { ...corsHeaders, "Content-Type": "application/json" },
  });
}

function cleanExpression(value: string): string {
  let expression = value.trim().replace(/```[a-z]*|```/gi, "").trim();
  expression = expression.split("\n")[0].trim();
  expression = expression.replace(/^f\s*\(\s*x\s*\)\s*=\s*/i, "");
  expression = expression.replace(/^y\s*=\s*/i, "");
  expression = expression.replace(/^["'`]|["'`]$/g, "").trim();
  return expression.slice(0, 300);
}

export default async function (req: Request): Promise<Response> {
  if (req.method === "OPTIONS") return new Response(null, { status: 204, headers: corsHeaders });
  if (req.method !== "POST") return jsonResponse({ error: "Only POST is supported" }, 405);

  try {
    const body = await req.json().catch(() => ({}));
    const imageData = typeof body?.image_data === "string" ? body.image_data : "";
    const method = typeof body?.method === "string" ? body.method : "numerico";
    if (!imageData.startsWith("data:image/png;base64,") || imageData.length > MAX_IMAGE_CHARS) {
      return jsonResponse({ error: "La imagen no es válida o es demasiado grande." }, 413);
    }

    const baseUrl = Deno.env.get("INSFORGE_BASE_URL");
    const anonKey = Deno.env.get("ANON_KEY");
    const apiKey = Deno.env.get("OPENROUTER_API_KEY");
    if (!baseUrl || !anonKey || !apiKey) return jsonResponse({ error: "AI backend not configured" }, 500);

    const { createClient } = await import("npm:@insforge/sdk");
    const client = createClient({ baseUrl, anonKey });
    const quotaResult = await client.database.rpc("consume_ai_quota", {
      p_quota_key: `draw|${req.headers.get("x-forwarded-for") || "unknown"}`,
      p_now: new Date().toISOString(),
    });
    const quota = Array.isArray(quotaResult.data) ? quotaResult.data[0] : quotaResult.data;
    if (quotaResult.error) return jsonResponse({ error: "No se pudo verificar el límite de uso." }, 503);
    if (!quota?.allowed) return jsonResponse({ error: "Has alcanzado el límite de interpretaciones." }, 429);

    const messages = [
      {
        role: "system",
        content: "Eres un lector de funciones matemáticas manuscritas. Devuelve SOLO la expresión de f(x), sin explicación, sin LaTeX, sin f(x)= y sin comillas. Usa sintaxis compatible con mXparser: x, números, + - * / ^, sin, cos, tan, log, ln, sqrt y exp. Si no puedes leerla con seguridad, devuelve INDETERMINADA.",
      },
      {
        role: "user",
        content: [
          { type: "text", text: `Interpreta esta función para el método ${method}. Devuelve una sola expresión.` },
          { type: "image_url", image_url: { url: imageData } },
        ],
      },
    ];

    const request = (model: string) => fetch("https://openrouter.ai/api/v1/chat/completions", {
      method: "POST",
      headers: { Authorization: `Bearer ${apiKey}`, "Content-Type": "application/json" },
      body: JSON.stringify({ model, messages, max_tokens: 120, temperature: 0 }),
    });

    let response = await request(FREE_VISION_MODEL);
    let model = FREE_VISION_MODEL;
    if (!response.ok) {
      response = await request(PAID_FALLBACK_MODEL);
      model = PAID_FALLBACK_MODEL;
    }
    if (!response.ok) return jsonResponse({ error: `No se pudo interpretar el dibujo (${response.status}).` }, 502);

    const data = await response.json();
    const rawText = typeof data?.choices?.[0]?.message?.content === "string"
      ? data.choices[0].message.content : "";
    const expression = cleanExpression(rawText);
    if (!expression || expression.toUpperCase() === "INDETERMINADA") {
      return jsonResponse({ error: "No pude leer la función con suficiente seguridad." }, 422);
    }
    return jsonResponse({ expression, model }, 200);
  } catch (error) {
    return jsonResponse({ error: error instanceof Error ? error.message : "Unknown error" }, 500);
  }
}
