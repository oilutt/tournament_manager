package com.oilutt.tournament_manager.presentation.Campeonato;

import android.view.Menu;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.TabAdapter;

/**
 * Created by Túlio on 17/09/2017.
 */

public interface CampeonatoCallback extends MvpView {

    void setUpToolbarText(String title, boolean back);

    void setAdapterTab(TabAdapter adapter);

    void showProgress();

    void hideProgress();

    void manageMenuOptions(Menu menu);
}
