package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.model.Rodada;
import com.oilutt.tournament_manager.ui.adapter.PartidaAdapter;
import com.oilutt.tournament_manager.ui.adapter.PartidaEditAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class RodadaFragmentPresenter extends MvpPresenter<RodadaFragmentCallback> {

    private PartidaAdapter adapter;
    public PartidaEditAdapter adapterEdit;
    private Rodada rodada;
    private int impar;
    private boolean edit;

    public RodadaFragmentPresenter() {
        getViewState().init();
    }

    public void setRodada(Rodada rodada) {
        this.rodada = rodada;
    }

    public void setImpar(int impar) {
        this.impar = impar;
    }

    public void setEdit(boolean edit){
        this.edit = edit;
        setAdapter();
    }

    private void setAdapter() {
        if(!edit) {
            adapter = new PartidaAdapter(rodada.getPartidas(), impar);
            getViewState().setAdapter(adapter);
        } else {
            adapterEdit = new PartidaEditAdapter(rodada.getPartidas(), impar);
            getViewState().setAdapter(adapterEdit);
        }
    }
}
