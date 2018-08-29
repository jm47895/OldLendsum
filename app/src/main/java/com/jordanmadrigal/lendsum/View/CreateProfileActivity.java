package com.jordanmadrigal.lendsum.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jordanmadrigal.lendsum.Model.User;
import com.jordanmadrigal.lendsum.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateProfileActivity extends AppCompatActivity {

    private static final String TAG = CreateProfileActivity.class.getName();
    private static final String USER_COLLECTION = "users";


    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateProfileBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirstName = findViewById(R.id.createProfFirstNameEditText);
        mLastName = findViewById(R.id.createProfLastNameEditText);
        mEmail = findViewById(R.id.createProfEmailEditText);
        mPassword = findViewById(R.id.createProfPassEditText);
        mCreateProfileBtn = findViewById(R.id.createProfNextBtn);

    }


    @Override
    protected void onStart() {
        super.onStart();

        mCreateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseFirestore.getInstance();
                String firstName = mFirstName.getText().toString().trim();
                String lastName = mLastName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if(isValidUserForm(firstName, lastName, email, password)){

                    signUpUser(firstName, lastName, email, password);
                }

            }
        });

    }

    //Authenticate user and add them to firestore
    public void signUpUser(final String firstName, final String lastName, final String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    String uID = mAuth.getUid();
                    User newUser = new User(uID, firstName, lastName, email);

                    mDatabase.collection(USER_COLLECTION).document(uID).set(newUser);

                    Toast.makeText(CreateProfileActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User Created: Success");
                    Intent intent = new Intent(CreateProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Log.d(TAG, task.getException().getMessage());
                    Toast.makeText(CreateProfileActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //User form validation check (unitTested)
    public boolean isValidUserForm(String firstName, String lastName, String email, String password){

        String firstNameErrMsg = "First name cannot be left blank";
        String lastNameErrMsg = "Last name cannot be left blank";
        String invalidEmailMsg = "Invalid Email";
        String invalidPasswordMsg = "Password must contain between 6 and 20 characters long and have at least one UpperCase, one number, and one symbol";

        if(TextUtils.isEmpty(firstName)
                && TextUtils.isEmpty(lastName)){
            mFirstName.setError(firstNameErrMsg);
            mLastName.setError(lastNameErrMsg);
            return false;
        }else if(!isValidEmail(email)){
                mEmail.setError(invalidEmailMsg);
            return false;
        }else if(TextUtils.isEmpty(password)
                || !isValidPassword(password)){
                mPassword.setError(invalidPasswordMsg);
            return false;
        }
        return true;
    }

    //Password validation with regex for at least one letter, one number, and one number in password
    public boolean isValidPassword(String password){

        Pattern pattern;
        Matcher matchCase;
        boolean isValid;
        final String PASSWORD_PATTERN = "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@%+/\'!#$^?:,(){}~_.]).{6,20})$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matchCase = pattern.matcher(password);
        isValid = matchCase.matches();

        return isValid;

    }

    //Email validation (unitTested)
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Get the initials of of username for uID (unitTested)
    /*public String getUserInitials(String fName, String lName){

        String  fInitial = fName.substring(0, 1).toUpperCase();
        String lInitial = lName.substring(0,1).toUpperCase();


        return fInitial+lInitial;
    }*/


}
