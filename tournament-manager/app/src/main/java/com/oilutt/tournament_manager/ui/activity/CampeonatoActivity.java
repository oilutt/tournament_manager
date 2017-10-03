package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Campeonato.CampeonatoCallback;
import com.oilutt.tournament_manager.presentation.Campeonato.CampeonatoPresenter;
import com.oilutt.tournament_manager.ui.dialog.DialogProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 17/09/2017.
 */

public class CampeonatoActivity extends BaseActivity implements CampeonatoCallback {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
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

    @Override
    public void setAdapterTab(FragmentPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showProgress() {
        progress.show();
    }

    @Override
    public void hideProgress() {
        progress.dismiss();
    }
}
