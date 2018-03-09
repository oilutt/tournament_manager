package com.oilutt.tournament_manager.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Fragments.SearchFragmentCallback;
import com.oilutt.tournament_manager.presentation.Fragments.SearchFragmentPresenter;
import com.oilutt.tournament_manager.ui.adapter.CampAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 08/03/2018.
 */

public class SearchFragment extends MvpAppCompatFragment implements SearchFragmentCallback{

    @BindView(R.id.recyclerViewBusca)
    RecyclerView recyclerViewBusca;
    @BindView(R.id.layout_busca)
    RelativeLayout layoutBusca;
    @BindView(R.id.edt_busca)
    EditText edtBusca;
    @BindView(R.id.placeholder_busca)
    TextView placeholderBusca;
    @BindView(R.id.text_busca)
    TextView textBusca;
    @BindView(R.id.progress)
    RelativeLayout progress;

    @InjectPresenter
    SearchFragmentPresenter presenter;

    public static SearchFragment newInstance(){
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeListener();
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
    public void showBusca() {
        progress.setVisibility(View.GONE);
        layoutBusca.setVisibility(View.VISIBLE);
        recyclerViewBusca.setVisibility(View.GONE);
        placeholderBusca.setVisibility(View.GONE);
        textBusca.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlaceHolderBusca() {
        progress.setVisibility(View.GONE);
        layoutBusca.setVisibility(View.VISIBLE);
        recyclerViewBusca.setVisibility(View.GONE);
        placeholderBusca.setVisibility(View.VISIBLE);
        textBusca.setVisibility(View.GONE);
    }

    @Override
    public void hidePlaceHolderBusca() {
        progress.setVisibility(View.GONE);
        layoutBusca.setVisibility(View.VISIBLE);
        recyclerViewBusca.setVisibility(View.VISIBLE);
        placeholderBusca.setVisibility(View.GONE);
        textBusca.setVisibility(View.GONE);
    }

    @Override
    public void setBuscaAdapter(CampAdapter adapter) {
        recyclerViewBusca.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewBusca.setHasFixedSize(true);
        recyclerViewBusca.setAdapter(adapter);
    }

    @Override
    public void setBuscaWatcher(TextWatcher watcher) {
        edtBusca.addTextChangedListener(watcher);
    }
}
