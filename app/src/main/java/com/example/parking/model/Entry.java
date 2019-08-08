package com.example.parking.model;

import com.example.parking.AppConstants;
import com.example.parking.data.local.entities.EntryTable;
import com.example.parking.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Entry {


    public String transaction_id;
    public Long receipt_number;

    public Date entryTime;

    public String parkingSlot="";

    public Date exitTime;

    public String  hours="";

    //public double  rate=0;

    public double  hourlyCharge=AppConstants.HOURLY_CHARGE;


    public double  estimatedAmount;

    public double  calculatedAmount;

    public double amountToBePaid;

    public int estimatedHours;


    public int serverID;

    public String createdTime;


    public Vehicle vehicle;
    public String phoneNumber;
    public String name;


    public String getAppEntryTime(){
        return StringUtils.getDateInUserFriendlyFormat(entryTime);
    }
    public String getAppExitTime(){
        return StringUtils.getDateInUserFriendlyFormat(exitTime);
    }
    public String getServerEntryTime(){
        return StringUtils.getDateInServerFormat(entryTime);
    }
    public String getServerExitTime(){
        if(exitTime==null)
            return "";
        return StringUtils.getDateInServerFormat(exitTime);
    }


    public void makeExit(){
        exitTime=getTime();
            long diffInMillies = Math.abs(exitTime.getTime() - entryTime.getTime());
            long minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long hours=minutes/60;
            minutes=minutes-hours*60;
            if(hours>0)
                this.hours= hours +" hrs "+ minutes +" min";
            else
                this.hours= minutes +" min";
            long hoursTobeConsidered= minutes>AppConstants.EXTRA_MINUTES?(hours+1):hours;
            double charge=hoursTobeConsidered*this.hourlyCharge;
            if(charge<=0)
                charge=hourlyCharge;
            if(minutes>0)
                this.calculatedAmount =charge;

            if(this.estimatedAmount<this.calculatedAmount && this.estimatedHours < hoursTobeConsidered) {
                // calculate additional minutes and add it to the estimated amount
                this.amountToBePaid = (hoursTobeConsidered - this.estimatedHours) * this.hourlyCharge + this.estimatedAmount;

            }else{
                // if estimatedamount is 0 , or estimated amount is greater than calculated amount, then calculated amount is the amount to be paid
                this.amountToBePaid=this.calculatedAmount;
            }

    }

    public void  startEntry(){

        transaction_id = UUID.randomUUID().toString();
        receipt_number= 0L;
        System.out.println("UUID created"+transaction_id);
        entryTime= getTime();
        vehicle= new Vehicle();
        hourlyCharge= AppConstants.HOURLY_CHARGE;//todo
    }

    private Date getTime() {
        return  new Date();
    }
    public Entry(){

    }

    public Entry(EntryTable entryTable){
        amountToBePaid=entryTable.amountToBePaid;
        transaction_id=entryTable.uid;
        estimatedHours=entryTable.estimatedHours;
        serverID=entryTable.serverID;
        exitTime=StringUtils.getDateFromServerFormat( entryTable.exitTime);
        parkingSlot=entryTable.parkingSlot;
        calculatedAmount=entryTable.calculatedAmount;
        hourlyCharge=entryTable.hourlyCharge;
        estimatedAmount=entryTable.estimatedAmount;
        entryTime=StringUtils.getDateFromServerFormat( entryTable.entryTime);
        receipt_number=entryTable.receiptNumber;
        phoneNumber=entryTable.phoneNumber;
        vehicle= new Vehicle();
        vehicle.vehicleNumber=entryTable.vehicleNumber;
        vehicle.vehicleModel=entryTable.vehicleModel;


    }



}
