package com.garande.g_vpn.providers.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.garande.g_vpn.model.DeviceInfo;

@Dao
public interface DeviceInfoDao {
    @Query("SELECT * FROM deviceInfo LIMIT 1")
    DeviceInfo getDeviceInfo();

    @Insert
    void insert(DeviceInfo deviceInfo);

    @Delete
    void delete(DeviceInfo deviceInfo);
}
