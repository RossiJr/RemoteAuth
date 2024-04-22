package com.rossijr.remoteauth.db.annotations.model_annotation;

import com.rossijr.remoteauth.db.annotations.ColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Column annotation to be used in the model class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * Type of the column in the database
     * @return ColumnType enum
     */
    ColumnType type() default ColumnType.TEXT;

    /**
     * Key to be used to retrieve the column name in the configuration file
     * @return String key
     */
    String key() default "";
}
