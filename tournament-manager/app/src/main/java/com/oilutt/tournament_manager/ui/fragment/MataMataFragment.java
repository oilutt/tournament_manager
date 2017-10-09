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
import com.oilutt.tournament_manager.presentation.Fragments.MataMataFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.MataMataFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.PartidaMatamataAdapter;
import com.oilutt.tournament_manager.ui.adapter.PartidaMatamataEditAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tulio on 03/10/2017.
 */

public class MataMataFragment extends MvpAppCompatFragment implements MataMataFragmentCallback {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectPresenter
    MataMataFragmentPresenter presenter;

    public static MataMataFragment newInstance() {
        Bundle args = new Bundle();
        MataMataFragment fragment = new MataMataFragment();
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
        presenter.setEdit(getArguments().getBoolean("edit", false));
        presenter.setFase(getArguments().getParcelable("fase"));
    }

    @Override
    public void setAdapter(PartidaMatamataAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setAdapter(PartidaMatamataEditAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }
}
