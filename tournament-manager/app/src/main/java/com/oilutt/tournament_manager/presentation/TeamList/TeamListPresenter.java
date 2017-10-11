package com.oilutt.tournament_manager.presentation.TeamList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.TournamentManagerApp;
import com.oilutt.tournament_manager.model.BestOf;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.Fase;
import com.oilutt.tournament_manager.model.Grupo;
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
            if(!listNomes.get(x).equals("")) {
                Time time = new Time(listNomes.get(x), x);
                listTimes.add(time);
            } else {
                getViewState().hideLoading();
                SnackbarManager.show(Snackbar.with(context)
                        .type(SnackbarType.MULTI_LINE)
                        .text(context.getString(R.string.time_empty))
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), (Activity) context);
                return;
            }
            for (int y = 0; y < listNomes.size(); y++){
                if(x != y) {
                    if (listNomes.get(x).equals(listNomes.get(y))) {
                        getViewState().hideLoading();
                        SnackbarManager.show(Snackbar.with(context)
                                .type(SnackbarType.MULTI_LINE)
                                .text(context.getString(R.string.nome_igual))
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), (Activity) context);
                        return;
                    }
                }
            }
        }

        if (campeonato != null) {
            campeonato.setTimes(listTimes);
            if (campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
                campeonato.getFormato().setRodadas(createSchedule(listTimes));
            } else if (campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))) {
                campeonato.getFormato().setFases(createScheduleFases(campeonato.getTimes()));
            } else {
                campeonato.getFormato().setGrupos(createScheduleTorneio(listTimes));
                campeonato.getFormato().setFases(createScheduleFasesTorneio(listTimes));
            }
            String key = campEndPoint.push().getKey();

            Map<String, Object> campeonatoValues = campeonato.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + key, campeonatoValues);
            childUpdates.put("/campeonatos/" + key, campeonatoValues);
            campEndPoint.updateChildren(childUpdates);

            getViewState().openActivityWithoutHist(MainActivity.class);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                campeonato = TournamentManagerApp.preferencesManager.getCampeonato();
                campeonato.setTimes(listTimes);
                if (campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
                    campeonato.getFormato().setRodadas(createSchedule(listTimes));
                } else if (campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))) {
                    campeonato.getFormato().setFases(createScheduleFases(campeonato.getTimes()));
                } else {
                    campeonato.getFormato().setGrupos(createScheduleTorneio(listTimes));
                    campeonato.getFormato().setFases(createScheduleFasesTorneio(listTimes));
                }
                String key = campEndPoint.push().getKey();

                Map<String, Object> campeonatoValues = campeonato.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + key, campeonatoValues);
                childUpdates.put("/campeonatos/" + key, campeonatoValues);
                campEndPoint.updateChildren(childUpdates);

                getViewState().openActivityWithoutHist(MainActivity.class);
            }, 2000);
        }
    }

    private List<Grupo> createScheduleTorneio(List<Time> list) {
        List<Time> listTime = new ArrayList<>();
        listTime.addAll(list);
        List<Grupo> result = new ArrayList<>();
        int grupos;
        if (listTime.size() == 8)
            grupos = 2;
        else if (listTime.size() == 16)
            grupos = 4;
        else
            grupos = 8;
        for (int x = 0; x < grupos; x++) {
            Grupo grupo = new Grupo();
            grupo.setNumero(x + 1);
            grupo.setRodadas(createSchedule(listTime.subList(x * 4, x * 4 + 4)));
            grupo.setTimes(listTime.subList(x * 4, x * 4 + 4));
            result.add(grupo);
        }
        return result;
    }

    private List<Fase> createScheduleFasesTorneio(List<Time> list) {
        List<Time> listTime = new ArrayList<>();
        listTime.addAll(list);
        List<Fase> result = new ArrayList<>();
        int fases = 0;
        if (listTime.size() == 8)
            fases = 2;
        else if (listTime.size() == 16)
            fases = 3;
        else if (listTime.size() == 32)
            fases = 4;
        for (int x = 0; x < fases; x++) {
            Fase fase = new Fase();
            fase.setPartidas(createBestOfTorneio(x, fases));
            fase.setNumero(x);
            result.add(fase);
        }
        return result;
    }

    private List<Fase> createScheduleFases(List<Time> list) {
        List<Time> listTime = new ArrayList<>();
        listTime.addAll(list);
        List<Fase> result = new ArrayList<>();
        int fases = 0;
        if (listTime.size() == 2)
            fases = 1;
        else if (listTime.size() > 2 && listTime.size() <= 4)
            fases = 2;
        else if (listTime.size() > 4 && listTime.size() <= 8)
            fases = 3;
        else if (listTime.size() > 8 && listTime.size() <= 16)
            fases = 4;
        else if (listTime.size() > 16 && listTime.size() <= 32)
            fases = 5;
        for (int x = 0; x < fases; x++) {
            Fase fase = new Fase();
            if (x == fases - 1) {
                fase.setPartidas(createBestOf(listTime));
            } else {
                fase.setPartidas(createBestOf(x));
            }
            fase.setNumero(x);
            result.add(fase);
        }
        return result;
    }

    private List<BestOf> createBestOfTorneio(int fase, int fases) {
        List<BestOf> result = new ArrayList<>();
        String aux = "Vencedor partida";
        if (fase == 0) {
            BestOf bestOf = new BestOf();
            bestOf.setId(0);
            bestOf.setTime1("Vencedor partida 1");
            bestOf.setTime2("Vencedor partida 2");
            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasFinal());
            List<Partida> listPartidas = new ArrayList<>();
            for (int x = 0; x < bestOf.getQuantity(); x++) {
                Partida partida = new Partida();
                partida.setId(x);
                listPartidas.add(partida);
            }
            bestOf.setPartidas(listPartidas);
            result.add(bestOf);
        } else if (fase == 1) {
            if(fase == fases -1){
                BestOf bestOf = new BestOf();
                bestOf.setId(0);
                bestOf.setTime1("1º Grupo 1");
                bestOf.setTime2("2º Grupo 2");
                bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas = new ArrayList<>();
                for (int y = 0; y < bestOf.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas.add(partida);
                }
                bestOf.setPartidas(listPartidas);
                result.add(bestOf);

                BestOf bestOf1 = new BestOf();
                bestOf1.setId(1);
                bestOf1.setTime1("1º Grupo 2");
                bestOf1.setTime2("2º Grupo 1");
                bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas1 = new ArrayList<>();
                for (int y = 0; y < bestOf1.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas1.add(partida);
                }
                bestOf1.setPartidas(listPartidas1);
                result.add(bestOf1);
            } else {
                for (int x = 0; x < 2; x++) {
                    BestOf bestOf = new BestOf();
                    bestOf.setId(x);
                    bestOf.setTime1("Vencedor partida " + (x * 2 + 1));
                    bestOf.setTime2("Vencedor partida " + (x * 2 + 2));
                    bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                    List<Partida> listPartidas = new ArrayList<>();
                    for (int y = 0; y < bestOf.getQuantity(); y++) {
                        Partida partida = new Partida();
                        partida.setId(y);
                        listPartidas.add(partida);
                    }
                    bestOf.setPartidas(listPartidas);
                    result.add(bestOf);
                }
            }
        } else if (fase == 2) {
            if (fase == fases - 1) {
                BestOf bestOf = new BestOf();
                bestOf.setId(0);
                bestOf.setTime1("1º Grupo 1");
                bestOf.setTime2("2º Grupo 2");
                bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas = new ArrayList<>();
                for (int y = 0; y < bestOf.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas.add(partida);
                }
                bestOf.setPartidas(listPartidas);
                result.add(bestOf);

                BestOf bestOf1 = new BestOf();
                bestOf1.setId(1);
                bestOf1.setTime1("1º Grupo 3");
                bestOf1.setTime2("2º Grupo 4");
                bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas1 = new ArrayList<>();
                for (int y = 0; y < bestOf1.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas1.add(partida);
                }
                bestOf1.setPartidas(listPartidas1);
                result.add(bestOf1);

                BestOf bestOf2 = new BestOf();
                bestOf2.setId(2);
                bestOf2.setTime1("1º Grupo 2");
                bestOf2.setTime2("2º Grupo 1");
                bestOf2.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas2 = new ArrayList<>();
                for (int y = 0; y < bestOf2.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas2.add(partida);
                }
                bestOf2.setPartidas(listPartidas2);
                result.add(bestOf2);

                BestOf bestOf3 = new BestOf();
                bestOf3.setId(3);
                bestOf3.setTime1("1º Grupo 4");
                bestOf3.setTime2("2º Grupo 3");
                bestOf3.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas3 = new ArrayList<>();
                for (int y = 0; y < bestOf3.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas3.add(partida);
                }
                bestOf3.setPartidas(listPartidas3);
                result.add(bestOf3);
            } else {
                for (int x = 0; x < 4; x++) {
                    BestOf bestOf = new BestOf();
                    bestOf.setId(x);
                    bestOf.setTime1("Vencedor partida " + (x * 2 + 1));
                    bestOf.setTime2("Vencedor partida " + (x * 2 + 2));
                    bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                    List<Partida> listPartidas = new ArrayList<>();
                    for (int y = 0; y < bestOf.getQuantity(); y++) {
                        Partida partida = new Partida();
                        partida.setId(y);
                        listPartidas.add(partida);
                    }
                    bestOf.setPartidas(listPartidas);
                    result.add(bestOf);
                }
            }
        } else if (fase == 3) {
            if (fase == fases - 1) {
                BestOf bestOf = new BestOf();
                bestOf.setId(0);
                bestOf.setTime1("1º Grupo 1");
                bestOf.setTime2("2º Grupo 2");
                bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas = new ArrayList<>();
                for (int y = 0; y < bestOf.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas.add(partida);
                }
                bestOf.setPartidas(listPartidas);
                result.add(bestOf);

                BestOf bestOf1 = new BestOf();
                bestOf1.setId(1);
                bestOf1.setTime1("1º Grupo 5");
                bestOf1.setTime2("2º Grupo 6");
                bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas1 = new ArrayList<>();
                for (int y = 0; y < bestOf1.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas1.add(partida);
                }
                bestOf1.setPartidas(listPartidas1);
                result.add(bestOf1);

                BestOf bestOf2 = new BestOf();
                bestOf2.setId(2);
                bestOf2.setTime1("1º Grupo 3");
                bestOf2.setTime2("2º Grupo 4");
                bestOf2.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas2 = new ArrayList<>();
                for (int y = 0; y < bestOf2.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas2.add(partida);
                }
                bestOf2.setPartidas(listPartidas2);
                result.add(bestOf2);

                BestOf bestOf3 = new BestOf();
                bestOf3.setId(3);
                bestOf3.setTime1("1º Grupo 7");
                bestOf3.setTime2("2º Grupo 8");
                bestOf3.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas3 = new ArrayList<>();
                for (int y = 0; y < bestOf3.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas3.add(partida);
                }
                bestOf3.setPartidas(listPartidas3);
                result.add(bestOf3);

                BestOf bestOf4 = new BestOf();
                bestOf4.setId(4);
                bestOf4.setTime1("1º Grupo 2");
                bestOf4.setTime2("2º Grupo 1");
                bestOf4.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas4 = new ArrayList<>();
                for (int y = 0; y < bestOf4.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas4.add(partida);
                }
                bestOf4.setPartidas(listPartidas4);
                result.add(bestOf4);

                BestOf bestOf5 = new BestOf();
                bestOf5.setId(5);
                bestOf5.setTime1("1º Grupo 6");
                bestOf5.setTime2("2º Grupo 5");
                bestOf5.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas5 = new ArrayList<>();
                for (int y = 0; y < bestOf5.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas5.add(partida);
                }
                bestOf5.setPartidas(listPartidas5);
                result.add(bestOf5);

                BestOf bestOf6 = new BestOf();
                bestOf6.setId(6);
                bestOf6.setTime1("1º Grupo 4");
                bestOf6.setTime2("2º Grupo 3");
                bestOf6.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas6 = new ArrayList<>();
                for (int y = 0; y < bestOf6.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas6.add(partida);
                }
                bestOf6.setPartidas(listPartidas6);
                result.add(bestOf6);

                BestOf bestOf7 = new BestOf();
                bestOf7.setId(7);
                bestOf7.setTime1("1º Grupo 8");
                bestOf7.setTime2("2º Grupo 7");
                bestOf7.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas7 = new ArrayList<>();
                for (int y = 0; y < bestOf7.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas7.add(partida);
                }
                bestOf7.setPartidas(listPartidas7);
                result.add(bestOf7);
            } else {
                for (int x = 0; x < 8; x++) {
                    BestOf bestOf = new BestOf();
                    bestOf.setId(x);
                    bestOf.setTime1("Vencedor partida " + (x * 2 + 1));
                    bestOf.setTime2("Vencedor partida " + (x * 2 + 2));
                    bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                    List<Partida> listPartidas = new ArrayList<>();
                    for (int y = 0; y < bestOf.getQuantity(); y++) {
                        Partida partida = new Partida();
                        partida.setId(y);
                        listPartidas.add(partida);
                    }
                    bestOf.setPartidas(listPartidas);
                    result.add(bestOf);
                }
            }
        }
        return result;
    }

    private List<BestOf> createBestOf(int fase) {
        List<BestOf> result = new ArrayList<>();
        if (fase == 0) {
            BestOf bestOf = new BestOf();
            bestOf.setId(0);
            bestOf.setTime1("Vencedor partida 1");
            bestOf.setTime2("Vencedor partida 2");
            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasFinal());
            List<Partida> listPartidas = new ArrayList<>();
            for (int x = 0; x < bestOf.getQuantity(); x++) {
                Partida partida = new Partida();
                partida.setId(x);
                listPartidas.add(partida);
            }
            bestOf.setPartidas(listPartidas);
            result.add(bestOf);
        } else if (fase == 1) {
            for (int x = 0; x < 2; x++) {
                BestOf bestOf = new BestOf();
                bestOf.setId(x);
                bestOf.setTime1("Vencedor partida " + (x * 2 + 1));
                bestOf.setTime2("Vencedor partida " + (x * 2 + 2));
                bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas = new ArrayList<>();
                for (int y = 0; y < bestOf.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas.add(partida);
                }
                bestOf.setPartidas(listPartidas);
                result.add(bestOf);
            }
        } else if (fase == 2) {
            for (int x = 0; x < 4; x++) {
                BestOf bestOf = new BestOf();
                bestOf.setId(x);
                bestOf.setTime1("Vencedor partida " + (x * 2 + 1));
                bestOf.setTime2("Vencedor partida " + (x * 2 + 2));
                bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas = new ArrayList<>();
                for (int y = 0; y < bestOf.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas.add(partida);
                }
                bestOf.setPartidas(listPartidas);
                result.add(bestOf);
            }
        } else if (fase == 3) {
            for (int x = 0; x < 8; x++) {
                BestOf bestOf = new BestOf();
                bestOf.setId(x);
                bestOf.setTime1("Vencedor partida " + (x * 2 + 1));
                bestOf.setTime2("Vencedor partida " + (x * 2 + 2));
                bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                List<Partida> listPartidas = new ArrayList<>();
                for (int y = 0; y < bestOf.getQuantity(); y++) {
                    Partida partida = new Partida();
                    partida.setId(y);
                    listPartidas.add(partida);
                }
                bestOf.setPartidas(listPartidas);
                result.add(bestOf);
            }
        }
        return result;
    }

    private List<BestOf> createBestOf(List<Time> list) {
        List<BestOf> listBest = new ArrayList<>();
        int partidas = 0;
        if (list.size() == 2)
            partidas = 1;
        else if (list.size() > 2 && list.size() <= 4)
            partidas = 2;
        else if (list.size() > 4 && list.size() <= 8)
            partidas = 4;
        else if (list.size() > 8 && list.size() <= 16)
            partidas = 8;
        else if (list.size() > 16 && list.size() <= 32)
            partidas = 16;

        for (int x = 0; x < partidas; x++) {
            BestOf best = new BestOf();
            best.setId(x);
            best.setTime1(list.size() > x ? list.get(x).getNome() : "");
            best.setTime2(list.size() > (partidas * 2 - x) - 1 ? list.get((partidas * 2 - x) - 1).getNome() : "");
            if (best.getTime1().equals("")) {
                if (!best.getTime2().equals("")) {
                    campeonato.getTimesClassificados().add(list.get((list.size() - x) - 1));
                }
            } else if (best.getTime2().equals("")) {
                if (!best.getTime1().equals("")) {
                    campeonato.getTimesClassificados().add(list.get(x));
                }
            }
            best.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
            List<Partida> listPartidas = new ArrayList<>();
            for (int y = 0; y < best.getQuantity(); y++) {
                Partida partida = new Partida();
                partida.setId(y);
                partida.setTime1(best.getTime1());
                partida.setTime2(best.getTime2());
                if (best.getTime1().equals("")) {
                    if (!best.getTime2().equals("")) {
                        partida.setValorTime1("0");
                        partida.setValorTime2("3");
                    }
                } else if (best.getTime2().equals("")) {
                    if (!best.getTime1().equals("")) {
                        partida.setValorTime1("3");
                        partida.setValorTime2("0");
                    }
                }
                listPartidas.add(partida);
            }
            best.setPartidas(listPartidas);
            if (best.getTime1().equals("")) {
                if (!best.getTime2().equals("")) {
                    best.setValorTime2(String.valueOf(best.getQuantity() - 1));
                    best.setValorTime1("0");
                }
            } else if (best.getTime2().equals("")) {
                if (!best.getTime1().equals("")) {
                    best.setValorTime1(String.valueOf(best.getQuantity() - 1));
                    best.setValorTime2("0");
                }
            }
            listBest.add(best);
        }
        return listBest;
    }

    private List<Rodada> createSchedule(List<Time> list) {
        List<Time> listTime1 = new ArrayList<>();
        List<Time> list2 = new ArrayList<>();
        listTime1.addAll(list);
        list2.addAll(list);
        if (list.size() % 2 == 1) {
            Time e = new Time("", -1);
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
        if (campeonato.getFormato().getIdaVolta() == 1) {
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
            if (!t1.getNome().equals("") && !t2.getNome().equals("")) {
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
            if (!t1.getNome().equals("") && !t2.getNome().equals("")) {
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
