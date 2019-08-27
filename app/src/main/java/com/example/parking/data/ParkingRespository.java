package com.example.parking.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.parking.AppConstants;
import com.example.parking.data.local.ParkingDao;
import com.example.parking.data.local.entities.ConfigTable;
import com.example.parking.data.local.entities.MonthlyCustomerTable;
import com.example.parking.data.network.models.*;
import com.example.parking.data.local.entities.EntryTable;
import com.example.parking.data.local.ParkingDatabase;
import com.example.parking.model.ConfigDetails;
import com.example.parking.model.Entry;
import com.example.parking.data.network.RetrofitClient;
import com.example.parking.data.network.ServiceInterface;
import com.example.parking.model.MonthlyCustomer;
import com.example.parking.utils.StringUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParkingRespository {
   private ParkingDao parkingDao;
    private ConfigDetails configDetails;

    /**CONFIG KEY NAME**/
     public final static String SLOTS_COUNT="slots_count";
    public final static String TOKEN="token";
    public final static String CLIENT_NAME="client_name";
    public final static String CLIENT_ADDRESS_1="client_address_1";
    public final static String CLIENT_ADDRESS_2="client_address_2";
    public final static String CLIENT_EMAIL="client_email";
    public final static String CLIENT_PHONE="client_phone";
    public final static String LAST_RECEIPT_NUMBER="app_id";
    public final static String LOCATION_ID="location_id";
    public final static String MONTHLY_PACKAGE_ID="monthly_package_id";
    public final static String MONTHLY_AMOUNT="monthly_amount";


    public static final String CHECK_IN = "Check-In";
    public static final String VISITOR_CUSTOMER = "Visitor";
    public static final String CHECK_OUT = "Check-Out";


    Context mContext;

    public ParkingRespository(Application application){
        ParkingDatabase database= ParkingDatabase.getDatabase(application);
        parkingDao=database.parkingDao();
        mContext=application.getApplicationContext();


    }

    public LiveData<List<Entry>> getParkedEntries(String vehicleNumberQuery) {
        return Transformations.map(
                        parkingDao.getParkedEntries(vehicleNumberQuery) ,

                s -> {
                    ArrayList<Entry> entries = new ArrayList<>();
                    for (EntryTable entryTable : s) {
                        entries.add(new Entry(entryTable));
                    }
                    return entries;
                });
    }


    public LiveData<List<String>>   getOccupiedSlots(){
       return parkingDao.getParkedSlots();
    }



    public LiveData<ConfigDetails> getConfig(){
        LiveData<List<ConfigTable>> rows=parkingDao.getConfig();

        return  Transformations.map(rows,s->{
            Log.d("Parking Info","Fetched rows from config");
           configDetails= new ConfigDetails();
           for(ConfigTable row : s){
               switch (row.keyField){
                   case TOKEN: configDetails.token=row.value;break;
                   case CLIENT_ADDRESS_1: configDetails.client_address_line_1=row.value;break;
                   case CLIENT_ADDRESS_2: configDetails.client_address_line_2=row.value;break;
                   case CLIENT_EMAIL: configDetails.client_email=row.value;break;
                   case CLIENT_PHONE: configDetails.client_phone=row.value;break;
                   case CLIENT_NAME: configDetails.client_name=row.value;break;
                   case SLOTS_COUNT: configDetails.slots_count= Integer.parseInt(row.value);break;
                   case LAST_RECEIPT_NUMBER: configDetails.last_receipt_number= Long.parseLong(row.value);break;
                   case LOCATION_ID: configDetails.location_id= Integer.parseInt(row.value);break;
                   case MONTHLY_PACKAGE_ID:configDetails.monthly_package_id=Integer.parseInt(row.value);break;
                   case MONTHLY_AMOUNT:configDetails.monthly_package_amount=Double.parseDouble(row.value);break;
               }
           }
           return configDetails;

        });
    }

    public LiveData<ConfigTable> getToken(){
      return parkingDao.getConfigValue(TOKEN);
    }

    public LiveData<String> getParkingCount(){
        return parkingDao.getValue(SLOTS_COUNT);
    }

    private void saveEntry(Entry entry, boolean synedToBackend){
       EntryTable table = new EntryTable();
       if(entry.exitTime!=null ){ //if exit time presents
           table.hours=entry.hours;
           table.exitTime=  entry.getServerExitTime();
           table.calculatedAmount=entry.calculatedAmount;
           table.amountToBePaid=entry.amountToBePaid;
       }
           table.estimatedAmount=entry.estimatedAmount;
           table.estimatedHours=entry.estimatedHours;
           table.hourlyCharge=entry.hourlyCharge;
           table.parkingSlot=entry.parkingSlot;
          // table.rate=entry.rate;
           table.vehicleNumber=entry.vehicle.vehicleNumber;
           table.vehicleModel=entry.vehicle.vehicleModel;
           table.entryTime=entry.getServerEntryTime();
            table.phoneNumber=entry.phoneNumber;
        System.out.println("UUID saved to sqlite is "+entry.transaction_id);
        table.uid=entry.transaction_id;
        table.receiptNumber=entry.receipt_number;
       table.serverID=entry.serverID;
       table.syncedToBackend=synedToBackend;
       new InsertEntryTask(parkingDao).execute(table);
    }

    private void saveMonthlyCustomer(MonthlyCustomer plan){
        new InsertMonthlyTask(parkingDao).execute(getMonthlyCustomerTable(plan));
    }

    private MonthlyCustomerTable  getMonthlyCustomerTable(MonthlyCustomer customer){
            MonthlyCustomerTable table = new MonthlyCustomerTable();
        table.customerName=customer.name;
         table.startDate=customer.getServerStartDate();
         table. endDate=customer.getServerEndDate();
        table . secondary_phone=customer.secondaryNumber;
        table . customerPhone=customer.phone;
        table .    company=customer.company;
        table .     grace_period=customer.gracePeriodDays;
        table .     is_not_paid=customer.isNotPaid;
        table .     vehicle_make=customer.vehicle.vehicleMake;
        table .     vehicleModel=customer.vehicle.vehicleModel;
        table .     vehicleNumber=customer.vehicle.vehicleNumber;
        table .     receipt_number=customer.receiptNumber;
        table .     subscription_date=customer.subscriptionDate;
        table .     server_id=customer.serverID;
        return table;


    }


    private static MutableLiveData<String> insertedRowId, insertedMonthlyRowId;

    private static class InsertEntryTask extends AsyncTask<EntryTable,Void,String>{
        private ParkingDao parkingDao;
        InsertEntryTask(ParkingDao dao){
            parkingDao=dao;
        }
        long id;
        @Override
        protected String doInBackground(EntryTable... entries) {

            parkingDao.insertEntry(entries[0]);
            Log.d(AppConstants.LOG,"Udpating receipt number as "+entries[0].receiptNumber);
            parkingDao.insertConfig(new ConfigTable(LAST_RECEIPT_NUMBER, String.valueOf(entries[0].receiptNumber)));
//                Log.d(AppConstants.LOG,"Udpating receipt number successful ");
//            else
//            Log.d(AppConstants.LOG,"Udpating receipt number failed ");;

            return entries[0].uid;
        }

        @Override
        protected void onPostExecute(String id) {
            super.onPostExecute(id);
            insertedRowId.setValue(id);

        }
    }


    private void insertConfig(ConfigDetails config){
        new InsertConfigTask(parkingDao).execute(config);
    }



    public  MutableLiveData<String>  sendEntryToBackend(Entry entry) {
        insertedRowId=new MutableLiveData<>();
        EntryRequest request = new EntryRequest();
        request.location_id=configDetails.location_id;
        request.key=configDetails.token;
        request.request_content=new Transaction(entry);
        request.request_type="push_parking_record";
        ServiceInterface service = RetrofitClient.getRetrofitInstance(mContext).create(ServiceInterface.class);

            service.sendTransaction(request).enqueue(new Callback<SaveCheckInResponse>() {
                @Override
                public void onResponse(Call<SaveCheckInResponse> call, Response<SaveCheckInResponse> response) {
                    SaveCheckInResponse data = response.body();
                    if (response.isSuccessful() && data.result.equals("success")) {
                        entry.serverID = data.order_id;
                        Log.d("Parking Info", "Saved in server. transaction_id is" + entry.serverID);
                        saveEntry(entry, true);
                    }else {
                        Log.d("Parking Info", "Could not save in server");
                        saveEntry(entry, false);
                    }

                }

                @Override
                public void onFailure(Call<SaveCheckInResponse> call, Throwable t) {
                    Log.d("Parking Info", "Culd not save in server");
                    saveEntry(entry,false);
                }
            });


        return insertedRowId;

    }


    public MutableLiveData<String> saveSubscription(MonthlyCustomer customer){
        insertedMonthlyRowId= new MutableLiveData<>();
        SubscriptionRequest request= new SubscriptionRequest(customer,configDetails);
        ServiceInterface service = RetrofitClient.getRetrofitInstance(mContext).create(ServiceInterface.class);

        service.sendSubscription(request).enqueue(new Callback<SubscriptionResponse>() {
            @Override
            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {
                SubscriptionResponse data = response.body();
                if (response.isSuccessful() && data.result.equals("success")) {
                    customer.serverID = data.subscription_id;
                    customer.subscriptionPaymentID=data.subscription_payment_id;
                    customer.customerID=data.customer_id;
                    customer.vehicleID=data.vehicle_id;
                    Log.d("Parking Info", "Saved in server. transaction_id is" + customer.serverID);
                    saveMonthlyCustomer(customer);

                }else {
                    insertedMonthlyRowId.setValue("failure");
                    Log.d("Parking Info", "Could not save in server");
                }



            }

            @Override
            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                Log.d("Parking Info", "Culd not save in server "+t.getLocalizedMessage());
                insertedMonthlyRowId.setValue("failure");
            }
        });
        return insertedMonthlyRowId;
    }

    private static class InsertConfigTask extends AsyncTask<ConfigDetails,Void,Void>{
        private ParkingDao parkingDao;
        InsertConfigTask(ParkingDao dao){
            parkingDao=dao;
        }
        @Override
        protected Void doInBackground(ConfigDetails... entries) {
            ConfigDetails details = entries[0];
            ArrayList<ConfigTable> rows=new ArrayList<>();
            rows.add(new ConfigTable(TOKEN,details.token));
            rows.add(new ConfigTable(CLIENT_NAME,details.client_name));
            rows.add(new ConfigTable(CLIENT_ADDRESS_1,details.client_address_line_1));
            rows.add(new ConfigTable(CLIENT_ADDRESS_2,details.client_address_line_2));
            rows.add(new ConfigTable(CLIENT_EMAIL,details.client_email));
            rows.add(new ConfigTable(CLIENT_PHONE,details.client_phone));
            rows.add(new ConfigTable(SLOTS_COUNT, String.valueOf( details.slots_count)));
            Log.d(AppConstants.LOG,"SAving receiptnumber as "+details.last_receipt_number);
            rows.add(new ConfigTable(LAST_RECEIPT_NUMBER, String.valueOf( details.last_receipt_number)));
            rows.add(new ConfigTable(LOCATION_ID, String.valueOf( details.location_id)));
            rows.add(new ConfigTable(MONTHLY_AMOUNT, String.valueOf( details.monthly_package_amount)));
            rows.add(new ConfigTable(MONTHLY_PACKAGE_ID, String.valueOf( details.monthly_package_id)));

            parkingDao.insertConfig(rows);
            return null;
        }
    }

    public MutableLiveData<Boolean> login(String username, String password) {

        MutableLiveData<Boolean> loginSuccessful= new MutableLiveData<>();
        /*Create handle for the RetrofitInstance interface*/
        ServiceInterface service = RetrofitClient.getRetrofitInstance(mContext).create(ServiceInterface.class);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account_secret", "1234567890")//todo remove hardcoded
                .addFormDataPart("username", username).addFormDataPart("password", password)
                .build();
        Call<SyncedData> call = service.login(requestBody);
        call.enqueue(new Callback<SyncedData>() {
            @Override
            public void onResponse(Call<SyncedData> call, Response<SyncedData> response) {
                SyncedData data = response.body();
                if (response.isSuccessful() && data.result.equals("success")) {
                    ConfigDetails details = new ConfigDetails();
                    details.token=data.key;

                    SyncedData.ParkingLocationData locationData= data.parking_location_data;

                    //slots count
                    details.slots_count=locationData.available_slots;

                    //name of client
                    details.client_name=data.business_data.business_name;

                    //contact details
                    if(data.business_data.support_email!=null)
                        details.client_email=data.business_data.support_email;
                    if(data.business_data.support_phone!=null)
                        details.client_phone+=data.business_data.support_phone;

                    //address details
                    details.client_address_line_1=locationData.location_name
                            +", "+locationData.location_address;
                    if(locationData.location_city!=null)
                        details.client_address_line_2=locationData.location_city;
                    if(locationData.location_state!=null)
                        details.client_address_line_2+=", "+locationData.location_state;
                    details.last_receipt_number=data.last_record_data.get(0).app_order_reference;
                    details.monthly_package_amount=data.packages_data.get(0).package_charges;
                    details.monthly_package_id=data.packages_data.get(0).package_id;

                    // insert data in database

                    details.location_id=data.location_id;
                    insertConfig(details);
                    loginSuccessful.setValue(true);
                }else
                    loginSuccessful.setValue(false);


            }

            @Override
            public void onFailure(Call<SyncedData> call, Throwable t) {
                loginSuccessful.setValue(false);            }
        });

        return loginSuccessful;
    }


    private static class InsertMonthlyTask extends AsyncTask<MonthlyCustomerTable,Void,String>{
        private ParkingDao parkingDao;
        InsertMonthlyTask(ParkingDao dao){
            parkingDao=dao;
        }
        long id;
        @Override
        protected String doInBackground(MonthlyCustomerTable... entries) {
            if(entries[0].uid==null || entries[0].uid.trim().equals("")) {
                entries[0].uid= UUID.randomUUID().toString();
                System.out.println("Parking Info :"+entries[0].uid +" is the id of the record inserted");
                 parkingDao.insertMonthly(entries[0]);
            }
            else
                parkingDao.updateMonthly(entries[0]);
            return entries[0].uid;
        }

        @Override
        protected void onPostExecute(String id) {
            super.onPostExecute(id);
            insertedMonthlyRowId.setValue(id);
            Log.d("Parking Info","Saved monthly to sqlite . Inserted transaction_id is " + id);

        }
    }

    public MutableLiveData<Integer> fetchTransactions(String startDateTime, String endDateTime) {

        MutableLiveData<Integer> transactionCount= new MutableLiveData<>();
        /*Create handle for the RetrofitInstance interface*/
        ServiceInterface service = RetrofitClient.getRetrofitInstance(mContext).create(ServiceInterface.class);
        TransactionsRequest request = new TransactionsRequest();
        request.key=configDetails.token;
        request.location_id=configDetails.location_id;
        request.request_type="transaction";
        request.request_content=request.new RequestContent();
        request.request_content.startdatetime=startDateTime;
        request.request_content.enddatetime=endDateTime;
        request.request_content.transaction_type="transaction";
        Call<TransactionsResponse> call = service.getTransactions(request);
        call.enqueue(new Callback<TransactionsResponse>() {
            @Override
            public void onResponse(Call<TransactionsResponse> call, Response<TransactionsResponse> response) {
                TransactionsResponse data = response.body();
                if (response.isSuccessful() ) {

                    transactionCount.setValue(data.response_data.size());

                    //todo do batch insert
                }


            }

            @Override
            public void onFailure(Call<TransactionsResponse> call, Throwable t) {
                transactionCount.setValue(0);          }
        });

        return transactionCount;
    }


    public LiveData<List<EntryTable>> getUnSyncedEntries(){
        return   Transformations.map(parkingDao.getUnSyncedEntries(),s->{
            if(s==null |s.size()==0)
                return s;
            MultiSyncingRequest request= new MultiSyncingRequest();
            request.location_id=configDetails.location_id;
            request.request_type="data_syncing";
            request.serverkey=configDetails.token;
            request.request_content=request.new RequestContent();

            request.request_content.transactions= new ArrayList<>();
            for(EntryTable table : s)
                request.request_content.transactions.add( new Transaction(table));
            ServiceInterface service = RetrofitClient.getRetrofitInstance(mContext).create(ServiceInterface.class);
            Call<MultiSyncingResponse> call = service.sendUnSyncedTransactions(request);
            call.enqueue(new Callback<MultiSyncingResponse>() {
                @Override
                public void onResponse(Call<MultiSyncingResponse> call, Response<MultiSyncingResponse> response) {
                    MultiSyncingResponse data = response.body();
                    if (response.isSuccessful() ) {

                        //transactionCount.setValue(data.synced_uuids.size());


                    }


                }

                @Override
                public void onFailure(Call<MultiSyncingResponse> call, Throwable t) {
                    //transactionCount.setValue(0);
                }
            });
            return s;
        });
    }

