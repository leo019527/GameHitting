package com.example.dong.gamehit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by dong on 2017/7/14.
 */

public class gameover extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart: {
                Intent intent = new Intent(this, hitting.class);
                startActivity(intent);
                break;
            }
            case R.id.back: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.exit:{
                finish();
            }
        }
    }
}
