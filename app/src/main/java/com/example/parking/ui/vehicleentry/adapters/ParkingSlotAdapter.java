package com.example.parking.ui.vehicleentry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;

import java.util.List;
import java.util.Locale;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ViewHolder> {
  //  private ArrayList<ParkingSlot> slots;
    private Context context;
    private VehicleEntryViewModel viewModel;
    private int count;
    private List<String> mParkedSlots;
    public ParkingSlotAdapter(int parkingSpacesCount, FragmentActivity context) {
        count=parkingSpacesCount;
        this.context=context;
        viewModel= ViewModelProviders.of(context).get(VehicleEntryViewModel.class);
       // slots= new ArrayList<>(count);


    }

    public void setCount(int count){
        this.count=count;
        notifyDataSetChanged();
//        slots= new ArrayList<>(count);
//        for(int i=1;i<=count;i++){
//            slots.add(new ParkingSlot());
//        }
    }
    public void setOccupiedSlots(List<String> slots){
        mParkedSlots=slots;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ParkingSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(viewType==1?  R.layout.parking_slot_grid_cell:R.layout.parking_slot_grid_cell_hor,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSlotAdapter.ViewHolder holder, int position) {
        String slot=getSlotByPosition(position);
        holder.parkingSlot.setText(slot);
        if(viewModel.slotNumber !=null && viewModel.slotNumber.getValue()!=null && viewModel.slotNumber.getValue().equals(slot)) {
            holder.itemView.setBackgroundColor(holder.blueColor);
        }else{
            boolean isOccupied = mParkedSlots != null && mParkedSlots.contains(slot);
            holder.itemView.setBackgroundColor(isOccupied ? holder.redColor : holder.greenColor);
            holder.itemView.setOnClickListener(view -> {
                if(isOccupied){
                    Toast.makeText(context, "Selected slot is occupied. Please select a vaccant slot", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.setParkingSlot(slot);
            });
        }



    }

    private String getSlotByPosition(int position){
      return  String.format(Locale.ENGLISH, "%03d",position+1);
    }

    @Override
    public int getItemCount() {
        return count;
    }

     class ViewHolder extends RecyclerView.ViewHolder{
         @BindView(R.id.cell_parent)
         public LinearLayout layout;
        @BindView(R.id.slot_number)
       public TextView parkingSlot;


        @BindColor(android.R.color.holo_red_dark)
        public int redColor;
         @BindColor(android.R.color.holo_green_dark)
         public int greenColor;
         @BindColor(android.R.color.holo_blue_dark)
         public int blueColor;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }


    }
}
