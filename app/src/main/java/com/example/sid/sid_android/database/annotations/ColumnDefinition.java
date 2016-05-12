package com.example.sid.sid_android.database.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jdandrade on 02-02-2016.
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface ColumnDefinition {

    SQLType type();
    String defaultValue() default "";

    // Constraints
    boolean primaryKey() default false;
    boolean notNull() default false;
    boolean unique() default false;
    boolean autoIncrement() default false;

    // OnConflict -- Only works for primary keys, unique and Not nulls fields
    OnConflict onConflict() default OnConflict.NONE;

}


