-- Increase the anonymous AI allowance because the primary models are free.
-- The paid Gemini model is used only as a server-side fallback.
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

  v_allowed := v_minute_count < 10 AND v_day_count < 50;
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

  RETURN QUERY SELECT v_allowed, v_minute_count, v_day_count,
    CASE WHEN v_minute_count >= 10 THEN 60 ELSE 0 END;
END;
$$;

REVOKE ALL ON FUNCTION public.consume_ai_quota(TEXT, TIMESTAMPTZ) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.consume_ai_quota(TEXT, TIMESTAMPTZ) TO anon, authenticated;

-- Return only chat fields and cap the amount returned to reduce accidental
-- exposure and oversized history reads from anonymous clients.
DROP FUNCTION IF EXISTS public.get_conversation_history(uuid, int);
CREATE FUNCTION public.get_conversation_history(p_device_id uuid, p_limit int DEFAULT 50)
RETURNS TABLE (
  id uuid,
  model_name text,
  prompt text,
  response text,
  method_context text,
  created_at timestamptz
)
LANGUAGE sql
STABLE
SECURITY DEFINER
SET search_path = pg_catalog, public, pg_temp
AS $$
  SELECT q.id, q.model_name, q.prompt, q.response, q.method_context, q.created_at
  FROM public.ai_queries q
  WHERE q.device_id = p_device_id
  ORDER BY q.created_at ASC
  LIMIT LEAST(GREATEST(COALESCE(p_limit, 50), 1), 50);
$$;

REVOKE ALL ON FUNCTION public.get_conversation_history(uuid, int) FROM PUBLIC;
GRANT EXECUTE ON FUNCTION public.get_conversation_history(uuid, int) TO anon, authenticated;
