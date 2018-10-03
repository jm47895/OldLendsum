package com.jordanmadrigal.lendsum.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.google.android.gms.common.api.BooleanResult;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataViewModel extends ViewModel {

    private final MutableLiveData<Boolean> booleanResult = new MutableLiveData<>();
    private final MutableLiveData<String> selectedStartDate = new MutableLiveData<>();
    private final MutableLiveData<Calendar> selectedReturnDate = new MutableLiveData<>();
    private final MutableLiveData<String> selectedLenderName = new MutableLiveData<>();
    private final MutableLiveData<List<Bitmap>> selectedImageArray = new MutableLiveData<>();

    public void hasDateChanged(boolean isDateSet){booleanResult.setValue(isDateSet);}

    public LiveData<Boolean> dateChangeResult(){return booleanResult;}

    public void setSelectedStartDate(String date){
        selectedStartDate.setValue(date);
    }

    public LiveData<String> getSelectedStartDate(){
        return selectedStartDate;
    }

    public void setSelectedReturnDate(Calendar date){
        selectedReturnDate.setValue(date);
    }

    public LiveData<Calendar> getSelectedReturnDate(){
        return selectedReturnDate;
    }

    public void setSelectedLenderName(String name){
        selectedLenderName.setValue(name);
    }

    public LiveData<String> getSelectedLenderName(){return selectedLenderName;}


    public void setSelectedImageArray(List<Bitmap> images){selectedImageArray.setValue(images); }

    public LiveData<List<Bitmap>> getSelectedImageArray(){
        return selectedImageArray;
    }
}
