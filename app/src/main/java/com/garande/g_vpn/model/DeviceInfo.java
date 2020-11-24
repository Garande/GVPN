package com.garande.g_vpn.model;

import android.os.Build;

import com.google.gson.Gson;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

    public final String OS_NAME = System.getProperty("os.name");
    public final String OS_VERSION = System.getProperty("os.version");
    public final String RELEASE = Build.VERSION.RELEASE;
    public final String DEVICE = Build.DEVICE;
    public final String MODEL = Build.MODEL;
    public final String PRODUCT = Build.PRODUCT;
    public final String BRAND = Build.BRAND;
    public final String UNKNOWN = Build.UNKNOWN;
    public final String HARDWARE = Build.HARDWARE;
    public final String SERIAL = Build.SERIAL;
    public final String USER = Build.USER;
    public final String HOST = Build.HOST;
    public final String MANUFACTURER = Build.MANUFACTURER;
    public final String SYSTEM_VERSION = String.valueOf(Build.VERSION.SDK_INT);
    public final String VERSION = String.valueOf(Build.VERSION.SDK_INT);


    public String LANGUAGE;
    public String TIME_ZONE;
    public String LOCAL_COUNTRY_CODE;
    public String TOTAL_MEMORY;
    public String FREE_MEMORY;
    public String USED_MEMORY;
    public String TOTAL_CPU_USAGE;
    public String TOTAL_CPU_USAGE_SYSTEM;
    public String TOTAL_CPU_USAGE_USER;
    public String TOTAL_CPU_IDLE;

    public String NETWORK;
    public String NETWORK_TYPE;
    public String TYPE;
    public String ID;


    public String MAC_ADDRESS;
    public String IP_ADDRESS;

    public String SCREEN_SIZE_IN_INCH;

    public DeviceInfo() {
    }

    public DeviceInfo(String LANGUAGE, String TIME_ZONE, String LOCAL_COUNTRY_CODE, String TOTAL_MEMORY, String FREE_MEMORY, String USED_MEMORY, String TOTAL_CPU_USAGE,
                      String TOTAL_CPU_USAGE_SYSTEM, String TOTAL_CPU_USAGE_USER, String TOTAL_CPU_IDLE, String NETWORK, String NETWORK_TYPE, String TYPE, String ID,
                      String MAC_ADDRESS, String SCREEN_SIZE_IN_INCH, String IP_ADDRESS) {
        this.LANGUAGE = LANGUAGE;
        this.TIME_ZONE = TIME_ZONE;
        this.LOCAL_COUNTRY_CODE = LOCAL_COUNTRY_CODE;
        this.TOTAL_MEMORY = TOTAL_MEMORY;
        this.FREE_MEMORY = FREE_MEMORY;
        this.USED_MEMORY = USED_MEMORY;
        this.TOTAL_CPU_USAGE = TOTAL_CPU_USAGE;
        this.TOTAL_CPU_USAGE_SYSTEM = TOTAL_CPU_USAGE_SYSTEM;
        this.TOTAL_CPU_USAGE_USER = TOTAL_CPU_USAGE_USER;
        this.TOTAL_CPU_IDLE = TOTAL_CPU_IDLE;
        this.NETWORK = NETWORK;
        this.NETWORK_TYPE = NETWORK_TYPE;
        this.TYPE = TYPE;
        this.ID = ID;
        this.MAC_ADDRESS = MAC_ADDRESS;
        this.SCREEN_SIZE_IN_INCH = SCREEN_SIZE_IN_INCH;
        this.IP_ADDRESS = IP_ADDRESS;
    }

    public String toStringJson(){
        Gson gson = new Gson();
        String str = gson.toJson(this);
        return str;
    }
}
