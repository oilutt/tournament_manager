package com.oilutt.tournament_manager.presentation.TeamList;

import android.content.Context;
import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.TournamentManagerApp;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.Partida;
import com.oilutt.tournament_manager.model.Rodada;
import com.oilutt.tournament_manager.model.Time;
import com.oilutt.tournament_manager.ui.activity.MainActivity;
import com.oilutt.tournament_manager.ui.adapter.TeamListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private int contadorPartida = 0, contadorRodada = 0;

    public TeamListPresenter(Context context) {
        this.context = context;
    }

    public void clickSave() {
        getViewState().showLoading();
        campeonato = TournamentManagerApp.preferencesManager.getCampeonato();

        List<String> listNomes = adapter.getData();
        List<Time> listTimes = new ArrayList<>();
        for (int x = 0; x < listNomes.size(); x++) {
            Time time = new Time(listNomes.get(x), x, x);
            listTimes.add(time);
        }

        if (campeonato != null) {
            campeonato.setTimes(listTimes);
            if(campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
                campeonato.getFormato().setRodadas(createSchedule(listTimes));

                String key = campEndPoint.push().getKey();

                Map<String, Object> campeonatoValues = campeonato.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + key, campeonatoValues);
                childUpdates.put("/campeonatos/" + key, campeonatoValues);
                campEndPoint.updateChildren(childUpdates);

                getViewState().openActivityWithoutHist(MainActivity.class);
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                campeonato = TournamentManagerApp.preferencesManager.getCampeonato();
                campeonato.setTimes(listTimes);
                if(campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
                    campeonato.getFormato().setRodadas(createSchedule(listTimes));

                    String key = campEndPoint.push().getKey();

                    Map<String, Object> campeonatoValues = campeonato.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + key, campeonatoValues);
                    childUpdates.put("/campeonatos/" + key, campeonatoValues);
                    campEndPoint.updateChildren(childUpdates);

                    getViewState().hideLoading();
                    getViewState().openActivityWithoutHist(MainActivity.class);
                }
            }, 2000);
        }
    }

    private List<Rodada> createSchedule(List<Time> list) {
        List<Time> listTime1 = new ArrayList<>();
        List<Time> list2 = new ArrayList<>();
        listTime1.addAll(list);
        list2.addAll(list);
        if (list.size() % 2 == 1) {
            Time e = new Time("", -1, -1);
            list2.add(e);
            listTime1.add(e);
        }

        List<Rodada> listRodada = new ArrayList<>();
        for (int i = 1; i < listTime1.size(); i++) {
            listRodada.add(createOneRound(i, listTime1));
            // Move last item to first
            listTime1.add(1, listTime1.get(listTime1.size() - 1));
            listTime1.remove(listTime1.size() - 1);
        }
        if(campeonato.getFormato().getIdaVolta() == 1){
            for (int i = 1; i < list2.size(); i++) {
                listRodada.add(createOneRoundVolta(i, list2));
                // Move last item to first
                list2.add(1, list2.get(list2.size() - 1));
                list2.remove(list2.size() - 1);
            }
        }
        return listRodada;
    }

    private Rodada createOneRound(int round, List<Time> teams) {
        int mid = teams.size() / 2;
        Rodada rodada = new Rodada();
        rodada.setNumero(++contadorRodada);
        // Split list into two
        ArrayList<Time> l1 = new ArrayList<>();
        // Can't use sublist (can't cast it to ArrayList - how stupid is that)??
        for (int j = 0; j < mid; j++) {
            l1.add(teams.get(j));
        }
        ArrayList<Time> l2 = new ArrayList<>();
        // We need to reverse the other list
        for (int j = teams.size() - 1; j >= mid; j--) {
            l2.add(teams.get(j));
        }

        List<Partida> listPartidas = new ArrayList<>();
        for (int tId = 0; tId < l1.size(); tId++) {
            Time t1;
            Time t2;
            // Switch sides after each round
            if (round % 2 == 1) {
                t1 = l1.get(tId);
                t2 = l2.get(tId);
            } else {
                t1 = l2.get(tId);
                t2 = l1.get(tId);
            }
            if(!t1.getNome().equals("") && !t2.getNome().equals("")) {
                Partida partida = new Partida();
                partida.setTime1(t1.getNome());
                partida.setTime2(t2.getNome());
                partida.setId(++contadorPartida);
                listPartidas.add(partida);
            }
        }
        rodada.setPartidas(listPartidas);
        return rodada;
    }

    private Rodada createOneRoundVolta(int round, List<Time> teams) {
        int mid = teams.size() / 2;
        Rodada rodada = new Rodada();
        rodada.setNumero(++contadorRodada);
        // Split list into two
        ArrayList<Time> l1 = new ArrayList<>();
        // Can't use sublist (can't cast it to ArrayList - how stupid is that)??
        for (int j = 0; j < mid; j++) {
            l1.add(teams.get(j));
        }
        ArrayList<Time> l2 = new ArrayList<>();
        // We need to reverse the other list
        for (int j = teams.size() - 1; j >= mid; j--) {
            l2.add(teams.get(j));
        }

        List<Partida> listPartidas = new ArrayList<>();
        for (int tId = 0; tId < l1.size(); tId++) {
            Time t1;
            Time t2;
            // Switch sides after each round
            if (round % 2 == 1) {
                t1 = l2.get(tId);
                t2 = l1.get(tId);
            } else {
                t1 = l1.get(tId);
                t2 = l2.get(tId);
            }
            if(!t1.getNome().equals("") && !t2.getNome().equals("")) {
                Partida partida = new Partida();
                partida.setTime1(t1.getNome());
                partida.setTime2(t2.getNome());
                partida.setId(++contadorPartida);
                listPartidas.add(partida);
            }
        }
        rodada.setPartidas(listPartidas);
        return rodada;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public void setQuantidade(int quantidade) {
        quantiadeTimes = quantidade;
    }

    public void setList(List<String> teams) {
        this.teams = teams;
        setAdapter();
    }

    private void setAdapter() {
        adapter = new TeamListAdapter(teams, quantiadeTimes, context);
        getViewState().setAdapter(adapter);
    }
}
