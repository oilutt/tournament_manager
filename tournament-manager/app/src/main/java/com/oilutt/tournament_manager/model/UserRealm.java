package com.oilutt.tournament_manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Tulio on 17/10/2017.
 */

@RealmClass
@IgnoreExtraProperties
public class UserRealm extends RealmObject implements Serializable, Parcelable {

    @PrimaryKey
    private String id;
    private String nome;
    private String email;
    private String foto;

    public UserRealm() {

    }

    public UserRealm(User user) {
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.id = user.getId();
        this.foto = user.getFoto();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nome);
        dest.writeString(this.email);
        dest.writeString(this.foto);
    }

    protected UserRealm(Parcel in) {
        this.id = in.readString();
        this.nome = in.readString();
        this.email = in.readString();
        this.foto = in.readString();
    }

    public static final Creator<UserRealm> CREATOR = new Creator<UserRealm>() {
        @Override
        public UserRealm createFromParcel(Parcel source) {
            return new UserRealm(source);
        }

        @Override
        public UserRealm[] newArray(int size) {
            return new UserRealm[size];
        }
    };
}
