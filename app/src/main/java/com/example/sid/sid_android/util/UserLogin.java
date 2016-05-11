package com.example.sid.sid_android.util;

public class UserLogin {

    private static UserLogin instance;
    private String ip;
    private String port;

    public UserLogin(String ip, String port) {
        instance = this;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public static UserLogin getInstance() {
        return instance;
    }

}

