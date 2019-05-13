package com.example.parking.ui.vehicleentry;

import android.content.Context;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;

public class TicketPrintFragment extends Fragment {

    private VehicleEntryViewModel mViewModel;
    private Context mContext;

    @BindView(R.id.qr_code_image)
    ImageView qrCodeImage;
    @BindView(R.id.textViewNumber)
    TextView textViewNumber;
    @BindView(R.id.textViewModel)
    TextView textViewModel;
    @BindView(R.id.textViewSlot)
    TextView textViewSlot;
    @BindView(R.id.textViewTime)
    TextView textViewTime;

    public static TicketPrintFragment newInstance() {
        return new TicketPrintFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_entry_print, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(VehicleEntryViewModel.class);
        qrCodeImage.setImageBitmap(mViewModel.qrCode.getValue());
        textViewNumber.setText(mViewModel.entry.vehicleNumber);
        textViewModel.setText(mViewModel.entry.vehicleModel);
       textViewTime.setText(mViewModel.entry.entryTime);
        textViewSlot.setText(mViewModel.entry.parkingSlot);
        ((FragmentActivity)  mContext). getActionBar().setTitle("Ticket Preview");


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
