package com.garande.g_vpn.providers.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.garande.g_vpn.providers.Database.Entities.CallLog;
import com.garande.g_vpn.providers.Database.Entities.SmsLog;

import java.util.List;

@Dao
public interface CallLogDao {
    @Query("SELECT * FROM calllog")
    List<SmsLog> fetchAll();

    @Query("SELECT * FROM calllog WHERE callerNumber LIKE :phoneNumber OR receiverNumber LIKE :phoneNumber")
    List<CallLog> getCallLogByNumber(String phoneNumber);

    @Insert
    void insertAll(CallLog... callLogs);

    @Insert
    void insert(CallLog callLog);

    @Delete
    void delete(CallLog callLog);
}
