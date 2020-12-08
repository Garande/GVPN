package com.garande.g_vpn.providers.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SmsLog {
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "senderName")
    public String senderName;

    @ColumnInfo(name = "senderNumber")
    public String senderNumber;

    @ColumnInfo(name = "receiverName")
    public String receiverName;

    @ColumnInfo(name = "receiverNumber")
    public String receiverNumber;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "creationDateTimeMillis")
    public long creationDateTimeMillis;
}
