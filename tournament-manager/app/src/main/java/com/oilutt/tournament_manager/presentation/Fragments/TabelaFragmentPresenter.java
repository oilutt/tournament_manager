package com.oilutt.tournament_manager.presentation.Fragments;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class TabelaFragmentPresenter extends MvpPresenter<TabelaFragmentCallback> {

    private Campeonato campeonato;
    private TabelaAdapter adapter;
    private int grupo;
    private Context context;

    public TabelaFragmentPresenter(Context context) {
        this.context = context;
        getViewState().init();
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
        setAdapter();
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    private void setAdapter() {
        if (campeonato.getFormato().getNome().equals("Liga") || campeonato.getFormato().getNome().equals("League")) {
            adapter = new TabelaAdapter(campeonato.getTimes());
            getViewState().setAdapter(adapter);
        } else if (campeonato.getFormato().getNome().equals("Torneio") || campeonato.getFormato().getNome().equals("Torneo") || campeonato.getFormato().getNome().equals("Tournament")) {
            adapter = new TabelaAdapter(campeonato.getFormato().getGrupos().get(grupo).getTimes());
            getViewState().setAdapter(adapter);
        }
    }
}
