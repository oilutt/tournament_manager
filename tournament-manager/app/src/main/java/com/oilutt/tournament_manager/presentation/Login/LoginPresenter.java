package com.oilutt.tournament_manager.presentation.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.ui.activity.ForgotPasswordActivity;
import com.oilutt.tournament_manager.utils.Utils;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oilut on 25/08/2017.
 */
@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginCallback> {

    private FirebaseAuth auth;
    private Context context;
    private String email, password, invite;
    private CallbackManager callbackManager;
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/");

    public LoginPresenter(Context context) {
        this.context = context;
        initFacebook();
        auth = FirebaseAuth.getInstance();
        getViewState().onObserverEdts();
    }

    private void initFacebook(){
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getUserFromToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.v("facebook error", exception.getMessage());
                        if (exception instanceof FacebookAuthorizationException) {
                            getViewState().showSnack(R.string.erro_generic);
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        } else {
                            Toast.makeText(context, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getUserFromToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveUser(token);
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success");
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.getException());
                getViewState().showSnack(R.string.login_fb_exists);
                LoginManager.getInstance().logOut();
            }
        });
    }

    private void saveUser(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(
                token, (object, response) -> {
                    try {
                        String email = object.getString("email");
                        String full_name = object.getString("name");
                        String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");

                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> userValues = new HashMap<>();
                        userValues.put("id", auth.getCurrentUser().getUid());
                        userValues.put("nome", full_name);
                        userValues.put("email", email);
                        userValues.put("foto", picture);
                        userValues.put("campeonatos", "");
                        childUpdates.put(auth.getCurrentUser().getUid(), userValues);
                        userEndPoint.updateChildren(childUpdates);
                        getViewState().openMain(invite);
                    } catch (JSONException e) {
                        Log.e("JSONException", e.getMessage());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void clickFb(){
        LoginManager.getInstance()
                .logInWithReadPermissions((Activity)context, Arrays.asList("public_profile", "email"));
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
            getViewState().hidenKeyBoard();
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
        if(invite != null && !invite.equals("")){
            getViewState().openDetails(invite);
        } else {
            getViewState().onBackPressed2();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
