-- Server-side quota buckets for the public MATHSUS AI function.
-- The client never receives access to this table; only the SECURITY DEFINER
-- procedure can update it atomically.
CREATE TABLE IF NOT EXISTS public.ai_usage_buckets (
  quota_key TEXT PRIMARY KEY,
  minute_started TIMESTAMPTZ NOT NULL,
  minute_count INTEGER NOT NULL DEFAULT 0,
  day_started DATE NOT NULL,
  day_count INTEGER NOT NULL DEFAULT 0,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE public.ai_usage_buckets ENABLE ROW LEVEL SECURITY;
REVOKE ALL ON public.ai_usage_buckets FROM anon, authenticated;

CREATE OR REPLACE FUNCTION public.consume_ai_quota(
  p_quota_key TEXT,
  p_now TIMESTAMPTZ DEFAULT now()
)
RETURNS TABLE (
  allowed BOOLEAN,
  minute_count INTEGER,
  day_count INTEGER,
  retry_after_seconds INTEGER
)
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = pg_catalog, public, pg_temp
AS $$
DECLARE
  v_key TEXT := md5(left(coalesce(p_quota_key, 'anonymous'), 512));
  v_minute TIMESTAMPTZ := date_trunc('minute', p_now);
  v_day DATE := (p_now AT TIME ZONE 'UTC')::date;
  v_minute_count INTEGER;
  v_day_count INTEGER;
  v_minute_started TIMESTAMPTZ;
  v_day_started DATE;
  v_allowed BOOLEAN;
BEGIN
  INSERT INTO public.ai_usage_buckets (quota_key, minute_started, day_started)
  VALUES (v_key, v_minute, v_day)
  ON CONFLICT (quota_key) DO NOTHING;

  SELECT b.minute_started, b.minute_count, b.day_started, b.day_count
  INTO v_minute_started, v_minute_count, v_day_started, v_day_count
  FROM public.ai_usage_buckets b
  WHERE b.quota_key = v_key
  FOR UPDATE;

  IF v_minute_started <> v_minute THEN
    v_minute_started := v_minute;
    v_minute_count := 0;
  END IF;

  IF v_day_started <> v_day THEN
    v_day_started := v_day;
    v_day_count := 0;
  END IF;

  v_allowed := v_minute_count < 5 AND v_day_count < 20;
  IF v_allowed THEN
    v_minute_count := v_minute_count + 1;
    v_day_count := v_day_count + 1;
  END IF;

  UPDATE public.ai_usage_buckets
  SET minute_started = v_minute_started,
      minute_count = v_minute_count,
      day_started = v_day_started,
      day_count = v_day_count,
      updated_at = p_now
  WHERE quota_key = v_key;

  RETURN QUERY SELECT
    v_allowed,
    v_minute_count,
    v_day_count,
    CASE WHEN v_minute_count >= 5 THEN 60 ELSE 0 END;
END;
$$;

REVOKE ALL ON FUNCTION public.consume_ai_quota(TEXT, TIMESTAMPTZ) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.consume_ai_quota(TEXT, TIMESTAMPTZ) TO anon, authenticated;
