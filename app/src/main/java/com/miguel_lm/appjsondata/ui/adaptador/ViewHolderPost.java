package com.miguel_lm.appjsondata.ui.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Posts;
import com.miguel_lm.appjsondata.modelo.Users;
import com.miguel_lm.appjsondata.ui.Activity_Info_Detalle;
import com.miguel_lm.appjsondata.ui.Activity_Info_Post;
import com.miguel_lm.appjsondata.ui.ListenerPost;

public class ViewHolderPost extends RecyclerView.ViewHolder {

    private TextView tv_titulo, tv_autor;
    private ConstraintLayout constraintLayoutJson;
    private CardView cardViewJson;
    private ListenerPost listener_post;

    public ViewHolderPost(@NonNull View itemView, ListenerPost listenerPost) {
        super(itemView);

        this.listener_post = listenerPost;

        tv_titulo = itemView.findViewById(R.id.tv_titulo);
        tv_autor = itemView.findViewById(R.id.tv_autor);
        constraintLayoutJson = itemView.findViewById(R.id.ConstaintLayoutJson);
        cardViewJson = itemView.findViewById(R.id.CardViewJson);
    }

    //public void mostrarTexto(final Users user, final Posts post, Context context) {
    public void mostrarTexto(final Posts post, Context context) {

        JsonLab jsonLab = JsonLab.get(context);
        Users autor = jsonLab.searchUserById(post.getUserId());

        tv_titulo.setText(post.getTitulo());
        tv_autor.setText(autor.getName());

        cardViewJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Activity_Info_Post.class);
                context.startActivity(intent);
            }
        });

        cardViewJson.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(context, cardViewJson);
                popupMenu.getMenuInflater().inflate(R.menu.menu_contextual, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.accionVerInfoAutor:
                                Intent intent = new Intent(context, Activity_Info_Detalle.class);
                                context.startActivity(intent);

                                break;
                            case R.id.accionModificarPost:
                                listener_post.modificarPosts(post);
                                break;
                            case R.id.accionEliminarPost:
                                listener_post.eliminarPosts(post);
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();

                return false;
            }
        });

    }
}
