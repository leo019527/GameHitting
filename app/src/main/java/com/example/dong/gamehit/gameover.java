package com.example.dong.gamehit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ljl on 2017/7/16.
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
            case R.id.ranklist: {
                //获取姓名等信息
                TextView nameview = (TextView) findViewById(R.id.Name);
                String name = nameview.getText().toString();

                //存入数据库
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("score",score);
                db.insert("Book",null,values);
                values.clear();

                //进入排名界面
                Intent intent = new Intent(gameover.this, rankscene.class);
                startActivity(intent);
                break;
            }
            case R.id.back: {
                finish();
            }
        }
    }
}