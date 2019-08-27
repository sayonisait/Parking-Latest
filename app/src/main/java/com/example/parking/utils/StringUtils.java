package com.example.parking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    public static String getAmountFormatted(double amount){
        return String.format( Locale.ENGLISH, "%.2f", amount);
    }

    public static String getAmountFormattedWithCurrency(double amount){
        return "AED "+ String.format( Locale.ENGLISH, " %.2f", amount);
    }
   static SimpleDateFormat userFriendlyFormat=new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH);
   static SimpleDateFormat serverFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);


    public static String getDateInUserFriendlyFormat(Date date){
        if(date==null)
            return "";
        return userFriendlyFormat.format(date);
    }

    public static String getDateInServerFormat(Date date){
        return serverFormat.format(date);
    }

    public static Date getDateFromServerFormat(String date){

        try {
            return serverFormat.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateFromUserFriendlyFormat(String date){
        if(date==null)
            return null;
        try {

            return userFriendlyFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isEmpty(String value){
        return value==null || value.trim().equals("");
    }
}
