package com.oilutt.tournament_manager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.CampeonatoDetail.CampeonatoDetailCallback;
import com.oilutt.tournament_manager.presentation.CampeonatoDetail.CampeonatoDetailPresenter;
import com.oilutt.tournament_manager.ui.adapter.TeamsAdapter;
import com.oilutt.tournament_manager.ui.dialog.DialogProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tulio on 06/10/2017.
 */

public class CampeonatoDetailsActivity extends BaseActivity implements CampeonatoDetailCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.brasao)
    ImageView brasao;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.txt_des_camp)
    TextView descricaoCamp;
    @BindView(R.id.formato)
    TextView formato;
    @BindView(R.id.partidas_chave)
    LinearLayout partidasChaveLayout;
    @BindView(R.id.txt_partidas_chave)
    TextView txtPartidasChave;
    @BindView(R.id.partidas_final)
    LinearLayout partidasFinalLayout;
    @BindView(R.id.txt_partidas_final)
    TextView txtPartidasFinal;
    @BindView(R.id.ida_e_volta)
    LinearLayout idaEVoltaLayout;
    @BindView(R.id.txt_ida_e_volta)
    TextView txtIdaEVolta;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.inicio)
    TextView inicio;
    @BindView(R.id.quantidade_times)
    TextView quantidadeTimes;
    @BindView(R.id.dono)
    TextView dono;
    @BindView(R.id.recycler_times)
    RecyclerView recyclerView;

    DialogProgress progress;

    @InjectPresenter
    CampeonatoDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campeonato_details);
        ButterKnife.bind(this);
        setupToolbar(true);
        progress = new DialogProgress(this);
        getBundle();
    }

    private void getBundle(){
        if(getIntent().hasExtra("campeonatoId")){
            presenter.setCampeonatoId(getIntent().getStringExtra("campeonatoId"));
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab)
    public void clickFab(){
        presenter.clickFab();
    }

    @Override
    public void setBrasao(String image) {
        Glide.with(this).load(Base64.decode(image, Base64.DEFAULT)).centerCrop().into(brasao);
    }

    @Override
    public void setNome(String nome) {
        collapsingToolbarLayout.setTitle(nome);
    }

    @Override
    public void setDescricao(String descricao) {
        descricaoCamp.setText(descricao);
    }

    @Override
    public void setFormato(String formato) {
        this.formato.setText(formato);
    }

    @Override
    public void hidePartidasChave() {
        partidasChaveLayout.setVisibility(View.GONE);
    }

    @Override
    public void hidePartidasFinal() {
        partidasFinalLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPartidasChave(String partidasChave) {
        txtPartidasChave.setText(partidasChave);
    }

    @Override
    public void setPartidasFinal(String partidasFinal) {
        txtPartidasFinal.setText(partidasFinal);
    }

    @Override
    public void hideIdaEVolta() {
        idaEVoltaLayout.setVisibility(View.GONE);
    }

    @Override
    public void setIdaEVolta(int idaEVolta) {
        txtIdaEVolta.setText(getString(idaEVolta));
    }

    @Override
    public void setStatus(String status) {
        this.status.setText(status);
    }

    @Override
    public void setInicio(String inicio) {
        this.inicio.setText(inicio);
    }

    @Override
    public void setQuantidadeTimes(String quantidade) {
        quantidadeTimes.setText(quantidade);
    }

    @Override
    public void setDono(String dono) {
        this.dono.setText(dono);
    }

    @Override
    public void setAdapter(TeamsAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setFocusable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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
    public void startCampeonato(String campeonatoId) {
        Intent intent = new Intent(this, CampeonatoActivity.class);
        intent.putExtra("campeonatoId", campeonatoId);
        startActivity(intent);
    }
}
