package com.oilutt.tournament_manager.presentation.Codigo;

import android.content.Context;
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
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.User;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tulio on 17/10/2017.
 */
@InjectViewState
public class CodigoPresenter extends MvpPresenter<CodigoCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/");
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("campeonatos/");
    private String codigo;
    private Campeonato campeonato;
    private User user;

    public void getCodigo(CharSequence codigo){
        this.codigo = codigo.toString();
    }

    public void clickInserir(){
        getViewState().showProgress();
        getUser();
        getCamp();
    }

    private void getCamp(){
        campEndPoint.child(codigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getViewState().hideProgress();
                campeonato = dataSnapshot.getValue(Campeonato.class);
                addCamp();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("getCAMPS", "Failed to read value.", error.toException());
            }
        });
    }

    private void getUser(){
        userEndPoint.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                getCamp();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addCamp(){
        if(campeonato != null){
            Map<String, Object> childUpdates = new HashMap<>();
            user.getInviteChamps().add(campeonato);
            Map<String, Object> userUpdates = user.toMap2();
            childUpdates.put(user.getId(), userUpdates);
            userEndPoint.child(mFirebaseUser.getUid()).updateChildren(childUpdates);
            getViewState().showSnack(R.string.codigo_sucesso);
            getViewState().finishIntent();
        } else {
            getViewState().showSnack(R.string.erro_camp);
        }
    }
}
