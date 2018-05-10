package com.example.kayll.myapplication;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReadFragment extends Fragment {

    private ViewPager vpager;
    private ArrayList<View> aList;
    private MyPagerAdapter mmAdapter;
    private CustomRecyclerAdapter mAdapter;
    private ArrayList<Read> mData ;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public ReadFragment(){}
    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        View view = li.inflate(R.layout.activity_read,container,false);

        vpager=(ViewPager)view.findViewById(R.id.pager) ;
        aList = new ArrayList<View>();

        aList.add(li.inflate(R.layout.view_one,null,false));
        aList.add(li.inflate(R.layout.view_two,null,false));
        aList.add(li.inflate(R.layout.view_three,null,false));
        aList.add(li.inflate(R.layout.view_four,null,false));
        aList.add(li.inflate(R.layout.view_five,null,false));
        mmAdapter = new MyPagerAdapter(aList);
        vpager.setAdapter(mmAdapter);

        mData=getData();
        mLayoutManager=new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false);
        mAdapter= new CustomRecyclerAdapter<Read>(mData, R.layout.item_read_list) {
            @Override
            protected void displayContents(ViewHolder holder, Read itemData) {
                holder.setText(R.id.name_read,itemData.getName());
                holder.setText(R.id.time_read,itemData.getTime());
                holder.setText(R.id.title_read,itemData.getTitle());
                holder.setImageResource(R.id.img_read_tou,itemData.getId());
                holder.setImageResource(R.id.img_read_wei,itemData.getPhoto());
            }

            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }
        };
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_read_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyDivider(this.getActivity(), LinearLayoutManager.VERTICAL));

        return view;
    }

    private ArrayList<Read> getData(){
        mData = new ArrayList<Read>();
        mData.add(new Read(R.mipmap.tou1,"柳绿樱白","1小时前","你提取公积金还要出门？",R.mipmap.photo1));
        mData.add(new Read(R.mipmap.tou2,"小老头儿","1小时前","在浙江城市规划中？",R.mipmap.photo2));
        mData.add(new Read(R.mipmap.tou3,"北极星光","1小时前","CBA半决赛群雄脸谱？",R.mipmap.photo3));
        return mData;
    }
}
