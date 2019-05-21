package com.example.parking.ui.monthly;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;

public class MonthlyPrintFragment extends Fragment {

    private MonthlyViewModel mViewModel;
    private Context mContext;

    @BindView(R.id.qr_code_image)
    ImageView qrCodeImage;
    @BindView(R.id.textViewNumber)
    TextView textViewNumber;
    @BindView(R.id.textViewModel)
    TextView textViewModel;
    @BindView(R.id.textViewSlot)
    TextView textEndDate;
    @BindView(R.id.textViewTime)
    TextView textViewTime;

    public static MonthlyPrintFragment newInstance() {
        return new MonthlyPrintFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_monthly_print, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(MonthlyViewModel.class);
        qrCodeImage.setImageBitmap(mViewModel.qrCode.getValue());
        textViewNumber.setText(mViewModel.monthlyPlan.vehicle.vehicleNumber);
        textViewModel.setText(mViewModel.monthlyPlan.vehicle.vehicleModel);
       textViewTime.setText(mViewModel.monthlyPlan.startDate);
        textEndDate.setText(mViewModel.monthlyPlan.endDate);
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
