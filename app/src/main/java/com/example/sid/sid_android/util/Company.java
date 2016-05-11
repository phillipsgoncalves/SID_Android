package com.example.sid.sid_android.util;

public class Company {

    private String name;
    private String email;
    private String password;
    private String apresentacao;

    public Company(String name, String email, String password, String apresentacao) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.apresentacao = apresentacao;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    @Override
    public String toString() {
        return "Nome: " + getName() + "\nEmail: " + getEmail() + "\nApresentacao: " + getApresentacao();
    }

}

