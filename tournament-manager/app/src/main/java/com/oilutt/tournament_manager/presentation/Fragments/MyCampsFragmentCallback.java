package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

/**
 * Created by Túlio on 08/03/2018.
 */

public interface MyCampsFragmentCallback extends MvpView {

    void showProgress();

    void hideProgress();

    void showPlaceHolder();

    void hidePlaceHolder();

    void setAdapter(CampAdapter adapter);

    void init();
}
