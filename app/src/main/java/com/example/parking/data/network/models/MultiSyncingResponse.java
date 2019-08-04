package com.example.parking.data.network.models;

import java.util.List;

public class MultiSyncingResponse
{
    public String result;
    public String message;
    public List<String> synced_uuids;
    public List<String> not_synced_uuids;
}
