package com.example.parking.data.network.models;


import com.example.parking.model.ConfigDetails;
import com.example.parking.model.MonthlyCustomer;

public class SubscriptionRequest {
    public int location_id;
    public String serverkey;
    public String request_type;
    public RequestContent request_content;

    public class CustomerData {
        public String customer_reference="";
        public String customer_name;
        public String address_line="";
        public String area="";
        public String city="";
        public String state="";
        public String country="";
        public String mobile;
        public String emergency_contact="";
        public String company_name="";
        public int grace_period_days;
    }

    public class VehicleData {
        public String registration_number;
        public String vehicle_make;
        public String vehicle_model;
        public String vehicle_color="";
        public String vehicle_type="";
        public String registration_year="";
    }

    public class SubscriptionData {
        public int package_id;
        public String start_date;
        public String expiry_date;
        public int added_by=1;
        public String payment_status;
    }

    public class PaymentData {
        public long app_order_reference;
        public String app_order_uuid="";
        public String payment_method="cash";
        public double amount_paid;
    }

    public class RequestContent {
        public CustomerData customer_data;
        public VehicleData vehicle_data;
        public SubscriptionData subscription_data;
        public PaymentData payment_data;
    }

    public SubscriptionRequest(MonthlyCustomer customer, ConfigDetails details){
        this.location_id=details.location_id;
        this.request_type="parking_subscription";
        this.serverkey=details.token;
        this.request_content= new RequestContent();
        this.request_content.customer_data= new CustomerData();
        request_content.customer_data.company_name=customer.company!=null?customer.company:"";
        request_content.customer_data.emergency_contact=customer.secondaryNumber!=null?customer.secondaryNumber:"";
        request_content.customer_data.mobile=customer.phone;
        request_content.customer_data.customer_name=customer.name;
        request_content.subscription_data=new SubscriptionData();
        request_content.subscription_data.start_date=customer.getServerStartDate();
        request_content.subscription_data.expiry_date=customer.getServerEndDate();
        request_content.subscription_data.package_id=details.monthly_package_id;

        if(!customer.isNotPaid) {
            request_content.payment_data = new PaymentData();
            request_content.payment_data.amount_paid = customer.amount;
            request_content.payment_data.app_order_reference = customer.receiptNumber;
            request_content.subscription_data.payment_status="Paid";
        }else{
            request_content.subscription_data.payment_status="Not Paid";
            request_content.customer_data.grace_period_days=customer.gracePeriodDays;
        }
        request_content.vehicle_data= new VehicleData();
        request_content.vehicle_data.registration_number=customer.vehicle.vehicleNumber;
        request_content.vehicle_data.vehicle_make=customer.vehicle.vehicleMake;
        request_content.vehicle_data.vehicle_model=customer.vehicle.vehicleModel;

    }



}

