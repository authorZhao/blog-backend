package com.git.blog.commmon;

/**
 * 描述表的类型和阶段
 * @see com.git.blog.dto.model.entity.Desc
 * @author authorZhao
 * @since 2021-03-17
 */


public class DescType {

    /**
     * 需求版本描述
     */
    public static final Integer TYPE_PRODUCT_FEATURE  = 10;

    /**
     * 微服务版本描述
     */
    public static final Integer TYPE_MICRO_SERVICE_FEATURE  = 20;

    /**
     * 集成的类型
     */
    public static final Integer TYPE_INTEGRATED  = 30;

    /**
     * 开始开发时间
     */
    public static final Integer STAGE_START_DEV_TIME  = 10;

    /**
     * 开发中
     */
    public static final Integer STAGE_IN_DEV_TIME  = 20;

    /**
     * 开始冒烟
     */
    public static final Integer STAGE_START_SMOKE_TIME  = 30;

    /**
     * 冒烟中
     */
    public static final Integer STAGE_IN_SMOKE_TEST_TIME  = 40;

    /**
     * 测试开始
     */
    public static final Integer STAGE_START_TEST_TIME  = 50;
    /**
     * 测试中
     */
    public static final Integer STAGE_IN_TEST_TIME  = 60;

    /**
     * 集成开始
     */
    public static final Integer STAGE_START_INTEGRATED_TIME  = 70;

    /**
     * 集成中
     */
    public static final Integer STAGE_IN_INTEGRATED_TIME  = 80;

    /**
     * 部署中
     */
    public static final Integer STAGE_DEPLOY_TIME  = 90;

}
