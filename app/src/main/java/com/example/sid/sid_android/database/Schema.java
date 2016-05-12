package com.example.sid.sid_android.database;


import com.example.sid.sid_android.database.annotations.ColumnDefinition;
import com.example.sid.sid_android.database.annotations.SQLType;

/**
 * Created by jdandrade on 02-02-2016.
 */
public class Schema {



    public static class Company {


        @ColumnDefinition(type = SQLType.TEXT)
        public final static String COLUMN_NOME_EMPRESA = "nome_empresa";

        @ColumnDefinition(type = SQLType.TEXT)
        public final static String COLUMN_APRESENTACAO = "apresentacao";

        @ColumnDefinition(type = SQLType.TEXT)
        public final static String COLUMN_PASSWORD = "password";

        @ColumnDefinition(type = SQLType.TEXT)
        public final static String COLUMN_EMAIL = "email";

        public static String getName() {
            return "company";
        }

    }

    public static class Translator {

        @ColumnDefinition(type = SQLType.INTEGER)
        public static final String COLUMN_NUMERO_ANUNCIO = "numero_anuncio";

        @ColumnDefinition(type = SQLType.TEXT)
        public static final String COLUMN_LINGUA_ORIGEM = "lingua_origem";

        @ColumnDefinition(type = SQLType.TEXT)
        public static final String COLUMN_LINGUA_DESTINO = "lingua_destino";

        @ColumnDefinition(type = SQLType.INTEGER)
        public static final String COLUMN_NUMERO_PALAVRAS = "numero_palavras";

        @ColumnDefinition(type = SQLType.REAL)
        public static final String COLUMN_VALOR = "valor";

        @ColumnDefinition(type = SQLType.DATE)
        public static final String COLUMN_DATA_INICIO = "data_inicio";

        @ColumnDefinition(type = SQLType.INTEGER)
        public static final String COLUMN_NUMERO_DIAS = "numero_dias";

        @ColumnDefinition(type = SQLType.TEXT)
        public static final String COLUMN_SOFTWARE = "software";

        @ColumnDefinition(type = SQLType.TEXT)
        public static final String COLUMN_ESTADO = "estado";

        @ColumnDefinition(type = SQLType.TEXT)
        public static final String COLUMN_EMAIL = "email";

        public static String getName() {
            return "translator";
        }
    }
}
