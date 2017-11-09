package com.oilutt.tournament_manager.presentation.Sorteio;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tulio on 01/11/2017.
 */
@InjectViewState
public class SorteioPresenter extends MvpPresenter<SorteioCallback> {

    private List<String> times = Arrays.asList("Barcelona", "Bayern de Munique", "Borussia", "Chelsea",
            "Juventus", "Liverpool", "Manchester City", "Manchester United", "Paris Saint German", "Real Madrid");
    private List<Integer> logos = Arrays.asList(R.drawable.barcelona, R.drawable.bayern, R.drawable.borussia,
            R.drawable.chelsea, R.drawable.juventus_logo, R.drawable.liverpool, R.drawable.city, R.drawable.united, R.drawable.psg, R.drawable.real_madrid);

    public void sortear(){
        int positionTime1 = (int)Math.round(Math.random() * 9);
        int positionTime2 = (int)Math.round(Math.random() * 9);
        getViewState().setLogoTime1(logos.get(positionTime1));
        getViewState().setLogoTime2(logos.get(positionTime2));
        getViewState().setNomeTime1(times.get(positionTime1));
        getViewState().setNomeTime2(times.get(positionTime2));
    }
}
