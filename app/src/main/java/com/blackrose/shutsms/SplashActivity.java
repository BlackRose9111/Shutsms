package com.blackrose.shutsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.splashRegister).setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
            finish();
        });
        findViewById(R.id.splashLogin).setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        });
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));

        }
    }
}