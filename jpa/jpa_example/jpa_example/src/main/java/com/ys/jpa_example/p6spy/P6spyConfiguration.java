package com.ys.jpa_example.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6spyConfiguration implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
        String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        Date currentDate = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

        return formatter.format(currentDate) + " | " + "OperationTime : " + elapsed + "ms" + sql;
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals("")) {
            return sql;
        }

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith(
                "comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            sql = "|\n Hibernate FormatSql(P6Spy sql, Hibernate format): " + sql + "\n";
        }

        return sql;
    }

}