//    public MutableLiveData<Integer> sendEntriesToBackend(List<EntryTable> entries){
//
//        MutableLiveData<Integer> transactionCount= new MutableLiveData<>();
//
//         Transformations.map(getUnSyncedEntries(),s->{
//
//            MultiSyncingRequest request= new MultiSyncingRequest();
//            request.location_id=configDetails.location_id;
//            request.request_type="data_syncing";
//            request.serverkey=configDetails.token;
//            request.request_content=request.new RequestContent();
//
//            request.request_content.transactions= new ArrayList<>();
//            for(EntryTable table : s)
//                request.request_content.transactions.add( new Transaction(table));
//          ServiceInterface service = RetrofitClient.getRetrofitInstance(mContext).create(ServiceInterface.class);
//          Call<MultiSyncingResponse> call = service.sendUnSyncedTransactions(request);
//          call.enqueue(new Callback<MultiSyncingResponse>() {
//              @Override
//              public void onResponse(Call<MultiSyncingResponse> call, Response<MultiSyncingResponse> response) {
//                  MultiSyncingResponse data = response.body();
//                  if (response.isSuccessful() ) {
//
//                      transactionCount.setValue(data.synced_uuids.size());
//
//
//                  }
//
//
//              }
//
//              @Override
//              public void onFailure(Call<MultiSyncingResponse> call, Throwable t) {
//                  transactionCount.setValue(0);          }
//          });
//          return s;
//        });
//        return transactionCount;
//    }
}
