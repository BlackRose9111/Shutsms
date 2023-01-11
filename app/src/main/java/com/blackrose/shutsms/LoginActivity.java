package com.blackrose.shutsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView email, password;
    FirebaseAuth auth;

    //thepasswordisomerfaruk1234567@gmail.com
    //newpassword
    //newaccount@gmail.com
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        findViewById(R.id.loginRegister).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
        findViewById(R.id.loginLoginButton).setOnClickListener(v -> {
            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

            } else {
                if (password.getText().length() < 10) {
                    Toast.makeText(this, "Password must be at least 10 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}