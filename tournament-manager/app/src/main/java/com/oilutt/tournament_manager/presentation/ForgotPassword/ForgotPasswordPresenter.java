package com.oilutt.tournament_manager.presentation.ForgotPassword;

import android.content.Context;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by oilut on 25/08/2017.
 */
@InjectViewState
public class ForgotPasswordPresenter extends MvpPresenter<ForgotPasswordCallback> {

    private Context context;
    private String email;
    private FirebaseAuth auth;

    public ForgotPasswordPresenter(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        getViewState().onObserverEdts();
    }

    public void getEmail(CharSequence email) {
        this.email = email.toString();
    }

    public void clickReset() {
        if (verifyInput()) {
            getViewState().showProgress();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getViewState().showSnack(R.string.success);
                        } else {
                            getViewState().showSnack(R.string.erro_generic);
                        }
                        getViewState().hideProgress();
                    });
        }
    }

    private boolean verifyInput() {
        boolean status = true;
        if (TextUtils.isEmpty(email)) {
            getViewState().showSnack(R.string.erro_email);
            status = false;
        } else if (!Utils.isValidEmail(email)) {
            getViewState().showSnack(R.string.invalid_email);
            status = false;
        }

        return status;
    }
}
