package com.oilutt.tournament_manager.presentation.ForgotPassword;

import com.arellomobile.mvp.MvpView;

/**
 * Created by oilut on 25/08/2017.
 */

public interface ForgotPasswordCallback extends MvpView {

    void showProgress();

    void hideProgress();

    void showSnack(int message);

    void onObserverEdts();
}
