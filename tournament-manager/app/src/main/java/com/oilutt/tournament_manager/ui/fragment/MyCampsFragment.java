package com.oilutt.tournament_manager.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Fragments.MyCampsFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.MyCampsFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 08/03/2018.
 */

public class MyCampsFragment extends MvpAppCompatFragment implements MyCampsFragmentCallback{

    @BindView(R.id.text_no_data)
    TextView textNoData;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    RelativeLayout progress;

    @InjectPresenter
    MyCampsFragmentPresenter presenter;

    public static MyCampsFragment newInstance(){
        Bundle args = new Bundle();
        MyCampsFragment fragment = new MyCampsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycamps, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        progress.setOnClickListener(view1 -> {});
        presenter.attachView(this);
        presenter.init(getActivity());
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showPlaceHolder() {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        textNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlaceHolder() {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        textNoData.setVisibility(View.GONE);
    }

    @Override
    public void setAdapter(CampAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}
