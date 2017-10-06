package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Túlio on 17/09/2017.
 */

@IgnoreExtraProperties
public class Time implements Serializable, Parcelable {

    private int id;
    private String nome;
    private int jogos;
    private int pontos;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsFeitos;
    private int golsSofridos;
    private int posicao;
    private boolean campeao;

    public Time() {

    }

    public Time(String nome, int id, int posicao) {
        this.nome = nome;
        this.id = id;
        this.posicao = posicao;
        jogos = 0;
        pontos = 0;
        vitorias = 0;
        empates = 0;
        derrotas = 0;
        golsFeitos = 0;
        golsSofridos = 0;
        campeao = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getJogos() {
        return jogos;
    }

    public void setJogos(int jogos) {
        this.jogos = jogos;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getGolsFeitos() {
        return golsFeitos;
    }

    public void setGolsFeitos(int golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    public int getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(int golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public boolean isCampeao() {
        return campeao;
    }

    public void setCampeao(boolean campeao) {
        this.campeao = campeao;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nome);
        dest.writeInt(this.jogos);
        dest.writeInt(this.pontos);
        dest.writeInt(this.vitorias);
        dest.writeInt(this.empates);
        dest.writeInt(this.derrotas);
        dest.writeInt(this.golsFeitos);
        dest.writeInt(this.golsSofridos);
        dest.writeInt(this.posicao);
        dest.writeByte(this.campeao ? (byte) 1 : (byte) 0);
    }

    protected Time(Parcel in) {
        this.id = in.readInt();
        this.nome = in.readString();
        this.jogos = in.readInt();
        this.pontos = in.readInt();
        this.vitorias = in.readInt();
        this.empates = in.readInt();
        this.derrotas = in.readInt();
        this.golsFeitos = in.readInt();
        this.golsSofridos = in.readInt();
        this.posicao = in.readInt();
        this.campeao = in.readByte() != 0;
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel source) {
            return new Time(source);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };
}
