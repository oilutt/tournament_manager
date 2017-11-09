package com.oilutt.tournament_manager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Edit.EditCallback;
import com.oilutt.tournament_manager.presentation.Edit.EditPresenter;
import com.oilutt.tournament_manager.ui.adapter.TabAdapter;
import com.oilutt.tournament_manager.ui.dialog.DialogProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tulio on 09/10/2017.
 */

public class EditActivity extends BaseActivity implements EditCallback {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    DialogProgress progress;

    @InjectPresenter
    EditPresenter presenter;
    @ProvidePresenter
    EditPresenter createPresenter(){
        return new EditPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        setUpToolbarText(R.string.edit_title, true);
        progress = new DialogProgress(this);
        getBundle();
    }

    private void getBundle() {
        if (getIntent().hasExtra("campeonatoId")) {
            presenter.getCampeonatoId(getIntent().getStringExtra("campeonatoId"));
        }
    }

    @OnClick(R.id.salvar)
    public void clickSalvar(){
        presenter.clickSalvar();
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
    public void setAdapterTab(TabAdapter adapterTab) {
        viewPager.setAdapter(adapterTab);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void startCampeonato(String campeonatoId){
        Intent intent = new Intent(this, CampeonatoActivity.class);
        intent.putExtra("campeonatoId", campeonatoId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, CampeonatoActivity.class);
        intent.putExtra("campeonatoId", presenter.campeonatoId);
        startActivity(intent);
        finish();
    }
}
