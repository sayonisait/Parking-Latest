package com.example.parking.ui.vehicleentry.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.*;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.parking.R;
import com.example.parking.model.Entry;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;
import com.example.parking.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class VehicleEntryFragment extends Fragment {

    private VehicleEntryViewModel mViewModel;



    @BindView(R.id.textInputHourlyCharge)
    TextView textInputHourlyCharge;
    @BindView(R.id.editTextHourlyCharge)
    TextView editTextHourlyCharge;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;




    @BindView(R.id.editTextName)
    MaterialEditText editTextName;


    @BindView(R.id.editTextModel)
    MaterialAutoCompleteTextView editTextModel;
    @BindView(R.id.editTextNumber)
    MaterialEditText editTextNumber;
    @BindView(R.id.editTextEntryTime)
    TextView editTextTime;

    @BindView(R.id.editTextSlot)
    MaterialEditText editTextSlot;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


//    @BindView(R.id.editTextEstAmount)
//    EditText editTextEstAmount;
//    @BindView(R.id.textInputEstAmount)
//    TextInputLayout textInputEstAmount;
//    @BindView(R.id.editTextEstHours)
//    EditText editTextEstHours;
//    @BindView(R.id.editTextHours)
//    EditText editTextHours;
//
//    @BindView(R.id.textInputEstHours)
//    TextInputLayout textInputEstHours;
//@BindView(R.id.editTextAmountCalculated)
//EditText editTextAmountCalculated;
//    @BindView(R.id.editTextAmount)
//    EditText editTextAmount;
//@BindView(R.id.textInputExitime)
//TextInputLayout textInputExitTime;
//    @BindView(R.id.editExitTime)
//    EditText editExitTime;

    public static VehicleEntryFragment newInstance() {
        return new VehicleEntryFragment();
    }
    private Context mContext;
//     NavController navController;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_entry, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
//        navController = Navigation.findNavController(view);
        return view;

    }




    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(VehicleEntryViewModel.class);

        editTextModel.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.models)));

        //if entry is created, then display
        if(mViewModel.entry!=null) {
            editTextTime.setText(mViewModel.entry.getAppEntryTime());
            editTextHourlyCharge.setText(StringUtils.getAmountFormattedWithCurrency(mViewModel.entry.hourlyCharge));
        }


        initToolBar();



        //on touching on parking slot field, navigates to parking slots listing screen

        editTextSlot.setOnClickListener(view -> {
           Navigation.findNavController(view) .navigate(R.id.parkingSlot);
        });


        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
            String text;
            //if field is the field for vehicle model , then its autocomplete field not normal edit field
            if (v.getId() == R.id.editTextModel)
                text = ((AutoCompleteTextView) v).getText().toString();
            else text = Objects.requireNonNull(((MaterialEditText) v).getText()).toString();
            if (hasFocus || text.equals(""))
                return;
            //todo check why
            switch (v.getId()) {
                case R.id.editTextNumber:
                    mViewModel.entry.vehicle.vehicleNumber = text;
                   // textInputNumber.setError(null);
                    break;
                case R.id.editTextModel:
                    mViewModel.entry.vehicle.vehicleModel = text;
                    editTextModel.setError(null);
                    break;
                case R.id.editTextSlot:
                    mViewModel.entry.parkingSlot = text;
                    break;

//                case R.id.editTextEstHours:
//                    mViewModel.setEstHours(text);
//                   textInputEstHours.setError(null);
//
//                    break;
//                case R.id.editTextEstAmount:
//                   editTextEstAmount.setText( mViewModel.getAmountFormatted(text));
//                   textInputEstAmount.setError(null);
//                   break;
                case R.id.editTextPhone:
                    mViewModel.entry.phoneNumber=text;
                    break;
                case R.id.editTextName:
                    mViewModel.entry.name=text;
                    break;

            }

        };

        editTextNumber.setOnFocusChangeListener(focusChangeListener);
        editTextModel.setOnFocusChangeListener(focusChangeListener);
        //todo commented two lines
