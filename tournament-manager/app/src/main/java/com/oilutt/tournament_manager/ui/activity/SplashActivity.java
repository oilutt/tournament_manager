package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.os.Handler;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends BaseActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() -> {
            finish();
            if(auth.getCurrentUser() != null) {
                openActivity(MainActivity.class);
            }else {
                openActivity(SignupActivity.class);
            }
        }, Constants.TIME_HANDLER);
    }
}
