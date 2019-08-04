package com.example.parking.data.network.models;

import java.util.List;

public class MultiSyncingRequest {
    public int location_id;
    public String serverkey;
    public String request_type;
    public RequestContent request_content;

    public class RequestContent
    {
        public List<Transaction> transactions;
        //public List<Object> monthly_vehciles; // todo add vehicles
    }
}
