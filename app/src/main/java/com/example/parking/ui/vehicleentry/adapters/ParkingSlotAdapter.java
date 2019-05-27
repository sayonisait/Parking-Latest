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
import com.example.parking.entities.ParkingSlot;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;

import java.util.ArrayList;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ViewHolder> {
    private ArrayList<ParkingSlot> slots;
    private Context context;
    private VehicleEntryViewModel viewModel;

    public ParkingSlotAdapter(ArrayList<ParkingSlot> slots, FragmentActivity context) {
        this.slots=slots;
        this.context=context;
        viewModel= ViewModelProviders.of(context).get(VehicleEntryViewModel.class);

    }


    @NonNull
    @Override
    public ParkingSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(viewType==1?  R.layout.parking_slot_grid_cell:R.layout.parking_slot_grid_cell_hor,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSlotAdapter.ViewHolder holder, int position) {
        ParkingSlot slot= slots.get(position);
        if(slot.slotNumber==-1) {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.parkingSlot.setVisibility(View.INVISIBLE);
           // holder.carImage.setVisibility(View.GONE);
            holder.itemView.setBackground(null);

        }
        else {
           // holder.carImage.setVisibility(slot.isOccupied?View.VISIBLE:View.INVISIBLE);
            holder.itemView.setBackgroundColor(slot.isOccupied?holder.redColor:holder.greenColor );
            holder.parkingSlot.setText(String.valueOf( slot.slotNumber));

            //holder.layout.setBackground(context.getDrawable(  R.drawable.two_side_rectangle));

        }

        holder.itemView.setOnClickListener(view -> {
            if(slot.slotNumber==-1)//if a space that doesnt belong to parking slot, no action need to be perfomed
                return;
            if(slot.isOccupied && !viewModel.isExit){
                Toast.makeText(context, "Selected slot is occupied. Please select a vaccant slot", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!slot.isOccupied && viewModel.isExit){
                Toast.makeText(context, "Please select an occupied slot to make an exit", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.setParkingSlot(String.valueOf(  slot.slotNumber));

        });

    }
    @Override
    public int getItemViewType(int position) {

        return slots.get(position).isHorizontal? 1 :2;
    }

    @Override
    public int getItemCount() {
        return slots.size();
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }
}
