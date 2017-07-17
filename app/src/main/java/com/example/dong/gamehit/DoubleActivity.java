package com.example.dong.gamehit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class DoubleActivity extends AppCompatActivity {
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;


    private BluetoothUtil mBlthChatUtil;

    //<editor-fold desc="Handler">
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothUtil.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(),"成功连接设备",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(getApplicationContext(),"连接断开",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.MESSAGE_READ: {
                    break;
                }
                case BluetoothUtil.MESSAGE_WRITE: {
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    //</editor-fold>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_modle);

        mContext = this;
        initBluetooth();

        mBlthChatUtil = BluetoothUtil.getInstance(mContext);
        mBlthChatUtil.registerHandler(mHandler);
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {//设备不支持蓝牙
            Toast.makeText(getApplicationContext(), "设备不支持蓝牙", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //判断蓝牙是否开启
        if (!mBluetoothAdapter.isEnabled()) {//蓝牙未开启
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 1);
        }
        //设置蓝牙可见性
        if (mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.getScanMode() !=
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                startActivity(discoverableIntent);
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!mBluetoothAdapter.isEnabled()) return;
        if (mBlthChatUtil != null) {
            if (mBlthChatUtil.getState() == BluetoothUtil.STATE_NONE) {
                // 启动蓝牙聊天服务
                mBlthChatUtil.startListen();
            } else if (mBlthChatUtil.getState() == BluetoothUtil.STATE_CONNECTED) {
                BluetoothDevice device = mBlthChatUtil.getConnectedDevice();
                if (null != device && null != device.getName()) {
                    Toast.makeText(getApplicationContext(),"成功连接设备"+device.getName(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
