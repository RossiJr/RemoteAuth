package com.rossijr.remoteauth.db.queries.generators;

import com.rossijr.remoteauth.db.annotations.model_annotation.Column;
import com.rossijr.remoteauth.db.annotations.model_annotation.Id;
import com.rossijr.remoteauth.db.annotations.model_annotation.Table;
import com.rossijr.remoteauth.db.config.DbConfig;
import com.rossijr.remoteauth.db.queries.QueryGenerator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class MySQLQueryGenerator implements QueryGenerator {
    @Override
    public String generateInsertObjectQuery(String tableName, Object object) {
        return null;
    }

    @Override
    public String generateSelectByID(Class<?> model, Object id) {
        String tableName = DbConfig.getValue(model.getAnnotation(Table.class).key());
        Field fieldId = Arrays.stream(model.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
        String idColumnName = fieldId.getAnnotation(Column.class).key().isBlank() ? fieldId.getName() : DbConfig.getValue(fieldId.getAnnotation(Column.class).key());

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ")
                .append(tableName)
                .append(" WHERE ")
                .append(idColumnName)
                .append(" = ")
                .append(id instanceof UUID ? "UUID_TO_BIN(?)" : "?")
                .append(" limit 1");

        return sqlBuilder.toString();
    }

    @Override
    public String generateQuery(Class<?> model, String query, Object... params) {

        return query;
    }
}
