package com.miguel_lm.appjsondata.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Post")
public class Post implements Serializable {

    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE)
    private String userId;

    @PrimaryKey
    @NonNull
    private String id;

    //todo: hace falta una atributo nombre y el id sería autogenerado?

    @ColumnInfo(name="titulo")
    private String titulo;

    @ColumnInfo(name="cuerpo")
    private String cuerpo;

    public Post(String userId, String id, String titulo, String cuerpo){
        this.userId = userId;
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
}
