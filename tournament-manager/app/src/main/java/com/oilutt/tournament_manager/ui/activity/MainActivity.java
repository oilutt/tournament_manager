package com.oilutt.tournament_manager.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.MainActivity.MainActivityCallback;
import com.oilutt.tournament_manager.presentation.MainActivity.MainActivityPresenter;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainActivityCallback,
        GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.text_no_data)
    TextView textNoData;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progress;

    @InjectPresenter
    MainActivityPresenter presenter;

    @ProvidePresenter
    MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpToolbarText(R.string.app_name, false);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.fab)
    public void onClickFab() {
        openActivity(AddCampActivity.class);
    }

    @Override
    public void showPlaceHolder() {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);
        textNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlaceHolder() {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        textNoData.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setAdapter(CampAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
