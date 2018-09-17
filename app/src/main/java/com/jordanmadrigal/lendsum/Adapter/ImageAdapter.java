package com.jordanmadrigal.lendsum.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter{

    private Context mContext;

    public ImageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private Integer[] mThumbIds = {};


    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
