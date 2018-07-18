package com.jordanmadrigal.lendsum;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassActivity extends AppCompatActivity {

    private static final String LOG_TAG = ForgotPassActivity.class.getName();

    private FirebaseAuth mAuth;
    private EditText mPassword;
    private Button mResetEmailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        mAuth = FirebaseAuth.getInstance();
        mPassword = findViewById(R.id.resetPassEditText);
        mResetEmailBtn = findViewById(R.id.resetPassBtn);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mResetEmailBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPassActivity.this, "Email cannot be left blank", Toast.LENGTH_SHORT).show();
                }else{
                    sendResetEmail(email);
                    Intent intent = new Intent(ForgotPassActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void sendResetEmail(final String email){
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassActivity.this, "Email sent to " + email, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(LOG_TAG, e.getMessage());
                            Toast.makeText(ForgotPassActivity.this, "Email does not exist in database", Toast.LENGTH_SHORT).show();
                        }
            });

    }

}
