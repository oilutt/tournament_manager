package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oilut on 25/08/2017.
 */
@IgnoreExtraProperties
public class Formato implements Serializable, Parcelable {

    private String nome;
    private int quantidadePartidasChave;
    private int quantidadePartidasFinal;
    private int idaVolta;   // 1 = sim, 0 = nao;
    private List<Rodada> rodadas;
    private List<Fase> fases;
    private List<Grupo> grupos;

    public Formato() {

    }

    public Formato(String nome, int quantidadePartidasChave, int quantidadePartidasFinal, int idaVolta) {
        this.nome = nome;
        this.quantidadePartidasChave = quantidadePartidasChave;
        this.quantidadePartidasFinal = quantidadePartidasFinal;
        this.idaVolta = idaVolta;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nome", nome);
        result.put("quantidadePartidasChave", quantidadePartidasChave);
        result.put("quantidadePartidasFinal", quantidadePartidasFinal);
        result.put("idaVolta", idaVolta);
        result.put("rodadas", rodadas);
        result.put("fases", fases);
        result.put("grupos", grupos);

        return result;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadePartidasChave() {
        return quantidadePartidasChave;
    }

    public void setQuantidadePartidasChave(int quantidadePartidasChave) {
        this.quantidadePartidasChave = quantidadePartidasChave;
    }

    public int getQuantidadePartidasFinal() {
        return quantidadePartidasFinal;
    }

    public void setQuantidadePartidasFinal(int quantidadePartidasFinal) {
        this.quantidadePartidasFinal = quantidadePartidasFinal;
    }

    public int getIdaVolta() {
        return idaVolta;
    }

    public void setIdaVolta(int idaVolta) {
        this.idaVolta = idaVolta;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

    public void setRodadas(List<Rodada> rodadas) {
        this.rodadas = rodadas;
    }

    public List<Fase> getFases() {
        return fases;
    }

    public void setFases(List<Fase> fases) {
        this.fases = fases;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nome);
        dest.writeInt(this.quantidadePartidasChave);
        dest.writeInt(this.quantidadePartidasFinal);
        dest.writeInt(this.idaVolta);
        dest.writeTypedList(this.rodadas);
        dest.writeTypedList(this.fases);
        dest.writeTypedList(this.grupos);
    }

    protected Formato(Parcel in) {
        this.nome = in.readString();
        this.quantidadePartidasChave = in.readInt();
        this.quantidadePartidasFinal = in.readInt();
        this.idaVolta = in.readInt();
        this.rodadas = in.createTypedArrayList(Rodada.CREATOR);
        this.fases = in.createTypedArrayList(Fase.CREATOR);
        this.grupos = in.createTypedArrayList(Grupo.CREATOR);
    }

    public static final Creator<Formato> CREATOR = new Creator<Formato>() {
        @Override
        public Formato createFromParcel(Parcel source) {
            return new Formato(source);
        }

        @Override
        public Formato[] newArray(int size) {
            return new Formato[size];
        }
    };
}
