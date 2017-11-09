package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.Constants;
import com.oilutt.tournament_manager.utils.AnimationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.image_splash)
    ImageView imageSplash;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        AnimationUtils.fadeInRight(imageSplash);
        auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() -> {
            finish();
            if (auth.getCurrentUser() != null) {
                openActivity(MainActivity.class);
            } else {
                openActivity(LoginActivity.class);
            }
        }, Constants.TIME_HANDLER);
    }
}
