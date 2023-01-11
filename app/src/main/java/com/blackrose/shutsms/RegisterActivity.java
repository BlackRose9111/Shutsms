package com.blackrose.shutsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    TextView email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        findViewById(R.id.registerLogin).setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
        findViewById(R.id.registerRegisterButton).setOnClickListener(v -> {
            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

            } else {
                if (password.getText().length() < 10) {
                    Toast.makeText(this, "Password must be at least 10 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}



