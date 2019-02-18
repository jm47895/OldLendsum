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

    private final MutableLiveData<String> selectedUserNameDisplay = new MutableLiveData<>();
    private final MutableLiveData<String> selectedStartDate = new MutableLiveData<>();
    private final MutableLiveData<Calendar> selectedReturnDate = new MutableLiveData<>();
    private final MutableLiveData<String> selectedLenderName = new MutableLiveData<>();
    private final MutableLiveData<List<Bitmap>> selectedImageArray = new MutableLiveData<>();
    private final MutableLiveData<Boolean>  selectedIsValidUser = new MutableLiveData<>();

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

    public void setSelectedUserNameDisplay(String userName){selectedUserNameDisplay.setValue(userName);}

    public LiveData<String> getSelectedUserNameDisplay(){return selectedUserNameDisplay;}

    public void setIsValidUser(boolean isValidUser){selectedIsValidUser.setValue(isValidUser);}

    public LiveData<Boolean> getValidUserResult(){return selectedIsValidUser;}
}
