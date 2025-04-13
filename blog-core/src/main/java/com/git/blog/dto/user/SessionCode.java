package com.git.blog.dto.user;

/**
 * @author authorZhao
 * @since 2025-04-12
 */
public record SessionCode(String sessionId, String code, long lastTime) {

    public static SessionCode buildMew(SessionCode sessionCode) {
        return new SessionCode(sessionCode.sessionId, sessionCode.code, System.currentTimeMillis());
    }
}
