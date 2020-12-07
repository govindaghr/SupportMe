package com.lhayuel.supportme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button reset_button;
    private EditText user_email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        reset_button = (Button) findViewById(R.id.reset_button);
        user_email = (EditText) findViewById(R.id.user_email);

    }

    public void resetUserPassword(View view) {
        String userEmail = user_email.getText().toString();

        if(TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(ForgotPasswordActivity.this, "Please Enter Email!", Toast.LENGTH_SHORT).show();
        }
        else if (!isValid(userEmail)) {
            Toast.makeText(ForgotPasswordActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ForgotPasswordActivity.this, "Please Check your Email Inbox to reset password", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    }
                    else
                    {
                        String message = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(ForgotPasswordActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            );
        }
    }

    public boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}