-- Voluntary product/app ideas submitted from the "Conoce más" screen.
-- Public clients may insert only; rows are visible to the project owner via dashboard/CLI.
CREATE TABLE public.idea_requests (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  audience TEXT NOT NULL DEFAULT 'otro' CHECK (audience IN ('estudiante', 'docente', 'otro')),
  institution TEXT CHECK (institution IS NULL OR char_length(institution) <= 200),
  contact_email TEXT CHECK (contact_email IS NULL OR char_length(contact_email) <= 320),
  idea TEXT NOT NULL CHECK (char_length(idea) BETWEEN 20 AND 4000),
  app_version TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE public.idea_requests ENABLE ROW LEVEL SECURITY;
REVOKE SELECT, UPDATE, DELETE ON public.idea_requests FROM anon, authenticated;
GRANT USAGE ON SCHEMA public TO anon, authenticated;
GRANT INSERT ON public.idea_requests TO anon, authenticated;

CREATE POLICY "anyone can submit an idea request" ON public.idea_requests
  FOR INSERT TO anon, authenticated
  WITH CHECK (true);

CREATE INDEX idx_idea_requests_created_at ON public.idea_requests (created_at DESC);
