package com.example.parking.data.network.models;

import java.util.List;



public class SyncedData
{
    public String result;
    public String message;
    public String key;
    public int location_id;
    public String account_uuid;
    public BusinessData business_data;
    public ParkingLocationData parking_location_data;
    public List<FloorsData> floors_data;
    public List<PackagesData> packages_data;
    public List<SubscriptionsData> subscriptions_data;
    public List<CustomersData> customers_data;
    public List<VehiclesData> vehicles_data;
    public List<LastRecordData> last_record_data;

    public class LastRecordData
    {
        //public String order_id;
        public long app_order_reference;
        //public object app_order_uuid;
    }

    public class BusinessData
    {
        public String business_name;
        public String support_phone;
        public String support_email;
        public String currency_code;
        public String currency_symbol;
        public String business_logo;
        public String business_trn;
    }

    public class ParkingLocationData
    {
        public String location_name;
        public int available_slots;
        public String parking_floors;
        public String location_address;
        public String location_city;
        public String location_state;
        public String location_postal_code;
        public String per_hour_charge;
    }

    public class FloorsData
    {
        public String floor_title;
        public String available_slots;
        public String slots_list;
    }

    public class PackagesData
    {
        public String package_name;
        public String package_type;
        public String vehicle_count;
        public double package_charges;
        public int package_id;

    }

    public class SubscriptionsData
    {
        public String subscription_id;
        public String customer_id;
        public String package_id;
        public String current_status;
    }

    public class CustomersData
    {
        public String customerid;
        public String customer_reference;
        public String first_name;
        public String middle_name;
        public String last_name;
        public String address1;
        public String address2;
        public String city;
        public String state;
        public String country;
        public String postal_code;
        public String mobile1;
        public String mobile2;
        public String home_phone;
        public String office_phone;
        public String email;
        public String emergency_contact;
        public String grace_period_day;
        public String grace_period_vehicle_count;
        public String added_on;
        public String added_by;
        public String account_uuid;
    }

    public class VehiclesData
    {
        public String customerid;
        public String vehicle_id;
        public String registration_number;
        public String make_model;
        public String registration_year;
        public String vehicle_color;
        public String vehicle_type;
        public String added_on;
        public String added_by;
        public String account_uuid;
    }
}