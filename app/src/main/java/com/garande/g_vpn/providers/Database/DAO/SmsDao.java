package com.garande.g_vpn.providers.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.garande.g_vpn.providers.Database.Entities.Contact;
import com.garande.g_vpn.providers.Database.Entities.SmsLog;

import java.util.List;

@Dao
public interface SmsDao {
    @Query("SELECT * FROM smslog")
    List<SmsLog> fetchAll();

    @Query("SELECT * FROM smslog WHERE senderNumber LIKE :phoneNumber OR receiverNumber LIKE :phoneNumber")
    List<SmsLog> getSmsByNumber(String phoneNumber);

    @Insert
    void insertAll(SmsLog... smsLogs);

    @Insert
    void insert(SmsLog smsLog);

    @Delete
    void delete(SmsLog smsLog);
}
