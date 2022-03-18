package com.git.blog.dto.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
/**
 * @author authorZhao
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)//决定返会的类型是void还是this
public class BlogFile {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;

    /**
     * 文件后缀
     */
    private String fileType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0是目录，1文件
     */
    private String hashId;

}
