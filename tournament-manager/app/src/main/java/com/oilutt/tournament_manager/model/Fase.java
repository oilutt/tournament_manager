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
 * Created by oilut on 28/09/2017.
 */

@IgnoreExtraProperties
public class Fase implements Serializable, Parcelable {

    private int numero;
    private List<BestOf> partidas;

    public Fase() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<BestOf> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<BestOf> partidas) {
        this.partidas = partidas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.numero);
        dest.writeTypedList(this.partidas);
    }

    protected Fase(Parcel in) {
        this.numero = in.readInt();
        this.partidas = in.createTypedArrayList(BestOf.CREATOR);
    }

    public static final Creator<Fase> CREATOR = new Creator<Fase>() {
        @Override
        public Fase createFromParcel(Parcel source) {
            return new Fase(source);
        }

        @Override
        public Fase[] newArray(int size) {
            return new Fase[size];
        }
    };
}
