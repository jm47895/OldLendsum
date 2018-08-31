package com.jordanmadrigal.lendsum.View;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.Adapter.PackageAdapter;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.ViewModel.PackageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LendFragment extends Fragment{

    private static final String LOG_TAG = LendFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PackageViewModel mPackViewModel;

    private List<Package> packages;

    public LendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mPackViewModel = ViewModelProviders.of(getActivity()).get(PackageViewModel.class);

        View rootView = inflater.inflate(R.layout.package_item_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.packageRecyclerList);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        packages = new ArrayList<>();

        PackageAdapter adapter = new PackageAdapter(packages);


        mPackViewModel.getSelectedPack().observe(this, new Observer<Package>() {
            @Override
            public void onChanged(@Nullable Package aPackage) {
                packages.add(aPackage);
                mRecyclerView.setAdapter(adapter);
            }
        });


        return rootView;

    }
}
