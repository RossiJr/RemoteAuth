package com.rossijr.remoteauth.db.queries.factories;

import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.QueryFactory;
import com.rossijr.remoteauth.db.queries.QueryGenerator;
import com.rossijr.remoteauth.db.queries.executors.PostgreSQLQueryExecutor;
import com.rossijr.remoteauth.db.queries.generators.PostgreSQLQueryGenerator;

public class PostgreSQLQueryFactory<T> implements QueryFactory {
    @Override
    public QueryGenerator createQueryGenerator() {
        return new PostgreSQLQueryGenerator();
    }

    @Override
    public QueryExecutor<T> createQueryExecutor() {
        return new PostgreSQLQueryExecutor<>();
    }
}
