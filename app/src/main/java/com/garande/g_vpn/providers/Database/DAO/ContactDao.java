package com.garande.g_vpn.providers.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.garande.g_vpn.providers.Database.Entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> fetchAll();

    @Query("SELECT * FROM contact WHERE phoneNumber IN (:phoneNumbers)")
    List<Contact> fetchAllByIds(String[] phoneNumbers);

    @Query("SELECT * FROM contact WHERE phoneNumber LIKE :phoneNumber LIMIT 1")
    Contact getContactByNumber(String phoneNumber);

    @Insert
    void insertAll(Contact... contacts);

    @Insert
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);
}
