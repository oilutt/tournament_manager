package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.PartidaMatamataAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */

public interface MataMataFragmentCallback extends MvpView {

    void setAdapter(PartidaMatamataAdapter adapter);

    void init();
}
