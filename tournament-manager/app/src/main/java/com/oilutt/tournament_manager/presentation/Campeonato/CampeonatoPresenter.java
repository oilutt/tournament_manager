package com.oilutt.tournament_manager.presentation.Campeonato;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.oilutt.tournament_manager.ui.adapter.TabAdapter;
import com.oilutt.tournament_manager.ui.fragment.MataMataFragment;
import com.oilutt.tournament_manager.ui.fragment.RodadaFragment;
import com.oilutt.tournament_manager.ui.fragment.TabelaFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Túlio on 17/09/2017.
 */

@InjectViewState
public class CampeonatoPresenter extends MvpPresenter<CampeonatoCallback> {

    private Campeonato campeonato;
    private String campeonatoId;
    private Context context;
    private TabAdapter adapter;
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
        Bundle extras = new Bundle();
        extras.putParcelable("campeonato", campeonato);
        if(campeonato.getFormato().getNome().equals(context.getString(R.string.liga))){
            List<Fragment> fragmentList = new ArrayList<>();
            TabelaFragment fragment = TabelaFragment.newInstance();
            fragment.setArguments(extras);
            fragmentList.add(fragment);
            for(int x = 0; x < campeonato.getFormato().getRodadas().size(); x++) {
                Bundle extras2 = new Bundle();
                extras2.putParcelable("rodada", campeonato.getFormato().getRodadas().get(x));
                extras2.putInt("impar", x % 2);
                RodadaFragment rodadaFragment = RodadaFragment.newInstance();
                rodadaFragment.setArguments(extras2);
                fragmentList.add(rodadaFragment);
            }
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity)context).getSupportFragmentManager(),campeonato);
            getViewState().setAdapterTab(adapter);
        } else if(campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))){
            List<Fragment> fragmentList = new ArrayList<>();
            for(int x = campeonato.getFormato().getFases().size() - 1; x >= 0 ; x--) {
                Bundle extras2 = new Bundle();
                extras2.putParcelable("fase", campeonato.getFormato().getFases().get(x));
                MataMataFragment fragment = MataMataFragment.newInstance();
                fragment.setArguments(extras2);
                fragmentList.add(fragment);
            }
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity)context).getSupportFragmentManager(), campeonato);
            getViewState().setAdapterTab(adapter);
        } else {
            List<Fragment> fragmentList = new ArrayList<>();
            for (int x = 0; x < campeonato.getFormato().getGrupos().size(); x++){
                TabelaFragment fragment = TabelaFragment.newInstance();
                extras.putInt("grupo", x);
                fragment.setArguments(extras);
                fragmentList.add(fragment);
                for(int y = 0; y < campeonato.getFormato().getGrupos().get(x).getRodadas().size(); y++) {
                    Bundle extras2 = new Bundle();
                    extras2.putParcelable("rodada", campeonato.getFormato().getGrupos().get(x).getRodadas().get(y));
                    extras2.putInt("impar", y % 2);
                    RodadaFragment rodadaFragment = RodadaFragment.newInstance();
                    rodadaFragment.setArguments(extras2);
                    fragmentList.add(rodadaFragment);
                }
            }
            for(int z = campeonato.getFormato().getFases().size() - 1; z >= 0 ; z--) {
                Bundle extras2 = new Bundle();
                extras2.putParcelable("fase", campeonato.getFormato().getFases().get(z));
                MataMataFragment fragment = MataMataFragment.newInstance();
                fragment.setArguments(extras2);
                fragmentList.add(fragment);
            }
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity)context).getSupportFragmentManager(), campeonato);
            getViewState().setAdapterTab(adapter);
        }
    }
}
