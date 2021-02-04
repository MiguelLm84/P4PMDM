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
import com.miguel_lm.appjsondata.modelo.Posts;
import com.miguel_lm.appjsondata.modelo.Users;

import java.util.List;

public class Activity_Info_Detalle extends AppCompatActivity {

    private long tiempoParaSalir = 0;
    TextView tv_nombre, tv_nickname, tv_email, tv_telefono, tv_company;
    Button btn_aceptar;
    JsonLab jsonLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__info__detalle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.json);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        /*JsonLab.get(this);
        List<Users> listaUsersBD =  jsonLab.getUsers();
        List<Posts> listaPostsBD =  jsonLab.getPosts();*/

        tv_nombre = findViewById(R.id.tv_titulo_nombre_completo);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_email = findViewById(R.id.tv_email);
        tv_telefono = findViewById(R.id.tv_telefono);
        tv_company = findViewById(R.id.tv_company);
        btn_aceptar = findViewById(R.id.btn_aceptar);

        /*tv_nombre.setText();
        tv_nickname.setText();
        tv_email.setText();
        tv_telefono.setText();
        tv_company.setText();*/

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
}