//        editTextEstAmount.setOnFocusChangeListener(focusChangeListener);
//        editTextEstHours.setOnFocusChangeListener(focusChangeListener);

        //when slot number is set after selected by user, display in edit text
        mViewModel.slotNumber.observe(this, s -> {
            editTextSlot.setText(mViewModel.slotNumber.getValue());
        });

        //todo check the need of this onbserver
        mViewModel.getConfigDetails().observe(this, s->{
            Log.d("Parking Info","got config info");
            //todo move this code to relevant screen
//            mViewModel.getTransactions().observe(this, d->{
//                Toast.makeText(mContext, "Fetched "+d+" transactions", Toast.LENGTH_SHORT).show();
//            });

        });




        buttonSubmit.setOnClickListener(view -> {

            boolean isValidated = true;
            if (((FragmentActivity) mContext).getCurrentFocus() != null)
                ((FragmentActivity) mContext).getCurrentFocus().clearFocus();

            if (editTextModel.getText().toString().trim().equals("")) {
                editTextModel.setError("Field can not be empty");
                isValidated = false;
            }

            if (editTextNumber.getText().toString().trim().equals("")) {
                editTextNumber.setError("Field can not be empty");
                isValidated = false;
            }


            //todo commented two lines

//            // if estimated amount is entered then estimated hours is also mandatory
//            if (editTextEstHours.getText().toString().trim().equals("") && !editTextEstAmount.getText().toString().trim().equals("")) {
//                textInputEstHours.setError("Field can not be empty");
//                isValidated = false;
//            }
//
//            // if estimated hours is entered then estimated amount is also mandatory
//            if (!editTextEstHours.getText().toString().trim().equals("") && editTextEstAmount.getText().toString().trim().equals("")) {
//                textInputEstAmount.setError("Field can not be empty");
//                isValidated = false;
//            }


            //validating special charge not greater than actual charge

//            if (mViewModel.entry.rate > mViewModel.entry.hourlyCharge) {
//                textInputSpecialCharge.setError("Special Charge cant be greater than actual charge");
//                isValidated = false;
//            }

            if (isValidated) {

                // once all validations are done , go to print screen to print all details
              //todo uncomment while implementting monthly plan ......  if (monthlyPlan == null) {
                  ProgressDialog dialog=  showProgress();
                    //saves and generates qr code
                    mViewModel.saveEntry().observe(this, s -> {
                      //  go to print screen to take out a print
                      Navigation.findNavController(view).navigate(R.id.action_entry_print);

                        dialog.dismiss();
                    });


//                } else if (mViewModel.isMonthlyPlan) {
//                    NavHostFragment.findNavController(VehicleEntryFragment.this).navigate(R.id.action_entry_print);
//                } else
//                    //todo make API call to submit the entry or save in room
//                    ((FragmentActivity) mContext).finish();
            }
        });
        //((FragmentActivity) mContext).getActionBar().setTitle("Regular Entry");

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

    @Override
    public void onResume() {
        super.onResume();

    }

    private ProgressDialog showProgress(){
        // Initialize a new instance of progress dialog
        final ProgressDialog pd = new ProgressDialog(mContext, R.style.MyDialogTheme);
        // Set progress dialog style horizontal

        // Set the progress dialog background color transparent
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pd.setCancelable(false);
        // Finally, show the progress dialog
        pd.show();
        return pd;

    }

    public void  initialiseFieldsForExit(Entry s){
        // disabling parking slot field during exit
        editTextSlot.setClickable(false);
       // editTextSlot.setFocusable(false);
        editTextTime.setText(s.getAppEntryTime());
        editTextSlot.setText(s.parkingSlot);
        editTextHourlyCharge.setText(StringUtils.getAmountFormattedWithCurrency(s.hourlyCharge));
        editTextPhone.setText(s.phoneNumber);
        editTextNumber.setText(s.vehicle.vehicleNumber);
        editTextModel.setText(s.vehicle.vehicleModel);


//        editExitTime.setText(s.getAppExitTime());
//        editTextHours.setText(s.hours);
//        editTextEstAmount.setFocusable(false);
//        editTextEstHours.setFocusableInTouchMode(false);
//        editTextEstHours.setFocusable(false);
//        editTextEstAmount.setFocusableInTouchMode(false);
//        editTextAmountCalculated.setText(StringUtils.getAmountFormatted(s.calculatedAmount));
//        editTextAmount.setText(StringUtils.getAmountFormatted(s.amountToBePaid));
//        if (s.estimatedHours != 0)
//            editTextEstHours.setText(StringUtils.getAmountFormatted(s.estimatedHours));
//        if (s.estimatedAmount != 0)
//            editTextEstAmount.setText(String.valueOf(s.estimatedAmount));
    }


    private void   initToolBar(){
        getActivity().setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(v ->getActivity().onBackPressed() );
    }


}
