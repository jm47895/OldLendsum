package com.jordanmadrigal.lendsum.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String LOG_TAG = SplashScreenActivity.class.getName();
    private FirebaseAuth mUserAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mUserAuth.getCurrentUser() != null) {
            FirebaseUser currentUser = mUserAuth.getCurrentUser();

            //TODO Update UI when Authenticated

            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            Log.i(LOG_TAG, "Continue to Home");
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
            Log.i(LOG_TAG, "Continue to Log In");
        }

    }
}
