package com.oilutt.tournament_manager.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.AddCamp.AddCampCallback;
import com.oilutt.tournament_manager.presentation.AddCamp.AddCampPresenter;
import com.oilutt.tournament_manager.utils.Utils;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by oilut on 25/08/2017.
 */

public class AddCampActivity extends BaseActivity implements AddCampCallback, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.foto_camp)
    ImageView fotoCamp;
    @BindView(R.id.txt_add_foto)
    TextView txtAddFoto;
    @BindView(R.id.nome_camp)
    EditText nomeCamp;
    @BindView(R.id.descricao_camp)
    EditText descricaoCamp;
    @BindView(R.id.data_inicio_camp)
    EditText dataInicioCamp;
    @BindView(R.id.formato_spinner)
    MaterialSpinner formatoSpinner;
    @BindView(R.id.quantidade_times)
    MaterialSpinner quantidadeTimes;
    @BindView(R.id.quantidade_partidas_chave)
    MaterialSpinner quantidadePartidasChave;
    @BindView(R.id.quantidade_partidas_final)
    MaterialSpinner quantidadePartidasFinal;
    @BindView(R.id.layout_matamata)
    LinearLayout layoutMataMata;
    @BindView(R.id.layout_liga)
    LinearLayout layoutLiga;
    @BindView(R.id.jogar_idaevolta)
    MaterialSpinner idaEVolta;
    @BindView(R.id.btn_continuar)
    Button continuar;

    DatePickerDialog datePickerDialog;

    @InjectPresenter
    AddCampPresenter presenter;
    @ProvidePresenter
    AddCampPresenter createPresenter(){
        return new AddCampPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camp);
        ButterKnife.bind(this);
        setDateListener();
        setUpToolbarText(R.string.criar_camp, true);
        datePickerDialog = new DatePickerDialog(
                this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dataInicioCamp.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)  + "/"
                + ((month+1) < 10 ? "0" + (month+1) : month) + "/"
                + year);
    }

    public void setDateListener(){
        dataInicioCamp.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                showDate();
        });
    }

    @Override
    public void onObserverEdts() {
        RxTextView.textChanges(nomeCamp)
                .subscribe(presenter::getNome);

        RxTextView.textChanges(descricaoCamp)
                .subscribe(presenter::getDescricao);

        RxTextView.textChanges(dataInicioCamp)
                .subscribe(presenter::getData);
    }

    @OnClick(R.id.btn_continuar)
    public void clickOnContinuar(){
        presenter.clickSave();
    }

    public void showDate(){
        datePickerDialog.setOnDismissListener(dialog -> {
            hidenKeyBoard();
            layoutMataMata.requestFocus();
        });
        datePickerDialog.show();
    }

    @OnClick(R.id.add_photo)
    public void clickAddPhoto(){
        presenter.addPhoto();
    }

    @Override
    public void showMatamata() {
        layoutMataMata.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMatamata() {
        layoutMataMata.setVisibility(View.GONE);
    }

    @Override
    public void showLiga() {
        layoutLiga.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLiga() {
        layoutLiga.setVisibility(View.GONE);
    }

    @Override
    public void hideTextFoto() {
        txtAddFoto.setVisibility(View.GONE);
    }

    @Override
    public void setFormatoAdapter(ArrayAdapter adapter) {
        formatoSpinner.setAdapter(adapter);
    }

    @Override
    public void setQuantidadeTeamAdapter(ArrayAdapter adapter) {
        quantidadeTimes.setAdapter(adapter);
    }

    @Override
    public void setQuantidadePartidasChaveAdapter(ArrayAdapter adapter) {
        quantidadePartidasChave.setAdapter(adapter);
    }

    @Override
    public void setQuantidadePartidasFinalAdapter(ArrayAdapter adapter) {
        quantidadePartidasFinal.setAdapter(adapter);
    }

    @Override
    public void setIdaEVoltaAdapter(ArrayAdapter adapter) {
        idaEVolta.setAdapter(adapter);
    }

    @Override
    public void setFormatoListener(AdapterView.OnItemSelectedListener listener) {
        formatoSpinner.setOnItemSelectedListener(listener);
    }

    @Override
    public void setQuantidadeTeamListener(AdapterView.OnItemSelectedListener listener) {
        quantidadeTimes.setOnItemSelectedListener(listener);
    }

    @Override
    public void setQuantidadePartidasChaveListener(AdapterView.OnItemSelectedListener listener) {
        quantidadePartidasChave.setOnItemSelectedListener(listener);
    }

    @Override
    public void setQuantidadePartidasFinalListener(AdapterView.OnItemSelectedListener listener) {
        quantidadePartidasFinal.setOnItemSelectedListener(listener);
    }

    @Override
    public void setIdaEVoltaListener(AdapterView.OnItemSelectedListener listener) {
        idaEVolta.setOnItemSelectedListener(listener);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void setFoto(String path) {
        Glide.with(this).load(path).into(fotoCamp);
    }

    @Override
    public void launchCrop(Uri uri) {
        Utils.launchCrop(uri, this);
    }

    @Override
    public void showSnack(int message) {
        showSnack(getString(message));
    }

}
