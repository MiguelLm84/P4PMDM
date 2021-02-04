package com.miguel_lm.appjsondata.ui;

import com.miguel_lm.appjsondata.modelo.Posts;

public interface ListenerPost {

    void infoAutor(Posts post);

    void anhadirPosts(Posts post);

    void modificarPosts(Posts post);

    void eliminarPosts(Posts post);
    //void jsonDataSeleccionado(JsonData jsonData);
}
