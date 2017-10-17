package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Túlio on 03/10/2017.
 */

@IgnoreExtraProperties
public class Grupo implements Serializable, Parcelable {

    @PrimaryKey
    private int numero;
    private List<Time> times;
    private List<Rodada> rodadas;

    public Grupo() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }

    public void setRodadas(List<Rodada> rodadas) {
        this.rodadas = rodadas;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.numero);
        dest.writeTypedList(this.times);
        dest.writeTypedList(this.rodadas);
    }

    protected Grupo(Parcel in) {
        this.numero = in.readInt();
        this.times = in.createTypedArrayList(Time.CREATOR);
        this.rodadas = in.createTypedArrayList(Rodada.CREATOR);
    }

    public static final Creator<Grupo> CREATOR = new Creator<Grupo>() {
        @Override
        public Grupo createFromParcel(Parcel source) {
            return new Grupo(source);
        }

        @Override
        public Grupo[] newArray(int size) {
            return new Grupo[size];
        }
    };
}
