package com.example.sid.sid_android.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sid.sid_android.util.Advertisement;
import com.example.sid.sid_android.util.Company;
import com.example.sid.sid_android.util.Translator;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private DatabaseSetup db_helper;
    private SQLiteDatabase db;
    private Activity context;

    public DatabaseHandler(Activity context) {
        db_helper = new DatabaseSetup(context);
        this.context = context;
    }

    public void open() {
        db = db_helper.getWritableDatabase();
    }

    public void close() {
        db_helper.close();
    }

    private ContentValues addCompanyValues(Company company) {
        ContentValues cv = new ContentValues();
        cv.put("nome_empresa", company.getName());
        cv.put("apresentacao", company.getApresentacao());
        cv.put("password", company.getPassword());
        cv.put("email", company.getEmail());
        return cv;
    }

    private ContentValues addAdvertisingValues(Advertisement ad) {
        ContentValues cv = new ContentValues();
        cv.put("numero_anuncio", ad.getNumero_anuncio());
        cv.put("lingua_origem", ad.getLingua_origem());
        cv.put("lingua_destino", ad.getLingua_destino());
        cv.put("numero_palavras", ad.getNumero_palavras());
        cv.put("valor", ad.getValor());
        cv.put("data_inicio", ad.getData_inicio() + "");
        cv.put("numero_dias", ad.getNumero_dias());
        cv.put("software", ad.getSoftware());
        cv.put("estado", ad.getEstado() + "");
        cv.put("email", ad.getEmail());
        return cv;
    }

    private ContentValues addTranslatorValues(Translator translator) {
        ContentValues cv = new ContentValues();
        cv.put("numero_anuncio", translator.getNumero_anuncio());
        cv.put("email", translator.getEmail());
        cv.put("relacao", translator.getRelacao());
        return cv;
    }

    public List<Company> getAllCompanies() {
        List<Company> all = new ArrayList<Company>();
        Cursor cursor = db.query(DatabaseSetup.COMPANY_TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Company company = new Company(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3));
            all.add(company);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }

    public Company getCompany(String mail) {
        Cursor cursor = db.query(DatabaseSetup.COMPANY_TABLE, null, "Company.email='" + mail + "'", null, null, null,
                null);
        cursor.moveToFirst();

        int nome_index = cursor.getColumnIndex(Schema.Company.COLUMN_NOME_EMPRESA);
        int email_index = cursor.getColumnIndex(Schema.Company.COLUMN_EMAIL);
        int password_index = cursor.getColumnIndex(Schema.Company.COLUMN_PASSWORD);
        int apresentacao_index = cursor.getColumnIndex(Schema.Company.COLUMN_APRESENTACAO);

        System.out.println("cursor company : " + DatabaseUtils.dumpCursorToString(cursor));
        Company c = new Company(cursor.getString(nome_index), cursor.getString(email_index), cursor.getString(password_index), cursor.getString(apresentacao_index));
        cursor.close();
        return c;
    }

    public List<Advertisement> getAllAds() {
        List<Advertisement> all = new ArrayList<Advertisement>();
        String whereClauseAds="estado='Y'";
        Cursor cursor = db.query(DatabaseSetup.TRANSLATOR_TABLE, null, whereClauseAds, null, null, null, null);
        cursor.moveToFirst();

        int numero_anuncio_index = cursor.getColumnIndex(Schema.Translator.COLUMN_NUMERO_ANUNCIO);
        int lingua_origem_index = cursor.getColumnIndex(Schema.Translator.COLUMN_LINGUA_ORIGEM);
        int lingua_destino_index = cursor.getColumnIndex(Schema.Translator.COLUMN_LINGUA_DESTINO);
        int numero_palavras_index = cursor.getColumnIndex(Schema.Translator.COLUMN_NUMERO_PALAVRAS);
        int valor_index = cursor.getColumnIndex(Schema.Translator.COLUMN_VALOR);
        int data_inicio_index = cursor.getColumnIndex(Schema.Translator.COLUMN_DATA_INICIO);
        int numero_dias_index = cursor.getColumnIndex(Schema.Translator.COLUMN_NUMERO_DIAS);
        int software_index = cursor.getColumnIndex(Schema.Translator.COLUMN_SOFTWARE);
        int estado_index = cursor.getColumnIndex(Schema.Translator.COLUMN_ESTADO);
        int email_index = cursor.getColumnIndex(Schema.Translator.COLUMN_EMAIL);

        while (!cursor.isAfterLast()) {

            Cursor c2=db.query(DatabaseSetup.MYADS_TABLE, null, null, null, null, null, null );
            c2.moveToFirst();

            String estado = "P";

            while(!c2.isAfterLast()){
                //ver se na tabela myads (dos tradutores) existe uma entrada com aquele numero de anuncio
                int myAd_Numero_Anuncio_index=c2.getColumnIndex(Schema.MyAds.COLUMN_NUMERO_ANUNCIO);
                int myAdNumber= c2.getInt(myAd_Numero_Anuncio_index);

                if(cursor.getInt(numero_anuncio_index) == myAdNumber){
                    estado = c2.getString(c2.getColumnIndex(Schema.MyAds.COLUMN_RELACAO));
                }
                c2.moveToNext();

            }
            c2.close();

            // numero_anuncio, lingua_origem, lingua_destino, numero_palavras,valor, data_inicio_trabalho, numero_dias, designacao_software, estado, email
            Advertisement ad = new Advertisement(cursor.getInt(numero_anuncio_index), cursor.getString(lingua_origem_index), cursor.getString(lingua_destino_index),
                    cursor.getInt(numero_palavras_index), cursor.getDouble(valor_index), cursor.getString(data_inicio_index), cursor.getInt(numero_dias_index), cursor.getString(software_index),
                    estado, cursor.getString(email_index));
            all.add(ad);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }

    public List<Advertisement> getMyAds(String mailToPass){
        List<Advertisement> myAds = new ArrayList<Advertisement>();

        //String whereClauseAnuncios="estado='Y'";
       // String []whereClauseAnunciosArgs={"'y'"};
        String whereClauseMyAds="email='" + mailToPass + "' AND relacao='Y'";
        //String []whereClauseMyAdsArgs={"'translator@iscte.pt'"};
        Cursor c = db.query(DatabaseSetup.TRANSLATOR_TABLE, null, null,null, null, null, null);
        c.moveToFirst();

        int numero_anuncio_index = c.getColumnIndex(Schema.Translator.COLUMN_NUMERO_ANUNCIO);
        int lingua_origem_index = c.getColumnIndex(Schema.Translator.COLUMN_LINGUA_ORIGEM);
        int lingua_destino_index = c.getColumnIndex(Schema.Translator.COLUMN_LINGUA_DESTINO);
        int numero_palavras_index = c.getColumnIndex(Schema.Translator.COLUMN_NUMERO_PALAVRAS);
        int valor_index = c.getColumnIndex(Schema.Translator.COLUMN_VALOR);
        int data_inicio_index = c.getColumnIndex(Schema.Translator.COLUMN_DATA_INICIO);
        int numero_dias_index = c.getColumnIndex(Schema.Translator.COLUMN_NUMERO_DIAS);
        int software_index = c.getColumnIndex(Schema.Translator.COLUMN_SOFTWARE);
        int estado_index = c.getColumnIndex(Schema.Translator.COLUMN_ESTADO);
        int email_index = c.getColumnIndex(Schema.Translator.COLUMN_EMAIL);


        int adNumber;
        /*criar outro cursor para a tabela de tradutor e ver se para este numero de anuncio, ta la o meu mail*/
        while(!c.isAfterLast()){
            Cursor c2=db.query(DatabaseSetup.MYADS_TABLE, null, whereClauseMyAds, null, null, null, null );
            c2.moveToFirst();

            adNumber=c.getInt(numero_anuncio_index);

            while(!c2.isAfterLast()){
                //ver se na tabela myads (dos tradutores) existe uma entrada com aquele numero de anuncio
                int myAd_Numero_Anuncio_index=c2.getColumnIndex(Schema.MyAds.COLUMN_NUMERO_ANUNCIO);
                int myAdNumber= c2.getInt(myAd_Numero_Anuncio_index);

                if(myAdNumber==adNumber){
                    Advertisement myAd=new Advertisement(myAdNumber, c.getString(lingua_origem_index), c.getString(lingua_destino_index),
                            c.getInt(numero_palavras_index), c.getDouble(valor_index), c.getString(data_inicio_index), c.getInt(numero_dias_index), c.getString(software_index),
                            c.getString(estado_index), c.getString(email_index));

                    Log.d(this.getClass().getName(), ""+myAdNumber+""+adNumber);

                    myAds.add(myAd);
                }
                c2.moveToNext();

            }
            c2.close();
            c.moveToNext();

        }
        c.close();
        return myAds;
    }

    public void insertCompany(Company company) {
        db.insert(DatabaseSetup.COMPANY_TABLE, "nome_empresa", addCompanyValues(company));
    }

    public void insertAd(Advertisement ad) {
        db.insert(DatabaseSetup.TRANSLATOR_TABLE, "numero_anuncio", addAdvertisingValues(ad));
    }

    /*pus o numero_anuncio pq esse e a maior garantia que vai ser unico*/
    public void insertTranslator(Translator translator){
        db.insert(DatabaseSetup.MYADS_TABLE, "numero_anuncio" , addTranslatorValues(translator));
    }

    public void deleteCompany(String email) {
        db.delete(DatabaseSetup.COMPANY_TABLE, "email = '" + email + "'", null);
    }

    public void deleteAd(int numero_anuncio) {
        db.delete(DatabaseSetup.TRANSLATOR_TABLE, "numero_anuncio = " + numero_anuncio, null);
    }

    public void deleteMyAd(int numero_anuncio) {
        db.delete(DatabaseSetup.MYADS_TABLE, "numero_anuncio = " + numero_anuncio, null);
    }

    public void updateCompany(String email, Company new_company) {
        db.update(DatabaseSetup.COMPANY_TABLE, addCompanyValues(new_company), "email = '" + email + "'", null);
    }

    public void updateAd(int numero_anuncio, Advertisement new_ad) {
        db.update(DatabaseSetup.TRANSLATOR_TABLE, addAdvertisingValues(new_ad), "numero_anuncio = " + numero_anuncio,
                null);
    }

    public void updateRelacaoTrad(Advertisement new_ad, int i, String email, String password) {
        Translator translator = null;
        if(i == 1) {
            translator = new Translator(new_ad.getNumero_anuncio(), email, "Y");
        }else if (i == 0){
            translator = new Translator(new_ad.getNumero_anuncio(), email, "P");
        }

        Cursor cursor = db.rawQuery("select 1 from myads where myads.numero_anuncio =" + new_ad.getNumero_anuncio(), null);
        boolean insertOrUpdate = false;
        try{
            insertOrUpdate = cursor.moveToFirst();
        }finally {
            cursor.close();
        }

        if(!insertOrUpdate && translator!= null){
            db.insert(DatabaseSetup.MYADS_TABLE, "numero_anuncio", addTranslatorValues(translator));
        }else if (translator != null) {
            db.update(DatabaseSetup.MYADS_TABLE, addTranslatorValues(translator), "numero_anuncio = " + new_ad.getNumero_anuncio(),
                    null);
        }

    }

    public void clearComps() {

        db.delete(DatabaseSetup.COMPANY_TABLE, null, null);
    }

    public void clearAds() {

        db.delete(DatabaseSetup.TRANSLATOR_TABLE, null, null);
    }

    public void clearTrads() {
        db.delete(DatabaseSetup.MYADS_TABLE, null, null);
    }

    public List<Translator> getAllTranslatorRelations(String mailToPass) {
        List<Translator> myTradsRelations = new ArrayList<Translator>();

//        String whereClauseTransl="email=" + mailToPass;

        Cursor c2=db.query(DatabaseSetup.MYADS_TABLE, null, null, null, null, null, null );
        c2.moveToFirst();

            while(!c2.isAfterLast()){
                int myAd_Numero_Anuncio_index=c2.getColumnIndex(Schema.MyAds.COLUMN_NUMERO_ANUNCIO);
                int myRelacao_index=c2.getColumnIndex(Schema.MyAds.COLUMN_RELACAO);
                int myEmail_index=c2.getColumnIndex(Schema.MyAds.COLUMN_EMAIL);

                int myAdNumber= c2.getInt(myAd_Numero_Anuncio_index);
                String relacao = c2.getString(myRelacao_index);
                String email = c2.getString(myEmail_index);
                Translator translator = new Translator(myAdNumber, email, relacao);
                myTradsRelations.add(translator);
                c2.moveToNext();
            }
            c2.close();
        return myTradsRelations;
    }
}

