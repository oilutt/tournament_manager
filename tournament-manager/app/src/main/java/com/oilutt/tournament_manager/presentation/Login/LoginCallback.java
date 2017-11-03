package com.oilutt.tournament_manager.presentation.Login;

import com.arellomobile.mvp.MvpView;

/**
 * Created by oilut on 25/08/2017.
 */

public interface LoginCallback extends MvpView {

    void showProgress();

    void hideProgress();

    void showSnack(int message);

    void openActivity(Class<?> openActivity);

    void openSignUp(String invite);

    void openMain(String invite);

    void openDetails(String invite);

    void onObserverEdts();

    void onBackPressed2();

    void hidenKeyBoard();
}
