package com.example.planetb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mainNav;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




        mainNav = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();


        NavigationUI.setupWithNavController(mainNav,navController);

    }


}