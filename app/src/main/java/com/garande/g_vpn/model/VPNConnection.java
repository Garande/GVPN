package com.garande.g_vpn.model;


import java.io.Serializable;

public class VPNConnection implements Serializable {
    public static String CONNECTING = "CONNECTING";
    public static String CONNECTED = "CONNECTED";
    public static String READY = "READY";
    public static String DISCONNECTED = "DISCONNECTED";
    public static String WAIT = "WAIT";
    public static String AUTH = "AUTH";
    public static String NO_NETWORK = "NO_NETWORK";

    public String state;


    public VPNConnection(){
    }

    public VPNConnection(String state) {
        this.state = state;
    }



}
