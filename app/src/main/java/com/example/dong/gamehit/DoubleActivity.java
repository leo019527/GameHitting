package com.example.dong.gamehit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DoubleActivity extends AppCompatActivity {

    private boolean isServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_modle);
        Intent intent = getIntent();
        if(intent != null)
        {
            isServer = intent.getBooleanExtra("choose",true);
        }
    }
}
