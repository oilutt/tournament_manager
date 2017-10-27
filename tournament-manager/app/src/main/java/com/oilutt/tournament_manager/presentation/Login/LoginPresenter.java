package com.oilutt.tournament_manager.presentation.Login;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.ui.activity.ForgotPasswordActivity;
import com.oilutt.tournament_manager.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by oilut on 25/08/2017.
 */
@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginCallback> {

    private FirebaseAuth auth;
    private Context context;
    private String email, password, invite;

    public LoginPresenter(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        getViewState().onObserverEdts();
    }

    public void getEmail(CharSequence email) {
        this.email = email.toString();
    }

    public void getPassword(CharSequence password) {
        this.password = password.toString();
    }

    public void getInvite(String invite){
        this.invite = invite;
    }

    public void clickLogin() {
        if (verifyInputs()) {
            getViewState().showProgress();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, task -> {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        getViewState().hideProgress();
                        if (!task.isSuccessful()) {
                            // there was an error
                            getViewState().showSnack(R.string.erro_login);
                        } else {
                            getViewState().openMain(invite);
                        }
                    }).addOnFailureListener((Activity) context, e -> getViewState().hideProgress());
        }
    }

    public void clickSignUp() {
        getViewState().openSignUp(invite);
    }

    public void clickReset() {
        getViewState().openActivity(ForgotPasswordActivity.class);
    }

    private boolean verifyInputs() {
        boolean status = true;
        if (TextUtils.isEmpty(email)) {
            getViewState().showSnack(R.string.erro_email);
            status = false;
        } else if (!Utils.isValidEmail(email)) {
            getViewState().showSnack(R.string.invalid_email);
            status = false;
        }

        if (TextUtils.isEmpty(password)) {
            getViewState().showSnack(R.string.erro_senha);
            status = false;
        } else if (password.length() < 6) {
            getViewState().showSnack(R.string.erro_min_senha);
            status = false;
        }
        return status;
    }

    public void onBackPressed(){
        if(invite.equals("")){
            getViewState().onBackPressed2();
        } else {
            getViewState().openDetails(invite);
        }
    }
}
