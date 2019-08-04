package com.example.parking.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "entry_exit")
public class EntryTable {

    @PrimaryKey @NonNull
    public String uid;

    @ColumnInfo(name = "entry_time")
    public String entryTime;

    @ColumnInfo(name = "slot_used")
    public String parkingSlot;

    @ColumnInfo(name = "exit_time")
    public String exitTime;

    @ColumnInfo(name = "hours_parked")
    public String  hours;

    @ColumnInfo(name = "special_charge")
    public double  specialCharge;

    @ColumnInfo(name = "hourly_charge")
    public double  hourlyCharge;

    @ColumnInfo(name = "estimated_amount")
    public double  estimatedAmount;

    @ColumnInfo(name = "calculated_mount")
    public double  calculatedAmount;

    @ColumnInfo(name = "amount_to_be_paid")
    public double amountToBePaid;

    @ColumnInfo(name = "estimated_hours")
    public int estimatedHours;

    @ColumnInfo(name = "vehicle_number")
    public String vehicleNumber;
    @ColumnInfo(name = "vehicle_model")
    public String vehicleModel;
    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    @ColumnInfo(name = "server_id")
    public int serverID;

    @ColumnInfo(name = "synced")
    public boolean syncedToBackend;

    @ColumnInfo(name = "receipt_number")
    public long receiptNumber;

}
