package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by oilut on 25/08/2017.
 */

@IgnoreExtraProperties
public class Campeonato implements Serializable, Parcelable {

    private String id;
    private String nome;
    private int quantidadeTimes;
    private Formato formato;
    private User dono;
    private List<Time> times;
    private String dataInicio;
    private String dataFim;
    private String dataAtualizacao;
    private String descricao;
    private String foto;
    private int status;   // 1:Aberto, 2:Em andamento, 3:Encerrado
    private String campeao;

    public Campeonato() {

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nome", nome);
        result.put("quantidadeTimes", quantidadeTimes);
        result.put("formato", formato.toMap());
        result.put("dataInicio", dataInicio);
        result.put("times", times);
        result.put("dataFim", dataFim);
        result.put("dataAtualizacao", dataAtualizacao);
        result.put("status", status);
        result.put("descricao", descricao);
        result.put("foto", foto);
        result.put("dono", dono.toMap());
        result.put("campeao", campeao);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeTimes() {
        return quantidadeTimes;
    }

    public void setQuantidadeTimes(int quantidadeTimes) {
        this.quantidadeTimes = quantidadeTimes;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    public User getDono() {
        return dono;
    }

    public void setDono(User dono) {
        this.dono = dono;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    public String getCampeao() {
        return campeao;
    }

    public void setCampeao(String campeao) {
        this.campeao = campeao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nome);
        dest.writeInt(this.quantidadeTimes);
        dest.writeParcelable(this.formato, flags);
        dest.writeParcelable(this.dono, flags);
        dest.writeTypedList(this.times);
        dest.writeString(this.dataInicio);
        dest.writeString(this.dataFim);
        dest.writeString(this.dataAtualizacao);
        dest.writeString(this.descricao);
        dest.writeString(this.foto);
        dest.writeString(this.campeao);
        dest.writeInt(this.status);
    }

    protected Campeonato(Parcel in) {
        this.id = in.readString();
        this.nome = in.readString();
        this.quantidadeTimes = in.readInt();
        this.formato = in.readParcelable(Formato.class.getClassLoader());
        this.dono = in.readParcelable(User.class.getClassLoader());
        this.times = in.createTypedArrayList(Time.CREATOR);
        this.dataInicio = in.readString();
        this.dataFim = in.readString();
        this.dataAtualizacao = in.readString();
        this.descricao = in.readString();
        this.campeao = in.readString();
        this.foto = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<Campeonato> CREATOR = new Creator<Campeonato>() {
        @Override
        public Campeonato createFromParcel(Parcel source) {
            return new Campeonato(source);
        }

        @Override
        public Campeonato[] newArray(int size) {
            return new Campeonato[size];
        }
    };
}
