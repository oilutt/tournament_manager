package com.oilutt.tournament_manager.presentation.Campeonato;

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
import com.oilutt.tournament_manager.ui.adapter.RodadaAdapter;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

import java.util.ArrayList;

/**
 * Created by Túlio on 17/09/2017.
 */

@InjectViewState
public class CampeonatoPresenter extends MvpPresenter<CampeonatoCallback> {

    private Campeonato campeonato;
    private String campeonatoId;
    private RodadaAdapter adapterRodada;
    private TabelaAdapter adapterTabela;
    private Context context;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference campEndPoint =  FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");

    public CampeonatoPresenter(Context context){
        this.context = context;
        getViewState().showProgress();
    }

    public void getCampeonatoId(String campeonatoId){
        this.campeonatoId = campeonatoId;
        getCampeonato();
    }

    private void getCampeonato(){
        campEndPoint.child(campeonatoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getViewState().hideProgress();
                campeonato = dataSnapshot.getValue(Campeonato.class);
                setTitle();
                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("getCAMPS", "Failed to read value.", error.toException());
            }
        });
    }

    private void setTitle(){
        getViewState().setUpToolbarText(campeonato.getNome(), true);
    }

    private void setAdapter(){
        if(campeonato.getFormato().getNome().equals(context.getString(R.string.liga))){
            adapterTabela = new TabelaAdapter(campeonato.getTimes());
            getViewState().setAdapterRecycler(adapterTabela);
            adapterRodada = new RodadaAdapter(campeonato.getrodadas(), context);
            getViewState().setAdapterViewPager(adapterRodada);
        } else if(campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))){
            getViewState().hideLayoutLiga();
            getViewState().hideViewPager();
        } else {

        }
    }
}
