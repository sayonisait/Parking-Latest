package com.example.parking;

import androidx.room.Insert;
import androidx.room.Query;
import com.example.parking.entities.Entry;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    void insert(Entry entry);

    @Query("DELETE FROM entry_exit")
    void deleteAll();

    @Query("SELECT * from entry_exit ORDER BY entry_time ASC")
    List<Entry> getAllEntries();

    @Query("SELECT * from entry_exit WHERE uid")
    List<Entry> getAllEntries();

}
