package com.jordanmadrigal.lendsum.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DateViewModel extends ViewModel {

    private final MutableLiveData<String> selected  = new MutableLiveData<>();

    public void setSelected(String data){
        selected.setValue(data);
    }

    public LiveData<String> getSelected(){
        return selected;
    }
}
