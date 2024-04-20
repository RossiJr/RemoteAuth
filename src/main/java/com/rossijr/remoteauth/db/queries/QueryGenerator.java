package com.rossijr.remoteauth.db.queries;

public interface QueryGenerator {
    String generateInsertQuery(String tableName, String firstName, String lastName);
}
