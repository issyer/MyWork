package com.example.kayll.myapplication;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class CustomRecyclerAdapter<T> extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private int mLayoutRes;           //布局id
    private ArrayList<T> mData;
    private CustomRecyclerAdapter.OnItemClickListener onItemClickListener;


    public CustomRecyclerAdapter(ArrayList<T> mData, int mLayoutRes){    //初始化适配器
        this.mData = mData;
        this.mLayoutRes = mLayoutRes;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
        T itemData = mData.get(position);
        displayContents(holder,itemData);

    }
    protected abstract void displayContents(ViewHolder holder, T itemData);

    //添加一个元素
    public void add(T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void add(int position, T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mData != null) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnItemClickListener( CustomRecyclerAdapter.OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private SparseArray<View> views;
        public ViewHolder(View itemView) {
            super(itemView);
            views = new SparseArray<View>();
        }
        private <T extends View> T converToViewFromId(int resId) {
            View view = views.get(resId);
            if(view == null){
                view = itemView.findViewById(resId);
            }
            views.put(resId,view);
            return (T)view;
        }

        public ViewHolder setText(int resId, String value){
            TextView itemView = converToViewFromId(resId);
            if (TextUtils.isEmpty(value)) {
                itemView.setText("");
            } else {
                itemView.setText(value);
            }
            return  this;
        }
        public ViewHolder setImageResource(int viewId, int imageResId) {
            ImageView view = converToViewFromId(viewId);
            view.setImageResource(imageResId);
            return this;
        }
        public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
            View view = converToViewFromId(viewId);
            view.setOnClickListener(listener);
            return this;
        }


    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        //void onItemLongClick(View view, int position);
    }
}
