-- Lets the app reload a device's own AI chat history without opening a broad
-- SELECT policy on ai_queries (which would let any anon client read every
-- device's conversations). SECURITY DEFINER + an explicit device_id filter
-- means the function itself is the only thing that can see across rows;
-- callers only ever get back rows matching the device_id they pass in.

CREATE OR REPLACE FUNCTION public.get_conversation_history(p_device_id uuid, p_limit int DEFAULT 100)
RETURNS SETOF public.ai_queries
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = pg_catalog, public, pg_temp
AS $$
  SELECT *
  FROM public.ai_queries
  WHERE device_id = p_device_id
  ORDER BY created_at ASC
  LIMIT p_limit;
$$;

GRANT EXECUTE ON FUNCTION public.get_conversation_history(uuid, int) TO anon, authenticated;
