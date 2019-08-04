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
import com.example.parking.print.BluetoothUtil;
import com.example.parking.print.DataConversionUtility;
import com.example.parking.print.PrintDataUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class MonthlyPrintFragment extends Fragment {

    private MonthlyViewModel mViewModel;
    private Context mContext;

    @BindView(R.id.textViewAddressLine)
    TextView textViewAddressLine;
    @BindView(R.id.textViewAddressLine_2)
    TextView textViewAddressLine_2;
    @BindView(R.id.textViewEmail)
    TextView textViewEmail;
    @BindView(R.id.textViewPhone)
    TextView textViewPhoneClient;
    @BindView(R.id.textViewClientName)
    TextView txtViewClientName;
    @BindView(R.id.qr_code_image)
    ImageView qrCodeImage;
    @BindView(R.id.textViewNumber)
    TextView textViewNumber;
    @BindView(R.id.textViewModel)
    TextView textViewModel;
    @BindView(R.id.textViewStartDate)
    TextView textViewStartDate;
    @BindView(R.id.textViewEndDate)
    TextView textViewEndDate;
    @BindView(R.id.textViewCompany)
    TextView textViewCompany;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewPhoneCustomer)
    TextView textViewPhone;
    @BindView(R.id.fabPrint)
    FloatingActionButton fabPrint;

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
        textViewNumber.setText(mViewModel.monthlyCustomer.vehicle.vehicleNumber);
        textViewModel.setText(mViewModel.monthlyCustomer.vehicle.vehicleModel);
        textViewStartDate.setText(mViewModel.monthlyCustomer.getAppStartDate());
        textViewEndDate.setText(mViewModel.monthlyCustomer.getAppEndDate());
        textViewPhone.setText(mViewModel.monthlyCustomer.phone);
        textViewCompany.setText(mViewModel.monthlyCustomer.company);
        textViewName.setText(mViewModel.monthlyCustomer.name);
        fabPrint.setOnClickListener(s->{printData();});
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
    private void printData(){
        OutputStream os= BluetoothUtil.getOutputStream(mContext);
        if(os==null)
            return;
        try {
            PrintDataUtility.addLineFeed(os,1);
            PrintDataUtility.alignCentre(os);
            PrintDataUtility.writeInBold(os,txtViewClientName .getText().toString()+"\n");
            String text=textViewAddressLine.getText().toString()+"\n"+textViewAddressLine_2.getText().toString()+"\n"+textViewEmail.getText().toString()+"\n"+textViewPhoneClient.getText().toString()+"\n";
            PrintDataUtility.writeInNormal(os,text);
            PrintDataUtility.writeData(os, DataConversionUtility.getByteArrayFromImageView(qrCodeImage));

            PrintDataUtility.alignLeft(os);
            PrintDataUtility.writeInBold(os," Name: ");
            PrintDataUtility.writeInNormal(os,textViewName.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os," Phone: ");
            PrintDataUtility.writeInNormal(os,textViewPhone.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os," Company: ");
            PrintDataUtility.writeInNormal(os,textViewCompany.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os," Vehicle Number: ");
            PrintDataUtility.writeInNormal(os,textViewNumber.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os," Vehicle Model: ");
            PrintDataUtility.writeInNormal(os,textViewModel.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os,"Start Date: ");
            PrintDataUtility.writeInNormal(os,textViewStartDate.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os,"End Date: ");
            PrintDataUtility.writeInNormal(os, textViewEndDate.getText().toString()+"\n");
            PrintDataUtility.addLineFeed(os,3);//todo change
            os.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }



}
