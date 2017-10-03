package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.PartidaAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */

public interface RodadaFragmentCallback extends MvpView {

    void setAdapter(PartidaAdapter adapter);
    void init();
}
