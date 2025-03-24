package com.git.blog.util;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.git.blog.exception.SqlException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author authorZhao
 * @since 2021-01-18
 */
@Slf4j
public class SqlUtil {
    private static final SQLParserFeature[] FORMAT_DEFAULT_FEATURES;
    private static String MYSQL = "mysql";

    static {
        FORMAT_DEFAULT_FEATURES = new SQLParserFeature[]{SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup};
    }

    /**
     * sql格式化
     * @param sql
     * @param dbType
     * @return
     */
    public static String formatSql(String sql,String dbType){
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, FORMAT_DEFAULT_FEATURES);
            List<SQLStatement> statementList = parser.parseStatementList();
            sql =  SQLUtils.toSQLString(statementList, DbType.valueOf(dbType), (List)null, (SQLUtils.FormatOption)null);
        } catch (Exception e) {
            log.error("sql格式化错误,sql{}",sql);
            throw SqlException.SQL_PARSE_EXCEPTION;
        }
        return sql;
    }

    /**
     * sql格式化
     * @param sql
     * @return
     */
    public static String formatSql(String sql){
        return formatSql(sql,MYSQL);
    }

    public static void main(String[] args) {
            String sql = """
                    CREATE TABLE `task_sheet` (
                            `task_id`
                            BIGINT NOT NULL AUTO_INCREMENT COMMENT 'taskId',
                            `task_name`
                            VARCHAR(255) COMMENT '工单名称',
                            `type`
                            INT NOT NULL DEFAULT 10 COMMENT '工单类型，10信息类，20任务类',
                            `agent_uid`
                            BIGINT COMMENT '经办人',
                            `reporter_uid`
                            BIGINT COMMENT '报告人',
                            `url`
                            VARCHAR(255) COMMENT '任务型工单跳转url',
                            `status`
                            INT DEFAULT NULL COMMENT '工单状态，0未读/未处理，1已读/已处理',
                            `introduce`
                            text COMMENT '工单说明',
                            `create_time`
                            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time`
                            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                            PRIMARY KEY(`task_id`)
                    ) COMMENT = '任务工单表;
                    """;
            try {
                System.out.println("formatSql(sql,\"mysql\") = \n" + formatSql(sql));
            }catch (Exception e){
                e.printStackTrace();
            }
        Arrays.stream(SqlException.SQL_PARSE_EXCEPTION.getStackTrace()).forEach(System.out::println);
    }
}
