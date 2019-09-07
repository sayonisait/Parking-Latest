package com.example.parking.data.network.models;

import com.example.parking.data.local.entities.EntryTable;
import com.example.parking.model.Entry;
import com.google.gson.annotations.SerializedName;

public class Transaction{

    public static final String CHECK_IN = "Check-In";
    public static final String VISITOR_CUSTOMER = "Visitor";
    public static final String CHECK_OUT = "Check-Out";
    public static final String CHECK_IN_UPDATE = "Check-In Update";

    @SerializedName("entry_time")
    public String entry_time;

    @SerializedName("parking_slot")
    public String parkingSlot;

    @SerializedName("exit_time")
    public String exitTime;

    public String entry_expiry_time="";


    public double rate;



    @SerializedName("estimated_amount")
    public double  estimatedAmount;

    @SerializedName("calculated_amount")
    public double  calculatedAmount;

    @SerializedName("amount_paid")
    public double amountToBePaid;

    public int estimatedHours;

    @SerializedName("app_order_reference")
    public long receipt_number;

    @SerializedName("app_order_uuid")
    public String app_id;

    public int order_id;

    public String transaction_type;
    public String entry_type;
    public String rate_type="hour";//todo
    public String payment_method="cash",subscription_id="", vehicle_id="";
    public String note="";
    public String registration_number;

    public Transaction(EntryTable table){
        amountToBePaid=table.amountToBePaid;
        app_id=table.uid;
        receipt_number=table.receiptNumber;
        calculatedAmount=table.calculatedAmount;
        entry_time=table.entryTime;
        estimatedAmount=table.estimatedAmount;
        estimatedHours=table.estimatedHours;
        exitTime=table.exitTime;
        order_id=table.serverID;
        parkingSlot=table.parkingSlot;
        rate=table.hourlyCharge;
        entry_type=VISITOR_CUSTOMER;
        transaction_type=table.exitTime!=null&& !table.exitTime.equals("")?CHECK_OUT:CHECK_IN;
    }

    public Transaction(Entry entry){
        amountToBePaid=entry.amountToBePaid;
        calculatedAmount=entry.calculatedAmount;
        entry_time=entry.getServerEntryTime();
        entry_type=VISITOR_CUSTOMER;//todo change this while doing monthly customer
        registration_number=entry.vehicle.vehicleNumber;
        parkingSlot=entry.parkingSlot;
        exitTime=entry.getServerExitTime();
        app_id =entry.transaction_id;
        receipt_number=entry.receipt_number;
        estimatedAmount=entry.estimatedAmount;
        estimatedHours=entry.estimatedHours;
        rate =entry.hourlyCharge;
        order_id=entry.serverID;
        if(entry.serverID!=0){
            transaction_type=CHECK_IN_UPDATE;
        }else
            transaction_type=entry.exitTime!=null&& !entry.exitTime.equals("")?CHECK_OUT:CHECK_IN;
    }


}
