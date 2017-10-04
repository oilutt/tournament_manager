package com.oilutt.tournament_manager.presentation.AddCamp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.Constants;
import com.oilutt.tournament_manager.app.TournamentManagerApp;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.model.Formato;
import com.oilutt.tournament_manager.model.User;
import com.oilutt.tournament_manager.ui.activity.TeamListActivity;
import com.oilutt.tournament_manager.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oilut on 25/08/2017.
 */
@InjectViewState
public class AddCampPresenter extends MvpPresenter<AddCampCallback> {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private String nome, formato, dataInicio, pathImage, imageBase64, descricao;
    private int quantidadeTimes, quantidadePartidasChave, quantidadePartidasFinal, idaEVolta = 2;

    private Context context;

    private AdapterView.OnItemSelectedListener formatoListener, timeListener, chaveListener, finalListener, idaEVoltaListener;
    private ArrayAdapter formatoAdapter, timesAdapter, chaveAdapter, finalAdapter, idaEVoltaAdapter;
    private List<String> formatoArray, idaEVoltaArray;
    private List<Integer> timesArray, chaveArray, finalArray, timesArrayTorneio;

    public AddCampPresenter(Context context) {
        this.context = context;
        initLists();
        getViewState().onObserverEdts();
        setListeners();
        setAdapters();
    }

    public void getNome(CharSequence nome) {
        this.nome = nome.toString();
    }

    public void getData(CharSequence data) {
        this.dataInicio = data.toString();
    }

    public void getDescricao(CharSequence descricao){
        this.descricao = descricao.toString();
    }

    public void clickSave() {
        if(verifyFields()) {
            Campeonato campeonato = new Campeonato();
            campeonato.setNome(nome);
            campeonato.setDataInicio(dataInicio);
            if (Utils.compareDates(dataInicio, Utils.getDataFromTimeStamp(Utils.getTimeStamp())).equals("after")) {
                campeonato.setStatus(1);
            } else if (Utils.compareDates(dataInicio, Utils.getDataFromTimeStamp(Utils.getTimeStamp())).equals("before")) {
                if (campeonato.getDataFim() != null) {
                    campeonato.setStatus(3);
                } else {
                    campeonato.setStatus(2);
                }
            } else {
                if (campeonato.getDataFim() != null) {
                    campeonato.setStatus(3);
                } else {
                    campeonato.setStatus(2);
                }
            }
            campeonato.setDono(new User(mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail(), mFirebaseUser.getUid()));
            campeonato.setFormato(new Formato(formato, quantidadePartidasChave, quantidadePartidasFinal, idaEVolta));
            campeonato.setQuantidadeTimes(quantidadeTimes);
            campeonato.setFoto(imageBase64);
            campeonato.setDescricao(descricao);
            campeonato.setTimesClassificados(new ArrayList<>());

            Handler handler = new Handler();
            handler.post(() -> TournamentManagerApp.preferencesManager.setCampeonato(campeonato));

            getViewState().openTeamList(quantidadeTimes);
        }
    }

    private boolean verifyFields(){
        if(pathImage == null || pathImage.equals("")){
            getViewState().showSnack(R.string.erro_foto);
            return false;
        }
        if(nome == null || nome.equals("")){
            getViewState().showSnack(R.string.erro_nome);
            return false;
        }
        if(descricao == null || descricao.equals("")){
            getViewState().showSnack(R.string.erro_descricao);
            return false;
        }
        if(dataInicio == null || dataInicio.equals("")){
            getViewState().showSnack(R.string.erro_data);
            return false;
        }
        if(formato == null || formato.equals("")){
            getViewState().showSnack(R.string.erro_formato);
            return false;
        } else if(formato.equals(context.getString(R.string.liga))) {
            if (idaEVolta == 2) {
                getViewState().showSnack(R.string.erro_quantidade_chave);
                return false;
            }
        } else if(formato.equals(context.getString(R.string.matamata))) {
            if (quantidadePartidasChave < 1) {
                getViewState().showSnack(R.string.erro_quantidade_chave);
                return false;
            }
            if (quantidadePartidasFinal < 1) {
                getViewState().showSnack(R.string.erro_quantidade_final);
                return false;
            }
        } else if (formato.equals(context.getString(R.string.torneio))){
            if (quantidadePartidasChave < 1) {
                getViewState().showSnack(R.string.erro_quantidade_chave);
                return false;
            }
            if (quantidadePartidasFinal < 1) {
                getViewState().showSnack(R.string.erro_quantidade_final);
                return false;
            }
        }
        if(quantidadeTimes < 2){
            getViewState().showSnack(R.string.erro_quantidade_times);
            return false;
        }

        return true;
    }

