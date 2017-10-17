package com.oilutt.tournament_manager.presentation.Codigo;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Tulio on 17/10/2017.
 */

public interface CodigoCallback extends MvpView {

    void onObserverEdts();

    void showProgress();

    void hideProgress();

    void showSnack(int msg);

    void onBackPressed();
}
