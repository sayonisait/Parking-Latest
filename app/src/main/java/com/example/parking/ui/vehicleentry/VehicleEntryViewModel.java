package com.example.parking.ui.vehicleentry;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VehicleEntryViewModel extends ViewModel {

   public MutableLiveData<String> slotNumber = new MutableLiveData<>();
    public MutableLiveData<Bitmap> qrCode= new MutableLiveData<>();

    Entry entry;
    class Entry{
        public String entryTime, vehicleNumber, parkingSlot, vehicleModel, hoursLeft;

    }

    public void createEntry(){
         entry= new Entry();
         entry.entryTime=getEntryTime();
    }





    public String getEntryTime() {
        Date date = new Date();
        SimpleDateFormat spf=new SimpleDateFormat("hh:mm aaa");
        return spf.format(date);
    }

    public void createQRCode(String vehicleNumber , String modelName) {
        QRCodeWriter writer = new QRCodeWriter();
        entry.parkingSlot=slotNumber.getValue();
        entry.vehicleNumber=vehicleNumber;
        entry.vehicleModel=modelName;
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
