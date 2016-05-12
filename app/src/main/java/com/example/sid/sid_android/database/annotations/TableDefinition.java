package com.example.sid.sid_android.database.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jdandrade on 02-02-2016.
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface TableDefinition {

    String[] primaryKey() default {};

    Composite_Unique[] uniques() default {};

    Index[] indexes() default {};


    @Retention(RetentionPolicy.RUNTIME)
    @interface Index {
        String index_name();
        boolean unique() default false;
        Key[] keys();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Key {
        String field();
        boolean descending() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Composite_Unique {
        String[] fields();
    }

}
