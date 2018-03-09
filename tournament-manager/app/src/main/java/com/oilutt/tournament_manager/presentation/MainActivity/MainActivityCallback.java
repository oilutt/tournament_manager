package com.oilutt.tournament_manager.presentation.MainActivity;

import android.net.Uri;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.MvpView;

/**
 * Created by oilut on 24/08/2017.
 */

public interface MainActivityCallback extends MvpView {

    void setFoto(String foto);

    void setFotoPath(String path);

    void setNome(String nome);

    void setEmail(String email);

    void openLogin();

    void openSorteio();

    void launchCrop(Uri uri);

    void openDetails(String invite);

    void replaceFragment(Fragment fragment);
}
