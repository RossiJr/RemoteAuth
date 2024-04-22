package com.rossijr.remoteauth.db.queries;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectBuilder<T>{
    T build(ResultSet rs) throws SQLException;
}
