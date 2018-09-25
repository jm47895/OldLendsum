package com.jordanmadrigal.lendsum.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jordanmadrigal.lendsum.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private List<Bitmap> mThumbIds;

    public ImageAdapter(Context mContext, List<Bitmap>mThumbIds){
        this.mContext = mContext;
        this.mThumbIds = mThumbIds;
    }



    @Override
    public int getCount() {

        return mThumbIds.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(250, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
            imageView.setCropToPadding(true);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mThumbIds.get(position));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Hold to delete", Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mThumbIds.remove(mThumbIds.get(position));
                imageView.setImageDrawable(null);
                notifyDataSetChanged();

                return false;
            }
        });


        return imageView;
    }



}
