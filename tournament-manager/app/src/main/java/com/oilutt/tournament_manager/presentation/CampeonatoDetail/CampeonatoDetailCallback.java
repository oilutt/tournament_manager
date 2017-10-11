package com.oilutt.tournament_manager.presentation.CampeonatoDetail;

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

    void setQuantidadeTimes(String quantidade);

    void setDono(String dono);

    void setAdapter(TeamsAdapter adapter);

    void showProgress();

    void hideProgress();

    void startCampeonato(String campeonatoId);
}
