package com.example.parking.ui.monthly;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import androidx.lifecycle.*;
import com.example.parking.data.ParkingRespository;
import com.example.parking.model.ConfigDetails;
import com.example.parking.model.MonthlyCustomer;
import com.example.parking.model.Vehicle;
import com.example.parking.utils.QRCodeUtils;
import com.example.parking.utils.StringUtils;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthlyViewModel extends AndroidViewModel {

    public MutableLiveData<Bitmap> qrCode= new MutableLiveData<>();

    MonthlyCustomer monthlyCustomer;
    ParkingRespository repository;
    LiveData<ConfigDetails> configDetails;

    public MonthlyViewModel(Application application) {
        super(application);
        repository = new ParkingRespository(application);
        createEntry();
        configDetails = repository.getConfig();


       // isPrintDone = false;
       // configDetails = repository.getConfig();
    }

    public LiveData<ConfigDetails> getConfigDetails() {
        return Transformations.map(configDetails, s -> {
            if(monthlyCustomer !=null ) {
                if(monthlyCustomer.receiptNumber==0)
                monthlyCustomer.receiptNumber = s.last_receipt_number + 1;
                monthlyCustomer.amount=s.monthly_package_amount;
                monthlyCustomer.amounFormatted = StringUtils.getAmountFormattedWithCurrency(s.monthly_package_amount);
                monthlyCustomer.packageID=s.monthly_package_id;

            }


            ;
            return s;
        });

    }


    public void createEntry(){
         monthlyCustomer = new MonthlyCustomer();
         monthlyCustomer.startDate= Calendar.getInstance().getTime();
         monthlyCustomer.endDate=getEndDate();
         monthlyCustomer.vehicle= new Vehicle();
    }


    private Date getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthlyCustomer.noOfMonths);
        return cal.getTime();
    }




    public void createQRCode(String vehicleNumber , String modelName) {
        QRCodeWriter writer = new QRCodeWriter();
        monthlyCustomer.vehicle = new Vehicle();
        monthlyCustomer.vehicle.vehicleNumber=vehicleNumber;
        monthlyCustomer.vehicle.vehicleModel=modelName;
        try {


            BitMatrix bitMatrix = writer.encode(new Gson().toJson(monthlyCustomer), BarcodeFormat.QR_CODE, 512, 512);
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

    public LiveData<String> saveSubscription() {
        return Transformations.map(repository.sendMonthlyCustomerTOBackend(monthlyCustomer), s -> {
            Log.d("Parking Info", "entry.transaction_id =" + String.valueOf(s));
            monthlyCustomer.rowID = s;
            generrateQR();
            return s;
        });
    }

    public void generrateQR() {
        Bitmap image = QRCodeUtils.generateQrCode(String.valueOf(monthlyCustomer.rowID));
        if (image != null)
            qrCode.postValue(image);

    }
}
