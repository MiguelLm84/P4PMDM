package com.miguel_lm.appjsondata.ui;

import com.miguel_lm.appjsondata.modelo.Post;

public interface ListenerPost {

    void seleccionarPost(Post post);

    void modificarPosts(Post post);

    void eliminarPosts(Post post);
}
