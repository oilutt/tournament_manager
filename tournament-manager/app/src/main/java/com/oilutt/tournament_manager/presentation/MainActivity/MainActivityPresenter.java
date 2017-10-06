package com.oilutt.tournament_manager.presentation.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.app.TournamentManagerApp;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by oilut on 24/08/2017.
 */
@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityCallback> {

    private CampAdapter adapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");
    private List<Campeonato> campeonatoList;
    private Activity activity;

    public MainActivityPresenter(Activity activity) {
        this.activity = activity;
        getCamps();
    }

    public void getCamps() {
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
            adapter = new CampAdapter(activity);
            adapter.setData(campeonatoList);
            getViewState().setAdapter(adapter);
        } else {
            getViewState().showPlaceHolder();
        }
    }
}
