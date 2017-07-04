package com.junhee.android.firebase_advanced;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Message", "Refreshed token: " + refreshedToken);
    }
}
