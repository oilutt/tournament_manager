package com.oilutt.tournament_manager.presentation.MainActivity;

import android.app.Activity;
import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.Constants;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.User;
import com.oilutt.tournament_manager.model.UserRealm;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;
import com.oilutt.tournament_manager.ui.fragment.FollowingFragment;
import com.oilutt.tournament_manager.ui.fragment.MyCampsFragment;
import com.oilutt.tournament_manager.ui.fragment.SearchFragment;
import com.oilutt.tournament_manager.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by oilut on 24/08/2017.
 */
@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid());
    private User user;
    private Activity activity;
    private String pathImage, imageBase64;

    public MainActivityPresenter(Activity activity) {
        this.activity = activity;
        getUser();
    }

    public void getInvite(String invite) {
        getViewState().openDetails(invite);
    }

    private void getUser() {
        userEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    saveUserRealm();
                    setFields();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserRealm() {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new UserRealm(user));
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFields() {
        getViewState().setFoto(user.getFoto() != null && !user.getFoto().equals("") ? user.getFoto() : "");
        getViewState().setEmail(user.getEmail());
        getViewState().setNome(user.getNome());
    }

    public void logout() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(UserRealm.class);
        realm.commitTransaction();
        mFirebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        getViewState().openLogin();
    }

    public void clickHeader() {
        Utils.showDialogCameraGallery(activity, activity.getString(R.string.change_profile));
    }

    public void clickMeusCamps() {
        getViewState().replaceFragment(MyCampsFragment.newInstance());
    }

    public void clickInviteCamp() {
        getViewState().replaceFragment(FollowingFragment.newInstance());
    }

    public void clickBuscaCamp() {
        getViewState().replaceFragment(SearchFragment.newInstance());
    }

    public void sorteio() {
        getViewState().openSorteio();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == Constants.REQUEST_CAMERA || requestCode == Constants.PICK_PHOTO_CODE) && resultCode == AppCompatActivity.RESULT_OK) {
            getViewState().launchCrop(data.getData());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null) {
                pathImage = result.getUri().getPath();
                getViewState().setFotoPath(pathImage);
                Bitmap bm = BitmapFactory.decodeFile(pathImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                imageBase64 = Base64.encodeToString(b, Base64.DEFAULT);
                updateUser();
            }
        }
    }

    private void updateUser() {
        user.setFoto(imageBase64);
        Map<String, Object> userUpdates = user.toMap2();
        userEndPoint.updateChildren(userUpdates);
    }
}
