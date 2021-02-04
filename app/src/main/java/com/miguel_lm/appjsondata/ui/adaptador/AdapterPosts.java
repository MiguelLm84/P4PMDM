package com.miguel_lm.appjsondata.ui.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.Posts;
import com.miguel_lm.appjsondata.ui.ListenerPost;

import java.util.List;

public class AdapterPosts extends RecyclerView.Adapter<ViewHolderPost> {

    private ListenerPost listenerJson;
    private List<Posts> listaPosts;
    Context context;

    public AdapterPosts(Context context, List<Posts> listaPosts, ListenerPost listenerJson) {

        this.context = context;
        this.listaPosts = listaPosts;
        this.listenerJson = listenerJson;
    }

    public void actualizarListado(List<Posts> listaPosts) {
        this.listaPosts = listaPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_texto_json, parent, false);
        return new ViewHolderPost(v, listenerJson);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost holder, int position) {
        holder.mostrarTexto(listaPosts.get(position), context);
    }

    @Override
    public int getItemCount() {
        return listaPosts.size();
    }
}
