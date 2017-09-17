package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Login.LoginCallback;
import com.oilutt.tournament_manager.presentation.Login.LoginPresenter;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oilut on 25/08/2017.
 */

public class LoginActivity extends BaseActivity implements LoginCallback {

    @BindView(R.id.email)
    public EditText inputEmail;
    @BindView(R.id.password)
    public EditText inputPassword;
    @BindView(R.id.btn_login)
    public Button btnLogin;
    @BindView(R.id.btn_signup)
    public Button btnSignUp;
    @BindView(R.id.btn_reset_password)
    public Button btnResetPassword;

    @InjectPresenter
    LoginPresenter presenter;
    @ProvidePresenter
    LoginPresenter createPresenter(){
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_signup, R.id.btn_reset_password})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                presenter.clickLogin();
                break;
            case R.id.sign_up_button:
                presenter.clickSignUp();
                break;
            case R.id.btn_reset_password:
                presenter.clickReset();
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
    public void onObserverEdts() {
        RxTextView.textChanges(inputEmail)
                .subscribe(presenter::getEmail);

        RxTextView.textChanges(inputPassword)
                .subscribe(presenter::getPassword);
    }

    @Override
    public void openSignUp() {
        openActivity(SignupActivity.class);
        finish();
    }

    @Override
    public void openMain() {
        openActivity(MainActivity.class);
        finish();
    }
}
