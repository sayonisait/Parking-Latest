package com.example.parking.data.network;

import com.example.parking.data.network.models.*;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ServiceInterface {

    @POST("authentication/handshake")
    Call<SyncedData> login(@Body RequestBody map);

    @POST("app_calls/push_single_parking_record")
    Call<SaveCheckInResponse> sendTransaction(@Body EntryRequest map);

    @POST("app_calls/data_request")
    Call<TransactionsResponse> getTransactions(@Body TransactionsRequest map);

    @POST("app_calls/push_multi_parking_records")
    Call<MultiSyncingResponse> sendUnSyncedTransactions(@Body MultiSyncingRequest map);

    @POST("app_calls/parking_subscription")
    Call<SubscriptionResponse> sendSubscription(@Body SubscriptionRequest map);

    @POST("app_calls/renew_subscription")
    Call<SubscriptionResponse> renewSubscription(@Body SubscriptionRequest map);
}
