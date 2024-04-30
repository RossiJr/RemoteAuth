package com.rossijr.remoteauth.db.queries.generators;

import com.rossijr.remoteauth.db.annotations.model_annotation.Column;
import com.rossijr.remoteauth.db.annotations.model_annotation.Id;
import com.rossijr.remoteauth.db.annotations.model_annotation.NotPersistent;
import com.rossijr.remoteauth.db.annotations.model_annotation.Table;
import com.rossijr.remoteauth.db.config.DbConfig;
import com.rossijr.remoteauth.db.queries.QueryGenerator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostgreSQLQueryGenerator implements QueryGenerator {
    @Override
    public String generateInsertObjectQuery(Class<?> model, Object object) {
        String tableName = DbConfig.getValue(model.getAnnotation(Table.class).key());
        List<Field> fields = Arrays.stream(model.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(NotPersistent.class)).collect(Collectors.toList());

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ")
                .append(tableName)
                .append(" (");
        sqlBuilder.append(fields.stream().map(field -> !field.isAnnotationPresent(Column.class) || field.getAnnotation(Column.class).key().isBlank() ? field.getName() : DbConfig.getValue(field.getAnnotation(Column.class).key()))
                .collect(Collectors.joining(",")));
        sqlBuilder.append(") VALUES (");
        sqlBuilder.append(fields.stream().map(field -> "?").collect(Collectors.joining(",")));
        sqlBuilder.append(");");
        return sqlBuilder.toString();
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
                .append("?")
                .append(" limit 1");

        return sqlBuilder.toString();
    }

    @Override
    public String generateQuery(Class<?> model, String query, Object... params) {
        return query;
    }

    @Override
    public String generateUpdateQuery(Class<?> model, Object object) {
        String tableName = DbConfig.getValue(model.getAnnotation(Table.class).key());
        List<Field> fields = Arrays.stream(model.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(NotPersistent.class)).collect(Collectors.toList());

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE ")
                .append(tableName)
                .append(" SET ");
        sqlBuilder.append(fields.stream().filter(f -> !f.isAnnotationPresent(Id.class))
                .map(field -> !field.isAnnotationPresent(Column.class) || field.getAnnotation(Column.class).key().isBlank() ? field.getName() : DbConfig.getValue(field.getAnnotation(Column.class).key())).collect(Collectors.joining(" = ?, ")));
        sqlBuilder.append(" = ?");
        sqlBuilder.append(" WHERE ");
        Field fieldId = Arrays.stream(model.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
        String columnName = fieldId.getAnnotation(Column.class).key().isBlank() ? fieldId.getName() : DbConfig.getValue(fieldId.getAnnotation(Column.class).key());

        sqlBuilder.append(columnName)
                .append(" = ?");

        return sqlBuilder.toString();
    }
}