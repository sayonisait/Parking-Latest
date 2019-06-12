package com.example.parking.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtil {
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String Innerprinter_Address = "00:11:22:33:44:55";
    public static BluetoothAdapter getBTAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter) {
        BluetoothDevice innerprinter_device = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(Innerprinter_Address)) {
                innerprinter_device = device;
                break;
            }
        }
        return innerprinter_device;
    }

    public static BluetoothSocket getSocket(BluetoothDevice device) throws IOException {
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
        socket.connect();
        return socket;
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        return b[3];
    }
    public static void sendData(byte[] bytes, BluetoothSocket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(bytes, 0, bytes.length);
        // Setting height
        int gs = 29;
        os.write(intToByteArray(gs));
        int h = 104;
        os.write(intToByteArray(h));
        int n = 162;
        os.write(intToByteArray(n));

        // Setting Width
        int gs_width = 29;
        os.write(intToByteArray(gs_width));
        int w = 119;
        os.write(intToByteArray(w));
        int n_width = 2;
        os.write(intToByteArray(n_width));
        os.close();
    }

    public static void printData(byte[] dataToPrint, Context context  ){
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter ();
        if (btAdapter ==  null) {
            Toast. makeText (context, "Please turn on your bluetooth to print!",Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothDevice device = BluetoothUtil. getDevice (btAdapter);
        if (device == null) {
            Toast. makeText (context, "Please make sure your device has inner printer!",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            BluetoothSocket socket= BluetoothUtil.getSocket(device);
            BluetoothUtil.sendData(dataToPrint,socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
