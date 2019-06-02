package com.example.parking.utils;

import java.util.Locale;

public class StringUtils {

    public static String getAmountFormatted(double amount){
        return String.format( Locale.ENGLISH, " %.2f", amount);
    }
}
