-- Feedback from users + reports of inappropriate/incorrect AI responses.
-- Both tables are anonymous public-insert only: the app has no auth system,
-- so any authenticated user is treated the same as anon for these tables.

CREATE TABLE public.user_feedback (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  rating SMALLINT CHECK (rating BETWEEN 1 AND 5),
  category TEXT NOT NULL DEFAULT 'general' CHECK (category IN ('bug', 'sugerencia', 'general')),
  message TEXT NOT NULL CHECK (char_length(message) BETWEEN 1 AND 4000),
  contact_email TEXT,
  app_version TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE public.ai_response_reports (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  prompt TEXT NOT NULL CHECK (char_length(prompt) BETWEEN 1 AND 4000),
  response TEXT NOT NULL CHECK (char_length(response) BETWEEN 1 AND 8000),
  reason TEXT CHECK (reason IS NULL OR char_length(reason) <= 2000),
  app_version TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE public.user_feedback ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_response_reports ENABLE ROW LEVEL SECURITY;

-- Insert-only: no one (anon or authenticated) can read, update, or delete
-- rows back through the public API. Only project_admin (dashboard/CLI) can.
REVOKE SELECT, UPDATE, DELETE ON public.user_feedback FROM anon, authenticated;
REVOKE SELECT, UPDATE, DELETE ON public.ai_response_reports FROM anon, authenticated;

GRANT USAGE ON SCHEMA public TO anon, authenticated;
GRANT INSERT ON public.user_feedback TO anon, authenticated;
GRANT INSERT ON public.ai_response_reports TO anon, authenticated;

CREATE POLICY "anyone can submit feedback" ON public.user_feedback
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE POLICY "anyone can submit an ai report" ON public.ai_response_reports
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE INDEX idx_user_feedback_created_at ON public.user_feedback (created_at DESC);
CREATE INDEX idx_ai_response_reports_created_at ON public.ai_response_reports (created_at DESC);
