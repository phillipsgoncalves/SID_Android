package com.example.sid.sid_android.util;

/**
 * Created by Filipe on 13-05-2016.
 */
public class Translator {

    private int numero_anuncio;
    private String email;
    private String relacao;

    public Translator(int numero_anuncio, String email, String relacao) {
        this.numero_anuncio = numero_anuncio;
        this.email = email;
        this.relacao = relacao;
    }

    public int getNumero_anuncio() {
        return numero_anuncio;
    }

    public void setNumero_anuncio(int numero_anuncio) {
        this.numero_anuncio = numero_anuncio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelacao() {
        return relacao;
    }

    public void setRelacao(String relacao) {
        this.relacao = relacao;
    }

    @Override
    public String toString() {
        return "Translator{" +
                "numero_anuncio=" + numero_anuncio +
                ", email='" + email + '\'' +
                ", relacao='" + relacao + '\'' +
                '}';
    }
}
