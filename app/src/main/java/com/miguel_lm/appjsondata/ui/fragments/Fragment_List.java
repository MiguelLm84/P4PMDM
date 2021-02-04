package com.miguel_lm.appjsondata.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Posts;
import com.miguel_lm.appjsondata.ui.ListenerPost;
import com.miguel_lm.appjsondata.ui.adaptador.AdapterPosts;

import java.util.List;

public class Fragment_List extends Fragment implements ListenerPost {

    RecyclerView recyclerViewPosts;

    AdapterPosts adapterPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment__list, container, false);

        JsonLab jsonLab = JsonLab.get(getContext());
        List<Posts> listPosts = jsonLab.getPosts();

        recyclerViewPosts = vista.findViewById(R.id.RecyclerViewJson);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPosts = new AdapterPosts(getContext(), listPosts, this);
        recyclerViewPosts.setAdapter(adapterPosts);

        return vista;
    }

    public void setListaPosts(List<Posts> listaPosts) {
        adapterPosts.actualizarListado(listaPosts);
    }

    @Override
    public void infoAutor(Posts post) {

    }

    @Override
    public void anhadirPosts(Posts post) {

    }

    @Override
    public void modificarPosts(Posts post) {

    }

    @Override
    public void eliminarPosts(Posts post) {

    }
}