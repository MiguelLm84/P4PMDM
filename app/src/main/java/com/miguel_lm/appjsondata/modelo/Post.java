package com.miguel_lm.appjsondata.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Post")
public class Post implements Serializable {

    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE)
    private int userId;

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int id;

    //todo: hace falta una atributo nombre y el id sería autogenerado?

    @ColumnInfo(name="titulo")
    private String titulo;

    @ColumnInfo(name="cuerpo")
    private String cuerpo;

    /** Constructor para un post descargado del servidor */
    public Post(int userId, int id, String titulo, String cuerpo){
        this.userId = userId;
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    /** Constructor para un nuevo post, no hace falta pasar el id */
    @Ignore
    public Post(int userId,  String titulo, String cuerpo){
        this.userId = userId;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int  getId() {
        return id;
    }

    public void setId(int  id) {
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

    public void modificar(int userId,  String titulo, String cuerpo){
        this.userId = userId;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }
}
