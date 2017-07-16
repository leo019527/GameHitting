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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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
            case R.id.exit:{
                System.exit(0);
            }
        }
    }

    //蓝牙相关变量
    public static final UUID uuid= UUID.fromString("245dc87c-683d-11e7-907b-a6006ad3dba0");
    private static boolean isOver=false;
    private BluetoothAdapter mAdapter=null;
    private Set<BluetoothDevice> blueSet = new HashSet<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    blueSet.add(device);
                }
            }
            //搜索完成
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                MainActivity.isOver=true;
            }
        }
    };
    private static BluetoothSocket clienSocket;
    private static BluetoothServerSocket serverSocketTmp;
    private static BluetoothSocket serverSocket;

    public void doubleClisk(View view) {
        //判断蓝牙可用
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null)
        {
            Toast.makeText(this,"本地蓝牙不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        //打开蓝牙模块
        if(!mAdapter.isEnabled()){
            //弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 0);
        }

        //选择是否服务端
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this,android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("选择阵营");
        final String[] choose = {"红色","黄色"};
        builder.setItems(choose,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 1)
                {
                    //服务器端
                    //设置蓝牙可见
                    if (mAdapter.isEnabled()) {
                        if (mAdapter.getScanMode() !=
                                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                            Intent discoverableIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            discoverableIntent.putExtra(
                                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                            startActivity(discoverableIntent);
                        }
                    }
                    //服务器端开始监听
                    new AcceptThread("doublehit",uuid,serverSocketTmp).run();
                }
                else
                {
                    //客户端
                    //添加蓝牙查找
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mReceiver, filter);
                    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    registerReceiver(mReceiver, filter);
                    // 如果正在搜索，就先取消搜索
                    if (mAdapter.isDiscovering()) {
                        mAdapter.cancelDiscovery();
                    }
                    // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
                    mAdapter.startDiscovery();
                    new ConnectThread(a).run();

                }
            }
        });
        builder.show();
    }
    //服务器端连接线程
    private class AcceptThread extends Thread
    {
        private String name;
        private UUID uuid;
        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;

        public AcceptThread(String name, UUID uuid, BluetoothServerSocket serverSocket) {
            this.name = name;
            this.uuid = uuid;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            //服务器端开始监听
            try {
                serverSocket = mAdapter. listenUsingRfcommWithServiceRecord("hit",uuid);
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO:传消息
            startActivity(intent);
        }
    }

    //客户端连接线程
    private class ConnectThread extends Thread{
        MainActivity a;

        public ConnectThread(MainActivity a) {
            this.a = a;
        }

        String choosenOne;
        @Override
        public void run() {
            while(!MainActivity.isOver)
            {
                System.out.println(blueSet.size());
            }
            //显示选择界面
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(a,android.R.style.Theme_Holo_Light_Dialog);
            //builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("选择一个蓝牙进行连接");
            //    指定下拉列表的显示数据
            final String[] blueTooth= new String[blueSet.size()];
            final Iterator<BluetoothDevice> iterator = blueSet.iterator();
            for(int i=0;i<blueSet.size();i++)
            {
                blueTooth[i] = iterator.next().getName();
            }
            //    设置一个下拉的列表选择项
            builder.setItems(blueTooth, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    choosenOne = blueTooth[which];
                    Iterator<BluetoothDevice> iterator1 = blueSet.iterator();
                    BluetoothDevice a;
                    while(iterator.hasNext())
                    {
                        a = iterator.next();
                        if (a.getName()==choosenOne)
                        {
                            try {
                                MainActivity.clienSocket = a.createRfcommSocketToServiceRecord(uuid);
                                clienSocket.connect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //TODO:传数据
                            startActivity(intent);
                        }
                    }
                }
            });
            builder.show();
        }
    }
}
