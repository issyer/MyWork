package com.example.kayll.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    private ViewPager vpager;
    private ArrayList<View> aList;
    private MyPagerAdapter mmAdapter;
    private CustomRecyclerAdapter mAdapter;
    private ArrayList<Shop> mData ;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public ShopFragment(){}
    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        View view = li.inflate(R.layout.activity_shop,container,false);

        vpager=(ViewPager)view.findViewById(R.id.pager_shop) ;
        aList = new ArrayList<View>();
        aList.add(li.inflate(R.layout.view_six,null,false));
        aList.add(li.inflate(R.layout.view_seven,null,false));
        aList.add(li.inflate(R.layout.view_eight,null,false));
        mmAdapter = new MyPagerAdapter(aList);
        vpager.setAdapter(mmAdapter);

        mData=getData();
        mLayoutManager=new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false);
        mAdapter= new CustomRecyclerAdapter<Shop>(mData, R.layout.item_shop_list) {
            @Override
            protected void displayContents(ViewHolder holder, Shop itemData) {
                holder.setText(R.id.whats,itemData.getTitle());
                holder.setText(R.id.money,itemData.getMoney());
                holder.setImageResource(R.id.img_shop_tou,itemData.getId1());
                holder.setImageResource(R.id.img_shop_cart,itemData.getId2());
            }

            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }
        };
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_shop_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyDivider(this.getActivity(), LinearLayoutManager.VERTICAL));


        return view;
    }
    private ArrayList<Shop> getData(){
        mData = new ArrayList<Shop>();
        mData.add(new Shop(R.mipmap.shop1,"维达抽纸","$59.90",R.mipmap.tou3));
        mData.add(new Shop(R.mipmap.shop2,"养乐多","$12.90",R.mipmap.tou3));
        mData.add(new Shop(R.mipmap.shop3,"威露士","$39.90",R.mipmap.tou3));
        mData.add(new Shop(R.mipmap.shop4,"甘栗仁","$9.90",R.mipmap.tou3));
        return mData;
    }
}
