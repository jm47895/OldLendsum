package com.jordanmadrigal.lendsum.View;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.ViewModel.DataViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    private static final String LOG_TAG = CalendarFragment.class.getSimpleName();
    private DataViewModel mDataModel;
    private ImageButton mCancelBtn;
    private Button mAddDateBtn;
    private DatePicker mDatePicker;
    private String mStartDate;
    private Calendar mReturnDate;



    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        mCancelBtn = view.findViewById(R.id.addDateCancelBtn);
        mAddDateBtn = view.findViewById(R.id.addDateBtn);
        mDatePicker = view.findViewById(R.id.calendar);
        mDataModel = ViewModelProviders.of(getActivity()).get(DataViewModel.class);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReturnDate = null;
                mStartDate = null;
                if (getParentFragment() != null) {
                    getParentFragment().getChildFragmentManager().popBackStack();
                }
            }
        });



        mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

                Calendar startDate = Calendar.getInstance();

                startDate.set(year, month, day);

                SimpleDateFormat formatEndDate = new SimpleDateFormat("MMM-dd", Locale.getDefault());
                mStartDate = formatEndDate.format(startDate.getTime());

                mReturnDate = Calendar.getInstance();
                mReturnDate.set(year, month, day);

            }
        });

        mAddDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDataModel.setSelectedStartDate(mStartDate);
                mDataModel.setSelectedReturnDate(mReturnDate);

                if (getParentFragment() != null) {
                    getParentFragment().getChildFragmentManager().popBackStackImmediate();
                }

            }
        });
    }




}
