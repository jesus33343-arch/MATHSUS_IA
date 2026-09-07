-- 👍/👎 on individual AI chat answers, same anonymous-insert-only pattern as
-- calculation_satisfaction. Lets the developer see which AI answers students
-- and domain experts found useful without identifying who they are.

CREATE TABLE public.ai_response_satisfaction (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  ai_query_id UUID NOT NULL REFERENCES public.ai_queries(id) ON DELETE CASCADE,
  device_id UUID NOT NULL,
  satisfied BOOLEAN NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE public.ai_response_satisfaction ENABLE ROW LEVEL SECURITY;

REVOKE SELECT, UPDATE, DELETE ON public.ai_response_satisfaction FROM anon, authenticated;

GRANT INSERT ON public.ai_response_satisfaction TO anon, authenticated;

CREATE POLICY "anyone can rate an ai response" ON public.ai_response_satisfaction
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE INDEX idx_ai_response_satisfaction_query ON public.ai_response_satisfaction (ai_query_id);
