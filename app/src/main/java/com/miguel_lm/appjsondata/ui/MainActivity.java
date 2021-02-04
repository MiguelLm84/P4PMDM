package com.miguel_lm.appjsondata.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Posts;
import com.miguel_lm.appjsondata.modelo.Users;
import com.miguel_lm.appjsondata.ui.fragments.Fragment_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ListenerPost {

    Toolbar toolbar;
    private long tiempoParaSalir = 0;
    private static final int REQUEST_NUEVO_POST = 1234;
    public static final String LOG_TAG = "log_json";
    TextView tv_json;
    RequestQueue queue;

    private Fragment_List fragLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rotation_lock);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        queue = Volley.newRequestQueue(this);
        conectividad();

        fragLista = new Fragment_List();
        getSupportFragmentManager().beginTransaction().add(R.id.ContenedorFragments, fragLista).commit();

        recuperacionDeDatosUser();
        recuperacionDeDatosPost();

        handleIntent(getIntent());

    }

    /**
     * Evento que se dispara cuando se realiza una búsqueda en el actionbar
     */
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            JsonLab jsonLab = JsonLab.get(MainActivity.this);
            List<Posts> listadoPostsEncontrados = jsonLab.searchPosts(query);

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

                Toast.makeText(MainActivity.this, "Usuarios añadidos correctamente a la BD", Toast.LENGTH_SHORT).show();

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

                Toast.makeText(MainActivity.this, "Posts añadidos correctamente a la BD", Toast.LENGTH_SHORT).show();

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

    private void anhadirDatosPosts() {

        Intent intentNuevaTarea = new Intent(this, Activity_Add_Post.class);
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVO_POST);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void modificarDatosPosts() {

        Intent intentNuevaTarea = new Intent(this, Activity_Add_Post.class);
        startActivityForResult(intentNuevaTarea, REQUEST_NUEVO_POST);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void eliminarDatosPosts() {

    }

    public void buttonFabAdd(View view) {
        anhadirDatosPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.accionAnhadirPost) {
            anhadirDatosPosts();
        } else if (item.getItemId() == R.id.accionModificarPost) {
            modificarDatosPosts();
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

            Users user = new Users(id, nom, username, email, phone, company);

            jsonLab.insertUsers(user);

            //   Log.d(LOG_TAG, "Usuario " + nom + " añadido");
                        /*Toast.makeText(MainActivity.this,"USER\n\n·ID: "+ id + "\n\n"+ "·NOMBRE: "+ nom + "\n\n"+
                                "·NICK: "+ username + "\n\n"+ "·EMAIL: "+ email + "\n\n"+
                                "·TELÉFONO: "+ phone + "\n\n"+ "·EMPRESA: \n\n"+ company + "\n\n",Toast.LENGTH_SHORT).show();*/

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

            Posts post = new Posts(userId, id, titulo, cuerpo);
            jsonLab.insertPosts(post);

                      /*  Toast.makeText(MainActivity.this,"POSTS\n\n"+ "·USERID: "+ userId + "\n\n" +
                                "·ID: "+id + "\n\n" + "·TITULO:\n" + titulo + "\n\n" +
                                "·CUERPO:\n" + cuerpo,Toast.LENGTH_SHORT).show();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void infoAutor(Posts post) {

    }

    @Override
    public void anhadirPosts(Posts post) {
        anhadirDatosPosts();
    }

    @Override
    public void modificarPosts(Posts post) {
        modificarDatosPosts();

    }

    @Override
    public void eliminarPosts(Posts post) {
        eliminarDatosPosts();

    }
}
