package com.oilutt.tournament_manager.presentation.Fragments;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

/**
 * Created by Túlio on 08/03/2018.
 */

public interface FollowingFragmentCallback extends MvpView {

    void showProgress();

    void hideProgress();

    void showPlaceHolderInvite();

    void hidePlaceHolderInvite();

    void setAdapter(CampAdapter adapter);

    void init();
}
