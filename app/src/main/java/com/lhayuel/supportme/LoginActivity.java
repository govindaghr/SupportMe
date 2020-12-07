package com.lhayuel.supportme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private TextView NeedNewAccountLink, ForgetPasswordLink;
    private Boolean checkEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        progressbar = findViewById(R.id.progressBar);

        NeedNewAccountLink = (TextView) findViewById(R.id.register_account_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);

        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });

        ForgetPasswordLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void loginUserAccount()
    {
        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                if (task.isSuccessful()) {
                                    //check if email verified
                                    VerifyEmailAddress();
                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }

                                else {
                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),"Login failed!!", Toast.LENGTH_LONG).show();
                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                });
    }
    private void VerifyEmailAddress()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        checkEmail = user.isEmailVerified();

        if(checkEmail)
        {
            // if sign-in is successful
            // intent to home activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            //Snackbar.make(findViewById(android.R.id.content), "Login Successful", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Please verify your Account", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}