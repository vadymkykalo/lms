package com.vadymkykalo.lms.constants;

import java.time.Duration;

public class CacheConstants {
    public static final Duration DEFAULT_CACHE_TTL = Duration.ofHours(1);

    public static final String ROLES_CACHE = "ROLES_CACHE";
    public static final Duration ROLES_CACHE_TTL = Duration.ofDays(1);
}
