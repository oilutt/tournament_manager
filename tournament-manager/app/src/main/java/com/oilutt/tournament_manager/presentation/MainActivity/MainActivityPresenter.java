package com.oilutt.tournament_manager.presentation.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oilutt.tournament_manager.app.Constants;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.User;
import com.oilutt.tournament_manager.model.UserRealm;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;
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

    private CampAdapter adapter;
    private CampAdapter buscaAdapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference buscaEndPoint = FirebaseDatabase.getInstance().getReference("campeonatos/");
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid());
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");
    private List<Campeonato> campeonatoList;
    private List<Campeonato> buscaList;
    private List<Campeonato> buscaFiltered;
    private TextWatcher watcher;
    private Activity activity;
    private User user;
    private boolean meusCamps = false;
    private boolean busca = false;
    private String pathImage, imageBase64;
    private Handler handler;

    public MainActivityPresenter(Activity activity) {
        this.activity = activity;
        getUser();
        getCamps();
        initWatcher();
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
            adapter = new CampAdapter(activity, false);
            adapter.setData(campeonatoList);
            getViewState().setAdapter(adapter);
        } else {
            getViewState().showPlaceHolder();
        }
    }

    private void setAdapterInvite() {
        if (campeonatoList.size() > 0) {
            getViewState().hidePlaceHolderInvite();
            adapter = new CampAdapter(activity, false);
            adapter.setData(campeonatoList);
            getViewState().setAdapter(adapter);
        } else {
            getViewState().showPlaceHolderInvite();
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
        } else if(busca){
            getCamps();
        }
        busca = false;
    }

    public void sorteio(){
        getViewState().openSorteio();
    }

    public void clickBuscaCamp(){
        if(!busca){
            busca = true;
            getViewState().setBuscaWatcher(watcher);
            getViewState().showBusca();
            makeBusca();
        }
    }

    public void clickInviteCamp(){
        if(meusCamps){
            getUserCamp();
        } else if(busca){
            getUserCamp();
        }
        busca = false;
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
                setAdapterInvite();
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

    private void initWatcher(){
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler = new Handler();
                handler.postDelayed(() -> searchCamps(s.toString()), 800);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void searchCamps(String s){
        buscaFiltered = new ArrayList<>();
        for (Campeonato camp: buscaList) {
            if(camp.getNome().toLowerCase().startsWith(s.toLowerCase()))
                buscaFiltered.add(camp);
        }
        if(buscaFiltered.size() > 0){
            getViewState().hidePlaceHolderBusca();
        } else {
            getViewState().showPlaceHolderBusca();
        }
        buscaAdapter.setData(buscaFiltered);
        getViewState().setBuscaAdapter(buscaAdapter);
    }

    private void makeBusca(){
        buscaEndPoint.orderByChild("privado").equalTo(0).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buscaList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    String key = noteSnapshot.getKey();
                    Campeonato note = noteSnapshot.getValue(Campeonato.class);
                    if(!note.getDono().getEmail().equals(mFirebaseUser.getEmail())) {
                        note.setId(key);
                        buscaList.add(note);
                    }
                }
                buscaAdapter = new CampAdapter(activity, true);
                buscaAdapter.setData(buscaList);
                new Handler().postDelayed(() -> {
                    getViewState().setBuscaAdapter(buscaAdapter);
                    if(buscaList.size() > 0) {
                        getViewState().hidePlaceHolderBusca();
                    }
                }, 1500);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
