package com.example.parking.print;

import java.io.IOException;
import java.io.OutputStream;

public class PrintDataUtility {

    public static void writeData(OutputStream os , byte[] bytes) throws IOException {
        os.write(bytes, 0, bytes.length);
       addLineFeed(os,1);

    }

    public static void alignCentre(OutputStream os) throws IOException {
        os.write(new byte[]{0x1B, 'a', 0x01});
    }
    public static void alignLeft(OutputStream os) throws IOException {
        os.write(new byte[]{0x1B, 'a', 0x00});

    }

    public static void writeInBold(OutputStream os, String text) throws IOException {
        os.write(new byte[]{0x1B,0x21,0x08});
        os.write(text.getBytes());
    }

    public static void writeInNormal(OutputStream os, String text) throws IOException {
        os.write(new byte[]{0x1B,0x21,0x00});
        os.write(text.getBytes());

    }

//    public static void writeBig(OutputStream os) throws IOException {
//        os.write( new byte[]{0x1B,0x21,0X04});
//        os.write(new byte[]{0x1B,0x21,0x08});
//
//    }


    public static void addLineFeed(OutputStream printerBuffer, int numLines) throws IOException {
         final byte[] PRINTER_PRINT_AND_FEED = {0x1B, 0x64};
         final byte BYTE_LF = 0xA;

            if (numLines <= 1) {
                printerBuffer.write(BYTE_LF);
            } else {
                printerBuffer.write(PRINTER_PRINT_AND_FEED);
                printerBuffer.write(numLines);
            }

    }

}
