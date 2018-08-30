package com.jordanmadrigal.lendsum.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jordanmadrigal.lendsum.View.BorrowFragment;
import com.jordanmadrigal.lendsum.View.ChatFragment;
import com.jordanmadrigal.lendsum.View.LendFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Lending","Messages","Borrowing"};

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new LendFragment();
        }else if(position == 1){
            return new ChatFragment();
        }else{
            return new BorrowFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
