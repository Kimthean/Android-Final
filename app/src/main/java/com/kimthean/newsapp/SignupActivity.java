package com.kimthean.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailEditText;
    EditText passwordEditText;

    EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

         emailEditText = findViewById(R.id.emailEditText);
         passwordEditText = findViewById(R.id.passwordEditText);
         usernameEditText = findViewById(R.id.usernameEditText);
        Button signUpButton = findViewById(R.id.signUpBtn);
        TextView login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signUpWithEmail(email, password);
            }
        });

    }

    private void signUpWithEmail(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(SignupActivity.this, "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usernameEditText.getText().toString())
                                        .build());
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            }
                        } else {
                                if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                    passwordEditText.setError("The provided password is too weak.");
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    emailEditText.setError("The email address is malformed.");
                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    emailEditText.setError("The email address is already in use.");
                                } else {
                                    emailEditText.setError("Authentication failed.");
                                }
                        }
                    }
                });
    }


}
