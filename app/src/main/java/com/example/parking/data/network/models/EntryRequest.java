package com.example.parking.data.network.models;

import com.google.gson.annotations.SerializedName;

public class EntryRequest {
    @SerializedName("location_id")
    public int location_id;
    @SerializedName("key")
    public String key;
    @SerializedName("request_type")
    public String request_type;
    @SerializedName("request_content")

    public Transaction request_content;

}
