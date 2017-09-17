package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.ForgotPassword.ForgotPasswordCallback;
import com.oilutt.tournament_manager.presentation.ForgotPassword.ForgotPasswordPresenter;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oilut on 25/08/2017.
 */

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordCallback {

    @BindView(R.id.email)
    public EditText inputEmail;
    @BindView(R.id.btn_reset_password)
    public Button btnResetPassword;

    @InjectPresenter
    ForgotPasswordPresenter presenter;
    @ProvidePresenter
    ForgotPasswordPresenter createPresenter(){
        return new ForgotPasswordPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        setUpToolbarText(R.string.lbl_forgot_password, true);
    }

    @OnClick(R.id.btn_reset_password)
    public void clickReset(){
        presenter.clickReset();
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
    }
}
