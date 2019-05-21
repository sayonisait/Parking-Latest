package com.example.parking.ui.monthly;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.parking.entities.MonthlyPlan;
import com.example.parking.entities.Vehicle;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthlyViewModel extends ViewModel {

    public MutableLiveData<Bitmap> qrCode= new MutableLiveData<>();

    MonthlyPlan monthlyPlan;


    public void createEntry(){
         monthlyPlan = new MonthlyPlan();
         monthlyPlan.startDate= getStartDate();
         monthlyPlan.endDate=getEndDate();
    }

    SimpleDateFormat spf=new SimpleDateFormat(" dd MMM yyyy ", Locale.ENGLISH);

    private String getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthlyPlan.noOfMonths);
        return spf.format(cal.getTime());
    }


    public String getStartDate() {
        Date date = Calendar.getInstance().getTime();
        return spf.format(date);
    }

    public void createQRCode(String vehicleNumber , String modelName) {
        QRCodeWriter writer = new QRCodeWriter();
        monthlyPlan.vehicle = new Vehicle();
        monthlyPlan.vehicle.vehicleNumber=vehicleNumber;
        monthlyPlan.vehicle.vehicleModel=modelName;
        try {


            BitMatrix bitMatrix = writer.encode(new Gson().toJson(monthlyPlan), BarcodeFormat.QR_CODE, 512, 512);
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
