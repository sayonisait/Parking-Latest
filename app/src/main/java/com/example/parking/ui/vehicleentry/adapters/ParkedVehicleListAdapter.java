package com.example.parking.ui.vehicleentry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parking.R;
import com.example.parking.databinding.RowParkedVehicleBinding;
import com.example.parking.model.Entry;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;

import java.util.List;

public class ParkedVehicleListAdapter extends RecyclerView.Adapter<ParkedVehicleListAdapter.ViewHolder> {

    List<Entry> entries;
    LayoutInflater inflater;
    private VehicleEntryViewModel viewModel;

    public ParkedVehicleListAdapter(List<Entry> entries, FragmentActivity context){
        this.entries=entries;
        viewModel= ViewModelProviders.of(context).get(VehicleEntryViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(inflater==null){
            inflater= LayoutInflater.from(parent.getContext());
        }
        RowParkedVehicleBinding binding=  DataBindingUtil.inflate(inflater, R.layout.row_parked_vehicle, parent,false);
        return new ParkedVehicleListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setParkedVehicle(entries.get(position));
        holder.binding.linearLayout.setOnClickListener(s->{viewModel.setSelectedEntry(entries.get(position));});
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final RowParkedVehicleBinding binding;


        public ViewHolder(RowParkedVehicleBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }


    }
}
