package com.jordanmadrigal.lendsum.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.Model.User;
import com.jordanmadrigal.lendsum.ViewModel.DataViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jordanmadrigal.lendsum.Utility.Constants.BORROW_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.LEND_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.USER_COLLECTION;

public class FirebaseService {

    private FirebaseUser mUser;
    private FirebaseFirestore mDatabase;
    private DataViewModel mDataModel;
    private String LOG_TAG = FirebaseService.class.getSimpleName();

    public FirebaseService(FirebaseUser mUser, FirebaseFirestore mDatabase) {
        this.mUser = mUser;
        this.mDatabase = mDatabase;
    }

    public FirebaseService(FirebaseUser mUser, FirebaseFirestore mDatabase, DataViewModel mDataModel) {
        this.mUser = mUser;
        this.mDatabase = mDatabase;
        this.mDataModel = mDataModel;
    }

    public void getFirestoreNameDisplay(){
        DocumentReference document = mDatabase.collection(USER_COLLECTION).document(mUser.getUid());

        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {

                    User user = documentSnapshot.toObject(User.class);

                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();
                    String fullName = firstName + " " + lastName;

                    mDataModel.setSelectedLenderName(firstName);
                    mDataModel.setSelectedUserNameDisplay(fullName);

                }
            }
        });
    }

    public void writePackageToFirestore(Context context, EditText borrowerEmailInput, String uId, String borrowerName, String borrowerEmail, String packName, String itemList, boolean indefinite, String date){


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
                    String borrowerId;
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if(document.exists()) {
                            isValidUser = true;
                            borrowerId = document.getReference().getId();

                            List<String> imagePaths = new ArrayList<>();
                            pushImageBitmapsToFirebaseStorage(context, uId, packId, imagePaths);

                            //Package added for lender
                            Package userPackage = new Package(lenderName, borrowerName, borrowerEmail, packId, packName, itemList, indefinite, date, imagePaths);
                            packRef.set(userPackage);

                            //package added for borrower
                            mDatabase.collection(USER_COLLECTION).document(borrowerId).collection(BORROW_PACKAGE_COLLECTION).document(packId).set(userPackage);

                        }else{
                            isValidUser = false;
                        }

                        mDataModel.setIsValidUser(isValidUser);
                    }
                    String invalidEmailErr = "User does not exist in Lendsum";

                    if(!isValidUser){
                        borrowerEmailInput.setError(invalidEmailErr);
                    }
                }

            }

        });


    }

    private void pushImageBitmapsToFirebaseStorage(Context context, String uId, String packId, List<String> imagePaths){
        List<Bitmap> imageBitmaps = mDataModel.getSelectedImageArray().getValue();

        if(imageBitmaps.size() > 0) {

            for (Bitmap bitmap : imageBitmaps) {

                //Create paths for all images
                String path = "lendsum/users/" + uId + "/" + packId + "/" + UUID.randomUUID() + ".jpg";

                FirebaseStorage imageStorage = FirebaseStorage.getInstance();
                StorageReference imageStorageRef = imageStorage.getReference(path);

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
                        Toast.makeText(context, "Problem Uploading Images", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                });

                //Add image paths to arraylist for firestore
                imagePaths.add(path);

            }
        }
    }

    public void deleteDataFromFirebase(String packageHeader, String borrowEmail){

        String uId = mUser.getUid();

        //Delete lender package from firestore
        CollectionReference lenderPackRef = mDatabase.collection(USER_COLLECTION).document(uId).collection(LEND_PACKAGE_COLLECTION);
        lenderPackRef.whereEqualTo("packageName", packageHeader).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Package userPackage = document.toObject(Package.class);

                    //delete back end storage photo files
                    deleteImagesFromFirebaseStorage(userPackage);

                    document.getReference().delete();


                }
            }
        });

        //Delete borrower package from firestore
        CollectionReference userColRef = mDatabase.collection(USER_COLLECTION);
        userColRef.whereEqualTo("email", borrowEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            String borrowerUId = "";
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot userDocument : task.getResult()) {
                    borrowerUId = userDocument.getReference().getId();
                }
                CollectionReference borrowPackRef = mDatabase.collection(USER_COLLECTION).document(borrowerUId).collection(BORROW_PACKAGE_COLLECTION);
                borrowPackRef.whereEqualTo("packageName", packageHeader).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot packDocument : task.getResult()) {
                            packDocument.getReference().delete();
                        }
                    }
                });
            }
        });


    }

    private void deleteImagesFromFirebaseStorage(Package userPackage){

        List<String> imagePaths = userPackage.getImagePaths();

        for (String path : imagePaths){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imagesRef = storage.getReference(path);

            imagesRef.delete();
        }


    }
}
