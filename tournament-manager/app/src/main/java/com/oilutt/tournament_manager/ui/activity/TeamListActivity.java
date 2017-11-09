package com.oilutt.tournament_manager.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.TeamList.TeamListCallback;
import com.oilutt.tournament_manager.presentation.TeamList.TeamListPresenter;
import com.oilutt.tournament_manager.ui.adapter.TeamListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Túlio on 16/09/2017.
 */

public class TeamListActivity extends BaseActivity implements TeamListCallback {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectPresenter
    TeamListPresenter presenter;

    @ProvidePresenter
    TeamListPresenter createPresenter() {
        return new TeamListPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        ButterKnife.bind(this);
        setUpToolbarText(R.string.team_list_title, true);
        getBundle();
    }

    private void getBundle() {
        if (getIntent().hasExtra("quantidade")) {
            presenter.setQuantidade(getIntent().getIntExtra("quantidade", 0));
        }
        if (getIntent().hasExtra("listTeams")) {
            presenter.setList(getIntent().getStringArrayListExtra("listTeams"));
        } else {
            presenter.setList(new ArrayList<>());
        }
    }

    @OnClick(R.id.btn_continuar)
    public void clickOnContinuar() {
        presenter.clickSave();
    }

    @Override
    public void setAdapter(TeamListAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setFocusable(false);
    }

    @Override
    public void showLoading() {
        showProgressDialog(getString(R.string.gerando_camp));
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        hidenKeyBoard();
    }

    @Override
    public void showSnack(int msg) {
        showSnack(msg, recyclerView);
    }
}
