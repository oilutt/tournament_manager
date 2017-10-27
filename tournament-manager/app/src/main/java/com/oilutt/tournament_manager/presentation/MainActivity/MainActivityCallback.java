package com.oilutt.tournament_manager.presentation.MainActivity;

import android.net.Uri;

import com.arellomobile.mvp.MvpView;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

/**
 * Created by oilut on 24/08/2017.
 */

public interface MainActivityCallback extends MvpView {

    void showPlaceHolder();

    void showPlaceHolderInvite();

    void hidePlaceHolder();

    void hidePlaceHolderInvite();

    void setAdapter(CampAdapter adapter);

    void setFoto(String foto);

    void setFotoPath(String path);

    void setNome(String nome);

    void setEmail(String email);

    void openLogin();

    void openCodigo();

    void launchCrop(Uri uri);

    void openDetails(String invite);
}
