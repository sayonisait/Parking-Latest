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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.databinding.FragmentMonthlyPrintBinding;
import com.example.parking.model.ConfigDetails;
import com.example.parking.print.BluetoothUtil;
import com.example.parking.print.DataConversionUtility;
import com.example.parking.print.PrintDataUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class MonthlyPrintFragment extends Fragment {

    private MonthlyViewModel mViewModel;
    private Context mContext;

    public static MonthlyPrintFragment newInstance() {
        return new MonthlyPrintFragment();
    }

    FragmentMonthlyPrintBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_print, container, false);


        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(MonthlyViewModel.class);
        binding.setViewmodel(mViewModel);
        binding.qrCodeImage.setImageBitmap(mViewModel.qrCode.getValue());
        //binding.layoutHeader.setConfig(mViewModel.configDetails.getValue());
        binding.fabPrint.setOnClickListener(s->{printData();});
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
    public void printData(){
        OutputStream os= BluetoothUtil.getOutputStream(mContext);
        if(os==null)
            return;
        try {
            //Header and qr code
            PrintDataUtility.addLineFeed(os,1);
            PrintDataUtility.alignCentre(os);
            PrintDataUtility.writeInBoldMedium(os,"Monthly Plan");
            PrintDataUtility.writeData(os, DataConversionUtility.getByteArrayFromImageView(binding.qrCodeImage));
            //Print data
            PrintDataUtility.alignLeft(os);
            PrintDataUtility.writeInBold(os,"Name: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.name+"\n");
            PrintDataUtility.writeInBold(os,"Phone: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.phone+"\n");
            PrintDataUtility.writeInBold(os,"Company: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.company+"\n");
            PrintDataUtility.writeInBold(os,"Vehicle Number: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.vehicle.vehicleNumber+"\n");
            PrintDataUtility.writeInBold(os,"Vehicle Model: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.vehicle.vehicleModel+"\n");
            PrintDataUtility.writeInBold(os,"Vehicle Make: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.vehicle.vehicleMake+"\n");
            PrintDataUtility.writeInBold(os,"Start Date: ");
            PrintDataUtility.writeInNormal(os,mViewModel.monthlyCustomer.getAppStartDate()+"\n");
            PrintDataUtility.writeInBold(os,"End Date: ");
            PrintDataUtility.writeInNormal(os, mViewModel.monthlyCustomer.getAppEndDate()+"\n");
            PrintDataUtility.writeInBold(os,"Amount: ");
            PrintDataUtility.writeInNormal(os, mViewModel.monthlyCustomer.amounFormatted+"\n");
            //print company details in footer
            PrintDataUtility.alignCentre(os);
            PrintDataUtility.writeInNormal(os,"------------------------------\n");

            ConfigDetails config=mViewModel.configDetails.getValue();
            PrintDataUtility.writeInBold(os,config.client_name+"\n");
            String text=config.client_address_line_1+"\n"+config.client_address_line_2+"\n"+config.client_email+"\n"+config.client_phone+"\n";
            PrintDataUtility.writeInNormal(os,text);
            PrintDataUtility.addLineFeed(os,3);//todo change

            os.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }



}
