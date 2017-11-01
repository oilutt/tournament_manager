package com.oilutt.tournament_manager.presentation.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.oilutt.tournament_manager.app.Constants;
import com.oilutt.tournament_manager.app.TournamentManagerApp;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.User;
import com.oilutt.tournament_manager.model.UserRealm;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oilutt.tournament_manager.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by oilut on 24/08/2017.
 */
@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityCallback> {

    private CampAdapter adapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid());
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");
    private List<Campeonato> campeonatoList;
    private Activity activity;
    private User user;
    private boolean meusCamps = false;
    private String pathImage, imageBase64;

    public MainActivityPresenter(Activity activity) {
        this.activity = activity;
        getUser();
        getCamps();
    }

    public void getInvite(String invite){
        getViewState().openDetails(invite);
    }

    private void getCamps() {
        campEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                campeonatoList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    String key = noteSnapshot.getKey();
                    Campeonato note = noteSnapshot.getValue(Campeonato.class);
                    note.setId(key);
                    campeonatoList.add(note);
                }
                meusCamps = true;
                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("getCAMPS", "Failed to read value.", error.toException());
            }
        });
    }

    private void getUser(){
        userEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                saveUserRealm();
                setFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserRealm(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new UserRealm(user));
        realm.commitTransaction();
    }

    private void setAdapter() {
        if (campeonatoList.size() > 0) {
            getViewState().hidePlaceHolder();
            adapter = new CampAdapter(activity);
            adapter.setData(campeonatoList);
            getViewState().setAdapter(adapter);
        } else {
            getViewState().showPlaceHolder();
        }
    }

    private void setFields(){
        getViewState().setFoto(user.getFoto() != null && !user.getFoto().equals("") ? user.getFoto() : "");
        getViewState().setEmail(user.getEmail());
        getViewState().setNome(user.getNome());
    }

    public void logout(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(UserRealm.class);
        realm.commitTransaction();
        mFirebaseAuth.signOut();
        getViewState().openLogin();
    }

    public void clickHeader(){
        Utils.showDialogCameraGallery(activity, "Alterar foto de perfil");
    }

    public void clickMeusCamps(){
        if(!meusCamps){
            getCamps();
        }
    }

    public void sorteio(){
        getViewState().openSorteio();
    }

    public void clickInviteCamp(){
        if(meusCamps){
            getUserCamp();
        }
    }

    private void getUserCamp(){
        userEndPoint.child("inviteChamps").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                campeonatoList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    String key = noteSnapshot.getKey();
                    Campeonato note = noteSnapshot.getValue(Campeonato.class);
                    note.setId(key);
                    campeonatoList.add(note);
                }
                meusCamps = false;
                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == Constants.CODIGO) {
            if (resultCode == Activity.RESULT_OK) {
                clickInviteCamp();
            }
        }
        else if ((requestCode == Constants.REQUEST_CAMERA || requestCode == Constants.PICK_PHOTO_CODE) && resultCode == AppCompatActivity.RESULT_OK) {
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

    private void updateUser(){
        user.setFoto(imageBase64);
        Map<String, Object> userUpdates = user.toMap2();
        userEndPoint.updateChildren(userUpdates);
    }
}
