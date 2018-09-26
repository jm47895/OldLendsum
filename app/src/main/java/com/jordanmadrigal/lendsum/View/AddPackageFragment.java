package com.jordanmadrigal.lendsum.View;


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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.List;
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
    private TextView mReturnDateTextView;
    private EditText mPackHeaderText, mItemList, mBorrowerEmail, mBorrowerName;
    private Button mAddPackBtn, mBackButton;
    private Switch mIndefSwitch;
    private ImageButton mDatePickerBtn;
    private boolean isIndefinite;
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

        isIndefinite = false;
        mDatabase = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mPackHeaderText = view.findViewById(R.id.createPackName);
        mBorrowerName= view.findViewById(R.id.createPackUserName);
        mBorrowerEmail = view.findViewById(R.id.createPackBorrowerEmail);
        mItemList = view.findViewById(R.id.createPackItemList);
        mBackButton = view.findViewById(R.id.createPackBackBtn);
        mAddPackBtn = view.findViewById(R.id.createPackBtn);
        mReturnDateTextView = view.findViewById(R.id.createPackReturnDateTextView);
        mDatePickerBtn = view.findViewById(R.id.datePickerBtn);
        mIndefSwitch = view.findViewById(R.id.createPackIndefSwitch);

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
                    mDatePickerBtn.setVisibility(View.INVISIBLE);
                    mReturnDateTextView.setVisibility(View.INVISIBLE);
                    isIndefinite = true;
                }else{
                    mDatePickerBtn.setVisibility(View.VISIBLE);
                    mReturnDateTextView.setVisibility(View.VISIBLE);
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


        mAddPackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String blankBorrowerName = "Borrower name cannot be left blank";
                String blankBorrowerEmailErr = "Borrower email cannot be left blank";
                String blankPackTextErr = "Package name cannot be left blank";
                String blankItemListErr = "Item List cannot be left blank";


                String borrowerName = mBorrowerName.getText().toString().trim();
                String borrowerEmail = mBorrowerEmail.getText().toString().trim().toLowerCase();
                String packName = mPackHeaderText.getText().toString().trim();
                String itemList = mItemList.getText().toString().trim();
                String date = mDataModel.getSelectedDate().getValue();
                boolean indefinite = isIndefinite;

                if(mIndefSwitch.isChecked()){
                    date = null;
                }

                if(TextUtils.isEmpty(borrowerName)){
                    mBorrowerName.setError(blankBorrowerName);
                }else if(TextUtils.isEmpty(borrowerEmail)){
                    mBorrowerEmail.setError(blankBorrowerEmailErr);
                }else if(TextUtils.isEmpty(packName)){
                    mPackHeaderText.setError(blankPackTextErr);
                }else if(TextUtils.isEmpty(itemList)){
                    mItemList.setError(blankItemListErr);
                }else{
                    String uId = mUser.getUid();

                    writePackageToFirestore(uId,borrowerName, borrowerEmail, packName, itemList, indefinite, date);

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
