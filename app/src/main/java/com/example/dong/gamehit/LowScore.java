package com.example.dong.gamehit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ljl on 2017/7/18.
 */

public class LowScore extends AppCompatActivity {
    int HighScore;
    int LowScore;
    TextView HightextView;
    TextView LowtextView;
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lowscore);

        Intent intent = getIntent();
        if (intent != null) {
            HighScore = intent.getIntExtra("MyScore", 0);
            LowScore = intent.getIntExtra("HisScore", 0);
        }
        HightextView = (TextView) findViewById(R.id.higher);
        HightextView.setText(HighScore + "");
        LowtextView = (TextView) findViewById(R.id.lower);
        LowtextView.setText(LowScore + "");

        editText = (EditText) findViewById(R.id.lowname);
        String name = editText.getText().toString();

        //存入数据库
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("score",LowScore);
        db.insert("Book",null,values);
        values.clear();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagehome2: {
                //进入主页面
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
