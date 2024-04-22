package com.rossijr.remoteauth.db.queries.factories;

import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.QueryFactory;
import com.rossijr.remoteauth.db.queries.QueryGenerator;
import com.rossijr.remoteauth.db.queries.executors.MySQLQueryExecutor;
import com.rossijr.remoteauth.db.queries.generators.MySQLQueryGenerator;

public class MySQLQueryFactory<T> implements QueryFactory {

    @Override
    public QueryGenerator createQueryGenerator() {
        return new MySQLQueryGenerator();
    }

    @Override
    public QueryExecutor<T> createQueryExecutor() {
        return new MySQLQueryExecutor<>();
    }
}
