package com.example.parking.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.parking.AppConstants;
import com.example.parking.data.local.entities.ConfigTable;
import com.example.parking.data.local.entities.EntryTable;
import com.example.parking.data.local.entities.MonthlyCustomerTable;

import java.util.List;

@androidx.room.Database(entities = {EntryTable.class, ConfigTable.class, MonthlyCustomerTable.class}, version =7, exportSchema = false)
public abstract class ParkingDatabase extends RoomDatabase {
    public abstract ParkingDao parkingDao();
    private static ParkingDatabase INSTANCE;

    public static ParkingDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ParkingDatabase.class, "parking_database").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }






}

