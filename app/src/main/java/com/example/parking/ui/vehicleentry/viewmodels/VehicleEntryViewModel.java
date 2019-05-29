package com.example.parking.ui.vehicleentry.viewmodels;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.parking.AppConstants;
import com.example.parking.entities.Entry;
import com.example.parking.entities.MonthlyPlan;
import com.example.parking.entities.Vehicle;
import com.example.parking.utils.StringUtils;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VehicleEntryViewModel extends ViewModel {

   public MutableLiveData<String> slotNumber = new MutableLiveData<>();
   public MutableLiveData<Bitmap> qrCode= new MutableLiveData<>();
    public MutableLiveData<MonthlyPlan> monthlyPlanMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<Entry> entryMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<String> estimatedAmountLiveData = new MutableLiveData<>();
    public MutableLiveData<String> specialChargeLiveData = new MutableLiveData<>();

    public Entry entry;
    public boolean isExit;


    public void setSpecialCharge(Double specialChargeLiveData){
        entry.specialCharge= specialChargeLiveData;
        setEstimatedAmount();
        this.specialChargeLiveData.postValue(StringUtils.getAmountFormatted(entry.specialCharge));

    }

    public void setEstHours(int hours){
        entry.estimatedHours=hours;
        setEstimatedAmount();

    }

    private void setEstimatedAmount(){
            entry.estimatedAmount = entry.specialCharge * entry.estimatedHours;
            if(entry.estimatedAmount>0)
                estimatedAmountLiveData.postValue(StringUtils.getAmountFormatted(entry.estimatedAmount));


    }



    public void createEntry(boolean isExit){
         entry= new Entry();
         if(!isExit)
            entry.entryTime= getTime();
         else
             entry.exitTime= getTime();
         this.isExit=isExit;
         entry.vehicle= new Vehicle();
         entry.hourlyCharge=AppConstants.HOURLY_CHARGE;


    }

    public void updateEntry(String json){
        if(isExit){
            // if exit , qr code scanned will be for entry
            Entry entry = new Gson().fromJson(json, Entry.class);
            entry.exitTime=getTime();
            try {
                long diffInMillies = Math.abs(spf.parse(entry.exitTime).getTime() - spf.parse(entry.entryTime).getTime());
                long minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
                long hours=minutes/60;
                minutes=minutes-hours*60;
                if(hours>0)
                    entry.hours= hours +" hrs "+ minutes +" min";
                else
                    entry.hours= minutes +" min";
                double charge=minutes>0 ?(hours+1)*entry.hourlyCharge:hours*entry.hourlyCharge;
                if(minutes>0)
                    entry.calculatedAmount =String.format(Locale.ENGLISH,"%.2f", charge)+" AED";
                this.entry=entry;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            entryMutableLiveData.setValue(entry);
        }else {
            // if entry scanned qr code is for a monthly plan car
            MonthlyPlan monthlyPlan = new Gson().fromJson(json, MonthlyPlan.class);
            // entry.monthlyPlan=monthlyPlan;

            monthlyPlanMutableLiveData.setValue(monthlyPlan);
        }
    }
    SimpleDateFormat spf=new SimpleDateFormat("hh:mm aaa", Locale.ENGLISH);

    private String getTime() {
        Date date = new Date();
        return spf.format(date);
    }

    public void setParkingSlot(String slot){
        entry.parkingSlot=slot;
        slotNumber.postValue( slot);
    }

    public void createQRCode(String modelName) {
        QRCodeWriter writer = new QRCodeWriter();
        entry.vehicle.vehicleModel=modelName;
        try {
            BitMatrix bitMatrix = writer.encode(new Gson().toJson(entry), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.postValue(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