    private void setListeners() {
        formatoListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(context.getString(R.string.formato_camp)))
                    formato = parent.getItemAtPosition(position).toString();
                if (formato != null && formato.equals(context.getString(R.string.matamata))) {
                    getViewState().showMatamata();
                    getViewState().hideLiga();
                    timesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, timesArray);
                    timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    getViewState().setQuantidadeTeamAdapter(timesAdapter);
                }else if(formato != null && formato.equals(context.getString(R.string.liga))) {
                    getViewState().showLiga();
                    getViewState().hideMatamata();
                    timesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, timesArray);
                    timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    getViewState().setQuantidadeTeamAdapter(timesAdapter);
                } else if(formato != null && formato.equals(context.getString(R.string.torneio))) {
                    getViewState().hideLiga();
                    getViewState().showMatamata();
                    timesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, timesArrayTorneio);
                    timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    getViewState().setQuantidadeTeamAdapter(timesAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        timeListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(context.getString(R.string.times_camp)))
                    quantidadeTimes = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        chaveListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(context.getString(R.string.partidas_camp_chave)))
                    quantidadePartidasChave = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        finalListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(context.getString(R.string.partidas_camp_final)))
                    quantidadePartidasFinal = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        idaEVoltaListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(context.getString(R.string.idaevolta))) {
                    if(parent.getItemAtPosition(position).toString().equals(context.getString(R.string.sim))) {
                        idaEVolta = 1;
                    } else {
                        idaEVolta = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        getViewState().setFormatoListener(formatoListener);
        getViewState().setQuantidadeTeamListener(timeListener);
        getViewState().setQuantidadePartidasChaveListener(chaveListener);
        getViewState().setQuantidadePartidasFinalListener(finalListener);
        getViewState().setIdaEVoltaListener(idaEVoltaListener);
    }

    private void setAdapters() {
        formatoAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, formatoArray);
        formatoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getViewState().setFormatoAdapter(formatoAdapter);

        timesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, timesArray);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getViewState().setQuantidadeTeamAdapter(timesAdapter);

        chaveAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, chaveArray);
        chaveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getViewState().setQuantidadePartidasChaveAdapter(chaveAdapter);

        finalAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, finalArray);
        finalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getViewState().setQuantidadePartidasFinalAdapter(finalAdapter);

        idaEVoltaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, idaEVoltaArray);
        idaEVoltaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getViewState().setIdaEVoltaAdapter(idaEVoltaAdapter);
    }

    private void initLists() {
        formatoArray = new ArrayList<>(Arrays.asList(context.getString(R.string.liga),
                context.getString(R.string.torneio),
                context.getString(R.string.matamata)));
        timesArray = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
                19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32));
        timesArrayTorneio = new ArrayList<>(Arrays.asList(8, 16, 32));
        chaveArray = new ArrayList<>(Arrays.asList(1, 2, 3, 5, 7));
        finalArray = new ArrayList<>(Arrays.asList(1, 2, 3, 5, 7, 9));
        idaEVoltaArray = new ArrayList<>(Arrays.asList(context.getString(R.string.sim),
                context.getString(R.string.nao)));
    }

    public void addPhoto(){
        Utils.showDialogCameraGallery((Activity) context);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == Constants.REQUEST_CAMERA || requestCode == Constants.PICK_PHOTO_CODE) && resultCode == AppCompatActivity.RESULT_OK) {
            getViewState().launchCrop(data.getData());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null) {
                pathImage = result.getUri().getPath();
                getViewState().setFoto(pathImage);
                getViewState().hideTextFoto();
                Bitmap bm = BitmapFactory.decodeFile(pathImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                imageBase64 = Base64.encodeToString(b, Base64.DEFAULT);
            }
        }
    }
}
