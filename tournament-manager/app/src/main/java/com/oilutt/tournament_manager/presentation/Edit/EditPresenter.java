package com.oilutt.tournament_manager.presentation.Edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.BestOf;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.Partida;
import com.oilutt.tournament_manager.model.Time;
import com.oilutt.tournament_manager.ui.adapter.TabAdapter;
import com.oilutt.tournament_manager.ui.fragment.MataMataFragment;
import com.oilutt.tournament_manager.ui.fragment.RodadaFragment;

import java.util.ArrayList;
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

    public EditPresenter(Context context){
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
            adapter = new TabAdapter(context, fragmentList, ((AppCompatActivity) context).getSupportFragmentManager(), campeonato,true);
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

    public void clickSalvar(){
        List<Fragment> fragmentList = adapter.getFragmentList();
        boolean pode = true;
        if(campeonato.getFormato().getNome().equals(context.getString(R.string.liga))) {
            for (int x = 0; x < fragmentList.size(); x++) {
                if(((RodadaFragment)fragmentList.get(x)).getList() != null) {
                    campeonato.getFormato().getRodadas().get(x).setPartidas(((RodadaFragment) fragmentList.get(x)).getList());
                    updatePositions(((RodadaFragment) fragmentList.get(x)).getList(), x);
                }
            }
        } else if(campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))) {
            for (int x = 0,  y = fragmentList.size()-1; x < fragmentList.size(); x++, y--) {
                if(((MataMataFragment)fragmentList.get(x)).getList() != null) {
                    campeonato.getFormato().getFases().get(x).setPartidas(((MataMataFragment) fragmentList.get(x)).getList());
                    pode = updateFases(((MataMataFragment) fragmentList.get(x)).getList(), y);
                }
            }
        }
        if(pode)
            updateCampeonato();
    }

    private void updateCampeonato(){
        Map<String, Object> campeonatoValues = campeonato.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + mFirebaseUser.getUid() + "/campeonatos/" + campeonatoId, campeonatoValues);
        childUpdates.put("/campeonatos/" + campeonatoId, campeonatoValues);
        campEndPointUpdate.updateChildren(childUpdates);

        getViewState().startCampeonato(campeonatoId);
    }

    private void updatePositions(List<Partida> partidaList, int rodada){
        for (int x = 0; x < partidaList.size(); x++){
            Partida partida = partidaList.get(x);
            if(!partida.isJafoi()) {
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

    private void showSnack(){
        SnackbarManager.show(Snackbar.with(context)
                .type(SnackbarType.MULTI_LINE)
                .text(context.getString(R.string.algum_valor_invalido))
                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), (Activity) context);
    }

    private boolean updateFases(List<BestOf> listBestOf, int fase){
        for(int x = 0; x < listBestOf.size(); x++){
            if (listBestOf.get(x).getValorTime1() != null && listBestOf.get(x).getValorTime2() != null) {
                if (fase != 0) {
                    if (Integer.parseInt(listBestOf.get(x).getValorTime1()) + Integer.parseInt(listBestOf.get(x).getValorTime2())
                            > campeonato.getFormato().getQuantidadePartidasChave()) {
                        showSnack();
                        return false;
                    }
                    if (listBestOf.get(x).getTime1().toLowerCase().contains("vencedor partida")
                            || listBestOf.get(x).getTime2().toLowerCase().contains("vencedor partida")) {
                        SnackbarManager.show(Snackbar.with(context)
                                .type(SnackbarType.MULTI_LINE)
                                .text(context.getString(R.string.partida_inexistente))
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), (Activity) context);
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
                            || listBestOf.get(x).getTime2().toLowerCase().contains("vencedor partida")) {
                        SnackbarManager.show(Snackbar.with(context)
                                .type(SnackbarType.MULTI_LINE)
                                .text(context.getString(R.string.partida_inexistente))
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT), (Activity) context);
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
        for (int x = 0; x < listBestOf.size(); x++){
            BestOf partida = listBestOf.get(x);
            if(!partida.isJaFoi()) {
                Time time1 = new Time(), time2 = new Time();
                int y1 = 0, y2 = 0;
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
                        List<BestOf> bestOfs = new ArrayList<>();
                        if(fase > 1) {
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase-1).getPartidas());
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasChave(); y++) {
                                BestOf bestOf = new BestOf();
                                if(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1() != null
                                        && !campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1().equals("")) {
                                    bestOf.setTime1(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1());
                                    bestOf.setTime2(time1.getNome());
                                } else {
                                    bestOf.setTime1(time1.getNome());
                                }
                                bestOfs.set(x/2, bestOf);
                            }
                            campeonato.getFormato().getFases().get(fase-1).setPartidas(bestOfs);
                        } else if (fase == 1){
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase-1).getPartidas());
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasFinal(); y++) {
                                BestOf bestOf = new BestOf();
                                if(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1() != null
                                        && !campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1().equals("")) {
                                    bestOf.setTime1(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1());
                                    bestOf.setTime2(time1.getNome());
                                } else {
                                    bestOf.setTime1(time1.getNome());
                                }
                                bestOfs.set(x/2, bestOf);
                            }
                            campeonato.getFormato().getFases().get(fase-1).setPartidas(bestOfs);
                        } else {
                            campeonato.setCampeao(time1.getNome());
                            time1.setCampeao(true);
                            campeonato.setStatus(3);
                        }
                    } else {
                        List<BestOf> bestOfs = new ArrayList<>();
                        if(fase > 1) {
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase-1).getPartidas());
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasChave(); y++) {
                                BestOf bestOf = new BestOf();
                                if(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1() != null
                                        && !campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1().equals("")) {
                                    bestOf.setTime1(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1());
                                    bestOf.setTime2(time2.getNome());
                                } else {
                                    bestOf.setTime1(time2.getNome());
                                }
                                bestOfs.set(x/2, bestOf);
                            }
                            campeonato.getFormato().getFases().get(fase-1).setPartidas(bestOfs);
                        } else if (fase == 1){
                            bestOfs.addAll(campeonato.getFormato().getFases().get(fase-1).getPartidas());
                            for(int y = 0; y < campeonato.getFormato().getQuantidadePartidasFinal(); y++) {
                                BestOf bestOf = new BestOf();
                                if(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1() != null
                                        && !campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1().equals("")) {
                                    bestOf.setTime1(campeonato.getFormato().getFases().get(fase-1).getPartidas().get(x/2).getValorTime1());
                                    bestOf.setTime2(time2.getNome());
                                } else {
                                    bestOf.setTime1(time2.getNome());
                                }
                                bestOfs.set(x/2, bestOf);
                            }
                            campeonato.getFormato().getFases().get(fase-1).setPartidas(bestOfs);
                        } else {
                            campeonato.setCampeao(time2.getNome());
                            time2.setCampeao(true);
                            campeonato.setStatus(3);
                        }
                    }
                    campeonato.getFormato().getFases().get(fase).getPartidas().get(x).setJaFoi(true);
                }
                campeonato.getTimes().set(y1, time1);
                campeonato.getTimes().set(y2, time2);
            }
        }
        return true;
    }
}
