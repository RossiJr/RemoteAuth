package com.rossijr.remoteauth.db.queries.generators;

import com.rossijr.remoteauth.db.queries.QueryGenerator;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MySQLQueryGenerator implements QueryGenerator {
    @Override
    public String generateInsertObjectQuery(String tableName, Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");
        Arrays.stream(fields).map(Field::getName).reduce((a, b) -> a + ", " + b).ifPresent(sqlBuilder::append);
        sqlBuilder.append(") VALUES (");

        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("?");
        }

        sqlBuilder.append(")");

        return sqlBuilder.toString();
    }
}
