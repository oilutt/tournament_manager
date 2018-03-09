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
import com.oilutt.tournament_manager.presentation.Fragments.FollowingFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.FollowingFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 08/03/2018.
 */

public class FollowingFragment extends MvpAppCompatFragment implements FollowingFragmentCallback{

    @BindView(R.id.text_no_data_invite)
    TextView textNoDataInvite;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    RelativeLayout progress;

    @InjectPresenter
    FollowingFragmentPresenter presenter;

    public static FollowingFragment newInstance(){
        Bundle args = new Bundle();
        FollowingFragment fragment = new FollowingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, parent, false);
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
    public void setAdapter(CampAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showPlaceHolderInvite() {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        textNoDataInvite.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlaceHolderInvite() {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        textNoDataInvite.setVisibility(View.GONE);
    }
}
