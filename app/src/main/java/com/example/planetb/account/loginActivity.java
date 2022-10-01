package com.example.planetb.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planetb.MainActivity;
import com.example.planetb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    TextView titleTxt,subTitleTxt;
    TextInputLayout logEmail, logPassword;
    ImageView googleOP, facebookOP, appleOP;

    Button goSignUp,login;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(loginActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = loginActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(loginActivity.this, R.color.gray_background));

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        goSignUp = findViewById(R.id.goSignUp);
        login = findViewById(R.id.signIn);
        titleTxt = findViewById(R.id.logo_name);
        subTitleTxt = findViewById(R.id.slogan_name);
        logEmail = findViewById(R.id.username);
        logPassword = findViewById(R.id.password);
        googleOP = findViewById(R.id.google_option);
        facebookOP = findViewById(R.id.facebook_option);
        appleOP = findViewById(R.id.apple_option);

        goSignUp.setOnClickListener(this);
        login.setOnClickListener(this);

        logEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validateEmailO()){
                    logEmail.setError(null);
                    logEmail.setErrorEnabled(false);
                }
            }
        });
        logPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (validatePasswordO()){
                    logPassword.setError(null);
                    logPassword.setErrorEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goSignUp:

                Pair[] pairs = new Pair[9];
                pairs[0]=new Pair<View, String>(titleTxt,"logo_text");
                pairs[1]=new Pair<View, String>(subTitleTxt,"slogan_txt");
                pairs[2]=new Pair<View, String>(logEmail,"mail_box");
                pairs[3]=new Pair<View, String>(logPassword,"password_box");
                pairs[4]=new Pair<View, String>(login,"go_button");
                pairs[5]=new Pair<View, String>(goSignUp,"change_button");
                pairs[6]=new Pair<View, String>(googleOP,"google_option");
                pairs[7]=new Pair<View, String>(facebookOP,"facebook_option");
                pairs[8]=new Pair<View, String>(appleOP,"apple_option");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(loginActivity.this,pairs);

                startActivity(new Intent(loginActivity.this, signupActivity.class),options.toBundle());
                break;
            case R.id.signIn:
               userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = logEmail.getEditText().getText().toString().trim();
        String password = logPassword.getEditText().getText().toString().trim();

        if (!validateEmail() | !validatePassword()){
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(loginActivity.this,MainActivity.class));
                }
            }
        });

    }

    private Boolean validateEmail(){
        String val = logEmail.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            logEmail.setError("Email is required");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            logEmail.setError("Please provide valid email");
            return false;
        }
        else {
            logEmail.setError(null);
            logEmail.setErrorEnabled(false);

            return true;
        }
    }
    private Boolean validatePassword(){
        String val = logPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            logPassword.setError("Password is required");
            return false;
        }
        else if (val.length()<6){
            logPassword.setError("Min password length should be 6 character!");
            return false;
        }
        else {
            logPassword.setError(null);
            logPassword.setErrorEnabled(false);

            return true;
        }
    }

    private Boolean validateEmailO(){
        String val = logEmail.getEditText().getText().toString().trim();
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
    private Boolean validatePasswordO(){
        String val = logPassword.getEditText().getText().toString().trim();
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