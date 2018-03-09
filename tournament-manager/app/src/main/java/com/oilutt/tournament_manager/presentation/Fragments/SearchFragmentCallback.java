package com.oilutt.tournament_manager.presentation.Fragments;

import android.text.TextWatcher;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

/**
 * Created by Túlio on 08/03/2018.
 */

public interface SearchFragmentCallback extends MvpView {

    void showProgress();

    void hideProgress();

    void showBusca();

    void showPlaceHolderBusca();

    void hidePlaceHolderBusca();

    void setBuscaWatcher(TextWatcher watcher);

    void setBuscaAdapter(CampAdapter adapter);

    void init();
}
