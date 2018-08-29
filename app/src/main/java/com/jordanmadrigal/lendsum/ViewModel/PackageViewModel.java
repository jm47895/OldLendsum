package com.jordanmadrigal.lendsum.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jordanmadrigal.lendsum.Model.Package;

public class PackageViewModel extends ViewModel {

    private final MutableLiveData<Package> selected  = new MutableLiveData<>();

    public void setSelectedPack(Package pack){
        selected.setValue(pack);
    }

    public LiveData<Package> getSelectedPack(){
        return selected;
    }

}
