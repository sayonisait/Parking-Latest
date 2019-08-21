package com.example.parking.data.local;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.parking.AppConstants;
import com.example.parking.data.local.entities.ConfigTable;
import com.example.parking.data.local.entities.EntryTable;
import com.example.parking.data.local.entities.MonthlyCustomerTable;

import java.util.List;

@androidx.room.Dao
public abstract class ParkingDao implements BaseDao {
    @Update
    public abstract int updateEntry(EntryTable entry);

    @Query("DELETE FROM entry_exit")
    public abstract void deleteAll();

    @Query("SELECT * from entry_exit ORDER BY entry_time ASC")
    public abstract LiveData<List<EntryTable>> getAllEntries();

    @Query("SELECT * from entry_exit where uid =:row_id LIMIT 1")
    public abstract LiveData<EntryTable> getEntryByID(String row_id);

    //todo order by entry time


    @Query("SELECT * from entry_exit where exit_time is null and vehicle_number LIKE :query ")
    public abstract LiveData<List<EntryTable>> getParkedEntries(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertConfig(ConfigTable entry);



    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insertConfig(List<ConfigTable> entries);

    @Query("SELECT * from config_table where key_field=:key")
    public abstract LiveData<ConfigTable> getConfigValue(String key);

    @Query("SELECT * from config_table")
    public abstract LiveData<List<ConfigTable>> getConfig();

    @Query("SELECT value from config_table where key_field=:key")
    public abstract LiveData<String> getValue(String key);

    @Query("SELECT slot_used from entry_exit where exit_time is null")
    public abstract LiveData<List<String>> getParkedSlots();





    @Query("SELECT * from entry_exit where synced is 0")
    public abstract LiveData<List<EntryTable>> getUnSyncedEntries();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insertMonthly(MonthlyCustomerTable monthly);

    @Update
    public abstract int updateMonthly(MonthlyCustomerTable entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertEntry(EntryTable entryTable);
//    @Transaction
//    public String upsertEntry(EntryTable e) {
//        try {
//            Log.d(AppConstants.LOG, "Trying to insert");
//            insertEntry(e);
//            return e.uid;
//        } catch (SQLiteConstraintException ex) {
//            Log.d(AppConstants.LOG, "Insert failed");
//            if (updateEntry(e) > 0) {
//                Log.d(AppConstants.LOG, "Updated record successfully");
//
//                return e.uid;
//            } else
//                Log.d(AppConstants.LOG, "Updated record failed");
//            ;
//            return null;
//        }
//
//    }
}