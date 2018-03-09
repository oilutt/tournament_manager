package com.oilutt.tournament_manager.presentation.Fragments;

import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

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
public class SearchFragmentPresenter extends MvpPresenter<SearchFragmentCallback> {

    private Handler handler;
    private String buscaStr = "";
    private CampAdapter buscaAdapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference buscaEndPoint = FirebaseDatabase.getInstance().getReference("campeonatos/");
    private List<Campeonato> buscaList;
    private ValueEventListener valueEventListener;
    private TextWatcher watcher;

    public SearchFragmentPresenter(){
        getViewState().init();
    }

    public void init(Activity activity){
        buscaAdapter = new CampAdapter(activity);
        buscaAdapter.setBusca(true);
        getViewState().showBusca();
        initWatcher();
    }

    private void initWatcher() {
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler = null;
                handler = new Handler();
                handler.postDelayed(() -> {
                    buscaStr = s.toString();
                    if (!s.toString().equals("")) {
                        getViewState().showProgress();
                        if (valueEventListener != null)
                            buscaEndPoint.removeEventListener(valueEventListener);
                        valueEventListener = null;
                        makeBusca();
                    }
                }, 1500);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        getViewState().setBuscaWatcher(watcher);
    }

    public void makeBusca() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buscaList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    String key = noteSnapshot.getKey();
                    Campeonato note = noteSnapshot.getValue(Campeonato.class);
                    if (!note.getDono().getEmail().equals(mFirebaseUser.getEmail())) {
                        note.setId(key);
                        if (note.getNome().toLowerCase().contains(buscaStr.toLowerCase())) {
                            buscaList.add(note);
                        }
                    }
                }
                buscaAdapter.setData(buscaList);
                getViewState().setBuscaAdapter(buscaAdapter);
                if (buscaList.size() > 0) {
                    getViewState().hidePlaceHolderBusca();
                } else {
                    getViewState().showPlaceHolderBusca();
                }
                getViewState().hideProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        buscaEndPoint.orderByChild("privado").equalTo(0).addListenerForSingleValueEvent(valueEventListener);
    }

    public void removeListener(){
        if(valueEventListener != null)
            buscaEndPoint.removeEventListener(valueEventListener);
        buscaEndPoint = null;
    }
}
