package com.oilutt.tournament_manager.presentation.Sorteio;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Tulio on 01/11/2017.
 */

public interface SorteioCallback extends MvpView {

    void setNomeTime1(String nome);

    void setNomeTime2(String nome);

    void setLogoTime2(int drawable);

    void setLogoTime1(int drawable);
}
