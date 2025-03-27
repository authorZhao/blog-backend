package com.git.blog.dto.blog;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author authorZhao
 * @since 2025-03-27
 */
@Data
@Accessors(chain = true)
public class StatisticsData {

    private long visits;
    //private long visits;
}
