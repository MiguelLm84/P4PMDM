package com.miguel_lm.appjsondata.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;

import java.util.List;

public class Activity_Add_Post extends AppCompatActivity {

    private static final int REQUEST_EDITAR_POST = 1222;
    public static final String PARAM_POST_EDITAR = "param_post_editar";
    private long tiempoParaSalir = 0;
    public enum ActivityPostModo { crear, editar}
    private ActivityPostModo activityPostModo;
    private Post postEditar;
    private JsonLab jsonLab;
    private List<Post> listaPosts;
    private EditText editTextNomAutor, editTextTituloPost, editTextCuerpoPost;
    TextView tv_tituloActivityPost;
    Button btn_aceptar, btn_cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__post);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.code_json_icon_136758);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        jsonLab = JsonLab.get(this);
        listaPosts = jsonLab.getPosts();

        editTextNomAutor = findViewById(R.id.ed_nombre_autor);
        editTextTituloPost = findViewById(R.id.ed_tituloPost);
        editTextCuerpoPost = findViewById(R.id.ed_cuerpoPost);
        tv_tituloActivityPost = findViewById(R.id.tv_title_anhadirPost);
        btn_aceptar = findViewById(R.id.btn_aceptar);
        btn_cancelar = findViewById(R.id.btn_cancelar);


        postEditar = (Post) getIntent().getSerializableExtra(PARAM_POST_EDITAR);

        activityPostModo = postEditar == null ? ActivityPostModo.crear : ActivityPostModo.editar;

        if (activityPostModo == ActivityPostModo.editar) {
            mostrarPost();
            tv_tituloActivityPost.setText("Modificar Post");
            Toast.makeText(getApplicationContext(),"Modificar post existente.",Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else {
            tv_tituloActivityPost.setText("Nuevo Post");
            Toast.makeText(getApplicationContext(),"Crear un nuevo post.",Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private void mostrarPost() {
        jsonLab = JsonLab.get(this);
        User autor = jsonLab.searchUserById(postEditar.getUserId());

        editTextNomAutor.setText(autor.getName());
        editTextTituloPost.setText(postEditar.getTitulo());
        editTextCuerpoPost.setText(postEditar.getCuerpo());

    }

    public void buttonCancelarClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void buttonOkClick(View view) {

        String autor = editTextNomAutor.getText().toString();
        String titulo = editTextTituloPost.getText().toString();
        String cuerpo = editTextCuerpoPost.getText().toString();

        if (autor.isEmpty()) {
            Toast.makeText(this, "Autor no puede estar en blanco", Toast.LENGTH_SHORT).show();
            return;
        }
        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título no puede estar en blanco", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cuerpo.isEmpty()) {
            Toast.makeText(this, "El cuerpo no puede estar en blanco", Toast.LENGTH_SHORT).show();
            return;
        }

        if (activityPostModo == activityPostModo.crear) {

            int newUserId = (listaPosts.size()) + 1;
            String userId = String.valueOf(newUserId);

            Post nuevoPost = new Post(userId,autor, titulo, cuerpo);
            jsonLab.insertPosts(nuevoPost);
            listaPosts.add(nuevoPost);

            Toast.makeText(getApplicationContext(),"Tarea añadida a la BD correctamente.",Toast.LENGTH_SHORT).show();

            Toast.makeText(this, "Evento añadido.", Toast.LENGTH_SHORT).show();
        }
        else {
            //postEditar.modificar(null, autor,titulo, cuerpo);
            jsonLab.updatePosts(postEditar);
            Toast.makeText(getApplicationContext(),"Post modificado correctamente.",Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed(){

        long tiempo = System.currentTimeMillis();
        if (tiempo - tiempoParaSalir > 3000) {
            tiempoParaSalir = tiempo;
            Toast.makeText(this, "Presione de nuevo 'Atrás' si desea salir", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}