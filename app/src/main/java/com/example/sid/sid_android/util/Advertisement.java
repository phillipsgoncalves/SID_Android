package com.example.sid.sid_android.util;

public class Advertisement {

    private int numero_anuncio;
    private String lingua_origem;
    private String lingua_destino;
    private int numero_palavras;
    private double valor;
    private String data_inicio;
    private int numero_dias;
    private String software;
    private String estado;
    private String email;

    public Advertisement(int numero_anuncio, String lingua_origem, String lingua_destino, int numero_palavras, double valor, String data_inicio, int numero_dias, String software, String estado, String email) {
        this.numero_anuncio = numero_anuncio;
        this.lingua_origem = lingua_origem;
        this.lingua_destino = lingua_destino;
        this.numero_palavras = numero_palavras;
        this.valor = valor;
        this.data_inicio = data_inicio;
        this.numero_dias = numero_dias;
        this.software = software;
        this.estado = estado;
        this.email = email;
    }

    public int getNumero_anuncio() {
        return numero_anuncio;
    }

    public String getLingua_origem() {
        return lingua_origem;
    }

    public String getLingua_destino() {
        return lingua_destino;
    }

    public int getNumero_palavras() {
        return numero_palavras;
    }

    public double getValor() {
        return valor;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public int getNumero_dias() {
        return numero_dias;
    }

    public String getSoftware() {
        return software;
    }

    public String getEstado() {
        return estado;
    }

    public String getEmail() {
        return email;
    }

    public void setNumero_anuncio(int numero_anuncio) {
        this.numero_anuncio = numero_anuncio;
    }

    public void setLingua_origem(String lingua_origem) {
        this.lingua_origem = lingua_origem;
    }

    public void setLingua_destino(String lingua_destino) {
        this.lingua_destino = lingua_destino;
    }

    public void setNumero_palavras(int numero_palavras) {
        this.numero_palavras = numero_palavras;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public void setNumero_dias(int numero_dias) {
        this.numero_dias = numero_dias;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Linguagem Origem: " + getLingua_origem() + ", Linguagem Destino: " + getLingua_destino()
                + ", Num Palavras: " + getNumero_palavras() + ", Valor: " + getValor() + " Euros, Dt. Inicio: "
                + getData_inicio() + ", Num Dias: " + getNumero_dias() + ", Software: " + getSoftware();
    }

}

