package com.example.parking.ui.vehicleentry;

import androidx.lifecycle.ViewModel;
import com.example.parking.entities.ParkingSlot;

import java.util.ArrayList;

public class ParkingSlotViewModel extends ViewModel {

   ArrayList<ParkingSlot> getParkingSpaces(){
       //todo fetch parking spaces
       ArrayList<ParkingSlot> slots= new ArrayList<>();
       for(int i=0;i<99;i++){
           ParkingSlot slot= new ParkingSlot();
           slot.slotNumber=i+100;
           if(i<=10)
               slot.isOccupied=true;
           if(i>=11 && i<22)
               slot.slotNumber=-1;
           if( i%11==0&& i>0)
               slot.slotNumber=-1;
           if(i==25|i==36|i==47|i==58|i==69|i==80|i==91)
               slot.slotNumber=-1;
           if(i==28|i==39|i==50|i==61|i==72|i==83|i==94)
               slot.slotNumber=-1;
           if(i==31|i==42|i==53|i==64|i==75|i==86|i==97)
               slot.slotNumber=-1;
           slots.add(slot);

       }
       return slots;


    }

}
