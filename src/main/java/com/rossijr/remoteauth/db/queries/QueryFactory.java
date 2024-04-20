package com.rossijr.remoteauth.db.queries;

public interface QueryFactory {
    QueryGenerator createQueryGenerator();
    QueryExecutor createQueryExecutor();
}
