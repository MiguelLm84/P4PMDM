package com.miguel_lm.appjsondata.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;
import com.miguel_lm.appjsondata.ui.adaptador.AdapterPosts;
import com.miguel_lm.appjsondata.ui.fragments.Fragment_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListenerPost {

    private long tiempoParaSalir = 0;
    private static final int REQUEST_NUEVO_POST = 1234;
    private Post postAmodificar;
    public static final String LOG_TAG = "log_json";
    TextView tv_json;
    RequestQueue queue;

    private Fragment_List fragLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.code_json_icon_136758);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        queue = Volley.newRequestQueue(this);
        conectividad();

        recuperacionDeDatosUser();
        recuperacionDeDatosPost();

        fragLista = new Fragment_List();
        getSupportFragmentManager().beginTransaction().add(R.id.ContenedorFragments, fragLista).commit();


        handleIntent(getIntent());
    }

        /**
         * Evento que se dispara cuando se realiza una búsqueda en el actionbar
         */
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            JsonLab jsonLab = JsonLab.get(MainActivity.this);
            List<Post> listadoPostsEncontrados = jsonLab.searchPosts(query);

            fragLista.setListaPosts(listadoPostsEncontrados);
        }
    }

    private void conectividad() {

        Log.d(LOG_TAG, "Comprobando conexión");
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean connected = (networkInfo != null && networkInfo.isConnected());

        if (!connected) {
            Toast.makeText(MainActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NUEVO_POST && resultCode == RESULT_OK) {
            //actualizarListado();
        }
    }

    private void recuperacionDeDatosUser() {

        String url_users = "https://jsonplaceholder.typicode.com/users?_end=5";

        JsonArrayRequest requestUser = new JsonArrayRequest(Request.Method.GET, url_users, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        parsearUsuario(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(requestUser);
    }

    private void recuperacionDeDatosPost() {

        String url_posts = "https://jsonplaceholder.typicode.com/posts?_end=50";

        JsonArrayRequest requestPost = new JsonArrayRequest(Request.Method.GET, url_posts, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        parsearPost(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(requestPost);
    }

    private void enviarDatosPosts() {

        String url_posts = "https://jsonplaceholder.typicode.com/posts?_end=50";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url_posts, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    String nom = tv_json.getText().toString();
                    response.put("nombre", nom);
                    Toast.makeText(MainActivity.this, "El nombre '" + nom + "' ha sido añadido", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error, no se ha podido conectar a la url solicitada", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    public void buttonFabAdd(View view) {

        Intent intentNuevaTarea = new Intent(this, Activity_Add_Post.class);
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVO_POST);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void crearOmodificarPosts(final Post postAmodificar) {

        Intent intentNuevaTarea = new Intent(this, Activity_Add_Post.class);
        intentNuevaTarea.putExtra(Activity_Add_Post.PARAM_POST_EDITAR, postAmodificar);
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVO_POST);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void accionEscogerYModificar() {

        JsonLab jsonLab = JsonLab.get(this);
        List<Post> listaPosts = jsonLab.getPosts();

        AlertDialog.Builder builderDialogEscogerTareas = new AlertDialog.Builder(this);
        builderDialogEscogerTareas.setIcon(R.drawable.editar);
        builderDialogEscogerTareas.setTitle("Modificar Tarea");

        final String[] arrayTareasAMostrar = new String[listaPosts.size()];
        for (int i = 0; i < listaPosts.size(); i++) {
            User autor = jsonLab.searchUserById(listaPosts.get(i).getUserId());
            arrayTareasAMostrar[i] = "\n· TITULO: " + listaPosts.get(i).getTitulo() + "\n\n· AUTOR:  " +autor.getName() + "\n";
        }
        builderDialogEscogerTareas.setSingleChoiceItems(arrayTareasAMostrar, -1, (dialog, posicionElementoSeleccionado) -> postAmodificar = listaPosts.get(posicionElementoSeleccionado));
        builderDialogEscogerTareas.setPositiveButton("Modificar", (dialog, i) -> {

            if (postAmodificar == null) {
                Toast.makeText(getApplicationContext(), "Debe escoger un post", Toast.LENGTH_SHORT).show();
            } else {
                crearOmodificarPosts(postAmodificar);
            }
        });
        builderDialogEscogerTareas.setNegativeButton("Cancelar", null);
        builderDialogEscogerTareas.create().show();
    }

    private void eliminarDatosPosts() {

        JsonLab jsonLab = JsonLab.get(MainActivity.this);
        List<Post> listaPosts = jsonLab.getPosts();

        AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
        builderEliminar.setIcon(R.drawable.eliminar__2_);
        builderEliminar.setTitle("Eliminar elementos");

        final ArrayList<String> listaPostsAeliminar = new ArrayList<>();
        String[] arrayTareas = new String[listaPosts.size()];
        final boolean[] postsSeleccionados = new boolean[listaPosts.size()];
        for (int i = 0; i < listaPosts.size(); i++) {
            User autor = jsonLab.searchUserById(listaPosts.get(i).getUserId());
            arrayTareas[i] = "\n· TITULO: " + listaPosts.get(i).getTitulo() + "\n\n· AUTOR:  " + autor.getName()+"\n";
        }
        builderEliminar.setMultiChoiceItems(arrayTareas, postsSeleccionados, (dialog, indiceSeleccionado, isChecked) -> {
            postsSeleccionados[indiceSeleccionado] = isChecked;
            User autor = jsonLab.searchUserById(listaPosts.get(indiceSeleccionado).getUserId());
            String postsParaEliminar = "\n· TITULO: " + listaPosts.get(indiceSeleccionado).getTitulo() + "\n\n· AUTOR:  " + autor.getName() + "\n";
            listaPostsAeliminar.add(postsParaEliminar);
        });

        builderEliminar.setPositiveButton("Borrar", (dialog, which) -> {

            AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(this);
            builderEliminar_Confirmar.setIcon(R.drawable.correo_no_deseado);
            builderEliminar_Confirmar.setTitle("¿Eliminar los elementos?");
            String postsPorBorrar = null;

            if(listaPostsAeliminar.isEmpty()){
                Toast.makeText(getApplicationContext(), "Debe escoger una elemento", Toast.LENGTH_SHORT).show();
                eliminarDatosPosts();
                return;
            }

            for (int i = 0; i < listaPostsAeliminar.size(); i++) {
                postsPorBorrar = listaPostsAeliminar.get(i);
            }
            builderEliminar_Confirmar.setMessage(postsPorBorrar);
            builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
            builderEliminar_Confirmar.setPositiveButton("Borrar", (dialogInterface, which1) -> {

                for (int i = listaPosts.size() - 1; i >= 0; i--) {
                    if (postsSeleccionados[i]) {
                        listaPosts.remove(i);
                        JsonLab.get(this).deletePosts(listaPosts.get(i));
                    }
                }
                fragLista.setListaPosts(listaPosts);
                Toast.makeText(getApplicationContext(), "Tareas eliminadas correctamente.", Toast.LENGTH_SHORT).show();
            });
            builderEliminar_Confirmar.create().show();
            dialog.dismiss();
        });

        builderEliminar.setNegativeButton("Cancelar", null);
        builderEliminar.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                List<Post> lista = null;
                AdapterPosts adapterPosts = new AdapterPosts(MainActivity.this, lista, MainActivity.this);
                adapterPosts.getFilter().filter(texto);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.accionAnhadirPost) {
            buttonFabAdd(null);
        } else if (item.getItemId() == R.id.accionModificarPost) {
            accionEscogerYModificar();
        } else if (item.getItemId() == R.id.accionEliminarPost) {
            eliminarDatosPosts();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        long tiempo = System.currentTimeMillis();
        if (tiempo - tiempoParaSalir > 3000) {
            tiempoParaSalir = tiempo;
            Toast.makeText(this, "Presione de nuevo 'Atrás' si desea salir", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private void parsearUsuario(JSONObject response) {

        JsonLab jsonLab = JsonLab.get(MainActivity.this);

        try {
            String id = response.getString("id");
            String nom = response.getString("name");
            String username = response.getString("username");
            String email = response.getString("email");
            String phone = response.getString("phone");

            JSONObject jsonObj = response.getJSONObject("company");
            String company = jsonObj.getString("name");

            User user = new User(id, nom, username, email, phone, company);

            jsonLab.insertUsers(user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsearPost(JSONObject response) {
        JsonLab jsonLab = JsonLab.get(MainActivity.this);

        try {
            String userId = response.getString("userId");
            String id = response.getString("id");
            String titulo = response.getString("title");
            String cuerpo = response.getString("body");

            Post post = new Post(userId, id, titulo, cuerpo);
            jsonLab.insertPosts(post);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seleccionarPost(Post post) {

    }

    @Override
    public void modificarPosts(Post post) {

        crearOmodificarPosts(post);
    }

    @Override
    public void eliminarPosts(Post post) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View dialogLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity__add__post, null);
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();

        JsonLab jsonLab = JsonLab.get(MainActivity.this);
        User autor = jsonLab.searchUserById(post.getUserId());

        final EditText ed_autor = dialogLayout.findViewById(R.id.ed_nombre_autor);
        final EditText ed_titulo = dialogLayout.findViewById(R.id.ed_tituloPost);
        final EditText ed_cuerpo = dialogLayout.findViewById(R.id.ed_cuerpoPost);
        final Button btnAceptar = dialogLayout.findViewById(R.id.btn_aceptar);
        final Button btnCancelar = dialogLayout.findViewById(R.id.btn_cancelar);

        ed_autor.setText(autor.getName());
        ed_titulo.setText(String.valueOf(post.getTitulo()));
        ed_cuerpo.setText(String.valueOf(post.getCuerpo()));


        btnAceptar.setOnClickListener(new View.OnClickListener() {

            List<Post> listaPosts = jsonLab.getPosts();

            @Override
            public void onClick(View v) {
                jsonLab.deletePosts(post);
                listaPosts.remove(post);

                //adapterPosts.actualizarListado();
                //MainActivity.this.adapterPosts.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Post eliminado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void botonReset(View view) {

        this.deleteDatabase("JsonData");
        conectividad();
        recuperacionDeDatosUser();
        recuperacionDeDatosPost();
    }
}
