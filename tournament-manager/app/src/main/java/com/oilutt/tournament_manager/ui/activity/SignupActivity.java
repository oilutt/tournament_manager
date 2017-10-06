package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Signup.SignupCallback;
import com.oilutt.tournament_manager.presentation.Signup.SignupPresenter;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oilut on 25/08/2017.
 */

public class SignupActivity extends BaseActivity implements SignupCallback {

    @BindView(R.id.email)
    public EditText inputEmail;
    @BindView(R.id.password)
    public EditText inputPassword;
    @BindView(R.id.sign_in_button)
    public Button btnSignIn;
    @BindView(R.id.sign_up_button)
    public Button btnSignUp;

    @InjectPresenter
    SignupPresenter presenter;

    @ProvidePresenter
    SignupPresenter createPresenter() {
        return new SignupPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @Override
    public void onObserverEdts() {
        RxTextView.textChanges(inputEmail)
                .subscribe(presenter::getEmail);

        RxTextView.textChanges(inputPassword)
                .subscribe(presenter::getPassword);
    }

    @OnClick({R.id.sign_in_button, R.id.sign_up_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                presenter.clickSignIn();
                break;
            case R.id.sign_up_button:
                presenter.clickSignUp();
                break;
        }
    }

    @Override
    public void showProgress() {
        showProgressDialog("");
    }

    @Override
    public void hideProgress() {
        dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgress();
    }

    @Override
    public void showSnack(int message) {
        showSnack(getString(message));
    }

    @Override
    public void openLogin(Class<?> openActivity) {
        openActivity(openActivity);
        finish();
    }

    @Override
    public void openMain(Class<?> openActivity) {
        openActivity(openActivity);
        finish();
    }
}
