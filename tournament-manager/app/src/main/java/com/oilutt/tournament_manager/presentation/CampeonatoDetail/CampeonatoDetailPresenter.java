package com.oilutt.tournament_manager.presentation.CampeonatoDetail;

import android.content.Context;
import android.util.Log;
import android.view.View;

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
import com.oilutt.tournament_manager.ui.activity.LoginActivity;
import com.oilutt.tournament_manager.ui.adapter.TeamsAdapter;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class CampeonatoDetailPresenter extends MvpPresenter<CampeonatoDetailCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference userEndPoint = FirebaseDatabase.getInstance().getReference("users/");
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference();
    private String campeonatoId;
    private Campeonato campeonato;
    private TeamsAdapter adapter;
    private Context context;
    private boolean invite;

    public CampeonatoDetailPresenter(Context context) {
        this.context = context;
    }

    public void setCampeonatoId(String campeonatoId, boolean invite) {
        this.invite = invite;
        getViewState().showProgress();
        this.campeonatoId = campeonatoId;
        getCampeonato();
    }

    public void showSnack(){
        snackSaveChamp(R.string.saveChamp);
    }

    private void showButton() {
        if (mFirebaseUser != null && mFirebaseUser.getUid().equals(campeonato.getDono().getId())) {
            getViewState().showButton();
        }
        if (invite) {
            snackSaveChamp(R.string.invite_camp);
        }
    }

    private void snackSaveChamp(int msg){
        if (mFirebaseUser != null) {
            if (!mFirebaseUser.getUid().equals(campeonato.getDono().getId())) {
                View.OnClickListener clickListener = v -> saveChamp();
                getViewState().showSnack(Utils.formatString(context.getString(msg), campeonato.getNome()),
                        R.string.sim, clickListener);
            }
        } else {
            View.OnClickListener clickListener = v -> saveChampWithoutLogin();
            getViewState().showSnack(Utils.formatString(context.getString(msg), campeonato.getNome()),
                    R.string.sim, clickListener);
        }
    }

    private void saveChamp() {
        userEndPoint.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> userValues = new HashMap<>();
                userValues.put(campeonatoId, campeonato);
                childUpdates.put("inviteChamps", userValues);
                userEndPoint.child(mFirebaseUser.getUid()).updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveChampWithoutLogin(){
        getViewState().openLogin(campeonatoId);
    }

    private void getCampeonato() {
        campEndPoint.child("campeonatos/" + campeonatoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getViewState().hideProgress();
                campeonato = dataSnapshot.getValue(Campeonato.class);
                setFields();
                showButton();
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
        if (campeonato.getDataFim() != null)
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
            if (campeonato.getFormato().getIdaVolta() == 1)
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
        getViewState().share(campeonato.getNome(), "http://tournamentmanager.com/invite/" + campeonatoId);
    }

    public void onBackPressed() {
        if (mFirebaseUser == null) {
            getViewState().openActivityWithoutHist(LoginActivity.class);
            getViewState().showSnack(R.string.text_no_login);
        } else {
            getViewState().onBackPressed2();
        }
    }
}
