package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */

public interface TabelaFragmentCallback extends MvpView {

    void setAdapter(TabelaAdapter adapter);
    void init();
}
