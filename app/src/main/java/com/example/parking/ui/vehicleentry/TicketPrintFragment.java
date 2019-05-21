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
    @BindView(R.id.textViewExitTime)
    TextView textViewExit;
    @BindView(R.id.textViewExitTimeTag)
    TextView textViewExitTag;
    @BindView(R.id.textViewHours)
    TextView textViewHours;
    @BindView(R.id.textViewHoursTag)
    TextView textViewHoursTag;
    @BindView(R.id.textViewAmount)
    TextView textViewAmount;
    @BindView(R.id.textViewAmountTag)
    TextView textViewAmountTag;

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
        if(mViewModel.qrCode.getValue()!=null)
            qrCodeImage.setImageBitmap(mViewModel.qrCode.getValue());
        else
            qrCodeImage.setVisibility(View.GONE);
        textViewNumber.setText(mViewModel.entry.vehicle.vehicleNumber);
        textViewModel.setText(mViewModel.entry.vehicle.vehicleModel);
        textViewTime.setText(mViewModel.entry.entryTime);
        textViewSlot.setText(mViewModel.entry.parkingSlot);
        if(mViewModel.isExit){
            textViewExit.setText(mViewModel.entry.exitTime);
            textViewHours.setText(mViewModel.entry.hours);
            textViewAmount.setText(mViewModel.entry.charge);
        }else{
            textViewExitTag.setVisibility(View.GONE);
            textViewHoursTag.setVisibility(View.GONE);
            textViewAmountTag.setVisibility(View.GONE);
            textViewExit.setVisibility(View.GONE);
            textViewHours.setVisibility(View.GONE);
            textViewAmount.setVisibility(View.GONE);
        }
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
