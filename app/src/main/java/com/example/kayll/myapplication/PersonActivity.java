package com.example.kayll.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CustomRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Person> data;
    private GestureDetector mDetector;
    private final static int MIN_MOVE = 0;   //最小距离
    private MyGestureListener mgListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initData();
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Person_age_Activity.action);
        filter.addAction(Person_name_Activity.action);
        filter.addAction(Person_sex_Activity.action);
        filter.addAction(Person_addr_Activity.action);
        registerReceiver(broadcastReceiver, filter);


        mgListener = new MyGestureListener();
        mDetector = new GestureDetector(this,mgListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            if(e1.getX() - e2.getX() < MIN_MOVE) {
                finish();
            }
            return true;
        }
    }
    private void initData(){
        mLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mAdapter = new CustomRecyclerAdapter<Person>(getData(), R.layout.view_rv_item) {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            protected void displayContents(ViewHolder holder, Person itemData) {
                holder.setText(R.id.item_tv,itemData.getBiaoti());
                holder.setText(R.id.item_tv2,itemData.getNr());
            }
        };
        mAdapter.setOnItemClickListener(new CustomRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                if(position==1){
                    Intent intent=new Intent(PersonActivity.this,Person_name_Activity.class);
                    startActivity(intent);
                }else if(position==2){
                    Intent intent=new Intent(PersonActivity.this,Person_age_Activity.class);
                    startActivity(intent);
                }else if(position==3){
                    Intent intent=new Intent(PersonActivity.this,Person_sex_Activity.class);
                    startActivity(intent);
                }else if(position==4){
                    Intent intent=new Intent(PersonActivity.this,Person_addr_Activity.class);
                    startActivity(intent);
                }
            }

        });

    }
    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyDivider(this, LinearLayoutManager.VERTICAL));
    }

    private ArrayList<Person> getData(){
        data = new ArrayList<>();
        data.add(new Person("账号","0001"));
        data.add(new Person("姓名","孙王河"));
        data.add(new Person("年龄","20"));
        data.add(new Person("性别","男"));
        data.add(new Person("地址","绍兴市"));
        return data;
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals(Person_name_Activity.action)){
                data.get(1).setNr(intent.getExtras().getString("name"));
                mAdapter.updateData(data);
            }else if(intent.getAction().equals(Person_age_Activity.action)){
                data.get(2).setNr(intent.getExtras().getString("age"));
                mAdapter.updateData(data);
            }else if(intent.getAction().equals(Person_sex_Activity.action)){
                data.get(3).setNr(intent.getExtras().getString("sex"));
                mAdapter.updateData(data);
            }else if(intent.getAction().equals(Person_addr_Activity.action)){
                data.get(4).setNr(intent.getExtras().getString("addr"));
                mAdapter.updateData(data);
            }

        }
        protected void onDestroy() {
            unregisterReceiver(broadcastReceiver);
        }
    };

}

