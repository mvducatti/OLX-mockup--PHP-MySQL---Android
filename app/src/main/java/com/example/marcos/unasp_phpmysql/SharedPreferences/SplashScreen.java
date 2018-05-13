package com.example.marcos.unasp_phpmysql.SharedPreferences;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.marcos.unasp_phpmysql.LoginActivity;
import com.example.marcos.unasp_phpmysql.MainMenu;
import com.example.marcos.unasp_phpmysql.R;

public class SplashScreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 500;

    SharedPrefManager userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make activity go full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                    startActivity(new Intent(getApplicationContext(), MainMenu.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}