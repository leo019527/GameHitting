package com.example.dong.gamehit;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Double2Activity extends AppCompatActivity {

    public static final String BLUETOOTH_NAME = "leo019527";
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothUtil mBlthChatUtil;
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(scanDevice == null || scanDevice.getName() == null) return;
                //蓝牙设备名称
                String name = scanDevice.getName();
                if(name != null && name.equals(BLUETOOTH_NAME)){
                    mBluetoothAdapter.cancelDiscovery(); //取消扫描
                    mProgressDialog.setTitle(getResources().getString(R.string.progress_connecting));
                    mBlthChatUtil.connect(scanDevice);
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            }
        }
    };


    //<editor-fold desc="handler">
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case BluetoothUtil.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(),"成功连接设备",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(getApplicationContext(),"连接断开",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.MESSAGE_READ:{
                    break;
                }
                case BluetoothUtil.MESSAGE_WRITE:{
                    break;
                }
                default:
                    break;
            }
        };
    };
    //</editor-fold>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_modle2);

        mContext = this;
        mProgressDialog = new ProgressDialog(this);
        initBluetooth();
        mBlthChatUtil = BluetoothUtil.getInstance(mContext);
        mBlthChatUtil.registerHandler(mHandler);
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {//设备不支持蓝牙
            Toast.makeText(getApplicationContext(), "设备不支持蓝牙",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //判断蓝牙是否开启
        if (!mBluetoothAdapter.isEnabled()) {//蓝牙未开启
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 1);
            //mBluetoothAdapter.enable();此方法直接开启蓝牙，不建议这样用。
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mBlthChatUtil != null) {
            if (mBlthChatUtil.getState() == BluetoothUtil.STATE_CONNECTED){
                BluetoothDevice device = mBlthChatUtil.getConnectedDevice();
                if(null != device && null != device.getName()){
                    Toast.makeText(getApplicationContext(),"成功连接设备"+device.getName(),Toast.LENGTH_LONG).show();
                }else {
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlthChatUtil = null;
        unregisterReceiver(mBluetoothReceiver);
    }

    public void connect(View view) {
        if (mBlthChatUtil.getState() == BluetoothUtil.STATE_CONNECTED) {
            Toast.makeText(mContext, "蓝牙已连接", Toast.LENGTH_SHORT).show();
        }else {
            discoveryDevices();
        }
    }

    private void discoveryDevices() {
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        if (mBluetoothAdapter.isDiscovering()){
            //如果正在扫描则返回
            return;
        }
        mProgressDialog.setTitle(getResources().getString(R.string.progress_scaning));
        mProgressDialog.show();
        // 扫描蓝牙设备
        mBluetoothAdapter.startDiscovery();

    }
}
