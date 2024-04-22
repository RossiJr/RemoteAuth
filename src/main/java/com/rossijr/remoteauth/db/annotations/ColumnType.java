package com.rossijr.remoteauth.db.annotations;


public enum ColumnType {
    UUID("UUID"),
    TEXT("TEXT"),
    INTEGER("INTEGER"),
    FLOAT("REAL"),
    DATE("DATE"),
    BOOLEAN("BOOLEAN"),
    BIGINT("BIGINT"),;

    private final String type;

    ColumnType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
