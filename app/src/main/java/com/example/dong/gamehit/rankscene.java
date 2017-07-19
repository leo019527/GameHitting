package com.example.dong.gamehit;

/**
 * Created by 李凌耀 on 2017/7/17.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class rankscene extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;

    private List<ListItemModel> myListItems;
    private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankscene);

        //Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.listView);
        listView.setCacheColorHint(0);
        myListItems = new ArrayList<ListItemModel>();
        initData();
        adapter = new MyListAdapter(rankscene.this);
        adapter.setListItems(myListItems);
        listView.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home: {
                //进入主页面
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.Return: {
                finish();
            }
        }
    }

    public void initData() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("Book",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                ListItemModel lm = new ListItemModel();
                lm.setTitle(name);
                lm.setData(String.valueOf(score));
                lm.setRid(R.mipmap.chuizi);
                myListItems.add(lm);
            }while(cursor.moveToNext());
        }
        Collections.sort(myListItems, new SortByScore());
        cursor.close();
        db.close();
    }
}

class SortByScore implements Comparator{

    @Override
    public int compare(Object o, Object t1) {
        ListItemModel L1 = (ListItemModel) o;
        ListItemModel L2 = (ListItemModel) t1;
        if (Integer.parseInt(L1.getData()) < Integer.parseInt(L2.getData())) {
            return 1;
        } else if (L1.getData().compareTo(L2.getData()) == 0) {
            if (L1.getTitle().compareTo(L2.getTitle()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
        else{
            return -1;
        }
    }
}
