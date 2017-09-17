package com.oilutt.tournament_manager.presentation.MainActivity;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

/**
 * Created by oilut on 24/08/2017.
 */

public interface MainActivityCallback extends MvpView {

    void showPlaceHolder();
    void hidePlaceHolder();

    void setAdapter(CampAdapter adapter);
}
