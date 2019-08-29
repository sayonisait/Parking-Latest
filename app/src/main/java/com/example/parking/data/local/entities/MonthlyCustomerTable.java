package com.example.parking.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.parking.model.MonthlyCustomer;

import java.util.Date;

@Entity(tableName = "monthly_customer")

public class MonthlyCustomerTable {
    @PrimaryKey
    @NonNull
    public String uid;

    @ColumnInfo(name = "name")
    public String customerName;

    @ColumnInfo(name = "phone")
    public String customerPhone;

    @ColumnInfo(name = "company")
    public String company;

    @ColumnInfo(name = "vehicle_number")
    public String vehicleNumber;

    @ColumnInfo(name = "vehicle_model")
    public String vehicleModel;

    @ColumnInfo(name = "start_date")
    public String startDate;

    @ColumnInfo(name = "end_date")
    public String endDate;

    public int grace_period;
    public boolean is_not_paid;
    public String secondary_phone;
    public String vehicle_make;
    public long receipt_number;
    public String subscription_date;
    public int server_id;
    public int vehicle_id;
    public int package_id;
    public int customer_id;
    public int payment_id;
    public String city;


}
