package com.git.blog.dto.config;

import java.time.LocalDateTime;

public record HistoryConfigItem(int version, String content, LocalDateTime updateTime) {
}
