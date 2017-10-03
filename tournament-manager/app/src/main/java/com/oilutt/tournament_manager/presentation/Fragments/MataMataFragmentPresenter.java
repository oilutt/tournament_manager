package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.model.Fase;
import com.oilutt.tournament_manager.ui.adapter.PartidaMatamataAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class MataMataFragmentPresenter extends MvpPresenter<MataMataFragmentCallback> {

    private PartidaMatamataAdapter adapter;
    private Fase fase;

    public MataMataFragmentPresenter(){
        getViewState().init();
    }

    public void setFase(Fase fase){
        this.fase = fase;
        setAdapter();
    }

    private void setAdapter(){
        adapter = new PartidaMatamataAdapter(fase.getPartidas());
        getViewState().setAdapter(adapter);
    }
}
