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
public class User implements Serializable, Parcelable {

    private String id;
    private String nome;
    private String email;
    private String foto;
    private List<Campeonato> inviteChamps;

    public User() {

    }

    public User(String nome, String email, String id, String foto) {
        this.nome = nome;
        this.email = email;
        this.id = id;
        this.foto = foto;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nome", nome);
        result.put("email", email);
        result.put("foto", foto);
        return result;
    }

    public Map<String, Object> toMap2() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nome", nome);
        result.put("email", email);
        result.put("foto", foto);
        result.put("invites", inviteChamps);
        return result;
    }

    public User(UserRealm userRealm) {
        this.nome = userRealm.getNome();
        this.email = userRealm.getEmail();
        this.id = userRealm.getId();
        this.foto = userRealm.getFoto();
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

    public List<Campeonato> getInviteChamps() {
        return inviteChamps;
    }

    public void setInviteChamps(List<Campeonato> inviteChamps) {
        this.inviteChamps = inviteChamps;
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
        dest.writeTypedList(this.inviteChamps);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.nome = in.readString();
        this.email = in.readString();
        this.foto = in.readString();
        this.inviteChamps = in.createTypedArrayList(Campeonato.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
