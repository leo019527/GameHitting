package com.example.dong.gamehit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dong on 2017/7/14.
 */

public class gameover extends AppCompatActivity implements View.OnClickListener{

    int score;
    TextView textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        Intent intent = getIntent();
        if (intent != null) {
            score = intent.getIntExtra("Score", 0);
        }
        textview = (TextView) findViewById(R.id.finalscore);
        textview.setText(score + "");
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
                finish();
            }
        }
    }
}