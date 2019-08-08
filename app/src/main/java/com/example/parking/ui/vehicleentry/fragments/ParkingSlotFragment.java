package com.example.parking.ui.vehicleentry.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.ui.vehicleentry.adapters.ParkingSlotAdapter;
import com.example.parking.ui.vehicleentry.viewmodels.ParkingSlotViewModel;

import java.util.List;

public class ParkingSlotFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context mContext;

    public static ParkingSlotFragment newInstance() {
        return new ParkingSlotFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.parking_slot_fragment, container, false);
        ButterKnife.bind(this,view);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ParkingSlotViewModel mViewModel = ViewModelProviders.of(this).get(ParkingSlotViewModel.class);
        LiveData<String> slotsCOunt = mViewModel.getParkingSlotsCount();
        LiveData<List<String>> occupiedSlots= mViewModel.getOccupiedSlots();
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 9, GridLayoutManager.VERTICAL, false));
        ParkingSlotAdapter adapter=new ParkingSlotAdapter(0, (FragmentActivity) mContext);
        recyclerView.setAdapter(adapter);

        slotsCOunt.observe(this, s -> {
            adapter.setCount(Integer.parseInt(slotsCOunt.getValue()));
        });

        occupiedSlots.observe(this, s->{
            adapter.setOccupiedSlots(occupiedSlots.getValue());
        });


       // ((FragmentActivity) mContext).getActionBar().setTitle("Select slot");


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem scan=menu.add("scan");
        scan.setIcon(R.drawable.ic_qr_code);
        scan.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }
}
