package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Sorteio.SorteioCallback;
import com.oilutt.tournament_manager.presentation.Sorteio.SorteioPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tulio on 01/11/2017.
 */

public class SorteioFIFAActivity extends BaseActivity implements SorteioCallback {

    @BindView(R.id.time1_image)
    ImageView imageTime1;
    @BindView(R.id.time2_image)
    ImageView imageTime2;
    @BindView(R.id.nome_time1)
    TextView nomeTime1;
    @BindView(R.id.nome_time2)
    TextView nomeTime2;

    @InjectPresenter
    SorteioPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio);
        ButterKnife.bind(this);
        setUpToolbarText(R.string.sorteio_title, true);
    }
}
