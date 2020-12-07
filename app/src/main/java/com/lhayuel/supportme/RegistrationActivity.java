package com.lhayuel.supportme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private EditText emailTextView, passwordTextView, confirm_password;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        confirm_password = findViewById(R.id.confirm_password);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {
       /* //keyboard hide
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }*/

        // Take the value of two edit texts in Strings
        String email, password, confirmp;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        confirmp = confirm_password.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),"Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),"Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(confirmp))
        {
            Toast.makeText(getApplicationContext(),"Please confirm your password",Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmp))
        {
            Toast.makeText(getApplicationContext(), "your password do not match", Toast.LENGTH_SHORT).show();
        }
        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Registration successful!", Toast.LENGTH_LONG).show();
                            SendEmailVerificationMessage();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }

                    private void SendEmailVerificationMessage() {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null)
                            {
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            //Toast.makeText(RegistrationActivity.this, "Registration Successful, we've sent you a mail. Please check and verify your account... ", Toast.LENGTH_SHORT).show();
                                            //SendUserToLoginActivity();

                                            //Redirect User to login activity
                                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                            mAuth.signOut();
                                        }
                                        else
                                        {
                                            String error = Objects.requireNonNull(task.getException()).getMessage();
                                            Toast.makeText(RegistrationActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                        }
                                    }
                                });
                            }
                    }
                });
    }
}
