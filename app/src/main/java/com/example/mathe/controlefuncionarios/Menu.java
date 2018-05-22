package com.example.mathe.controlefuncionarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mathe.controlefuncionarios.Listas.ListaCargos;
import com.example.mathe.controlefuncionarios.Listas.ListaFuncionarios;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        setTitle("Controle de Funcionários");
    }

    public void onClickListarFuncionarios(View v){
        Intent it = new Intent(this, ListaFuncionarios.class);
        startActivity(it);
    }

    public void onClickListarCargos(View v){
        Intent it = new Intent(this, ListaCargos.class);
        startActivity(it);
    }


}
