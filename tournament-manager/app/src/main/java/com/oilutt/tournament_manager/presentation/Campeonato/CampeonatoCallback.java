package com.oilutt.tournament_manager.presentation.Campeonato;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.MataMataAdapter;
import com.oilutt.tournament_manager.ui.adapter.RodadaAdapter;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

/**
 * Created by Túlio on 17/09/2017.
 */

public interface CampeonatoCallback extends MvpView {

    void setUpToolbarText(String title, boolean back);

    void hideLayoutLiga();

    void setAdapterRecycler(TabelaAdapter adapter);
//    void setAdapterRecycler(MataMataAdapter adapter);

    void setAdapterViewPager(RodadaAdapter adapter);
    void setAdapterViewPager(MataMataAdapter adapter);
    void hideViewPager();
    void hideRecycler();

    void showProgress();
    void hideProgress();
}
