package com.example.retrofitapp.Paytm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.retrofitapp.R;
import com.example.retrofitapp.Webservice.WebService;
import com.paytm.pgsdk.PaytmOrder;

import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PaytmActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {
Button btnPay;
    private static final String TAG = "PaytmActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        btnPay=findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCheckSum();
            }
        });
    }

    private void generateCheckSum() {
        /*
        //getting the tax amount first.
        String txnAmount = "10";

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constant.M_ID,
                Constant.Channel_ID,
                txnAmount,
                Constant.Website,
                Constant.Callback_URL,
                Constant.Industry_Type_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                WebService service=new WebService();
                final Paytm paytm = new Paytm(
                        Constant.M_ID,
                        Constant.Channel_ID,
                        "12",
                        Constant.Website,
                        Constant.Callback_URL,
                        Constant.Industry_Type_ID
                );
            String checksum=
            service.getChecksum(Constant.M_ID,paytm.getOrderId(), "1EnkbYPm9iKOJEGW");
            Log.d("PaytmActivity","Checksum"+checksum);
                initializePaytmPayment(checksum,paytm);
            }
        }).start();

    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {
        String result = checksumHash.replaceAll("^\"+|\"+$", "");
        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/theia/processTransaction");

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constant.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", result);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());

        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this,
                true, true,
                PaytmActivity.this);


       /* PaytmOrder paytmOrder = new PaytmOrder( paytm.getOrderId()
                ,Constant.M_ID, result, "10", Constant.Callback_URL);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, this);
        transactionManager.startTransaction(this, 101);*/
    }


    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        Toast.makeText(this, "Payment Transaction response:"+bundle.toString(), Toast.LENGTH_LONG).show();
        Log.d(TAG,"Payment Transaction response:"+bundle.toString());
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorProceed(String s) { }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}