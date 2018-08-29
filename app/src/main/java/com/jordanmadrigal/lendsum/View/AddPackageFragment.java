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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jordanmadrigal.lendsum.Interfaces.OnActivityToFragmentListener;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.ViewModel.DateViewModel;
import com.jordanmadrigal.lendsum.ViewModel.PackageViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPackageFragment extends Fragment {

    private static final String LOG_TAG = AddPackageFragment.class.getSimpleName();
    private static final String USER_COLLECTION = "users";
    private static final String PACKAGE_COLLECTION = "packages";

    private FirebaseFirestore mDatabase;
    private FirebaseUser mUser;
    private PackageViewModel mPackViewModel;
    private DateViewModel mDateModel;
    private TextView mReturnDateTextView;
    private EditText mPackHeaderText, mItemList;
    private Button mAddPackBtn;
    private Switch mIndefSwitch;
    private ImageButton mDatePickerBtn, mCloseBtn;
    private boolean mIndefinite;
    private OnActivityToFragmentListener mActionBarListener, mFragmentVisibilityListener;

    public AddPackageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDateModel = ViewModelProviders.of(getActivity()).get(DateViewModel.class);
        mPackViewModel = ViewModelProviders.of(getActivity()).get(PackageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_add_package, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String packHeadTextErr = "Package name cannot be left blank";
        final String packItemListErr = "Item List cannot be left blank";

        mIndefinite = false;
        mDatabase = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mCloseBtn = view.findViewById(R.id.createPackCloseBtn);
        mPackHeaderText = view.findViewById(R.id.createPackName);
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

                mFragmentVisibilityListener.onFragmentVisible(false);
                String packName = mPackHeaderText.getText().toString().trim();
                String itemList = mItemList.getText().toString().trim();
                String date = mDateModel.getSelected().getValue();
                boolean indefinite = mIndefinite;

                if(mIndefSwitch.isChecked()){
                    date = null;
                }

                if(TextUtils.isEmpty(packName)){
                    mPackHeaderText.setError(packHeadTextErr);
                }else if(TextUtils.isEmpty(itemList)){
                    mItemList.setError(packItemListErr);
                }else{

                    String uId = mUser.getUid();
                    Package userPackage = new Package(packName, itemList, indefinite, date);

                    //Add data to Firestore and populate in UI
                    addPackageToFirestore(uId, userPackage);

                    mPackViewModel.setSelectedPack(userPackage);

                    //displayFirstorePackageUI(uId, packName);

                    mActionBarListener.onActionBarListener(R.string.app_name);

                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }



                    Toast.makeText(getActivity(), "Package Added", Toast.LENGTH_SHORT).show();
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

    public void addPackageToFirestore(String uId, Package userPackage){

        String packageName = userPackage.getPackageName();

        //Add to firestore database
        mDatabase.collection(USER_COLLECTION).document(uId).collection(PACKAGE_COLLECTION).document(packageName).set(userPackage);

    }

    //TODO Check into getting info from firestore.
    /*public  void displayFirstorePackageUI(String uID, String packageName){

        DocumentReference document = mDatabase.collection(USER_COLLECTION).document(uID).collection(PACKAGE_COLLECTION).document(packageName);

        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {

                    Package userPackage = documentSnapshot.toObject(Package.class);

                    String packNameDisplay = userPackage.getPackageName();
                    String itemListDisplay = userPackage.getItemList();
                    boolean indefiniteDisplay = userPackage.isIndefinite();
                    String returnDateDisplay = userPackage.getReturnDate();

                    mPackViewModel.setSelectedPack(new Package(packNameDisplay, itemListDisplay, indefiniteDisplay, returnDateDisplay));

                }

            }
        });
    }*/



    @Override
    public void onDetach() {
        super.onDetach();
        mActionBarListener = null;
        mFragmentVisibilityListener = null;
    }

}
