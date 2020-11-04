package com.example.retrofitapp.GooglePay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofitapp.R;

import java.util.UUID;

public class GooglePayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TEZ_REQUEST_CODE = 123;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    Button btnPayNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay);
        btnPayNow = findViewById(R.id.txnProcessBtn);
        btnPayNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "8983914397@ybl")
                        .appendQueryParameter("pn", "Test Merchant")
                        .appendQueryParameter("tr", generateString())
                        .appendQueryParameter("tn", "test transaction note")
                        .appendQueryParameter("am", "10.01")
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url","https://test.merchant.website")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
        startActivityForResult(intent, TEZ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            Toast.makeText(this, data.getStringExtra("Status"), Toast.LENGTH_SHORT).show();
            Log.d("result", data.getStringExtra("Status"));
        }
    }
    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}