package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.model.Fase;
import com.oilutt.tournament_manager.ui.adapter.PartidaMatamataAdapter;
import com.oilutt.tournament_manager.ui.adapter.PartidaMatamataEditAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */
@InjectViewState
public class MataMataFragmentPresenter extends MvpPresenter<MataMataFragmentCallback> {

    private PartidaMatamataAdapter adapter;
    public PartidaMatamataEditAdapter adapterEdit;
    private Fase fase;
    private boolean edit;

    public MataMataFragmentPresenter() {
        getViewState().init();
    }

    public void setFase(Fase fase) {
        this.fase = fase;
        setAdapter();
    }

    public void setEdit(boolean edit){
        this.edit = edit;
    }

    private void setAdapter() {
        if(!edit) {
            adapter = new PartidaMatamataAdapter(fase.getPartidas());
            getViewState().setAdapter(adapter);
        } else {
            adapterEdit = new PartidaMatamataEditAdapter(fase.getPartidas());
            getViewState().setAdapter(adapterEdit);
        }
    }
}
