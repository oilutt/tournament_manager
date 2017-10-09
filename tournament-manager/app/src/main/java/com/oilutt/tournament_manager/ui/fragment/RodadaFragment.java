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
import com.oilutt.tournament_manager.model.Partida;
import com.oilutt.tournament_manager.presentation.Fragments.RodadaFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.RodadaFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.PartidaAdapter;
import com.oilutt.tournament_manager.ui.adapter.PartidaEditAdapter;

import java.util.List;

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
        presenter.setEdit(getArguments().getBoolean("edit", false));
    }

    @Override
    public void setAdapter(PartidaAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setAdapter(PartidaEditAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    public List<Partida> getList(){
        if(presenter != null && presenter.adapterEdit != null) {
            return presenter.adapterEdit.getData();
        } else {
            return null;
        }
    }
}
