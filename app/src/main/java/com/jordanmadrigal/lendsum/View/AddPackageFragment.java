package com.jordanmadrigal.lendsum.View;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jordanmadrigal.lendsum.Interfaces.OnActivityToFragmentListener;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.ViewModel.DataViewModel;
import static com.jordanmadrigal.lendsum.Utility.Constants.BORROW_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.LEND_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.USER_COLLECTION;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPackageFragment extends Fragment{

    private static final String LOG_TAG = AddPackageFragment.class.getSimpleName();

    private FirebaseFirestore mDatabase;
    private FirebaseUser mUser;
    private DataViewModel mDataModel;
    private TextView mReturnDateTextView;
    private EditText mPackHeaderText, mItemList, mBorrowerEmail;
    private Button mAddPackBtn;
    private Switch mIndefSwitch;
    private ImageButton mDatePickerBtn, mCloseBtn;
    private boolean mIndefinite;
    private String packageName;
    private OnActivityToFragmentListener mActionBarListener, mFragmentVisibilityListener;

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

        mIndefinite = false;
        mDatabase = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mCloseBtn = view.findViewById(R.id.createPackCloseBtn);
        mPackHeaderText = view.findViewById(R.id.createPackName);
        mBorrowerEmail = view.findViewById(R.id.createPackBorrowerEmail);
        mItemList = view.findViewById(R.id.createPackItemList);
        mAddPackBtn = view.findViewById(R.id.createPackBtn);
        mReturnDateTextView = view.findViewById(R.id.createPackReturnDateTextView);
        mDatePickerBtn = view.findViewById(R.id.datePickerBtn);
        mIndefSwitch = view.findViewById(R.id.createPackIndefSwitch);

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentVisibilityListener.onFragmentVisible(false);
                mActionBarListener.onActionBarListener(R.string.app_name);
                getFragmentManager().popBackStack();
            }
        });

        mIndefSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mDatePickerBtn.setVisibility(View.INVISIBLE);
                    mReturnDateTextView.setVisibility(View.INVISIBLE);
                    mIndefinite = true;
                }else{
                    mDatePickerBtn.setVisibility(View.VISIBLE);
                    mReturnDateTextView.setVisibility(View.VISIBLE);
                    mIndefinite = false;
                }
            }
        });

        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment calendarFragment = new CalendarFragment();
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.addPackageContainer, calendarFragment).addToBackStack("AddPackFrag").commit();
            }
        });


        mAddPackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String packHeadTextErr = "Package name cannot be left blank";
                String packItemListErr = "Item List cannot be left blank";
                String packBorrowerNameErr = "Borrower name cannot be left blank";

                String packName = mPackHeaderText.getText().toString().trim();
                String borrowerEmail = mBorrowerEmail.getText().toString().trim();
                String itemList = mItemList.getText().toString().trim();
                String date = mDataModel.getSelectedDate().getValue();
                boolean indefinite = mIndefinite;

                if(mIndefSwitch.isChecked()){
                    date = null;
                }

                if(TextUtils.isEmpty(packName)){
                    mPackHeaderText.setError(packHeadTextErr);
                }else if(TextUtils.isEmpty(itemList)){
                    mItemList.setError(packItemListErr);
                }else if(TextUtils.isEmpty(borrowerEmail)){
                    mBorrowerEmail.setError(packBorrowerNameErr);
                }else{

                    String uId = mUser.getUid();

                    writePackageToFirestore(uId, packName, borrowerEmail, itemList, indefinite, date);

                }
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mFragmentVisibilityListener = (OnActivityToFragmentListener) context;
            mActionBarListener = (OnActivityToFragmentListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnFragmentInteraction Listener");
        }
    }

    public void writePackageToFirestore(String uId, String packName, String borrowerEmail, String itemList, boolean indefinite, String date){

        //Add to lender collection
        DocumentReference packRef = mDatabase.collection(USER_COLLECTION).document(uId).collection(LEND_PACKAGE_COLLECTION).document();
        String packId = packRef.getId();

        Package userPackage = new Package(packName, packId, borrowerEmail, itemList, indefinite, date);

        //Add to borrower collection with same packUid
        CollectionReference borrowerRef = mDatabase.collection(USER_COLLECTION);
        borrowerRef.whereEqualTo("email", borrowerEmail).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    String borrowerId = "";
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if(document.exists()) {
                            mActionBarListener.onActionBarListener(R.string.app_name);
                            borrowerId = document.getReference().getId();
                            packRef.set(userPackage);
                            mDatabase.collection(USER_COLLECTION).document(borrowerId).collection(BORROW_PACKAGE_COLLECTION).document(packId).set(userPackage);
                            getFragmentManager().popBackStack();
                            Toast.makeText(getActivity(), "Package Added", Toast.LENGTH_SHORT).show();
                            mFragmentVisibilityListener.onFragmentVisible(false);
                        }else{
                            mBorrowerEmail.setError("Error");
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActionBarListener = null;
        mFragmentVisibilityListener = null;
    }


}
