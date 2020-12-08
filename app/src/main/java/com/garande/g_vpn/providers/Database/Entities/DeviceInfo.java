package com.garande.g_vpn.providers.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DeviceInfo {
    @PrimaryKey
    public String ID;

    @ColumnInfo
    public String OS_NAME;

    @ColumnInfo
    public String OS_VERSION;

    @ColumnInfo
    public String RELEASE;

    @ColumnInfo
    public String DEVICE;

    @ColumnInfo
    public String MODEL;

    @ColumnInfo
    public String PRODUCT;

    @ColumnInfo
    public String BRAND;

    @ColumnInfo
    public String UNKNOWN;

    @ColumnInfo
    public String HARDWARE;

    @ColumnInfo
    public String SERIAL;

    @ColumnInfo
    public String USER;

    @ColumnInfo
    public String HOST;

    @ColumnInfo
    public String MANUFACTURER;

    @ColumnInfo
    public String SYSTEM_VERSION;

    @ColumnInfo
    public String VERSION;

    @ColumnInfo
    public String LANGUAGE;

    @ColumnInfo
    public String TIME_ZONE;

    @ColumnInfo
    public String LOCAL_COUNTRY_CODE;

    @ColumnInfo
    public String TOTAL_MEMORY;

    @ColumnInfo
    public String FREE_MEMORY;

    @ColumnInfo
    public String USED_MEMORY;

    @ColumnInfo
    public String TOTAL_CPU_USAGE;

    @ColumnInfo
    public String TOTAL_CPU_USAGE_SYSTEM;

    @ColumnInfo
    public String TOTAL_CPU_USAGE_USER;

    @ColumnInfo
    public String TOTAL_CPU_IDLE;

    @ColumnInfo
    public String NETWORK;

    @ColumnInfo
    public String NETWORK_TYPE;

    @ColumnInfo
    public String TYPE;

    @ColumnInfo
    public String MAC_ADDRESS;

    @ColumnInfo
    public String IP_ADDRESS;

    @ColumnInfo
    public String SCREEN_SIZE_IN_INCH;
}
