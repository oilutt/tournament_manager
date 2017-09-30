package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by oilut on 28/09/2017.
 */

@IgnoreExtraProperties
public class BestOf implements Serializable, Parcelable {

    private int quantity;
    private List<Partida> partidas;
    private String time1;
    private String time2;
    private String valorTime1;
    private String valorTime2;

    public BestOf(){}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quantity);
        dest.writeTypedList(this.partidas);
        dest.writeString(this.time1);
        dest.writeString(this.time2);
        dest.writeString(this.valorTime1);
        dest.writeString(this.valorTime2);
    }

    protected BestOf(Parcel in) {
        this.quantity = in.readInt();
        this.partidas = in.createTypedArrayList(Partida.CREATOR);
        this.time1 = in.readString();
        this.time2 = in.readString();
        this.valorTime1 = in.readString();
        this.valorTime2 = in.readString();
    }

    public static final Creator<BestOf> CREATOR = new Creator<BestOf>() {
        @Override
        public BestOf createFromParcel(Parcel source) {
            return new BestOf(source);
        }

        @Override
        public BestOf[] newArray(int size) {
            return new BestOf[size];
        }
    };
}
