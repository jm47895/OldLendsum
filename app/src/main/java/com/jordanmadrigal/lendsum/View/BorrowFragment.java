package com.jordanmadrigal.lendsum.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jordanmadrigal.lendsum.Adapter.BorrowPackageAdapter;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.Adapter.LendPackageAdapter;
import com.jordanmadrigal.lendsum.R;

import static com.jordanmadrigal.lendsum.Utility.Constants.BORROW_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.USER_COLLECTION;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BorrowPackageAdapter adapter;

    public BorrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.package_item_list, container, false);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseFirestore.getInstance().collection(USER_COLLECTION)
                .document(mUser.getUid()).collection(BORROW_PACKAGE_COLLECTION);
        FirestoreRecyclerOptions<Package> options = new FirestoreRecyclerOptions.Builder<Package>()
                .setQuery(query, Package.class).build();

        mRecyclerView = rootView.findViewById(R.id.packageRecyclerList);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new BorrowPackageAdapter(options);
        mRecyclerView.setAdapter(adapter);



        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
