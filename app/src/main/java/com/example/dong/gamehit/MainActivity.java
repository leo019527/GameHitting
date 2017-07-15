package com.example.dong.gamehit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                Intent intent = new Intent(this, hitting.class);
                startActivity(intent);
                break;
            }
            case R.id.score: {
                Intent intent = new Intent(this, score.class);
                startActivity(intent);
                break;
            }
            case R.id.about:{
                break;
            }
            case R.id.doubleModle:{
                break;
            }

            case R.id.exit:{
                finish();
            }
        }

    }
}
