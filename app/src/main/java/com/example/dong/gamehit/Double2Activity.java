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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

public class Double2Activity extends AppCompatActivity {
    public static final int STATE_CHANGE=1;


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

    //按钮组
    private ImageButton one;
    private ImageButton two;
    private ImageButton three;
    private ImageButton four;
    private ImageButton five;
    private ImageButton six;
    private ImageButton seven;
    private ImageButton eight;
    private ImageButton nine;
    private ImageButton ten;
    private ImageButton eleven;
    private ImageButton twleve;
    private ImageButton thirdteen;
    private ImageButton fourteen;
    private ImageButton fifteen;
    private ImageButton sixteen;
    Double2Activity.MyClick click;//自定义的按钮事件
    int nextlocation;//地鼠下次出现的位置
    HashMap<ImageButton, Integer> buttonIntegerHashMap;
    HashMap<Integer, ImageButton> integerButtonHashMap;
    int time;
    int timeleft;//剩余时间
    int score;//自己的得分
    int score2;//别人得分
    long halt;
    TextView yourscore;//layout中的score
    TextView hisscore;//别人的分数
    TextView showtime;//layout中的time
    Thread hittingthread;
    private static boolean getmessage=false;

    //<editor-fold desc="handler">
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case BluetoothUtil.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(),"成功连接设备2"+msg.getData().getString(BluetoothUtil.DEVICE_NAME),Toast.LENGTH_LONG).show();
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    break;
                case BluetoothUtil.STATAE_CONNECT_FAILURE:
                    Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.MESSAGE_DISCONNECTED:
                    Toast.makeText(getApplicationContext(),"连接断开",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothUtil.MESSAGE_READ:{
                    int tmp;
                    int i;
                    byte[] buf = msg.getData().getByteArray(BluetoothUtil.READ_MSG);
                    String str = null;
                    str = new String(buf,0,buf.length);
                    if(buf[0] == 'T')
                    {
                        getmessage = true;
                        tmp=0;
                        i = 1;
                        while(buf[i] != '\0')
                        {
                            tmp = tmp*10+buf[i]-'0';
                            i++;
                        }
                        nextlocation = tmp;
                    }
                    else
                    {
//                        tmp=0;
//                        i = 1;
//                        while(buf[i] != '\0')
//                        {
//                            tmp = tmp*10+buf[i]-'0';
//                            i++;
//                        }
//                        tmp++;
                        ImageButton v = integerButtonHashMap.get(nextlocation);
                        v.setBackgroundResource(R.drawable.bang);
                        hisscore.setText((score2 += 10) + "");
                        nextlocation = -1;
                    }
                    break;
                }
                case BluetoothUtil.MESSAGE_WRITE:{
                    break;
                }
                case STATE_CHANGE:{
                    changeUI();
                    break;
                }
                default:
                    break;
            }
        };
    };
    //</editor-fold>

    private void changeUI() {
        if (time % 2 == 0)
            showtime.setText(--timeleft + "");
        if (nextlocation == -1)
            return;
        //地鼠出现
        //原按钮初始化
        refreshButton();
        //获取按钮
        ImageButton bt = integerButtonHashMap.get(nextlocation);
        //更新按钮背景
        bt.setBackgroundResource(R.drawable.otter);
    }

    private void InitButton (){//获取按钮的id信息
        one = (ImageButton) findViewById(R.id.button11);
        two = (ImageButton) findViewById(R.id.button12);
        three = (ImageButton) findViewById(R.id.button13);
        four = (ImageButton) findViewById(R.id.button14);
        five = (ImageButton) findViewById(R.id.button21);
        six = (ImageButton) findViewById(R.id.button22);
        seven = (ImageButton) findViewById(R.id.button23);
        eight = (ImageButton) findViewById(R.id.button24);
        nine = (ImageButton) findViewById(R.id.button31);
        ten = (ImageButton) findViewById(R.id.button32);
        eleven = (ImageButton) findViewById(R.id.button33);
        twleve = (ImageButton) findViewById(R.id.button34);
        thirdteen = (ImageButton) findViewById(R.id.button41);
        fourteen = (ImageButton) findViewById(R.id.button42);
        fifteen = (ImageButton) findViewById(R.id.button43);
        sixteen = (ImageButton) findViewById(R.id.button44);
        yourscore = (TextView) findViewById(R.id.showscore);
        hisscore = (TextView)findViewById(R.id.showscore2);
        showtime = (TextView) findViewById(R.id.showtime);
    }

    private void InitClick (){
        click = new Double2Activity.MyClick();
        one.setOnClickListener(click);
        two.setOnClickListener(click);
        three.setOnClickListener(click);
        four.setOnClickListener(click);
        five.setOnClickListener(click);
        six.setOnClickListener(click);
        seven.setOnClickListener(click);
        eight.setOnClickListener(click);
        nine.setOnClickListener(click);
        ten.setOnClickListener(click);
        eleven.setOnClickListener(click);
        twleve.setOnClickListener(click);
        thirdteen.setOnClickListener(click);
        fourteen.setOnClickListener(click);
        fifteen.setOnClickListener(click);
        sixteen.setOnClickListener(click);
    }

    //初始化random值与Button的对应关系
    private void InitIntegerButtonHashMap(){
        integerButtonHashMap.put(1, one);
        integerButtonHashMap.put(2, two);
        integerButtonHashMap.put(3, three);
        integerButtonHashMap.put(4, four);
        integerButtonHashMap.put(5, five);
        integerButtonHashMap.put(6, six);
        integerButtonHashMap.put(7, seven);
        integerButtonHashMap.put(8, eight);
        integerButtonHashMap.put(9, nine);
        integerButtonHashMap.put(10, ten);
        integerButtonHashMap.put(11, eleven);
        integerButtonHashMap.put(12, twleve);
        integerButtonHashMap.put(13, thirdteen);
        integerButtonHashMap.put(14, fourteen);
        integerButtonHashMap.put(15, fifteen);
        integerButtonHashMap.put(16, sixteen);
    }

    //初始化Button与random值的对应关系
    private void InitButtonIntegerHashMap(){
        buttonIntegerHashMap.put(one, 1);
        buttonIntegerHashMap.put(two, 2);
        buttonIntegerHashMap.put(three, 3);
        buttonIntegerHashMap.put(four, 4);
        buttonIntegerHashMap.put(five, 5);
        buttonIntegerHashMap.put(six, 6);
        buttonIntegerHashMap.put(seven, 7);
        buttonIntegerHashMap.put(eight, 8);
        buttonIntegerHashMap.put(nine, 9);
        buttonIntegerHashMap.put(ten, 10);
        buttonIntegerHashMap.put(eleven, 11);
        buttonIntegerHashMap.put(twleve, 12);
        buttonIntegerHashMap.put(thirdteen, 13);
        buttonIntegerHashMap.put(fourteen, 14);
        buttonIntegerHashMap.put(fifteen, 15);
        buttonIntegerHashMap.put(sixteen, 16);
    }

    //按钮初始化
    private void refreshButton(){
        one.setBackgroundResource(R.drawable.testhole);
        two.setBackgroundResource(R.drawable.testhole);
        three.setBackgroundResource(R.drawable.testhole);
        four.setBackgroundResource(R.drawable.testhole);
        five.setBackgroundResource(R.drawable.testhole);
        six.setBackgroundResource(R.drawable.testhole);
        seven.setBackgroundResource(R.drawable.testhole);
        eight.setBackgroundResource(R.drawable.testhole);
        nine.setBackgroundResource(R.drawable.testhole);
        ten.setBackgroundResource(R.drawable.testhole);
        eleven.setBackgroundResource(R.drawable.testhole);
        twleve.setBackgroundResource(R.drawable.testhole);
        thirdteen.setBackgroundResource(R.drawable.testhole);
        fourteen.setBackgroundResource(R.drawable.testhole);
        fifteen.setBackgroundResource(R.drawable.testhole);
        sixteen.setBackgroundResource(R.drawable.testhole);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_modle2);

        mContext = this;
        mProgressDialog = new ProgressDialog(this);
        initBluetooth();
        mBlthChatUtil = BluetoothUtil.getInstance(mContext);
        mBlthChatUtil.registerHandler(mHandler);

        buttonIntegerHashMap = new HashMap<ImageButton, Integer>();
        integerButtonHashMap = new HashMap<Integer, ImageButton>();
        InitButton();
        InitClick();
        InitIntegerButtonHashMap();
        InitButtonIntegerHashMap();
        nextlocation = -1;
        timeleft = 2;
        time = 0;
        score = 0;
        score2 = 0;
        showtime.setText(timeleft + "");
        yourscore.setText(score + "");
        hisscore.setText(score2+"");
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
    protected void onResume() {
        super.onResume();
        if (mBlthChatUtil != null) {
            if (mBlthChatUtil.getState() == BluetoothUtil.STATE_CONNECTED){
                BluetoothDevice device = mBlthChatUtil.getConnectedDevice();
                if(null != device && null != device.getName()){
                    Toast.makeText(getApplicationContext(),"成功连接设备"+device.getName(),Toast.LENGTH_LONG).show();
                }else {
                }
            }
        }
        if (hittingthread == null){
            hittingthread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (hittingthread != null) {
                        while (timeleft > 0) {
                            while(mBlthChatUtil.getState() != BluetoothUtil.STATE_CONNECTED);
                            while(!getmessage);
                            time++;
                            //对游戏界面做相应的改变
//                                handler.sendEmptyMessage(1);
                            Message msg = mHandler.obtainMessage(STATE_CHANGE);
                            mHandler.sendMessage(msg);
                            getmessage = false;
                        }
                    }
                    if (timeleft == 0){//时间到 结束游戏
                        if(score >= score2)
                        {
                            Intent intent = new Intent(Double2Activity.this,HighScore.class);
                            intent.putExtra("MyScore",score);
                            intent.putExtra("HisScore",score2);
                            Double2Activity.this.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(Double2Activity.this,LowScore.class);
                            intent.putExtra("MyScore",score);
                            intent.putExtra("HisScore",score2);
                            Double2Activity.this.startActivity(intent);
                        }
//                        Intent intention = new Intent(Double2Activity.this, gameover.class);
//                        //intention.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                        intention.putExtra("Score",score);
//                        intention.putExtra("Score2",score2);
//                        Double2Activity.this.startActivity(intention);
                        finish();
                    }
                }
            });
            hittingthread.start();
        }
    }

    class MyClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int hit = buttonIntegerHashMap.get(v);
            if (hit == nextlocation){//打中
                v.setBackgroundResource(R.drawable.bang2);
                yourscore.setText((score += 10) + "");
                String send = hit+"";
                mBlthChatUtil.write((send+'\0').getBytes());
                nextlocation = -1;
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

    @Override
    protected void onStop() {
        super.onStop();
        if (hittingthread != null) {
            hittingthread.interrupt();
            hittingthread = null;
        }
    }
}
