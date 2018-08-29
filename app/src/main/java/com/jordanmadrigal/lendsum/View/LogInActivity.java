package com.jordanmadrigal.lendsum.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jordanmadrigal.lendsum.R;

public class LogInActivity extends AppCompatActivity{

    private static final String LOG_TAG = LogInActivity.class.getName();

    private FirebaseAuth mAuth;
    private TextView mForgotPass;
    private TextView mCreateNewProf;
    private EditText mLogInEmail;
    private EditText mLogInPassword;
    private Button mLogInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mForgotPass = findViewById(R.id.logInForgotPasswordTextView);
        mCreateNewProf = findViewById(R.id.logInCreateAccountTextView);
        mLogInEmail = findViewById(R.id.logInEmailEditText);
        mLogInPassword = findViewById(R.id.logInPasswordEditText);
        mLogInBtn = findViewById(R.id.logInButton);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });

        mLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String email = mLogInEmail.getText().toString().trim();
                 String password = mLogInPassword.getText().toString().trim();
                 String emailErrMsg = "Email cannot be empty";
                 String passwordErrMsg = "Password cannot be empty";

                 if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                     mLogInEmail.setError(emailErrMsg);
                     mLogInPassword.setError(passwordErrMsg);
                 }else{
                     logIn(email, password);
                 }
            }
        });

        mCreateNewProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createProfIntent = new Intent(LogInActivity.this, CreateProfileActivity.class);
                startActivity(createProfIntent);
            }
        });



    }

    public void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(LOG_TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO update UI
                            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
