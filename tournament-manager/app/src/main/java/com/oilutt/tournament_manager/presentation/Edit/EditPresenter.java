package com.oilutt.tournament_manager.presentation.Edit;

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
import com.oilutt.tournament_manager.model.BestOf;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.Partida;
import com.oilutt.tournament_manager.model.Time;
import com.oilutt.tournament_manager.ui.adapter.TabAdapter;
import com.oilutt.tournament_manager.ui.fragment.MataMataFragment;
import com.oilutt.tournament_manager.ui.fragment.RodadaFragment;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tulio on 09/10/2017.
 */

@InjectViewState
public class EditPresenter extends MvpPresenter<EditCallback> {

    private Campeonato campeonato;
    public String campeonatoId;
    private Context context;
    private TabAdapter adapter;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private DatabaseReference campEndPoint = FirebaseDatabase.getInstance().getReference("users/" + mFirebaseUser.getUid() + "/campeonatos");
    private DatabaseReference campEndPointUpdate = FirebaseDatabase.getInstance().getReference();

    public EditPresenter(Context context) {
        this.context = context;
    }

    public void getCampeonatoId(String campeonatoId) {
        this.campeonatoId = campeonatoId;
        getCampeonato();
    }

    private void getCampeonato() {
        campEndPoint.child(campeonatoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getViewState().hideProgress();
                campeonato = dataSnapshot.getValue(Campeonato.class);
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
        Bundle extras = new Bundle();
        extras.putParcelable("campeonato", campeonato);
        if (campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
            List<Fragment> fragmentList = new ArrayList<>();
            for (int x = 0; x < campeonato.getFormato().getRodadas().size(); x++) {
                Bundle extras2 = new Bundle();
                extras2.putParcelable("rodada", campeonato.getFormato().getRodadas().get(x));
                extras2.putInt("impar", x % 2);
                extras2.putBoolean("edit", true);
                RodadaFragment rodadaFragment = RodadaFragment.newInstance();
                rodadaFragment.setArguments(extras2);
                fragmentList.add(rodadaFragment);
            }
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity) context).getSupportFragmentManager(), campeonato, true);
            getViewState().setAdapterTab(adapter);
        } else if (campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))) {
            List<Fragment> fragmentList = new ArrayList<>();
            for (int x = campeonato.getFormato().getFases().size() - 1; x >= 0; x--) {
                Bundle extras2 = new Bundle();
                extras2.putParcelable("fase", campeonato.getFormato().getFases().get(x));
                extras2.putBoolean("edit", true);
                MataMataFragment fragment = MataMataFragment.newInstance();
                fragment.setArguments(extras2);
                fragmentList.add(fragment);
            }
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity) context).getSupportFragmentManager(), campeonato, true);
            getViewState().setAdapterTab(adapter);
        } else {
            List<Fragment> fragmentList = new ArrayList<>();
            for (int x = 0; x < campeonato.getFormato().getGrupos().size(); x++) {
                for (int y = 0; y < campeonato.getFormato().getGrupos().get(x).getRodadas().size(); y++) {
                    Bundle extras2 = new Bundle();
                    extras2.putParcelable("rodada", campeonato.getFormato().getGrupos().get(x).getRodadas().get(y));
                    extras2.putInt("impar", y % 2);
                    extras2.putBoolean("edit", true);
                    RodadaFragment rodadaFragment = RodadaFragment.newInstance();
                    rodadaFragment.setArguments(extras2);
                    fragmentList.add(rodadaFragment);
                }
            }
            for (int z = campeonato.getFormato().getFases().size() - 1; z >= 0; z--) {
                Bundle extras2 = new Bundle();
                extras2.putParcelable("fase", campeonato.getFormato().getFases().get(z));
                extras2.putBoolean("edit", true);
                MataMataFragment fragment = MataMataFragment.newInstance();
                fragment.setArguments(extras2);
                fragmentList.add(fragment);
            }
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity) context).getSupportFragmentManager(), campeonato, true);
            getViewState().setAdapterTab(adapter);
        }
    }

    public void clickSalvar() {
        List<Fragment> fragmentList = adapter.getFragmentList();
        boolean pode = true;
        if (campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
            for (int x = 0; x < fragmentList.size(); x++) {
                if (((RodadaFragment) fragmentList.get(x)).getList() != null) {
                    campeonato.getFormato().getRodadas().get(x).setPartidas(((RodadaFragment) fragmentList.get(x)).getList());
                    updatePositions(((RodadaFragment) fragmentList.get(x)).getList(), x);
                }
            }
        } else if (campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))) {
            for (int x = 0, y = fragmentList.size() - 1; x < fragmentList.size(); x++, y--) {
                if (((MataMataFragment) fragmentList.get(x)).getList() != null) {
                    pode = updateFases(((MataMataFragment) fragmentList.get(x)).getList(), y);
                }
            }
        } else if (campeonato.getFormato().getNome().equals(context.getString(R.string.torneio))){
            for (int x = 0, y = fragmentList.size(); x < fragmentList.size(); x++) {
                if(fragmentList.get(x) instanceof RodadaFragment) {
                    if (((RodadaFragment) fragmentList.get(x)).getList() != null) {
                        campeonato.getFormato().getGrupos().get(x / 3).getRodadas().get(x % 3).setPartidas(((RodadaFragment) fragmentList.get(x)).getList());
                        updatePositionsTorneio(((RodadaFragment) fragmentList.get(x)).getList(), x % 3, x / 3, y - 3*campeonato.getFormato().getGrupos().size() -1);
                    }
                } else if(fragmentList.get(x) instanceof MataMataFragment){
                    if (((MataMataFragment) fragmentList.get(x)).getList() != null) {
                        pode = updateFases(((MataMataFragment) fragmentList.get(x)).getList(), y - 3*campeonato.getFormato().getGrupos().size() -1);
                        y--;
                    }
                }
            }
        }
        if (pode)
            updateCampeonato();
    }

    private void updateCampeonato() {
        Map<String, Object> campeonatoValues = campeonato.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + campeonatoId, campeonatoValues);
        childUpdates.put("/campeonatos/" + campeonatoId, campeonatoValues);
        campEndPointUpdate.updateChildren(childUpdates);

        getViewState().startCampeonato(campeonatoId);
    }

    private void updatePositionsTorneio(List<Partida> partidaList, int rodada, int grupo, int fase) {
        for (int x = 0; x < partidaList.size(); x++) {
            Partida partida = partidaList.get(x);
            if (!partida.isJafoi()) {
                int y1 = 0, y2 = 0;
                Time time1 = new Time(), time2 = new Time();
                for (int y = 0; y < campeonato.getFormato().getGrupos().get(grupo).getTimes().size(); y++) {
                    if (partida.getTime1().equals(campeonato.getFormato().getGrupos().get(grupo).getTimes().get(y).getNome())) {
                        time1 = campeonato.getTimes().get(y);
                        y1 = y;
                    } else if (partida.getTime2().equals(campeonato.getFormato().getGrupos().get(grupo).getTimes().get(y).getNome())) {
                        time2 = campeonato.getTimes().get(y);
                        y2 = y;
                    }
                }
                if (partida.getValorTime1() != null && partida.getValorTime2() != null) {
                    if (Integer.parseInt(partida.getValorTime1()) > Integer.parseInt(partida.getValorTime2())) {
                        time1.setPontos(time1.getPontos() + 3);
                        time1.setVitorias(time1.getVitorias() + 1);
                        time2.setDerrotas(time2.getDerrotas() + 1);
                    } else if (Integer.parseInt(partida.getValorTime1()) == Integer.parseInt(partida.getValorTime2())) {
                        time1.setPontos(time1.getPontos() + 1);
                        time2.setPontos(time2.getPontos() + 1);
                        time1.setEmpates(time1.getEmpates() + 1);
                        time2.setEmpates(time2.getEmpates() + 1);
                    } else {
                        time2.setPontos(time2.getPontos() + 3);
                        time2.setVitorias(time2.getVitorias() + 1);
                        time1.setDerrotas(time1.getDerrotas() + 1);
                    }
                    time1.setJogos(time1.getJogos() + 1);
                    time2.setJogos(time2.getJogos() + 1);
                    time1.setGolsFeitos(time1.getGolsFeitos() + Integer.parseInt(partida.getValorTime1()));
                    time2.setGolsFeitos(time2.getGolsFeitos() + Integer.parseInt(partida.getValorTime2()));
                    time1.setGolsSofridos(time1.getGolsSofridos() + Integer.parseInt(partida.getValorTime2()));
                    time2.setGolsSofridos(time2.getGolsSofridos() + Integer.parseInt(partida.getValorTime1()));
                    campeonato.getFormato().getGrupos().get(grupo).getRodadas().get(rodada).getPartidas().get(x).setJafoi(true);
                }
                campeonato.getTimes().set(y1, time1);
                campeonato.getTimes().set(y2, time2);
                campeonato.getFormato().getGrupos().get(grupo).getTimes().set(y1, time1);
                campeonato.getFormato().getGrupos().get(grupo).getTimes().set(y2, time2);
                boolean acabou = true;
                for(int z = 0; z < campeonato.getFormato().getGrupos().get(grupo).getRodadas().size() && acabou; z++){
                    for (int c = 0; c < campeonato.getFormato().getGrupos().get(grupo).getRodadas().get(z).getPartidas().size() && acabou; c++) {
                        if (!campeonato.getFormato().getGrupos().get(grupo).getRodadas().get(z).getPartidas().get(c).isJafoi()){
                            acabou = false;
                        }
                    }
                }
                if(acabou) {
                    List<Time> listAux = new ArrayList<>();
                    listAux.addAll(campeonato.getFormato().getGrupos().get(grupo).getTimes());
                    Collections.sort(listAux, (o1, o2) -> {
                        if(o1.getPontos() < o2.getPontos()){
                            return 1;
                        } else if (o1.getPontos() > o2.getPontos()){
                            return -1;
                        } else {
                            if(o1.getVitorias() < o2.getVitorias()){
                                return 1;
                            } else if (o1.getVitorias() > o2.getVitorias()){
                                return -1;
                            } else {
                                if ((o1.getGolsFeitos() - o1.getGolsSofridos()) < (o2.getGolsFeitos() - o2.getGolsSofridos())){
                                    return 1;
                                } else if ((o1.getGolsFeitos() - o1.getGolsSofridos()) > (o2.getGolsFeitos() - o2.getGolsSofridos())){
                                    return -1;
                                }
                                else{
                                    return o1.getNome().compareTo(o2.getNome());
                                }
                            }
                        }
                    });
                    if(campeonato.getFormato().getFases().size() == 4){
                        if(grupo == 1){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(0);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(0).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(4);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(4).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(0, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(4, bestOf1);

                        } else if(grupo == 2){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(0);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(0).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(4);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(4).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(0, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(4, bestOf1);

                        } else if(grupo == 3){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(2);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(2).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(6);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(6).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(2, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(6, bestOf1);

                        } else if(grupo == 4){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(2);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(2).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(6);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(6).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(2, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(6, bestOf1);

                        } else if(grupo == 5){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(1);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(1).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(5);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(5).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(1, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(5, bestOf1);

                        } else if(grupo == 6){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(1);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(1).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(5);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(5).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(1, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(5, bestOf1);

                        } else if(grupo == 7){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(3);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(3).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(7);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(7).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(3, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(7, bestOf1);

                        } else if(grupo == 8){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(3);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(3).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(7);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(7).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(3, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(7, bestOf1);

                        }

                    } else if(campeonato.getFormato().getFases().size() == 3){
                        if(grupo == 1){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(0);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(0).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(2);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(2).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(0, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(2, bestOf1);

                        } else if(grupo == 2){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(0);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(0).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(2);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(2).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(0, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(2, bestOf1);

                        } else if(grupo == 3){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(1);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(1).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(3);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(3).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(1, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(3, bestOf1);

                        } else if(grupo == 4){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(1);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(1).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(3);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(3).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(1, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(3, bestOf1);

                        }

                    } else if(campeonato.getFormato().getFases().size() == 2){
                        if(grupo == 1){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(0);
                            bestOf.setTime1(listAux.get(0).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(0).getTime2());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(1);
                            bestOf1.setTime2(listAux.get(1).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(1).getTime1());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(0, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(1, bestOf1);

                        } else if(grupo == 2){
                            BestOf bestOf = new BestOf();
                            bestOf.setId(0);
                            bestOf.setTime2(listAux.get(1).getNome());
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf.setTime1(campeonato.getFormato().getFases().get(fase).getPartidas().get(0).getTime1());
                            List<Partida> partidas = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partida1.setId(j);
                                partidas.add(partida1);
                            }
                            bestOf.setPartidas(partidas);

                            BestOf bestOf1 = new BestOf();
                            bestOf1.setId(1);
                            bestOf1.setTime1(listAux.get(0).getNome());
                            bestOf1.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOf1.setTime2(campeonato.getFormato().getFases().get(fase).getPartidas().get(1).getTime2());
                            List<Partida> partidas1 = new ArrayList<>();
                            for(int j = 0; j < campeonato.getFormato().getQuantidadePartidasChave(); j++){
                                Partida partida1 = new Partida();
                                partida1.setTime1(bestOf1.getTime1());
                                partida1.setTime2(bestOf1.getTime2());
                                partida1.setId(j);
                                partidas1.add(partida1);
                            }
                            bestOf1.setPartidas(partidas1);

                            campeonato.getFormato().getFases().get(fase).getPartidas().set(0, bestOf);
                            campeonato.getFormato().getFases().get(fase).getPartidas().set(1, bestOf1);

                        }
                    }
                }
            }
        }
    }

    private void updatePositions(List<Partida> partidaList, int rodada) {
        for (int x = 0; x < partidaList.size(); x++) {
            Partida partida = partidaList.get(x);
            if (!partida.isJafoi()) {
                int y1 = 0, y2 = 0;
                Time time1 = new Time(), time2 = new Time();
                for (int y = 0; y < campeonato.getTimes().size(); y++) {
                    if (partida.getTime1().equals(campeonato.getTimes().get(y).getNome())) {
                        time1 = campeonato.getTimes().get(y);
                        y1 = y;
                    } else if (partida.getTime2().equals(campeonato.getTimes().get(y).getNome())) {
                        time2 = campeonato.getTimes().get(y);
                        y2 = y;
                    }
                }
                if (partida.getValorTime1() != null && partida.getValorTime2() != null) {
                    if (Integer.parseInt(partida.getValorTime1()) > Integer.parseInt(partida.getValorTime2())) {
                        time1.setPontos(time1.getPontos() + 3);
                        time1.setVitorias(time1.getVitorias() + 1);
                        time2.setDerrotas(time2.getDerrotas() + 1);
                    } else if (Integer.parseInt(partida.getValorTime1()) == Integer.parseInt(partida.getValorTime2())) {
                        time1.setPontos(time1.getPontos() + 1);
                        time2.setPontos(time2.getPontos() + 1);
                        time1.setEmpates(time1.getEmpates() + 1);
                        time2.setEmpates(time2.getEmpates() + 1);
                    } else {
                        time2.setPontos(time2.getPontos() + 3);
                        time2.setVitorias(time2.getVitorias() + 1);
                        time1.setDerrotas(time1.getDerrotas() + 1);
                    }
                    time1.setJogos(time1.getJogos() + 1);
                    time2.setJogos(time2.getJogos() + 1);
                    time1.setGolsFeitos(time1.getGolsFeitos() + Integer.parseInt(partida.getValorTime1()));
                    time2.setGolsFeitos(time2.getGolsFeitos() + Integer.parseInt(partida.getValorTime2()));
                    time1.setGolsSofridos(time1.getGolsSofridos() + Integer.parseInt(partida.getValorTime2()));
                    time2.setGolsSofridos(time2.getGolsSofridos() + Integer.parseInt(partida.getValorTime1()));
                    campeonato.getFormato().getRodadas().get(rodada).getPartidas().get(x).setJafoi(true);
                }
                campeonato.getTimes().set(y1, time1);
                campeonato.getTimes().set(y2, time2);
            }
        }
    }

    private void showSnack() {
        getViewState().showSnack(R.string.algum_valor_invalido);
    }

    private boolean updateFases(List<BestOf> listBestOf, int fase) {
        for (int x = 0; x < listBestOf.size(); x++) {
            if (listBestOf.get(x).getValorTime1() != null && listBestOf.get(x).getValorTime2() != null) {
                if (fase != 0) {
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) + Integer.parseInt(listBestOf.get(x).getValorTime2())
                            > campeonato.getFormato().getQuantidadePartidasChave()) {
                        showSnack();
                        return false;
                    }
                    if (listBestOf.get(x).getTime1().toLowerCase().contains("vencedor partida")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("vencedor partida") ||
                            listBestOf.get(x).getTime1().toLowerCase().contains("1º grupo")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("1º grupo") ||
                    listBestOf.get(x).getTime1().toLowerCase().contains("2º grupo")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("2º grupo")) {
                        getViewState().showSnack(R.string.partida_inexistente);
                        return false;
                    }
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) == Integer.parseInt(listBestOf.get(x).getValorTime2())) {
                        showSnack();
                        return false;
                    }
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) < (int) (campeonato.getFormato().getQuantidadePartidasChave() / 2)
                            && Integer.parseInt(listBestOf.get(x).getValorTime2()) < (int) (campeonato.getFormato().getQuantidadePartidasChave() / 2)) {
                        showSnack();
                        return false;
                    }
                } else {
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) + Integer.parseInt(listBestOf.get(x).getValorTime2())
                            > campeonato.getFormato().getQuantidadePartidasFinal()) {
                        showSnack();
                        return false;
                    }
                    if (listBestOf.get(x).getTime1().toLowerCase().contains("vencedor partida")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("vencedor partida") ||
                            listBestOf.get(x).getTime1().toLowerCase().contains("1º grupo")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("1º grupo") ||
                            listBestOf.get(x).getTime1().toLowerCase().contains("2º grupo")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("2º grupo")) {
                        getViewState().showSnack(R.string.partida_inexistente);
                        return false;
                    }
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) == Integer.parseInt(listBestOf.get(x).getValorTime2())) {
                        showSnack();
                        return false;
                    }
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) < (int) (campeonato.getFormato().getQuantidadePartidasFinal() / 2)
                            && Integer.parseInt(listBestOf.get(x).getValorTime2()) < (int) (campeonato.getFormato().getQuantidadePartidasFinal() / 2)) {
                        showSnack();
                        return false;
                    }
                }
            }
        }
        for (int x = 0; x < listBestOf.size(); x++) {
            BestOf partida = listBestOf.get(x);
            if (!partida.isJaFoi()) {
                Time time1 = new Time(), time2 = new Time();
                for (int y = 0; y < campeonato.getTimes().size(); y++) {
                    if (partida.getTime1().equals(campeonato.getTimes().get(y).getNome())) {
                        time1 = campeonato.getTimes().get(y);
                    } else if (partida.getTime2().equals(campeonato.getTimes().get(y).getNome())) {
                        time2 = campeonato.getTimes().get(y);
                    }
                }
                if (partida.getValorTime1() != null && partida.getValorTime2() != null) {
                    if (Integer.parseInt(partida.getValorTime1()) > Integer.parseInt(partida.getValorTime2())) {
                        List<BestOf> bestOfs = new ArrayList<>();
                        if (fase > 1) {
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase - 1).getPartidas());
                            BestOf bestOf = new BestOf();
                            if (!campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1().toLowerCase().contains("vencedor partida")) {
                                bestOf.setTime1(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1());
                                bestOf.setTime2(time1.getNome());
                            } else {
                                bestOf.setTime1(time1.getNome());
                                bestOf.setTime2(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime2());
                            }
                            List<Partida> partidaList = new ArrayList<>();
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasChave(); y++) {
                                Partida partida1 = new Partida();
                                partida1.setId(y);
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partidaList.add(partida1);
                            }
                            bestOf.setPartidas(partidaList);
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOfs.set(x / 2, bestOf);
                            campeonato.getFormato().getFases().get(fase - 1).setPartidas(bestOfs);
                        } else if (fase == 1) {
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase - 1).getPartidas());
                            BestOf bestOf = new BestOf();
                            if (!campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1().toLowerCase().contains("vencedor partida")) {
                                bestOf.setTime1(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1());
                                bestOf.setTime2(time1.getNome());
                            } else {
                                bestOf.setTime1(time1.getNome());
                                bestOf.setTime2(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime2());
                            }
                            List<Partida> partidaList = new ArrayList<>();
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasFinal(); y++) {
                                Partida partida1 = new Partida();
                                partida1.setId(y);
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partidaList.add(partida1);
                            }
                            bestOf.setPartidas(partidaList);
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasFinal());
                            bestOfs.set(x / 2, bestOf);
                            campeonato.getFormato().getFases().get(fase - 1).setPartidas(bestOfs);
                        } else {
                            campeonato.setCampeao(time1.getNome());
                            time1.setCampeao(true);
                            campeonato.setStatus(3);
                            campeonato.setDataFim(Utils.getDateFromTimeStamp(Utils.getTimeStamp()));
                        }
                    } else {
                        List<BestOf> bestOfs = new ArrayList<>();
                        if (fase > 1) {
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase - 1).getPartidas());
                            BestOf bestOf = new BestOf();
                            if (!campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1().toLowerCase().contains("vencedor partida")) {
                                bestOf.setTime1(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1());
                                bestOf.setTime2(time2.getNome());
                            } else {
                                bestOf.setTime1(time2.getNome());
                                bestOf.setTime2(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime2());
                            }
                            List<Partida> partidaList = new ArrayList<>();
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasChave(); y++) {
                                Partida partida1 = new Partida();
                                partida1.setId(y);
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partidaList.add(partida1);
                            }
                            bestOf.setPartidas(partidaList);
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasChave());
                            bestOfs.set(x / 2, bestOf);
                            campeonato.getFormato().getFases().get(fase - 1).setPartidas(bestOfs);
                        } else if (fase == 1) {
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase - 1).getPartidas());
                            BestOf bestOf = new BestOf();
                            if (!campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1().toLowerCase().contains("vencedor partida")) {
                                bestOf.setTime1(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime1());
                                bestOf.setTime2(time2.getNome());
                            } else {
                                bestOf.setTime1(time2.getNome());
                                bestOf.setTime2(campeonato.getFormato().getFases().get(fase - 1).getPartidas().get(x / 2).getTime2());
                            }
                            List<Partida> partidaList = new ArrayList<>();
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasFinal(); y++) {
                                Partida partida1 = new Partida();
                                partida1.setId(y);
                                partida1.setTime1(bestOf.getTime1());
                                partida1.setTime2(bestOf.getTime2());
                                partidaList.add(partida1);
                            }
                            bestOf.setPartidas(partidaList);
                            bestOf.setQuantity(campeonato.getFormato().getQuantidadePartidasFinal());
                            bestOfs.set(x / 2, bestOf);
                            campeonato.getFormato().getFases().get(fase - 1).setPartidas(bestOfs);
                        } else {
                            campeonato.setCampeao(time2.getNome());
                            time2.setCampeao(true);
                            campeonato.setStatus(3);
                            campeonato.setDataFim(Utils.getDateFromTimeStamp(Utils.getTimeStamp()));
                        }
                    }
                    campeonato.getFormato().getFases().get(fase).getPartidas().get(x).setJaFoi(true);
                    campeonato.getFormato().getFases().get(fase).getPartidas().set(x, partida);
                }
            }
        }
        return true;
    }
}
