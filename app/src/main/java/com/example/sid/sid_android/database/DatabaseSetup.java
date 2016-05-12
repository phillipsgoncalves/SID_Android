package com.example.sid.sid_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sid.sid_android.database.annotations.ColumnDefinition;
import com.example.sid.sid_android.database.annotations.OnConflict;
import com.example.sid.sid_android.database.annotations.SQLType;
import com.example.sid.sid_android.database.annotations.TableDefinition;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

public class DatabaseSetup extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "translator.db";
    public static final String TRANSLATOR_TABLE = "Translator";
    public static final String COMPANY_TABLE = "Company";
    private static DatabaseSetup dbSetupInstance;
    private boolean primaryKeyDefined;

    public DatabaseSetup(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(this.getClass().getName(), "Creating Database");
        try {
            createDatabase(db);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSLATOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COMPANY_TABLE);
        onCreate(db);
    }

    /** pode nao ser preciso para este trabalho **/
    public static DatabaseSetup getInstance(Context context){
        if(dbSetupInstance==null){
            dbSetupInstance=new DatabaseSetup(context);
        }
        return dbSetupInstance;
    }

    public void createDatabase(SQLiteDatabase db) throws IllegalAccessException {
        //these tables need to be updated. The fields are wrong.
//        db.execSQL("CREATE TABLE IF NOT EXISTS"+ TRANSLATOR_TABLE+"(Username VARCHAR,Password VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS"+ COMPANY_TABLE+"(Username VARCHAR,Password VARCHAR)");

        Class[] db_tables = Schema.class.getDeclaredClasses();

        String sql_statement;

        // Table
        TableDefinition table_definition;
        ColumnDefinition column_definition;
        Field[] table_columns;

        for (Class table : db_tables) {
            primaryKeyDefined = false;
            table_columns = table.getDeclaredFields();
            table_definition = ((TableDefinition) table.getAnnotation(TableDefinition.class));

            sql_statement = "CREATE TABLE IF NOT EXISTS " + table.getSimpleName().toLowerCase(Locale.ENGLISH) + " (";

            // Table_collumns
            Field column;

            Iterator<Field> it = Arrays.asList(table_columns).iterator();

            while (it.hasNext()) {
                column = it.next();
                column_definition = column.getAnnotation(ColumnDefinition.class);
                column.setAccessible(true);

                sql_statement += column.get(null) + " " + column.getAnnotation(ColumnDefinition.class).type();

                if (!column_definition.defaultValue().equals("")) {
                    sql_statement += " DEFAULT \"" + column_definition.defaultValue() + "\"";
                }

                sql_statement += getColumnConstraints(column_definition);

                if (it.hasNext()) {
                    sql_statement += ", ";
                }
            }
            // ------------------------- Table primary key -------------------------------


            if (!primaryKeyDefined) {
                if (table_definition != null && table_definition.primaryKey().length != 0) {
                    sql_statement += ", ";
                    sql_statement += getPrimaryKey(table_definition);
                }
            } else {
                if (table_definition != null && table_definition.primaryKey().length != 0) {
                    throw new IllegalArgumentException("PRIMARY KEY defined twice, at column and table level!");

                }
            }

            // --------------------------------- Table Unique Composite Fields --------------------------------------------
            if (table_definition != null && table_definition.uniques().length != 0) {
                sql_statement += ", ";
                sql_statement += getCompositeUniques(table_definition);
            }
            sql_statement += ")";

            db.execSQL(sql_statement);

            // --------------------------------------------------- Indexes Creation ----------------------------------------------

            if (table_definition != null) {
                createTableIndexes(table_definition, table.getSimpleName(), db);
            }
        }


    }

    private void createTableIndexes(TableDefinition table_definition, String table_name, SQLiteDatabase db) {
        TableDefinition.Index[] indexes = table_definition.indexes();
        String indexes_stmt;

        Iterator<TableDefinition.Index> iterator = Arrays.asList(indexes).iterator();

        TableDefinition.Index index;
        while (iterator.hasNext()) {
            indexes_stmt = "CREATE ";
            index = iterator.next();

            if (index.unique()) {
                indexes_stmt += "UNIQUE ";
            }

            indexes_stmt += "INDEX IF NOT EXISTS " + index.index_name() + " ON " + table_name + " (";

            TableDefinition.Key[] keys = index.keys();
            Iterator<TableDefinition.Key> keys_iterator = Arrays.asList(keys).iterator();

            TableDefinition.Key key;
            while (keys_iterator.hasNext()) {
                key = keys_iterator.next();
                indexes_stmt += key.field();
                if (key.descending()) {
                    indexes_stmt += " DESC";
                }
                if (keys_iterator.hasNext()) {
                    indexes_stmt += ", ";
                }
            }
            indexes_stmt += ");";

            db.execSQL(indexes_stmt);
        }
    }

    private String getCompositeUniques(TableDefinition table_definition) {
        TableDefinition.Composite_Unique[] uniques = table_definition.uniques();

        String uniques_stmt = "";
        String[] unique_fields;
        Iterator<TableDefinition.Composite_Unique> iterator = Arrays.asList(uniques).iterator();
        while (iterator.hasNext()) {
            uniques_stmt = "UNIQUE (";
            unique_fields = iterator.next().fields();

            Iterator<String> iterator1 = Arrays.asList(unique_fields).iterator();
            while (iterator1.hasNext()) {
                uniques_stmt += iterator1.next();
                if (iterator1.hasNext()) {
                    uniques_stmt += ", ";
                }
            }
            uniques_stmt += ")";

            if (iterator.hasNext()) {
                uniques_stmt += ", ";
            }
        }
        return uniques_stmt;
    }

    private String getPrimaryKey(TableDefinition table_definition) {
        String[] primary_key = table_definition.primaryKey();
        String pk = "PRIMARY KEY (";

        Iterator<String> iterator = Arrays.asList(primary_key).iterator();
        while (iterator.hasNext()) {

            pk += iterator.next();
            if (iterator.hasNext()) {
                pk += ", ";
            }
        }
        pk += ")";
        return pk;
    }

    private String getColumnConstraints(ColumnDefinition column_definition) {
        String column_constraints = "";
        if (column_definition.primaryKey()) {
            if (primaryKeyDefined) {
                throw new IllegalArgumentException("Can only define one PRIMARY KEY, to define a composite PRIMARY KEY, use @TableDefinition annotation");
            }
            primaryKeyDefined = true;
            column_constraints += " PRIMARY KEY";
        }
        if (column_definition.autoIncrement()) {
            if (!column_definition.primaryKey() || column_definition.type() != SQLType.INTEGER) {
                throw new IllegalArgumentException("AUTOINCREMENT only allowed to PRIMARY KEYs with type INTEGER");
            }
            column_constraints += " AUTOINCREMENT";
        }
        if (column_definition.unique()) {
            column_constraints += " UNIQUE";
        }
        if (column_definition.notNull()) {
            column_constraints += " NOT NULL";
        }
        if(!column_definition.onConflict().equals(OnConflict.NONE)){
            column_constraints += " ON CONFLICT " + column_definition.onConflict().name();
        }
        return column_constraints;
    }

}

