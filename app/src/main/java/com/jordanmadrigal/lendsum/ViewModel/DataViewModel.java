package com.jordanmadrigal.lendsum.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {

    private final MutableLiveData<String> selected  = new MutableLiveData<>();

    public void setSelectedDate(String date){
        selected.setValue(date);
    }

    public LiveData<String> getSelectedDate(){
        return selected;
    }

    public void setSelectedUid(String uId){
        selected.setValue(uId);
    }

    public LiveData<String> getSelectedUid(){return selected;}
}
