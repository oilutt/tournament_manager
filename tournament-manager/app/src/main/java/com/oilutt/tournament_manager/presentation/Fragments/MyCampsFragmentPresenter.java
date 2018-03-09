package com.oilutt.tournament_manager.presentation.Fragments;

import android.app.Activity;
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
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.User;
import com.oilutt.tournament_manager.model.UserRealm;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Túlio on 08/03/2018.
 */
@InjectViewState
public class MyCampsFragmentPresenter extends MvpPresenter<MyCampsFragmentCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private List<Campeonato> campeonatoList;
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid());
    private CampAdapter adapter;
    private User user;

    public MyCampsFragmentPresenter(){
        getViewState().init();
    }

    public void init(Activity activity){
        adapter = new CampAdapter(activity);
        adapter.setBusca(false);
        getUser();
    }

    private void getUser() {
        userEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    saveUserRealm();
                    getCamps();
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
                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("getCAMPS", "Failed to read value.", error.toException());
            }
        });
    }

    private void setAdapter() {
        if (campeonatoList.size() > 0) {
            getViewState().hidePlaceHolder();
            adapter.setData(campeonatoList);
            getViewState().setAdapter(adapter);
        } else {
            getViewState().showPlaceHolder();
        }
    }
}
