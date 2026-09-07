const SYSTEM_CONTEXT =
  "Eres un tutor de métodos numéricos para resolver ecuaciones de la forma f(x) = 0, " +
  "integrado en la app educativa MATHSUS. La app implementa cuatro métodos: bisección, regla falsa (falsi), " +
  "Newton-Raphson y secante. Responde siempre en español, de forma clara, breve y pedagógica, mostrando pasos " +
  "cuando ayude a entender el procedimiento. Si la pregunta no tiene relación con matemáticas o métodos numéricos, " +
  "indícalo amablemente y redirige la conversación hacia el tema de la app.\n\n" +
  "Formato matemático obligatorio: toda expresión, ecuación o fórmula matemática debe escribirse en LaTeX, " +
  "nunca en texto plano ni con paréntesis/asteriscos como sustituto. El motor de renderizado SOLO reconoce " +
  "doble signo de pesos '$$...$$' como delimitador matemático (NUNCA un solo '$'; un solo '$' no se procesa y " +
  "queda como texto literal). Usa '$$...$$' tanto para una fórmula corta dentro de una frase (por ejemplo: la " +
  "derivada $$f'(x)$$ se evalúa en...) como para fórmulas o pasos importantes en su propia línea (por ejemplo: " +
  "$$x_{n+1} = x_n - \\frac{f(x_n)}{f'(x_n)}$$). No dejes ninguna fórmula, variable con subíndice/exponente, o " +
  "símbolo matemático sin envolver en '$$'. Los dos signos de '$$' deben quedar SIEMPRE juntos, sin ningún " +
  "espacio ni salto de línea entre ellos, tanto al abrir como al cerrar (nunca escribas '$' seguido de un salto " +
  "de línea y luego otro '$').";

const DEFAULT_MODEL = "google/gemini-2.5-flash";
const MAX_HISTORY_TURNS = 12;

export default async function (req: Request): Promise<Response> {
  const corsHeaders = {
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type, Authorization",
  };

  if (req.method === "OPTIONS") {
    return new Response(null, { status: 204, headers: corsHeaders });
  }

  try {
    const body = await req.json().catch(() => ({}));
    const prompt = typeof body?.prompt === "string" ? body.prompt.trim() : "";

    if (!prompt) {
      return new Response(JSON.stringify({ error: "Missing 'prompt'" }), {
        status: 400,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      });
    }

    const rawHistory = Array.isArray(body?.history) ? body.history : [];
    const history = rawHistory
      .filter(
        (turn: unknown): turn is { role: string; content: string } =>
          !!turn &&
          typeof (turn as { role?: unknown }).role === "string" &&
          typeof (turn as { content?: unknown }).content === "string" &&
          ((turn as { role: string }).role === "user" || (turn as { role: string }).role === "assistant")
      )
      .slice(-MAX_HISTORY_TURNS * 2)
      .map((turn: { role: string; content: string }) => ({ role: turn.role, content: turn.content }));

    const apiKey = Deno.env.get("OPENROUTER_API_KEY");
    if (!apiKey) {
      return new Response(JSON.stringify({ error: "AI backend not configured" }), {
        status: 500,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      });
    }

    const model = Deno.env.get("AI_MODEL_NAME") || DEFAULT_MODEL;

    const orResponse = await fetch("https://openrouter.ai/api/v1/chat/completions", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${apiKey}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        model,
        messages: [
          { role: "system", content: SYSTEM_CONTEXT },
          ...history,
          { role: "user", content: prompt },
        ],
      }),
    });

    if (!orResponse.ok) {
      const detail = await orResponse.text();
      return new Response(
        JSON.stringify({ error: `Modelo no disponible (${orResponse.status})`, detail }),
        { status: 502, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      );
    }

    const data = await orResponse.json();
    const text = data?.choices?.[0]?.message?.content;

    if (!text) {
      return new Response(JSON.stringify({ error: "El modelo no devolvió una respuesta" }), {
        status: 502,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      });
    }

    return new Response(JSON.stringify({ text, model }), {
      status: 200,
      headers: { ...corsHeaders, "Content-Type": "application/json" },
    });
  } catch (e) {
    return new Response(
      JSON.stringify({ error: e instanceof Error ? e.message : "Unknown error" }),
      { status: 500, headers: { ...corsHeaders, "Content-Type": "application/json" } }
    );
  }
}
