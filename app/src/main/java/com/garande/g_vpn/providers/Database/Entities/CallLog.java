package com.garande.g_vpn.providers.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CallLog {
    @PrimaryKey
    public String id;

    @ColumnInfo
    public long creationDateTimeMillis;

    @ColumnInfo
    public String callerNumber;

    @ColumnInfo
    public String callerName;

    @ColumnInfo
    public String receiverName;

    @ColumnInfo
    public String receiverNumber;

    @ColumnInfo
    public long durationInSeconds;
}
