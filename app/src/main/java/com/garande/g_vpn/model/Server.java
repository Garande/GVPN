package com.garande.g_vpn.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Server implements Serializable {
    public String country;
    public String ovpn;
    public String userName;
    public String password;
    public int countryFlag;

    public Server() {
    }

    public Server(String country, String ovpn, String userName, String password, int countryFlag) {
        this.country = country;
        this.ovpn = ovpn;
        this.userName = userName;
        this.password = password;
        this.countryFlag = countryFlag;
    }


}
