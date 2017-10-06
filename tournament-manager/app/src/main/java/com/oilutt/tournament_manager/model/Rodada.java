package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Túlio on 17/09/2017.
 */

@IgnoreExtraProperties
public class Rodada implements Serializable, Parcelable {

    private int numero;
    private List<Partida> partidas;
    private boolean completa = false;

    public Rodada() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public boolean isCompleta() {
        return completa;
    }

    public void setCompleta(boolean completa) {
        this.completa = completa;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.numero);
        dest.writeTypedList(this.partidas);
        dest.writeByte(this.completa ? (byte) 1 : (byte) 0);
    }

    protected Rodada(Parcel in) {
        this.numero = in.readInt();
        this.partidas = in.createTypedArrayList(Partida.CREATOR);
        this.completa = in.readByte() != 0;
    }

    public static final Creator<Rodada> CREATOR = new Creator<Rodada>() {
        @Override
        public Rodada createFromParcel(Parcel source) {
            return new Rodada(source);
        }

        @Override
        public Rodada[] newArray(int size) {
            return new Rodada[size];
        }
    };
}
