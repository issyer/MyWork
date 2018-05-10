package com.example.kayll.myapplication;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SocialFragment  extends Fragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener{

    private ViewPager vpager;
    private ImageView img_cursor;
    private TextView tv_one;
    private TextView tv_two;
    private CustomRecyclerAdapter mAdapter,mAdapter2;
    private ArrayList<Notice> mData2 ;
    private ArrayList<Message> mData ;
    private RecyclerView mRecyclerView,mRecyclerView2;
    private RecyclerView.LayoutManager mLayoutManager,mLayoutManager2;
    private ArrayList<View> listViews;
    private int offset = 0;//移动条图片的偏移量
    private int currIndex = 0;//当前页面的编号
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private Context context;
    public SocialFragment(){}
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        View view = li.inflate(R.layout.activity_social,container,false);

        vpager = (ViewPager) view.findViewById(R.id.vpager_three);
        tv_one = (TextView) view.findViewById(R.id.tv_one2);
        tv_two = (TextView) view.findViewById(R.id.tv_two2);
        img_cursor = (ImageView) view.findViewById(R.id.img_cursor2);

        //下划线动画的相关设置：
        Bitmap bt= BitmapFactory.decodeResource(getResources(), R.mipmap.line);
        bmpWidth = bt.getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        img_cursor.setImageMatrix(matrix);// 设置动画初始位置
        //移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3
        //two = one * 2;// 移动两页的偏移量,比如1直接跳3


        //下面是对消息和通知的两个recycler的描述
        mData=getData();
        mData2=getData2();
        mLayoutManager2=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        mLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);

        mAdapter= new CustomRecyclerAdapter<Message>(mData, R.layout.item_list) {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            protected void displayContents(ViewHolder holder, Message itemData) {
                holder.setText(R.id.name,itemData.getName());
                holder.setText(R.id.time,itemData.getTime());
                holder.setText(R.id.says,itemData.getTitle());
                holder.setImageResource(R.id.imgtou,itemData.getId());

            }
        };
        mAdapter2= new CustomRecyclerAdapter<Notice>(mData2, R.layout.item_list) {

            @Override
            protected void displayContents(ViewHolder holder, Notice itemData) {
                holder.setText(R.id.name,itemData.getName());
                holder.setText(R.id.time,itemData.getTime());
                holder.setText(R.id.says,itemData.getTitle());
                holder.setImageResource(R.id.imgtou,itemData.getId());
            }

            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }

        };
        mRecyclerView = (RecyclerView) LayoutInflater.from(this.getActivity()).inflate(R.layout.view_msg_one,null).findViewById(R.id.my_msg_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView2 = (RecyclerView) LayoutInflater.from(this.getActivity()).inflate(R.layout.view_msg_two,null).findViewById(R.id.my_notice_view);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setAdapter(mAdapter2);

        listViews = new ArrayList<View>();

        listViews.add(mRecyclerView);
        listViews.add(mRecyclerView2);
        vpager.setAdapter(new MyPagerAdapter(listViews));
        vpager.setCurrentItem(0);          //设置ViewPager当前页，从0开始算

        tv_one.setOnClickListener(this);
        tv_two.setOnClickListener(this);
        vpager.addOnPageChangeListener(this);
        return view;
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        Animation animation = null;
        switch (position) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                }
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                }
                break;
        }
        currIndex = position;
        animation.setFillAfter(true);// true表示图片停在动画结束位置
        animation.setDuration(300); //设置动画时间为300毫秒
        img_cursor.startAnimation(animation);//开始动画
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_one2:
                vpager.setCurrentItem(0);
                break;
            case R.id.tv_two2:
                vpager.setCurrentItem(1);
                break;
        }
    }
    private ArrayList<Message> getData(){
        mData = new ArrayList<Message>();
        mData.add(new Message(R.mipmap.msg1,"物流消息","您的快递已有菜鸟驿站代收","03-19 13:39"));
        mData.add(new Message(R.mipmap.repaire,"报修反馈","关于您的报修情况，我们会尽快进行处理","03-18 10:39"));
        return mData;
    }
    private ArrayList<Notice> getData2(){
        mData2 = new ArrayList<Notice>();
        mData2.add(new Notice(R.mipmap.house,"社区物业","关于本社区3月21日停水通知","03-19 13:39"));
        return mData2;
    }
}
