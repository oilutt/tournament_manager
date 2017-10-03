package com.oilutt.tournament_manager.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Fragments.RodadaFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.RodadaFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.PartidaAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tulio on 03/10/2017.
 */

public class RodadaFragment extends MvpAppCompatFragment implements RodadaFragmentCallback {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectPresenter
    RodadaFragmentPresenter presenter;

    public static RodadaFragment newInstance() {
        Bundle args = new Bundle();
        RodadaFragment fragment = new RodadaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rodada, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void init() {
        presenter.attachView(this);
        presenter.setImpar(getArguments().getInt("impar"));
        presenter.setRodada(getArguments().getParcelable("rodada"));
    }

    @Override
    public void setAdapter(PartidaAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }
}
