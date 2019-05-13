package com.example.parking.ui.vehicleentry;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;

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
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 11,GridLayoutManager.HORIZONTAL,false));//todo change number of columns
        recyclerView.setAdapter(new ParkingSlotAdapter(mViewModel.getParkingSpaces(),(FragmentActivity) mContext));
        ((FragmentActivity)  mContext). getActionBar().setTitle("Select slot");


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


}
