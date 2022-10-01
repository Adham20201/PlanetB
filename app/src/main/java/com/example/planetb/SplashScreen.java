package com.example.planetb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.planetb.account.loginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    ImageView img_logo, txt_logo;
    Animation topAnim,bottomAnim;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        img_logo = findViewById(R.id.imgLogo);
        txt_logo = findViewById(R.id.txtLogo);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        //Set animation to elements
        img_logo.setAnimation(topAnim);
        txt_logo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, loginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0]=new Pair<View, String>(img_logo,"logo_image");
                pairs[1]=new Pair<View, String>(txt_logo,"logo_text");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                if (mAuth.getCurrentUser() != null){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                }
                else {
                    startActivity(intent, options.toBundle());

                }

                finish();
            }
        },SPLASH_SCREEN);

    }
}