package com.oilutt.tournament_manager.presentation.Signup;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.ui.activity.LoginActivity;
import com.oilutt.tournament_manager.ui.activity.MainActivity;
import com.oilutt.tournament_manager.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by oilut on 25/08/2017.
 */
@InjectViewState
public class SignupPresenter extends MvpPresenter<SignupCallback> {

    private FirebaseAuth auth;
    private String email, password;
    private Context context;

    public SignupPresenter(Context context) {
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

    public void clickSignIn() {
        getViewState().openLogin(LoginActivity.class);
    }

    public void clickSignUp() {
        if (verifyInputs()) {
            getViewState().showProgress();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, task -> {
                        getViewState().hideProgress();
                        if (!task.isSuccessful()) {
                            getViewState().showSnack(R.string.erro_generic);
                        } else {
                            getViewState().openMain(MainActivity.class);
                        }
                    }).addOnFailureListener((Activity) context, e -> getViewState().hideProgress());
        }
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
}
