package com.git.blog.commmon;

/**
 * 系统默认字符串配置
 * @author authorZhao
 * @since 2021-02-03
 */
public class CommonString {

    public static final String LAST_SQL_LIMIT_1 = "Limit 1";

    public static final String LAST_SQL_LIMIT = "Limit %d";

    /**
     * dev环境
     */
    public static final String PROFILE_DEV = "dev";

    /**
     * test环境
     */
    public static final String PROFILE_TEST = "test";

    /**
     * pre环境
     */
    public static final String PROFILE_PRE= "pre";

    /**
     * prod环境
     */
    public static final String PROFILE_PROD = "prod";

    public static final String FEATURE_TAG_BASE = "base";

    /**
     * 微服务列表页面
     */
    public static final String GROUP_MICRO_SERVICE_MANAGE = "微服务管理组";

    /**
     * 账号管理组
     */
    public static final String GROUP_ACCOUNT_MANAGE = "账号管理组";

    /**
     * 测试组
     */
    public static final String GROUP_QA = "QA组";

    /**
     * 开发组
     */
    public static final String GROUP_DEV = "dev";

    /**
     * 运维组
     */
    public static final String GROUP_OPERATION = "运维组";




    /**
     * 角色类
     */
    public static final String ROLE_DEFAULT_ADMIN = "admin";
    /**
     * 运维
     */
    public static final String ROLE_DEFAULT_OPERATION  = "operation";
    /**
     * TYPE
     */
    public static final String TYPE = "TYPE";
    /**
     * TAG
     */
    public static final String TAG = "TAG";
    /**
     * myself
     */
    public static final String ROLE_DEFAULT_MYSELF = "myself";

    //===================== 权限相关
    /**修改他人标签的权限*/
    public static final String TAG_UPDATE_OTHER = "TAG_UPDATE_OTHER";
    /**删除啊他人标签的权限*/
    public static final String TAG_DELETE_OTHER = "TAG_UPDATE_OTHER";

    /**文章类型删除状态*/
    public static final Integer TYPE_DELETE_STATUS = 10;
    /**文章类型正常状态*/
    public static final Integer TYPE_NORMAL_STATUS = 0;

    /**文章查看其他人文章*/
    public static final String ARTICLE_READ_LIST_OTHER = "ARTICLE_READ_LIST_OTHER";
    /**文章查询已被删除的文章*/
    public static final String ARTICLE_READ_DELETE = "ARTICLE_READ_DELETE";
    /**删除别人的文章*/
    public static final String ARTICLE_DELETE_OTHER = "ARTICLE_DELETE_OTHER";

    /**最新几篇文章*/
    public static final String ARTICLE_NEW = "ARTICLE_NEW";

    /**文章删除状态*/
    public static final Integer ARTICLE_DELETE_STATUS = 10;
    /**文章正常状态*/
    public static final Integer ARTICLE_NORMAL_STATUS = 0;
    /**文章草稿状态*/
    public static final Integer ARTICLE_DRAFT_STATUS = 5;

}
