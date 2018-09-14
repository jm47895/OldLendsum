package com.jordanmadrigal.lendsum.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {

    private final MutableLiveData<String> selectedDate  = new MutableLiveData<>();
    private final MutableLiveData<String> selectedLenderName = new MutableLiveData<>();

    public void setSelectedDate(String date){
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedDate(){
        return selectedDate;
    }


    public void setSelectedLenderName(String name){
        selectedLenderName.setValue(name);
    }

    public LiveData<String> getSelectedLenderName(){return selectedLenderName;}
}
