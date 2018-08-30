package com.jordanmadrigal.lendsum.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jordanmadrigal.lendsum.R;

import static com.jordanmadrigal.lendsum.Utility.Constants.USER_COLLECTION;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getName();
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button mDeleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDeleteBtn = findViewById(R.id.deleteProfBtn);
        mDatabase = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteUserFromFirestore();

                mUser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(SettingsActivity.this, "User account deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                        Toast.makeText(SettingsActivity.this, "You must log out and back in to delete account", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void deleteUserFromFirestore(){

        String uID = mUser.getUid();

        DocumentReference userRef = mDatabase.collection(USER_COLLECTION).document(uID);
        userRef.delete();

    }
}
