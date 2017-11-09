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
import com.oilutt.tournament_manager.presentation.Fragments.TabelaFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.TabelaFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.TabelaAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tulio on 03/10/2017.
 */

public class TabelaFragment extends MvpAppCompatFragment implements TabelaFragmentCallback {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectPresenter
    TabelaFragmentPresenter presenter;

    public static TabelaFragment newInstance() {
        Bundle args = new Bundle();
        TabelaFragment fragment = new TabelaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void init() {
        presenter.attachView(this);
        presenter.setGrupo(getArguments().getInt("grupo"));
        presenter.setCampeonato(getArguments().getParcelable("campeonato"));
    }

    @Override
    public void setAdapter(TabelaAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }
}
