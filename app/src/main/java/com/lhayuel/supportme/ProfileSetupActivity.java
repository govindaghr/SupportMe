package com.lhayuel.supportme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class ProfileSetupActivity extends AppCompatActivity {

    private EditText UserName, FullName, Dsitrict;
    private Button SaveInformationbutton;
    //private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        //UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_full_name);
        Dsitrict = (EditText) findViewById(R.id.setup_district);
        SaveInformationbutton = (Button) findViewById(R.id.setup_information_button);
        //ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        loadingBar = new ProgressDialog(this);

        SaveInformationbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SaveAccountSetupInformation();
            }
        });

    }

    private void SaveAccountSetupInformation()
    {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String district = Dsitrict.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your user name...", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(district))
        {
            Toast.makeText(this, "Please write your country...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("district", district);
            userMap.put("address_current", "none");
            userMap.put("occupation", "none");
            userMap.put("mobile", "none"); //mobile No
            userMap.put("gender", "none");
            userMap.put("dob", "none"); //date of birth
            userMap.put("marital_status", "none");
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(ProfileSetupActivity.this, "Your Account is created Successfully.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(ProfileSetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(ProfileSetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}