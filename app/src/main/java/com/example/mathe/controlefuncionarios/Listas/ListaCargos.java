package com.example.mathe.controlefuncionarios.Listas;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mathe.controlefuncionarios.BD.CargoRepositorio;
import com.example.mathe.controlefuncionarios.BD.DadosOpenHelper;
import com.example.mathe.controlefuncionarios.Objetos.Cargo;
import com.example.mathe.controlefuncionarios.Perfis.MostraCargo;
import com.example.mathe.controlefuncionarios.R;
import java.util.ArrayList;

public class ListaCargos extends AppCompatActivity {

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private LinearLayout layoutContentLista;
    private ListView cargoList;

    ArrayList<Cargo> arrayListCargo;
    ArrayAdapter<Cargo> arrayAdapterCargo;
    CargoRepositorio repositorioCargo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cargos);
        setTitle("Lista de Cargos");
        layoutContentLista = findViewById(R.id.layoutContentLista);
        cargoList = findViewById(R.id.cargoList);
        criarConexao();
        preencheLista();

        cargoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int
                    position, long id) {
                Cargo cargoEnviado = arrayAdapterCargo.getItem(position);
                Intent it = new Intent(ListaCargos.this, MostraCargo.class);
                it.putExtra("chave_cargo", cargoEnviado);
                startActivity(it);
            }
        });


    }

    public void preencheLista(){
        arrayListCargo = repositorioCargo.buscarTodos();

        if(cargoList !=null) {
            arrayAdapterCargo = new ArrayAdapter<Cargo>(ListaCargos.this, android.R.layout.simple_list_item_1, arrayListCargo);
            cargoList.setAdapter(arrayAdapterCargo);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        preencheLista();
    }

    public void onClickAdicionarCargo(View v){
        Intent it = new Intent(this, com.example.mathe.controlefuncionarios.Cadastros.CadastroCargo.class);
        startActivity(it);
    }

    public void criarConexao(){
        try{
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            repositorioCargo = new CargoRepositorio(conexao);
        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

}

