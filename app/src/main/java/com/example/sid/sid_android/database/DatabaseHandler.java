package com.example.sid.sid_android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.sid.sid_android.util.Advertisement;
import com.example.sid.sid_android.util.Company;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private DatabaseSetup db_helper;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        db_helper = new DatabaseSetup(context);
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
        Cursor cursor = db.query(DatabaseSetup.TRANSLATOR_TABLE, null, null, null, null, null, null);
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
            // numero_anuncio, lingua_origem, lingua_destino, numero_palavras,valor, data_inicio_trabalho, numero_dias, designacao_software, estado, email
            Advertisement ad = new Advertisement(cursor.getInt(numero_anuncio_index), cursor.getString(lingua_origem_index), cursor.getString(lingua_destino_index),
                    cursor.getInt(numero_palavras_index), cursor.getDouble(valor_index), cursor.getString(data_inicio_index), cursor.getInt(numero_dias_index), cursor.getString(software_index),
                    cursor.getString(estado_index), cursor.getString(email_index));
            all.add(ad);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }

    public void insertCompany(Company company) {
        db.insert(DatabaseSetup.COMPANY_TABLE, "nome_empresa", addCompanyValues(company));
    }

    public void insertAd(Advertisement ad) {
        db.insert(DatabaseSetup.TRANSLATOR_TABLE, "numero_anuncio", addAdvertisingValues(ad));
    }

    public void deleteCompany(String email) {
        db.delete(DatabaseSetup.COMPANY_TABLE, "email = '" + email + "'", null);
    }

    public void deleteAd(int numero_anuncio) {
        db.delete(DatabaseSetup.TRANSLATOR_TABLE, "numero_anuncio = " + numero_anuncio, null);
    }

    public void updateCompany(String email, Company new_company) {
        db.update(DatabaseSetup.COMPANY_TABLE, addCompanyValues(new_company), "email = '" + email + "'", null);
    }

    public void updateAd(int numero_anuncio, Advertisement new_ad) {
        db.update(DatabaseSetup.TRANSLATOR_TABLE, addAdvertisingValues(new_ad), "numero_anuncio = " + numero_anuncio,
                null);
    }

    public void clearComps() {

        db.delete(DatabaseSetup.COMPANY_TABLE, null, null);
    }

    public void clearAds() {

        db.delete(DatabaseSetup.TRANSLATOR_TABLE, null, null);
    }
}

