package com.git.blog.commmon.enums;

/**
 * @author : yjs
 */
public class AuthTheadLocal {
    private static final ThreadLocal<Long> AUTH_TRACK_THREAD_LOCAL = new ThreadLocal<>();

    public static Long get() {
        return AUTH_TRACK_THREAD_LOCAL.get();
    }

    public static void set(Long value) {
        AUTH_TRACK_THREAD_LOCAL.set(value);
    }

    public static void remove() {
        AUTH_TRACK_THREAD_LOCAL.remove();
    }

}
