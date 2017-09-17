package com.oilutt.tournament_manager.presentation.AddCamp;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.arellomobile.mvp.MvpView;

/**
 * Created by oilut on 25/08/2017.
 */

public interface AddCampCallback extends MvpView {

    void showMatamata();
    void hideMatamata();
    void showLiga();
    void hideLiga();
    void hideTextFoto();
    void setFoto(String path);

    void setFormatoAdapter(ArrayAdapter adapter);
    void setQuantidadeTeamAdapter(ArrayAdapter adapter);
    void setQuantidadePartidasChaveAdapter(ArrayAdapter adapter);
    void setQuantidadePartidasFinalAdapter(ArrayAdapter adapter);
    void setIdaEVoltaAdapter(ArrayAdapter adapter);
    void setFormatoListener(AdapterView.OnItemSelectedListener listener);
    void setQuantidadeTeamListener(AdapterView.OnItemSelectedListener listener);
    void setQuantidadePartidasChaveListener(AdapterView.OnItemSelectedListener listener);
    void setQuantidadePartidasFinalListener(AdapterView.OnItemSelectedListener listener);
    void setIdaEVoltaListener(AdapterView.OnItemSelectedListener listener);

    void onObserverEdts();
    void onBackPressed();

    void launchCrop(Uri uri);

    void openTeamList(int quantidade);

    void showSnack(int message);
}
