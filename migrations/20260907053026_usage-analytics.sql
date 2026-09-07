-- Anonymous usage analytics: which numerical method students use, what they
-- solved, whether they were satisfied with the result, and (optionally) a
-- comment when they think the app got an exercise wrong. There is no login —
-- "device_id" is a random UUID generated once on-device and sent with every
-- event, only good for grouping/aggregation, never identifies a real person.
-- Every table here is anonymous-insert-only, same pattern as user_feedback /
-- ai_response_reports: no SELECT/UPDATE/DELETE from the client, only from the
-- project owner via dashboard/CLI.

CREATE TABLE public.calculation_events (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID NOT NULL,
  method TEXT NOT NULL CHECK (method IN ('bisection', 'falsi', 'newton', 'secante')),
  function_expr TEXT NOT NULL CHECK (char_length(function_expr) BETWEEN 1 AND 500),
  params JSONB NOT NULL,
  root_value DOUBLE PRECISION,
  iterations INTEGER,
  converged BOOLEAN NOT NULL DEFAULT true,
  exercise_label TEXT,
  locale_country TEXT,
  timezone TEXT,
  app_version TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE public.calculation_satisfaction (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  calculation_event_id UUID NOT NULL REFERENCES public.calculation_events(id) ON DELETE CASCADE,
  device_id UUID NOT NULL,
  satisfied BOOLEAN NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE public.exercise_comments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  calculation_event_id UUID REFERENCES public.calculation_events(id) ON DELETE CASCADE,
  device_id UUID NOT NULL,
  comment TEXT NOT NULL CHECK (char_length(comment) BETWEEN 1 AND 2000),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE public.ai_queries (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID NOT NULL,
  model_name TEXT NOT NULL,
  prompt TEXT NOT NULL CHECK (char_length(prompt) BETWEEN 1 AND 4000),
  response TEXT,
  method_context TEXT,
  locale_country TEXT,
  timezone TEXT,
  app_version TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE public.calculation_events ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.calculation_satisfaction ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.exercise_comments ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_queries ENABLE ROW LEVEL SECURITY;

REVOKE SELECT, UPDATE, DELETE ON public.calculation_events FROM anon, authenticated;
REVOKE SELECT, UPDATE, DELETE ON public.calculation_satisfaction FROM anon, authenticated;
REVOKE SELECT, UPDATE, DELETE ON public.exercise_comments FROM anon, authenticated;
REVOKE SELECT, UPDATE, DELETE ON public.ai_queries FROM anon, authenticated;

GRANT USAGE ON SCHEMA public TO anon, authenticated;
GRANT INSERT ON public.calculation_events TO anon, authenticated;
GRANT INSERT ON public.calculation_satisfaction TO anon, authenticated;
GRANT INSERT ON public.exercise_comments TO anon, authenticated;
GRANT INSERT ON public.ai_queries TO anon, authenticated;

CREATE POLICY "anyone can log a calculation" ON public.calculation_events
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE POLICY "anyone can rate a calculation" ON public.calculation_satisfaction
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE POLICY "anyone can comment on an exercise" ON public.exercise_comments
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE POLICY "anyone can log an ai query" ON public.ai_queries
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE INDEX idx_calculation_events_method ON public.calculation_events (method);
CREATE INDEX idx_calculation_events_device ON public.calculation_events (device_id);
CREATE INDEX idx_calculation_events_created_at ON public.calculation_events (created_at DESC);
CREATE INDEX idx_calculation_satisfaction_event ON public.calculation_satisfaction (calculation_event_id);
CREATE INDEX idx_exercise_comments_event ON public.exercise_comments (calculation_event_id);
CREATE INDEX idx_ai_queries_device ON public.ai_queries (device_id);
CREATE INDEX idx_ai_queries_created_at ON public.ai_queries (created_at DESC);
