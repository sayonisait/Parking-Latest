package com.example.parking.data.network.models;

public class TransactionsRequest {

    public int location_id;
    public String key;
    public String request_type;
    public RequestContent request_content;

    public class RequestContent
    {
        public String transaction_type;
        public String startdatetime;
        public String enddatetime;
    }
}

