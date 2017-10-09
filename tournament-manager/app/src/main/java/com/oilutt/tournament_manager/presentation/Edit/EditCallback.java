package com.oilutt.tournament_manager.presentation.Edit;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.TabAdapter;

/**
 * Created by Tulio on 09/10/2017.
 */

public interface EditCallback extends MvpView {

    void showProgress();

    void hideProgress();

    void setAdapterTab(TabAdapter adapterTab);

    void onBackPressed();
}
