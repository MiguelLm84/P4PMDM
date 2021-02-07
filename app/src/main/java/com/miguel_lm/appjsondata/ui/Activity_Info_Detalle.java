package com.miguel_lm.appjsondata.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.JsonLab;
import com.miguel_lm.appjsondata.modelo.Post;
import com.miguel_lm.appjsondata.modelo.User;

import java.util.List;

public class Activity_Info_Detalle extends AppCompatActivity implements ListenerPost {

    private long tiempoParaSalir = 0;
    TextView tv_nombre, tv_nickname, tv_email, tv_telefono, tv_company;
    Button btn_aceptar;
    JsonLab jsonLab;
    private Post postInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__info__detalle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.code_json_icon_136758);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        jsonLab = JsonLab.get(this);
        List<Post> listaPosts =  jsonLab.getPosts();

        tv_nombre = findViewById(R.id.tv_nombre_completo);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_email = findViewById(R.id.tv_email);
        tv_telefono = findViewById(R.id.tv_telefono);
        tv_company = findViewById(R.id.tv_company);
        btn_aceptar = findViewById(R.id.btn_aceptar);

        seleccionarPost(postInfo);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Info_Detalle.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
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

    @Override
    public void seleccionarPost(Post post) {

        User autor = jsonLab.searchUserById(post.getUserId());

        tv_nombre.setText(autor.getName());
        tv_nickname.setText(autor.getNickname());
        tv_email.setText(autor.getEmail());
        tv_telefono.setText(autor.getPhone());
        tv_company.setText(autor.getCompany());
    }

    @Override
    public void modificarPosts(Post post) {

    }

    @Override
    public void eliminarPosts(Post post) {

    }
}