package com.example.parking.ui.vehicleentry.fragments;

import android.content.Context;
import android.view.*;
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
import com.example.parking.print.BluetoothUtil;
import com.example.parking.print.DataConversionUtility;
import com.example.parking.print.PrintDataUtility;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;
import com.example.parking.utils.StringUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class TicketPrintFragment extends Fragment {

    private VehicleEntryViewModel mViewModel;
    private Context mContext;

    @BindView(R.id.textViewAddressLine)
    TextView textViewAddressLine;
    @BindView(R.id.textViewAddressLine_2)
    TextView textViewAddressLine_2;
    @BindView(R.id.textViewEmail)
    TextView textViewEmail;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;
    @BindView(R.id.textViewClientName)
    TextView txtViewClientName;
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
    @BindView(R.id.textViewEstAmountTag)
    TextView textViewEstAmountTag;
    @BindView(R.id.textViewEstAmount)
    TextView textViewEstAmount;
//    @BindView(R.id.textViewSpecialPriceTag)
//    TextView textViewSpecialPriceTag;
//    @BindView(R.id.textViewSpecialPrice)
//    TextView textViewSpecialPrice;
    @BindView(R.id.textViewChargeTag)
    TextView textViewChargeTag;
    @BindView(R.id.textViewCharge)
    TextView textViewCharge;
    @BindView(R.id.textViewEstHoursTag)
    TextView textViewEstHoursTag;
    @BindView(R.id.textViewEstHours)
    TextView textViewEstHours;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton printButton;
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

       // mViewModel.qrCode.observe(this, s->{
            qrCodeImage.setImageBitmap(mViewModel.qrCode.getValue());

       // });


        mViewModel.getConfigDetails().observe(this, s->{
            txtViewClientName.setText(s.client_name);
            textViewAddressLine.setText(s.client_address_line_1);
            textViewAddressLine_2.setText(s.client_address_line_2);
            textViewEmail.setText(s.client_email!=null?s.client_email:"Email: ******************");//todo
            textViewPhone.setText(s.client_phone!=null?s.client_phone:"Phone: ******************");//todo

        });

        textViewNumber.setText(mViewModel.entry.vehicle.vehicleNumber);
        textViewModel.setText(mViewModel.entry.vehicle.vehicleModel);
        textViewTime.setText(mViewModel.entry.getAppEntryTime());
        textViewSlot.setText(mViewModel.entry.parkingSlot);
        textViewEstHours.setText( String.valueOf( mViewModel.entry.estimatedHours));

        textViewEstAmount.setText(StringUtils.getAmountFormattedWithCurrency( mViewModel.entry.estimatedAmount));//todo
        textViewCharge.setText(StringUtils.getAmountFormattedWithCurrency( mViewModel.entry.hourlyCharge));
       // textViewSpecialPrice.setText( StringUtils.getAmountFormattedWithCurrency(mViewModel.entry.rate));

        //todo deal with monthly plan
        if(mViewModel.isMonthlyPlan){
            textViewExit.setText(mViewModel.entry.getAppExitTime());
            textViewHours.setText(mViewModel.entry.hours);
            textViewAmount.setText(StringUtils.getAmountFormattedWithCurrency( mViewModel.entry.calculatedAmount));
        }else{
            textViewExitTag.setVisibility(View.GONE);
            textViewHoursTag.setVisibility(View.GONE);
            textViewAmountTag.setVisibility(View.GONE);
            textViewExit.setVisibility(View.GONE);
            textViewHours.setVisibility(View.GONE);
            textViewAmount.setVisibility(View.GONE);
        }

        if(mViewModel.entry.estimatedHours<=0){
            textViewEstHours.setVisibility(View.GONE);
            textViewEstHoursTag.setVisibility(View.GONE);
            textViewEstAmount.setVisibility(View.GONE);
            textViewEstAmountTag.setVisibility(View.GONE);
        }
        ((FragmentActivity)  mContext). getActionBar().setTitle("Ticket Preview");

        printButton.setOnClickListener(view -> {
           printData();

    });


    }

    private void printData(){
        OutputStream os= BluetoothUtil.getOutputStream(mContext);
        if(os==null)
            return;
        try {
            PrintDataUtility.addLineFeed(os,1);
            PrintDataUtility.alignCentre(os);
            PrintDataUtility.writeInBold(os, txtViewClientName.getText().toString()+"\n");
            String text=textViewAddressLine.getText().toString()+"\n"+textViewAddressLine_2.getText().toString()+"\n"+textViewEmail.getText().toString()+"\n"+textViewPhone.getText().toString()+"\n";
            PrintDataUtility.writeInNormal(os,text);
            PrintDataUtility.writeData(os, DataConversionUtility.getByteArrayFromImageView(qrCodeImage));

            PrintDataUtility.alignLeft(os);
            PrintDataUtility.writeInBold(os,"Vehicle Number: ");
            PrintDataUtility.writeInNormal(os,textViewNumber.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os,"Vehicle Model: ");
            PrintDataUtility.writeInNormal(os,textViewModel.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os,"Slot Number: ");
            PrintDataUtility.writeInNormal(os,textViewSlot.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os,"Entry Time: ");
            PrintDataUtility.writeInNormal(os,textViewTime.getText().toString()+"\n");
            PrintDataUtility.writeInBold(os,"Charge per hour ");
            PrintDataUtility.writeInNormal(os, textViewCharge.getText().toString()+"\n");

            if(mViewModel.entry.estimatedHours>0) {
                PrintDataUtility.writeInBold(os, "Estimated Hours: ");
                PrintDataUtility.writeInBold(os, textViewEstHours.getText().toString()+"\n");
//                PrintDataUtility.writeInBold(os, "Special Price ");
//                PrintDataUtility.writeInBold(os, textViewSpecialPrice.getText().toString()+"\n");
                PrintDataUtility.writeInBold(os, "Estimated Amount ");
                PrintDataUtility.writeInBold(os, textViewEstAmount.getText().toString()+"\n");
            }

            PrintDataUtility.addLineFeed(os,3);//todo change
            mViewModel.isPrintDone=true;
            os.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

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
