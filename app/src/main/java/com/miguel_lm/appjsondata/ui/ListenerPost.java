package com.miguel_lm.appjsondata.ui;

import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;

public interface ListenerPost {

    void seleccionarUser(User user);

    void modificarPosts(Post post);

    void eliminarPosts(Post post);
}
