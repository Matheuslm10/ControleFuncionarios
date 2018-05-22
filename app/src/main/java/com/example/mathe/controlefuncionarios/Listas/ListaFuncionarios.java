package com.example.mathe.controlefuncionarios.Listas;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mathe.controlefuncionarios.Cadastros.CadastroFuncionario;
import com.example.mathe.controlefuncionarios.BD.DadosOpenHelper;
import com.example.mathe.controlefuncionarios.Objetos.Funcionario;
import com.example.mathe.controlefuncionarios.BD.FuncionarioRepositorio;
import com.example.mathe.controlefuncionarios.Perfis.MostraFuncionario;
import com.example.mathe.controlefuncionarios.R;

import java.util.ArrayList;

public class ListaFuncionarios extends AppCompatActivity {

    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private LinearLayout layoutContentLista;
    private ListView funcionarioList;

    ArrayList<Funcionario> arrayListFuncionario;
    ArrayAdapter<Funcionario> arrayAdapterFuncionario;
    FuncionarioRepositorio repositorioFunc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_funcionarios);
        setTitle("Lista de Funcionários");
        layoutContentLista = findViewById(R.id.layoutContentLista);
        funcionarioList = findViewById(R.id.funcionarioList);
        criarConexao();
        preencheLista();

        funcionarioList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int
                    position, long id) {
                Funcionario funcEnviado = (Funcionario) arrayAdapterFuncionario.getItem(position);
                Intent it = new Intent(ListaFuncionarios.this,MostraFuncionario.class);
                it.putExtra("chave_pessoa", funcEnviado);
                startActivity(it);
            }
        });


    }

    public void preencheLista(){
        arrayListFuncionario = repositorioFunc.buscarTodos();

        if(funcionarioList!=null) {
            arrayAdapterFuncionario = new ArrayAdapter<Funcionario>(ListaFuncionarios.this, android.R.layout.simple_list_item_1, arrayListFuncionario);
            funcionarioList.setAdapter(arrayAdapterFuncionario);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        preencheLista();
    }

    public void onClickAdicionarFuncionario(View v){
        Intent it = new Intent(this, CadastroFuncionario.class);
        startActivity(it);
    }

    public void criarConexao(){
        try{
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            repositorioFunc = new FuncionarioRepositorio(conexao);
        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

}
