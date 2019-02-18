package com.jordanmadrigal.lendsum.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.jordanmadrigal.lendsum.ViewModel.DataViewModel;

import java.util.List;

public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private List<Bitmap> mImageBitmaps;

    public ImageAdapter(Context mContext, List<Bitmap> mImageBitmaps){
        this.mContext = mContext;
        this.mImageBitmaps = mImageBitmaps;
    }



    @Override
    public int getCount() {

        return mImageBitmaps.size();
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
            imageView.setPadding(8,4,8,4);
            imageView.setCropToPadding(true);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(mImageBitmaps.get(position));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Hold to delete", Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mImageBitmaps.remove(mImageBitmaps.get(position));
                imageView.setImageDrawable(null);
                notifyDataSetChanged();

                return false;
            }
        });


        return imageView;
    }



}
