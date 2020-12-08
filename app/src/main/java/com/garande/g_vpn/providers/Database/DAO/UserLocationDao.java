package com.garande.g_vpn.providers.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.garande.g_vpn.providers.Database.Entities.SmsLog;
import com.garande.g_vpn.providers.Database.Entities.UserLocation;

import java.util.List;

@Dao
public interface UserLocationDao {
    @Query("SELECT * FROM userlocation")
    List<UserLocation> fetchAll();

    @Insert
    void insertAll(UserLocation... userLocations);

    @Insert
    void insert(UserLocation userLocation);

    @Delete
    void delete(UserLocation userLocation);
}
