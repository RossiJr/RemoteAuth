package com.rossijr.remoteauth.db.queries;

public interface QueryGenerator {
    String generateInsertObjectQuery(Class<?> model, Object object);
    String generateSelectByID(Class<?> model, Object id);
    String generateQuery(Class<?> model, String query, Object... params);
    String generateUpdateQuery(Class<?> model, Object object);
}
