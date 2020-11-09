package com.yellowbird.g_vpn.model;

import java.io.Serializable;

public class Server implements Serializable {
    public String country;
    public String ovpn;
    public String userName;
    public String password;

    public Server() {
    }

    public Server(String country, String ovpn, String userName, String password) {
        this.country = country;
        this.ovpn = ovpn;
        this.userName = userName;
        this.password = password;
    }
}
