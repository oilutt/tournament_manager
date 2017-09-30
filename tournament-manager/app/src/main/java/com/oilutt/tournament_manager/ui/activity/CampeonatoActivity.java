package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.presentation.Campeonato.CampeonatoCallback;
import com.oilutt.tournament_manager.presentation.Campeonato.CampeonatoPresenter;
import com.oilutt.tournament_manager.ui.adapter.MataMataAdapter;
import com.oilutt.tournament_manager.ui.adapter.RodadaAdapter;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;
import com.oilutt.tournament_manager.ui.dialog.DialogProgress;
import com.oilutt.tournament_manager.utils.WrapContentViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 17/09/2017.
 */

public class CampeonatoActivity extends BaseActivity implements CampeonatoCallback {

    @BindView(R.id.layoutLiga)
    LinearLayout layoutLiga;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.viewPager)
    WrapContentViewPager viewPager;

    DialogProgress progress;

    @InjectPresenter
    CampeonatoPresenter presenter;
    @ProvidePresenter
    CampeonatoPresenter createPresenter(){
        return new CampeonatoPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campeonato);
        ButterKnife.bind(this);
        progress = new DialogProgress(this);
        getBundle();
    }

    private void getBundle(){
        if(getIntent().hasExtra("campeonatoId")){
            presenter.getCampeonatoId(getIntent().getStringExtra("campeonatoId"));
        }
    }

    private void configRecycler(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void hideLayoutLiga() {
        layoutLiga.setVisibility(View.GONE);
    }

    @Override
    public void setAdapterRecycler(TabelaAdapter adapter) {
        recyclerView.setAdapter(adapter);
        configRecycler();
    }

    @Override
    public void setAdapterViewPager(RodadaAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setAdapterViewPager(MataMataAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @Override
    public void hideViewPager() {
        viewPager.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progress.show();
    }

    @Override
    public void hideProgress() {
        progress.dismiss();
    }

    @Override
    public void hideRecycler() {
        recyclerView.setVisibility(View.GONE);
    }
}
