package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Túlio on 16/09/2017.
 */

@IgnoreExtraProperties
public class Partida implements Serializable, Parcelable {

    private int id;
    private String time1;
    private String time2;
    private String valorTime1;
    private String valorTime2;
    private String dataPartida;

    public Partida() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getValorTime1() {
        return valorTime1;
    }

    public void setValorTime1(String valorTime1) {
        this.valorTime1 = valorTime1;
    }

    public String getValorTime2() {
        return valorTime2;
    }

    public void setValorTime2(String valorTime2) {
        this.valorTime2 = valorTime2;
    }

    public String getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(String dataPartida) {
        this.dataPartida = dataPartida;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.time1);
        dest.writeString(this.time2);
        dest.writeString(this.valorTime1);
        dest.writeString(this.valorTime2);
        dest.writeString(this.dataPartida);
    }

    protected Partida(Parcel in) {
        this.id = in.readInt();
        this.time1 = in.readString();
        this.time2 = in.readString();
        this.valorTime1 = in.readString();
        this.valorTime2 = in.readString();
        this.dataPartida = in.readString();
    }

    public static final Creator<Partida> CREATOR = new Creator<Partida>() {
        @Override
        public Partida createFromParcel(Parcel source) {
            return new Partida(source);
        }

        @Override
        public Partida[] newArray(int size) {
            return new Partida[size];
        }
    };
}
