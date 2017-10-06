package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.model.Rodada;
import com.oilutt.tournament_manager.ui.adapter.PartidaAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class RodadaFragmentPresenter extends MvpPresenter<RodadaFragmentCallback> {

    private PartidaAdapter adapter;
    private Rodada rodada;
    private int impar;

    public RodadaFragmentPresenter() {
        getViewState().init();
    }

    public void setRodada(Rodada rodada) {
        this.rodada = rodada;
        setAdapter();
    }

    public void setImpar(int impar) {
        this.impar = impar;
    }

    private void setAdapter() {
        adapter = new PartidaAdapter(rodada.getPartidas(), impar);
        getViewState().setAdapter(adapter);
    }
}
