package com.rossijr.remoteauth.db.queries;

public interface QueryGenerator {
    String generateInsertObjectQuery(String tableName, Object object);
}
