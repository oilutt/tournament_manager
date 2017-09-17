package com.oilutt.tournament_manager.presentation.Campeonato;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.ui.adapter.RodadaAdapter;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

/**
 * Created by Túlio on 17/09/2017.
 */

@InjectViewState
public class CampeonatoPresenter extends MvpPresenter<CampeonatoCallback> {

    private Campeonato campeonato;
    private RodadaAdapter adapterRodada;
    private TabelaAdapter adapterTabela;
    private Context context;

    public CampeonatoPresenter(Context context){
        this.context = context;
    }

    public void getCampeonato(Campeonato campeonato){
        this.campeonato = campeonato;
        setTitle();
        setAdapter();
    }

    private void setTitle(){
        getViewState().setUpToolbarText(campeonato.getNome(), true);
    }

    private void setAdapter(){
        if(campeonato.getFormato().getNome().equals(context.getString(R.string.liga))){
            adapterTabela = new TabelaAdapter(campeonato.getTimes());
            getViewState().setAdapterRecycler(adapterTabela);
            adapterRodada = new RodadaAdapter(campeonato.getrodadas());
            getViewState().setAdapterViewPager(adapterRodada);
        } else if(campeonato.getFormato().getNome().equals(context.getString(R.string.matamata))){
            getViewState().hideLayoutLiga();
            getViewState().hideViewPager();
        } else {

        }
    }
}
