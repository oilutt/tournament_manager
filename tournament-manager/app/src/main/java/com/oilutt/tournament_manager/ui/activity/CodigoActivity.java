package com.oilutt.tournament_manager.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.presentation.Codigo.CodigoCallback;
import com.oilutt.tournament_manager.presentation.Codigo.CodigoPresenter;
import com.oilutt.tournament_manager.ui.dialog.DialogProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tulio on 17/10/2017.
 */

public class CodigoActivity extends BaseActivity implements CodigoCallback {

    @BindView(R.id.codigo_camp)
    EditText codigoCamp;

    DialogProgress progress;

    @InjectPresenter
    CodigoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo);
        ButterKnife.bind(this);
        setUpToolbarText(R.string.codigo_title, true);
        progress = new DialogProgress(this);
    }

    @Override
    public void onObserverEdts() {
        RxTextView.textChanges(codigoCamp)
                .subscribe(presenter::getCodigo);
    }

    @OnClick(R.id.btn_inserir)
    public void clickInserir(){
        presenter.clickInserir();
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
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void finishIntent(){
        Intent i = new Intent();

        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
