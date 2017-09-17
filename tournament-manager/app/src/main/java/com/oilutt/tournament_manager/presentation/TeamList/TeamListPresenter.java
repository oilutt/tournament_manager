package com.oilutt.tournament_manager.presentation.TeamList;

import android.content.Context;
import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.app.TournamentManagerApp;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.Time;
import com.oilutt.tournament_manager.ui.activity.MainActivity;
import com.oilutt.tournament_manager.ui.adapter.TeamListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Túlio on 16/09/2017.
 */
@InjectViewState
public class TeamListPresenter extends MvpPresenter<TeamListCallback> {

    private TeamListAdapter adapter;
    private List<String> teams;
    private Context context;
    private Campeonato campeonato;
    private int quantiadeTimes;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference();

    public TeamListPresenter(Context context){
        this.context = context;
    }

    public void clickSave(){
        campeonato = TournamentManagerApp.preferencesManager.getCampeonato();
        List<String> listNomes = adapter.getData();
        List<Time> listTimes = new ArrayList<>();
        for (int x = 0; x < listNomes.size(); x++) {
            Time time = new Time(listNomes.get(x), x, x);
            listTimes.add(time);
        }
        if(campeonato != null) {
            campeonato.setTimes(listTimes);

            String key = campEndPoint.push().getKey();

            Map<String, Object> campeonatoValues = campeonato.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + key, campeonatoValues);
            childUpdates.put("/campeonatos/" + mFirebaseUser.getUid() + "/" + key, campeonatoValues);
            campEndPoint.updateChildren(childUpdates);

            getViewState().openActivityWithoutHist(MainActivity.class);
        } else {
            getViewState().showLoading();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                campeonato = TournamentManagerApp.preferencesManager.getCampeonato();
                campeonato.setTimes(listTimes);

                String key = campEndPoint.push().getKey();

                Map<String, Object> campeonatoValues = campeonato.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + key, campeonatoValues);
                childUpdates.put("/campeonatos/" + mFirebaseUser.getUid() + "/" + key, campeonatoValues);
                campEndPoint.updateChildren(childUpdates);

                getViewState().hideLoading();
                getViewState().openActivityWithoutHist(MainActivity.class);
            }, 2000);
        }
    }

    public void setCampeonato(Campeonato campeonato){
        this.campeonato = campeonato;
    }

    public void setQuantidade(int quantidade){
        quantiadeTimes = quantidade;
    }

    public void setList(List<String> teams){
        this.teams = teams;
        setAdapter();
    }

    private void setAdapter(){
        adapter = new TeamListAdapter(teams, quantiadeTimes, context);
        getViewState().setAdapter(adapter);
    }
}
