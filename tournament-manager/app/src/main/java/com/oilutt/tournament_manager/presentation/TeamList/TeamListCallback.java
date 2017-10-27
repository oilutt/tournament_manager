package com.oilutt.tournament_manager.presentation.TeamList;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.TeamListAdapter;

/**
 * Created by Túlio on 16/09/2017.
 */

public interface TeamListCallback extends MvpView {

    void setAdapter(TeamListAdapter adapter);

    void openActivityWithoutHist(Class<?> openActivity);

    void showLoading();

    void hideLoading();

    void showSnack(int msg);
}
