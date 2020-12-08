package com.garande.g_vpn.providers.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey
    public String phoneNumber;

    @ColumnInfo(name = "name")
    public String name;

}
