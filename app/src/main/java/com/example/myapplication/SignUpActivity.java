package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEdt, passwordEdt, userName;
    private ProgressBar progressBarRegister;

    private FirebaseAuth mAuth;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        userName = findViewById(R.id.userName);
        progressBarRegister = findViewById(R.id.progressBarRegister);



    }

    public void returnLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void goHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void Register(View view) {
        closeKeyboard();
        progressBarRegister.setVisibility(View.VISIBLE);
        String user_name = userName.getText().toString().trim();
        String email = emailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();


        if (user_name.isEmpty()) {
            userName.setError("Empty Field");
            userName.requestFocus();
            progressBarRegister.setVisibility(View.INVISIBLE);

            return;
        }

        if (password.isEmpty() || password.length() < 6) {

            passwordEdt.setError("The password should contain more than 6 symbols");
            passwordEdt.requestFocus();
            progressBarRegister.setVisibility(View.INVISIBLE);

            return;
        }
        if (email.isEmpty()) {
            emailEdt.setError("Please provide valid email");
            emailEdt.requestFocus();
            progressBarRegister.setVisibility(View.INVISIBLE);

            return;

        }

        if (password.isEmpty()) {
            passwordEdt.setError("Field can't be empty");
            progressBarRegister.setVisibility(View.INVISIBLE);


        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordEdt.setError("Password too weak , Add Upper Case and special symbols");
            progressBarRegister.setVisibility(View.INVISIBLE);


        } else {
            if (!user_name.isEmpty() && !email.isEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    passwordEdt.setError(null);
                    progressBarRegister.setVisibility(View.VISIBLE);


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                User user = new User(user_name, password, email, date);
                                firestore.collection("User").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBarRegister.setVisibility(View.GONE);
                                            Toast.makeText(SignUpActivity.this, "Register success :)", Toast.LENGTH_SHORT).show();
                                            goHomeActivity();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Register Failed :(", Toast.LENGTH_SHORT).show();
                                            progressBarRegister.setVisibility(View.GONE);


                                        }
                                    }
                                });


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressBarRegister.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    emailEdt.setError("Please provide valid email");
                    progressBarRegister.setVisibility(View.INVISIBLE);

                }


            }


        }


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}