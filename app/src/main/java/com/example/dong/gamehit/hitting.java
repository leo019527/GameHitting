package com.example.dong.gamehit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by dong on 2017/7/14.
 */

public class hitting extends AppCompatActivity {

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

    MyClick click;//自定义的按钮事件
    int nextlocation;//地鼠下次出现的位置
    HashMap<ImageButton, Integer> buttonIntegerHashMap;
    HashMap<Integer, ImageButton> integerButtonHashMap;
    Random random;//随机器
    int time;
    int timeleft;//剩余时间
    int score;//得分
    long halt;
    TextView showscore;//layout中的score
    TextView showtime;//layout中的time
    Thread hittingthread;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            changeUI();
        }
    };

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

    //初始化按钮等界面元素
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
        showscore = (TextView) findViewById(R.id.showscore);
        showtime = (TextView) findViewById(R.id.showtime);
    }

    //给按钮添加自定义的Click事件
    private void InitClick (){
        click = new MyClick();
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

    @Override//界面开始时执行
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hitting);

        buttonIntegerHashMap = new HashMap<ImageButton, Integer>();
        integerButtonHashMap = new HashMap<Integer, ImageButton>();
        InitButton();
        InitClick();
        InitIntegerButtonHashMap();
        InitButtonIntegerHashMap();
        random = new Random();
        nextlocation = random.nextInt(16) + 1;
        timeleft = 2;
        time = 0;
        score = 0;
        showtime.setText(timeleft + "");
        showscore.setText(score + "");
        Intent intent = getIntent();
        if (intent != null){
            halt = intent.getIntExtra("status", 1000);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hittingthread == null){
            hittingthread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (hittingthread != null) {
                        while (timeleft > 0) {
                            try {
                                Thread.sleep(halt);
                                nextlocation = random.nextInt(16) + 1;
                                time++;
                                //对游戏界面做相应的改变
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (timeleft == 0){//时间到 结束游戏
                        Intent intention = new Intent(hitting.this, gameover.class);
                        //intention.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                        intention.putExtra("Score",score);
                        hitting.this.startActivity(intention);
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
                v.setBackgroundResource(R.drawable.bang);
                showscore.setText((score += 10) + "");
                v.setBackgroundResource(R.drawable.testhole);
                nextlocation = -1;
            }
            else{
                //没打中的处理
            }
        }
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
