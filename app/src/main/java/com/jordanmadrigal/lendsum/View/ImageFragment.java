package com.jordanmadrigal.lendsum.View;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.jordanmadrigal.lendsum.Interfaces.OnActivityToFragmentListener;
import com.jordanmadrigal.lendsum.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private GridView mGridView;
    private Button mNextBtn;
    private ImageButton mAddImageBtn, mCloseImageBtn;
    private OnActivityToFragmentListener mOnFragmentStateChange;


    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGridView = view.findViewById(R.id.pictureGridView);
        mCloseImageBtn = view.findViewById(R.id.createPackImageCloseBtn);
        mNextBtn = view.findViewById(R.id.createPackNextBtn);
        mAddImageBtn = view.findViewById(R.id.createPackSelectImgBtn);

        mCloseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnFragmentStateChange.setActionBarListener(R.string.app_name);
                mOnFragmentStateChange.setFragmentVisible(false);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addPackageFragment = new AddPackageFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.imageFragmentContainer, addPackageFragment).addToBackStack("imageFrag").commit();
            }
        });

        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnFragmentStateChange = (OnActivityToFragmentListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnFragmentInteraction Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentStateChange = null;
    }
}
