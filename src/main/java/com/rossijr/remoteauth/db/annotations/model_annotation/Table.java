package com.rossijr.remoteauth.db.annotations.model_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * Key to be used to retrieve the table name in the configuration file
     * @return String key
     */
    String key() default "default_table_name";
}
