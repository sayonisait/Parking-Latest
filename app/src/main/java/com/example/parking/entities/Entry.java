package com.example.parking.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.parking.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "entry_exit")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "entry_time")
    private String entryTime;

    @ColumnInfo(name = "slot_used")
    private String parkingSlot;

    @ColumnInfo(name = "exit_time")
    private String exitTime;

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



    public Vehicle vehicle;
    public MonthlyPlan monthlyPlan;
    SimpleDateFormat spf=new SimpleDateFormat("hh:mm aaa", Locale.ENGLISH);

    public void makeExit(){
        exitTime=getTime();
        try {
            long diffInMillies = Math.abs(spf.parse(exitTime).getTime() - spf.parse(entryTime).getTime());
            long minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long hours=minutes/60;
            minutes=minutes-hours*60;
            if(hours>0)
                this.hours= hours +" hrs "+ minutes +" min";
            else
                this.hours= minutes +" min";
            long hoursTobeConsidered= minutes>AppConstants.EXTRA_MINUTES?(hours+1):hours;
            double charge=hoursTobeConsidered*this.hourlyCharge;

            if(minutes>0)
                this.calculatedAmount =charge;
            if(this.estimatedAmount<this.calculatedAmount && this.estimatedHours >= hoursTobeConsidered) {
                // calculate additional minutes and add it to the estimated amount
                this.amountToBePaid = (hoursTobeConsidered - this.estimatedHours) * this.hourlyCharge + this.estimatedAmount;

            }else{
                // if estimatedamount is 0 , or estimated amount is greater than calculated amount, then calculated amount is the amount to be paid
                this.amountToBePaid=this.calculatedAmount;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void  startEntry(){
        entryTime= getTime();
        vehicle= new Vehicle();
        hourlyCharge= AppConstants.HOURLY_CHARGE;
    }

    private String getTime() {
        Date date = new Date();
        return spf.format(date);
    }



}
