package com.oilutt.tournament_manager.presentation.CampeonatoDetail;

import android.view.View;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.TeamsAdapter;

/**
 * Created by Tulio on 03/10/2017.
 */

public interface CampeonatoDetailCallback extends MvpView {

    void setBrasao(String image);

    void setNome(String nome);

    void setDescricao(String descricao);

    void setFormato(String formato);

    void hidePartidasChave();

    void hidePartidasFinal();

    void setPartidasChave(String partidasChave);

    void setPartidasFinal(String partidasFinal);

    void hideIdaEVolta();

    void setIdaEVolta(int idaEVolta);

    void setStatus(String status);

    void setInicio(String inicio);

    void setFim(String fim);

    void hideFim();

    void setQuantidadeTimes(String quantidade);

    void setDono(String dono);

    void setAdapter(TeamsAdapter adapter);

    void showProgress();

    void hideProgress();

    void startCampeonato(String campeonatoId);

    void share(String nome, String codigo);

    void showButton();

    void openActivityWithoutHist(Class<?> openActivity);

    void onBackPressed2();

    void showSnack(int msg);

    void showSnack(String msg, int button, View.OnClickListener clickListener);

    void openLogin(String campeonato);
}
