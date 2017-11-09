package com.oilutt.tournament_manager.presentation.Signup;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.ui.activity.LoginActivity;
import com.oilutt.tournament_manager.ui.activity.MainActivity;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oilut on 25/08/2017.
 */
@InjectViewState
public class SignupPresenter extends MvpPresenter<SignupCallback> {

    private FirebaseAuth auth;
    private String email, password, nome, invite;
    private Context context;
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/");

    public SignupPresenter(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        getViewState().onObserverEdts();
    }

    public void getEmail(CharSequence email) {
        this.email = email.toString();
    }

    public void getInvite(String invite){
        this.invite = invite;
    }

    public void getPassword(CharSequence password) {
        this.password = password.toString();
    }

    public void getNome(CharSequence nome) {
        this.nome = nome.toString();
    }

    public void clickSignIn() {
        getViewState().openLogin(LoginActivity.class, invite);
    }

    public void clickSignUp() {
        if (verifyInputs()) {
            getViewState().showProgress();
            getViewState().hidenKeyBoard();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, task -> {
                        getViewState().hideProgress();
                        if (!task.isSuccessful()) {
                            getViewState().showSnack(R.string.erro_generic);
                        } else {
                            Map<String, Object> childUpdates = new HashMap<>();
                            Map<String, Object> userValues = new HashMap<>();
                            userValues.put("id", auth.getCurrentUser().getUid());
                            userValues.put("nome", nome);
                            userValues.put("email", email);
                            userValues.put("foto", "");
                            userValues.put("campeonatos", "");
                            childUpdates.put(auth.getCurrentUser().getUid(), userValues);
                            userEndPoint.updateChildren(childUpdates);
                            getViewState().openMain(MainActivity.class, invite);
                        }
                    }).addOnFailureListener((Activity) context, e -> getViewState().hideProgress());
        }
    }

    private boolean verifyInputs() {
        boolean status = true;
        if (TextUtils.isEmpty(nome)) {
            getViewState().showSnack(R.string.erro_nome_user);
            status = false;
        }

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
