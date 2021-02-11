package com.miguel_lm.appjsondata.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_Add_Post extends AppCompatActivity {

    public static final String PARAM_POST_EDITAR = "param_post_editar";
    public static final String PARAM_MODO = "param_modo";
    private long tiempoParaSalir = 0;
    public enum ActivityPostModo { crear, editar, eliminar, visualizar}
    private ActivityPostModo activityPostModo;
    private Post postEditar;
    private JsonLab jsonLab;
    private EditText editTextTituloPost, editTextCuerpoPost;
    TextView tv_tituloActivityPost;
    Button btn_aceptar, btn_cancelar;
    RequestQueue queue;
    List<User> autoresList;

    private Spinner spinnerAutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__post);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        queue = Volley.newRequestQueue(this);
        jsonLab = JsonLab.get(this);

        spinnerAutor = findViewById(R.id.spinnerAutor);
        editTextTituloPost = findViewById(R.id.ed_tituloPost);
        editTextCuerpoPost = findViewById(R.id.ed_cuerpoPost);
        tv_tituloActivityPost = findViewById(R.id.tv_title_anhadirPost);
        btn_aceptar = findViewById(R.id.btn_aceptarPost);
        btn_cancelar = findViewById(R.id.btn_cancelar);

        activityPostModo = ActivityPostModo.values()[getIntent().getIntExtra(PARAM_MODO, ActivityPostModo.crear.ordinal())];
        postEditar = (Post) getIntent().getSerializableExtra(PARAM_POST_EDITAR);

        // Configurar spinner, mostrar el listado de autores
        configurarSpinner();

        if (activityPostModo == ActivityPostModo.editar) {
            mostrarPost();
            tv_tituloActivityPost.setText("Modificar Post");
            btn_aceptar.setText("Modificar");
        }
        else if (activityPostModo == ActivityPostModo.crear){
            tv_tituloActivityPost.setText("Nuevo Post");
            btn_aceptar.setText("Añadir");
        }
        else if (activityPostModo == ActivityPostModo.eliminar) {
            mostrarPost();
            tv_tituloActivityPost.setText("Eliminar Post");
            btn_aceptar.setText("Eliminar");
        }
        else if (activityPostModo == ActivityPostModo.visualizar) {
            mostrarPost();
            tv_tituloActivityPost.setText("Info Post");
            btn_cancelar.setVisibility(View.GONE);
            btn_aceptar.setText("Aceptar");
        }
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /** Configura un spinner para mostrar el listado de nombres de usuarios */
    private void configurarSpinner() {

        autoresList = jsonLab.getUsers();
        List<String> nombresAutores = new ArrayList();
        for (User autor : autoresList)
            nombresAutores.add(autor.getName());
        ArrayAdapter arrayAdapterAutores = new ArrayAdapter(this,android.R.layout.simple_spinner_item, nombresAutores);
        spinnerAutor.setAdapter(arrayAdapterAutores);

        // Si se está en modo edición, o info, o eliminar, hay que mostrar el autor del post
        if (activityPostModo != ActivityPostModo.crear) {

            // Hay que saber qué indice se corresponde con el id del autor
            int indiceAutor = -1;
            for (int i=0 ; i<autoresList.size() ; i++)
                if (autoresList.get(i).getId() == postEditar.getUserId())
                    indiceAutor = i;

            if (indiceAutor != -1)
                spinnerAutor.setSelection(indiceAutor);
        }
    }

    private void mostrarPost() {
        jsonLab = JsonLab.get(this);
        User autor = jsonLab.searchUserById(postEditar.getUserId());

        editTextTituloPost.setText(postEditar.getTitulo());
        editTextCuerpoPost.setText(postEditar.getCuerpo());

    }

    public void buttonCancelarClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    public void buttonOkClick(View view) {

        String titulo = editTextTituloPost.getText().toString();
        String cuerpo = editTextCuerpoPost.getText().toString();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título no puede estar en blanco", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cuerpo.isEmpty()) {
            Toast.makeText(this, "El cuerpo no puede estar en blanco", Toast.LENGTH_SHORT).show();
            return;
        }

        // Coger el índice del autor seleccionado en el spinner
        int indiceAutorSeleccionado = spinnerAutor.getSelectedItemPosition();
        int userId = autoresList.get(indiceAutorSeleccionado).getId();


        // MODO CREAR /////////////////////////////////////////////////////////////////
        if (activityPostModo == ActivityPostModo.crear) {

            Post nuevoPost = new Post(userId, titulo, cuerpo);
            jsonLab.insertPosts(nuevoPost);

            setResult(RESULT_OK);

        }
        // MODO EDITAR /////////////////////////////////////////////////////////////////
        else if (activityPostModo == ActivityPostModo.editar) {
            postEditar.modificar(userId, titulo, cuerpo);
            jsonLab.updatePosts(postEditar);
            Toast.makeText(getApplicationContext(),"Post modificado correctamente.",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);

        }
        // MODO ELIMINAR /////////////////////////////////////////////////////////////////
        else if (activityPostModo == ActivityPostModo.eliminar) {
            jsonLab.deletePosts(postEditar);
            Toast.makeText(getApplicationContext(),"Post eliminado correctamente",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);

        }
        // MODO VISUALIZAR /////////////////////////////////////////////////////////////////
        else if (activityPostModo == ActivityPostModo.visualizar) {
            // No hay que hacer nada en este caso
        }

        finish();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void enviarDatosPosts() {

        String url_posts = "https://jsonplaceholder.typicode.com/posts?_end=50";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url_posts, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int autor = (int) spinnerAutor.getSelectedItemId();
                    String titulo = editTextTituloPost.getText().toString();
                    String cuerpo = editTextCuerpoPost.getText().toString();

                    response.put("userId", autor);
                    response.put("title", titulo);
                    response.put("body", cuerpo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Activity_Add_Post.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void modificarDatosPosts() {

        String url_posts = "https://jsonplaceholder.typicode.com/posts?_end=50";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url_posts, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int autor = (int) spinnerAutor.getSelectedItemId();
                    String titulo = editTextTituloPost.getText().toString();
                    String cuerpo = editTextCuerpoPost.getText().toString();

                    response.put("userId", autor);
                    response.put("title", titulo);
                    response.put("body", cuerpo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Activity_Add_Post.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void eliminarDatosPosts() {

        String url_posts = "https://jsonplaceholder.typicode.com/posts?_end=50";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url_posts, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int autor = (int) spinnerAutor.getSelectedItemId();
                    String titulo = editTextTituloPost.getText().toString();
                    String cuerpo = editTextCuerpoPost.getText().toString();

                    response.put("userId", autor);
                    response.put("title", titulo);
                    response.put("body", cuerpo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Activity_Add_Post.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    public void onBackPressed(){

        long tiempo = System.currentTimeMillis();
        if (tiempo - tiempoParaSalir > 3000) {
            tiempoParaSalir = tiempo;
            Toast.makeText(this, "Presione de nuevo 'Atrás' si desea salir", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }
}