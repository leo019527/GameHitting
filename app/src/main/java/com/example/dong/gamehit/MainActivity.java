package com.example.dong.gamehit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static Intent intent = null;
    int halt;
    private static MainActivity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        a = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        halt = 0;
        intent = new Intent(this, DoubleActivity.class);
    }

    private void showListDialog() {
        String[] items = {"简单", "一般", "困难"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
        listDialog.setTitle("请选择难度：");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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

    private Handler handler = new Handler() {
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
                Intent intent = new Intent(this, rankscene.class);
                startActivity(intent);
                break;
            }
            case R.id.about: {
                showNormalDialog();
                break;
            }
            case R.id.exit: {
                System.exit(0);
            }
        }
    }


    public void doubleClisk(View view) {
        //选择是否服务端
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("选择阵营");
        final String[] choose = {"红色", "黄色"};
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)//红色
                {
                    Intent server = new Intent(a,DoubleActivity.class);
                    startActivity(server);
                    finish();
                } else {
                    Intent client = new Intent(a,Double2Activity.class);
                    startActivity(client);
                    finish();
                }
            }
        });
        builder.show();
    }
}
