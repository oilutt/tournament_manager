package com.oilutt.tournament_manager.presentation.Campeonato;

import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Túlio on 17/09/2017.
 */

public interface CampeonatoCallback extends MvpView {

    void setUpToolbarText(String title, boolean back);

    void setAdapterTab(FragmentPagerAdapter adapter);

    void showProgress();

    void hideProgress();

    void manageMenuOptions(Menu menu);
}
