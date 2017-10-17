package com.oilutt.tournament_manager.presentation.CampeonatoDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.appinvite.AppInviteInvitation;
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
import com.oilutt.tournament_manager.ui.adapter.TeamsAdapter;
import com.oilutt.tournament_manager.utils.Utils;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class CampeonatoDetailPresenter extends MvpPresenter<CampeonatoDetailCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");
    private String campeonatoId;
    private Campeonato campeonato;
    private TeamsAdapter adapter;
    private Context context;

    public CampeonatoDetailPresenter(Context context){
        this.context = context;
    }

    public void setCampeonatoId(String campeonatoId) {
        getViewState().showProgress();
        this.campeonatoId = campeonatoId;
        getCampeonato();
    }

    private void getCampeonato() {
        campEndPoint.child(campeonatoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getViewState().hideProgress();
                campeonato = dataSnapshot.getValue(Campeonato.class);
                setFields();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("getCAMPS", "Failed to read value.", error.toException());
            }
        });
    }

    private void setFields() {
        getViewState().setBrasao(campeonato.getFoto());
        getViewState().setNome(campeonato.getNome());
        getViewState().setDescricao(campeonato.getDescricao());
        getViewState().setDono(campeonato.getDono().getNome());
        getViewState().setFormato(campeonato.getFormato().getNome());
        getViewState().setInicio(campeonato.getDataInicio());
        if(campeonato.getDataFim() != null)
            getViewState().setFim(campeonato.getDataFim());
        else
            getViewState().hideFim();
        getViewState().setQuantidadeTimes(String.valueOf(campeonato.getQuantidadeTimes()));
        if (campeonato.getStatus() == 3)
            getViewState().setStatus("Concluído");
        else if (campeonato.getStatus() == 2)
            getViewState().setStatus("Ocorrendo");
        else if (campeonato.getStatus() == 1)
            getViewState().setStatus("Aberto");
        if (campeonato.getFormato().getNome().equals("Liga")) {
            if(campeonato.getFormato().getIdaVolta() == 1)
                getViewState().setIdaEVolta(R.string.sim);
            else
                getViewState().setIdaEVolta(R.string.nao);
            getViewState().hidePartidasChave();
            getViewState().hidePartidasFinal();
        } else {
            getViewState().hideIdaEVolta();
            getViewState().setPartidasChave(String.valueOf(campeonato.getFormato().getQuantidadePartidasChave()));
            getViewState().setPartidasFinal(String.valueOf(campeonato.getFormato().getQuantidadePartidasFinal()));
        }
        adapter = new TeamsAdapter(campeonato.getTimes());
        getViewState().setAdapter(adapter);
    }

    public void clickFab() {
        getViewState().startCampeonato(campeonatoId);
    }

    public void clickInvite() {
        getViewState().share(campeonato.getNome(), campeonatoId);
    }
}
