package com.jordanmadrigal.lendsum.View;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jordanmadrigal.lendsum.Interfaces.OnActivityToFragmentListener;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.ViewModel.DataViewModel;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.jordanmadrigal.lendsum.Utility.Constants.BORROW_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.LEND_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.USER_COLLECTION;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPackageFragment extends Fragment{

    private static final String LOG_TAG = AddPackageFragment.class.getSimpleName();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private FirebaseFirestore mDatabase;
    private FirebaseUser mUser;
    private DataViewModel mDataModel;
    private TextView mStartDateText, mReturnDateText, mReturnDateTextValue, mRateText, mDollarSignText,
            mPerText, mLendPeriodText, mForText, mLendPeriodTextValue, mStartDateTextValue;
    private Spinner mLendPeriodSpinner;
    private EditText mPackHeaderText, mItemList, mBorrowerEmail, mBorrowerName, mRateValue, mCalPeriodValue;
    private Button mAddPackBtn, mBackButton;
    private Switch mIndefSwitch;
    private ImageButton mDatePickerBtn;
    private boolean isIndefinite, isDateSet;
    private String mStartDate, mReturnDate;
    private List<String> mImagePaths;
    private OnActivityToFragmentListener mOnFragmentStateChange;

    public AddPackageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataModel = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_add_package, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isDateSet = false;
        isIndefinite = false;   //needed for package constructor
        mDatabase = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mPackHeaderText = view.findViewById(R.id.createPackName);
        mBorrowerName= view.findViewById(R.id.createPackUserName);
        mBorrowerEmail = view.findViewById(R.id.createPackBorrowerEmail);
        mItemList = view.findViewById(R.id.createPackItemList);
        mBackButton = view.findViewById(R.id.createPackBackBtn);
        mAddPackBtn = view.findViewById(R.id.createPackBtn);
        mStartDateText = view.findViewById(R.id.createPackStartDateTextView);
        mStartDateTextValue = view.findViewById(R.id.createPackStartDateValueTextview);
        mRateText = view.findViewById(R.id.createPackLendRateText);
        mLendPeriodSpinner = view.findViewById(R.id.createPackLendPeriodSpinner);
        mReturnDateText = view.findViewById(R.id.createPackReturnDateTextView);
        mReturnDateTextValue = view.findViewById(R.id.createPackReturnValueTextView);
        mDatePickerBtn = view.findViewById(R.id.datePickerBtn);
        mIndefSwitch = view.findViewById(R.id.createPackIndefSwitch);
        mDollarSignText = view.findViewById(R.id.createPackDollarSignTextView);
        mRateValue = view.findViewById(R.id.createPackDollarEditText);
        mPerText = view.findViewById(R.id.createPackPerTextView);
        mLendPeriodText = view.findViewById(R.id.createPackLendPeriodTextView);
        mCalPeriodValue = view.findViewById(R.id.createPackLendPeriodEditText);
        mLendPeriodTextValue = view.findViewById(R.id.createPackRatePeriodValueTextView);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragment().getChildFragmentManager().popBackStack("imageFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                hideKeyboard();
            }
        });

        mIndefSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    hideViews();
                    isIndefinite = true;
                }else{
                    showViews();
                    isIndefinite = false;
                }
            }
        });

        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment calendarFragment = new CalendarFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.addPackageContainer, calendarFragment).addToBackStack("addPackFrag").commit();
            }
        });



        //initialize spinner, spinner adapter, handle spinner cases
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.lend_period_choices, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLendPeriodSpinner.setAdapter(spinnerAdapter);
        mLendPeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            Calendar returnDate;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                mDataModel.getSelectedStartDate().observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String date) {
                        mStartDateTextValue.setText(date);
                        isDateSet = true;
                    }
                });

                if(!TextUtils.isEmpty(mCalPeriodValue.getText()) && !mIndefSwitch.isChecked() && isDateSet) {

                    int numOfPeriod = Integer.parseInt(mCalPeriodValue.getText().toString().trim());
                    returnDate = mDataModel.getSelectedReturnDate().getValue();

                    switch (position) {
                        case 1:
                            returnDate.add(Calendar.DAY_OF_MONTH, numOfPeriod);  //Used to calculate return date in calendar fragment
                            mLendPeriodTextValue.setText(R.string.days);
                            break;
                        case 2:
                            returnDate.add(Calendar.WEEK_OF_YEAR, numOfPeriod);
                            mLendPeriodTextValue.setText(R.string.weeks);
                            break;
                        case 3:
                            returnDate.add(Calendar.MONTH, numOfPeriod);
                            mLendPeriodTextValue.setText(R.string.months);
                            break;
                    }

                    SimpleDateFormat formatEndDate = new SimpleDateFormat("MMM-dd-YY", Locale.getDefault());
                    mReturnDate = formatEndDate.format(returnDate.getTime());
                    mReturnDateTextValue.setText(mReturnDate);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAddPackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String blankFieldErr = "This field is empty";
                String spinnerNotSetErr = "You must pick a time period";
                String calendarNotSetErr = "You must pick a date if not lending indefinitely";

                String borrowerName = mBorrowerName.getText().toString().trim();
                String borrowerEmail = mBorrowerEmail.getText().toString().trim().toLowerCase();
                String packName = mPackHeaderText.getText().toString().trim();
                String itemList = mItemList.getText().toString().trim();
                String rate = mRateValue.getText().toString().trim();
                String lendPeriod = mCalPeriodValue.getText().toString().trim();

                boolean indefinite = isIndefinite;

                if(mIndefSwitch.isChecked()){
                    mStartDate = null;
                    mReturnDate = null;
                }

                if(TextUtils.isEmpty(borrowerName) || TextUtils.isEmpty(borrowerEmail) || TextUtils.isEmpty(packName)
                        || TextUtils.isEmpty(itemList)){

                    mBorrowerName.setError(blankFieldErr);
                    mBorrowerEmail.setError(blankFieldErr);
                    mPackHeaderText.setError(blankFieldErr);
                    mItemList.setError(blankFieldErr);

                    //TODO Fix validation logic
                /*}else if(!mIndefSwitch.isChecked()){
                    if(TextUtils.isEmpty(rate) || TextUtils.isEmpty(lendPeriod)) {
                        mRateValue.setError(blankFieldErr);
                        mCalPeriodValue.setError(blankFieldErr);
                    }*/
                }else{
                    String uId = mUser.getUid();

                    writePackageToFirestore(uId,borrowerName, borrowerEmail, packName, itemList, indefinite, mReturnDate);

                }

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

    public void writePackageToFirestore(String uId, String borrowerName, String borrowerEmail, String packName, String itemList, boolean indefinite, String date){


        //Add to lender collection
        DocumentReference packRef = mDatabase.collection(USER_COLLECTION).document(uId).collection(LEND_PACKAGE_COLLECTION).document();
        String packId = packRef.getId();
        String lenderName = mDataModel.getSelectedLenderName().getValue();

        //Add to borrower collection with same packUid
        CollectionReference borrowerRef = mDatabase.collection(USER_COLLECTION);
        borrowerRef.whereEqualTo("email", borrowerEmail).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    boolean isValidUser = false;
                    String borrowerId = "";
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if(document.exists()) {
                            isValidUser = true;
                            borrowerId = document.getReference().getId();
                            pushImageBitmapsToFirebaseStorage(uId, packId);

                            //Package added for lender
                            Package userPackage = new Package(lenderName, borrowerName, borrowerEmail, packId, packName, itemList, indefinite, date, mImagePaths);
                            packRef.set(userPackage);

                            //package added for borrower
                            mDatabase.collection(USER_COLLECTION).document(borrowerId).collection(BORROW_PACKAGE_COLLECTION).document(packId).set(userPackage);

                            Toast.makeText(getActivity(), "Package Added", Toast.LENGTH_SHORT).show();
                            mOnFragmentStateChange.setActionBarListener(R.string.app_name);
                            mOnFragmentStateChange.setFragmentVisible(false);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.popBackStack("homeFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            hideKeyboard();
                        }else{
                            isValidUser = false;
                        }
                    }
                    String invalidEmailErr = "User does not exist in Lendsum";

                    if(!isValidUser){
                        mBorrowerEmail.setError(invalidEmailErr);
                    }
                }

            }

        });


    }

    private void pushImageBitmapsToFirebaseStorage(String uId, String packId){
        List<Bitmap> imageBitmaps = mDataModel.getSelectedImageArray().getValue();

        mImagePaths = new ArrayList<>();

        if(imageBitmaps.size() > 0) {

            for (Bitmap bitmap : imageBitmaps) {

                //Create paths for all images
                String path = "lendsum/users/" + uId + "/" + packId + "/" + UUID.randomUUID() + ".jpg";

                StorageReference imageStorageRef = mStorage.getReference(path);

                //Turn image to byte image
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                //Upload to Firebase Storage
                UploadTask uploadTask = imageStorageRef.putBytes(data);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Problem Uploading Images", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                });

                //Add image paths to arraylist for firestore
                mImagePaths.add(path);

            }
        }
    }

    private void hideSpecificView(View v){
        v.setVisibility(View.INVISIBLE);
    }

    private void showSpecificView(View v){
        v.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        mDatePickerBtn.setVisibility(View.INVISIBLE);
        mStartDateText.setVisibility(View.INVISIBLE);
        mReturnDateText.setVisibility(View.INVISIBLE);
        mReturnDateTextValue.setVisibility(View.INVISIBLE);
        mLendPeriodSpinner.setVisibility(View.INVISIBLE);
        mRateText.setVisibility(View.INVISIBLE);
        mReturnDateTextValue.setVisibility(View.INVISIBLE);
        mDollarSignText.setVisibility(View.INVISIBLE);
        mRateValue.setVisibility(View.INVISIBLE);
        mPerText.setVisibility(View.INVISIBLE);
        mLendPeriodText.setVisibility(View.INVISIBLE);
        mCalPeriodValue.setVisibility(View.INVISIBLE);
        mLendPeriodTextValue.setVisibility(View.INVISIBLE);
    }

    public void showViews(){
        mDatePickerBtn.setVisibility(View.VISIBLE);
        mStartDateText.setVisibility(View.VISIBLE);
        mReturnDateText.setVisibility(View.VISIBLE);
        mReturnDateTextValue.setVisibility(View.VISIBLE);
        mLendPeriodSpinner.setVisibility(View.VISIBLE);
        mRateText.setVisibility(View.VISIBLE);
        mReturnDateTextValue.setVisibility(View.VISIBLE);
        mDollarSignText.setVisibility(View.VISIBLE);
        mRateValue.setVisibility(View.VISIBLE);
        mPerText.setVisibility(View.VISIBLE);
        mLendPeriodText.setVisibility(View.VISIBLE);
        mCalPeriodValue.setVisibility(View.VISIBLE);
        mLendPeriodTextValue.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard(){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentStateChange = null;
    }

}
