package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Túlio on 16/09/2017.
 */

@IgnoreExtraProperties
public class PartidaBO implements Serializable, Parcelable {

    private int id;
    private String valorTime1;
    private String valorTime2;
    private String dataPartida;

    public PartidaBO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeString(this.valorTime1);
        dest.writeString(this.valorTime2);
        dest.writeString(this.dataPartida);
    }

    protected PartidaBO(Parcel in) {
        this.id = in.readInt();
        this.valorTime1 = in.readString();
        this.valorTime2 = in.readString();
        this.dataPartida = in.readString();
    }

    public static final Creator<PartidaBO> CREATOR = new Creator<PartidaBO>() {
        @Override
        public PartidaBO createFromParcel(Parcel source) {
            return new PartidaBO(source);
        }

        @Override
        public PartidaBO[] newArray(int size) {
            return new PartidaBO[size];
        }
    };
}
