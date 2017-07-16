package com.example.dong.gamehit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    int halt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        halt = 0;
    }

    private void showListDialog() {
        String[] items = {"简单","一般","困难"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
        listDialog.setTitle("请选择难度：");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        halt = 1500;
                        break;
                    case 1:
                        halt = 1000;
                        break;
                    case 2:
                        halt = 500;
                        break;
                }
                Intent intent = new Intent(MainActivity.this, hitting.class);
                intent.putExtra("status", halt);
                startActivity(intent);
            }
        });
        listDialog.show();
    }

    private void showNormalDialog() {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("关于");
        normalDialog.setMessage("北京交通大学计算机与信息技术学院专业实践与训练Ⅰ课程项目\n作者：董林华、李凌耀、陆金梁");
        normalDialog.show();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                showListDialog();
                break;
            }
            case R.id.score: {
                Intent intent = new Intent(this, score.class);
                startActivity(intent);
                break;
            }
            case R.id.about:{
                showNormalDialog();
                break;
            }
            case R.id.doubleModle:{
                break;
            }
            case R.id.exit:{
                System.exit(0);
            }
        }
    }
}
