package com.example.planetb.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.planetb.MainActivity;
import com.example.planetb.R;
import com.example.planetb.lists.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    TextInputLayout regFirstName, regLastName, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;
    ConstraintLayout containerLayout;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(signupActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = signupActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(signupActivity.this, R.color.gray_background));

        setContentView(R.layout.activity_signup);

        containerLayout = findViewById(R.id.container);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://planetb-c524b-default-rtdb.firebaseio.com/");

        regFirstName = findViewById(R.id.reg_first_name);
        regLastName = findViewById(R.id.reg_last_name);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);

        regBtn.setOnClickListener(this);

        regFirstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateFirstNameO()){
                    regFirstName.setError(null);
                    regFirstName.setErrorEnabled(false);
                }
            }
        });
        regLastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateLastNameO()){
                    regLastName.setError(null);
                    regLastName.setErrorEnabled(false);
                }
            }
        });
        regEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateEmailO()){
                    regEmail.setError(null);
                    regEmail.setErrorEnabled(false);
                }
            }
        });
        regPhoneNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validatePhoneNoO()){
                    regPhoneNo.setError(null);
                    regPhoneNo.setErrorEnabled(false);
                }
            }
        });
        regPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validatePasswordO()){
                    regPassword.setError(null);
                    regPassword.setErrorEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_btn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String firstName = regFirstName.getEditText().getText().toString().trim();
        String lastName = regLastName.getEditText().getText().toString().trim();
        String email = regEmail.getEditText().getText().toString().trim();
        String phoneNo = regPhoneNo.getEditText().getText().toString().trim();
        String password = regPassword.getEditText().getText().toString().trim();

       if (!validateFirstName() | !validateLastName() | !validateEmail() | !validatePhoneNo() | !validatePassword()){
           return;
       }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e("reg","account created");
                            Users user = new Users(firstName,lastName,email,phoneNo,password);
                            rootNode.getReference("Users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Log.e("reg","data created");
                                                startActivity(new Intent(signupActivity.this,MainActivity.class));
                                            }
                                            else {
                                                Log.e("reg","data not created");
                                            }
                                        }
                                    });

                        }
                        else {
                        }
                    }
                });
    }

    private Boolean validateFirstName(){
        String val = regFirstName.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regFirstName.setError("First Name is required");
            return false;
        }
        else {
            regFirstName.setError(null);
            regFirstName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateLastName(){
        String val = regLastName.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regLastName.setError("Last Name is required");
            return false;
        }
        else {
            regLastName.setError(null);
            regLastName.setErrorEnabled(false);

            return true;
        }
    }
    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regEmail.setError("Email is required");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            regEmail.setError("Please provide valid email");
            return false;
        }
        else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);

            return true;
        }
    }
    private Boolean validatePhoneNo(){
        String val = regPhoneNo.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regPhoneNo.setError("Phone No is required");
            return false;
        }
        else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);

            return true;
        }
    }
    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regPassword.setError("Password is required");
            return false;
        }
        else if (val.length()<6){
            regPassword.setError("Min password length should be 6 character!");
            return false;
        }
        else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);

            return true;
        }
    }

    private Boolean validateFirstNameO(){
        String val = regFirstName.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
    private Boolean validateLastNameO(){
        String val = regLastName.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
    private Boolean validateEmailO(){
        String val = regEmail.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            return false;
        }
        else {
            return true;
        }
    }
    private Boolean validatePhoneNoO(){
        String val = regPhoneNo.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
    private Boolean validatePasswordO(){
        String val = regPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            return false;
        }
        else if (val.length()<6){
            return false;
        }
        else {
            return true;
        }
    }

}