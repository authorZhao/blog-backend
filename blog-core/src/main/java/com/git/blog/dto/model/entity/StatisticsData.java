package com.git.blog.dto.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 统计表
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="统计表", description="统计表")
public class StatisticsData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /// 日期数字
    private Integer dayTime;

    /// 总访问量
    private Long totalVisits;

    /// 日访问量
    private Long dayVisits;

    /// 日访问用户数
    private Long dayVisitors;

    /// 总访问用户数
    private Long totalVisitors;

    /// 文章总数
    private Long blogNum;

    /// 创建时间
    private LocalDateTime createTime;

    /// 修改时间
    private LocalDateTime updateTime;

    /// 最后修改时间
    private LocalDateTime lastUpdateTime;


}
