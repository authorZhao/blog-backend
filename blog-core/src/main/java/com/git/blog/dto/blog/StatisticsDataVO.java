package com.git.blog.dto.blog;

import com.git.blog.dto.model.entity.StatisticsData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author authorZhao
 * @since 2025-03-27
 */
@Data
@Accessors(chain = true)
public class StatisticsDataVO {

    /// 总访问量
    private long totalVisits = 0L;

    /// 日访问量
    private long dayVisits = 0L;

    /// 文章数量
    private long blogNo = 0L;

    /// 最后更新时间
    private LocalDateTime lastUpdateTime = LocalDateTime.now();

    /// 访问人数
    private long visitorsNum = 0L;


    public void init(StatisticsData data) {
        this.totalVisits = data.getTotalVisits();
        this.dayVisits = data.getDayVisits();
        this.blogNo = data.getBlogNum();
        this.lastUpdateTime = data.getLastUpdateTime();
        this.visitorsNum = data.getTotalVisitors();
    }

}
