package com.jordanmadrigal.lendsum.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.Adapter.PackageAdapter;
import com.jordanmadrigal.lendsum.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public BorrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.package_item_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.packageRecyclerList);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Package> packages = new ArrayList<>();

        /*packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));
        packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));
        packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));
        packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));
        packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));
        packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));
        packages.add(new Package("Gamesystem", "Xbox, Controllers, Horizon Zero Dawn", "$5 per day", "5/5/18", "borrowing from " + "username4567"));*/


        PackageAdapter adapter = new PackageAdapter(packages);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

}
