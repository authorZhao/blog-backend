package com.git.blog.dto.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author authorZhao
 * @since 2025-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_data")
public class ConfigEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /***
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 服务名称
     */
    private String service;

    /**
     * 内容
     */
    private String content;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 历史内容，json数组，每个元素包含版本号和version和修改时间
     */
    private String history;

    /**
     * 1 yml  2 xml
     */
    private Integer type;

    /**
     * 0正常 1 删除
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
