package com.git.blog.util;

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
            sql =  SQLUtils.toSQLString(statementList, dbType, (List)null, (SQLUtils.FormatOption)null);
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
            String sql = "CREATE TABLE `task_sheet` (\n" +
                    "\t`task_id`\n" +
                    "\tBIGINT NOT NULL AUTO_INCREMENT COMMENT 'taskId',\n" +
                    "\t`task_name`\n" +
                    "\tVARCHAR(255) COMMENT '工单名称',\n" +
                    "\t`type`\n" +
                    "\tINT NOT NULL DEFAULT 10 COMMENT '工单类型，10信息类，20任务类',\n" +
                    "\t`agent_uid`\n" +
                    "\tBIGINT COMMENT '经办人',\n" +
                    "\t`reporter_uid`\n" +
                    "\tBIGINT COMMENT '报告人',\n" +
                    "\t`url`\n" +
                    "\tVARCHAR(255) COMMENT '任务型工单跳转url',\n" +
                    "\t`status`\n" +
                    "\tINT DEFAULT NULL COMMENT '工单状态，0未读/未处理，1已读/已处理',\n" +
                    "\t`introduce`\n" +
                    "\ttext COMMENT '工单说明',\n" +
                    "\t`create_time`\n" +
                    "\tTIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "\t`update_time`\n" +
                    "\tTIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                    "\tPRIMARY KEY(`task_id`)\n" +
                    ") COMMENT = '任务工单表;'";
            try {
                System.out.println("formatSql(sql,\"mysql\") = \n" + formatSql(sql));
            }catch (Exception e){
                e.printStackTrace();
            }
        Arrays.stream(SqlException.SQL_PARSE_EXCEPTION.getStackTrace()).forEach(System.out::println);
    }
}
