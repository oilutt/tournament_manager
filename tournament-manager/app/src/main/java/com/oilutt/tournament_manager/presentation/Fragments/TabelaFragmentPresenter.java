package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class TabelaFragmentPresenter extends MvpPresenter<TabelaFragmentCallback> {

    private Campeonato campeonato;
    private TabelaAdapter adapter;

    public TabelaFragmentPresenter(){
        getViewState().init();
    }

    public void setCampeonato(Campeonato campeonato){
        this.campeonato = campeonato;
        setAdapter();
    }

    private void setAdapter(){
        adapter = new TabelaAdapter(campeonato.getTimes());
        getViewState().setAdapter(adapter);
    }
}
