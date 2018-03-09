package com.oilutt.tournament_manager.presentation.Fragments;

import android.app.Activity;

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
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Túlio on 08/03/2018.
 */
@InjectViewState
public class FollowingFragmentPresenter extends MvpPresenter<FollowingFragmentCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private List<Campeonato> campeonatoList;
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid());
    private CampAdapter adapter;
    private User user;

    public FollowingFragmentPresenter(){
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
                    getUserCamp();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserCamp() {
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
                setAdapterInvite();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapterInvite() {
        if (campeonatoList.size() > 0) {
            getViewState().hidePlaceHolderInvite();
            adapter.setData(campeonatoList);
            getViewState().setAdapter(adapter);
        } else {
            getViewState().showPlaceHolderInvite();
        }
    }

}
