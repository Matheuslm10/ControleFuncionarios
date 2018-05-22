package com.example.mathe.controlefuncionarios.Perfis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathe.controlefuncionarios.BD.CargoRepositorio;
import com.example.mathe.controlefuncionarios.BD.DadosOpenHelper;
import com.example.mathe.controlefuncionarios.Objetos.Cargo;
import com.example.mathe.controlefuncionarios.R;

public class MostraCargo extends AppCompatActivity {
    private TextView cargoNomeTextView, quantidade;
    public Cargo cargoSelecionado, cargoEncontrado;
    CargoRepositorio repositorioCarg;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private AlertDialog alerta;
    public int qtdPorCargo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_cargo);
        setTitle("O Cargo");
        atualizarDados();

        final Button altera = findViewById(R.id.btnAlterar);

        altera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MostraCargo.this, com.example.mathe.controlefuncionarios.Cadastros.CadastroCargo.class);
                it.putExtra("cargo_selecionado",cargoSelecionado);
                startActivityForResult(it, 1);
            }
        });

    }

    public void criarConexao(){
        try{
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            repositorioCarg = new CargoRepositorio(conexao);
        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    public void atualizarDados(){
        Intent it = getIntent();
        cargoSelecionado = (Cargo) it.getSerializableExtra("chave_cargo");

        criarConexao();
        cargoEncontrado = repositorioCarg.buscarCargo(cargoSelecionado.cargoId);
        qtdPorCargo = repositorioCarg.consultaQtd(cargoSelecionado.cargoId);
        cargoNomeTextView = findViewById(R.id.cargoNome);
        quantidade = findViewById(R.id.qtdCargo);

        cargoNomeTextView.setText(cargoEncontrado.cargoNome);
        quantidade.setText(Integer.toString(qtdPorCargo));

    }

    public void onClickDeletar(View v){
        verificaDelecao();

    }

    
    
    private void verificaDelecao() {
        if(qtdPorCargo>0){
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle("Ops!");
            builder2.setMessage("Parece que ainda existem funcionários com este cargo na empresa. Por favor, delete esses funcionários ou altere seus cargos.");
            builder2.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //não faz nada
                }
            });
            alerta = builder2.create();
            alerta.show();

        }else if(qtdPorCargo == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atenção!");
            builder.setMessage("Tem certeza que deseja deletar este cargo?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    criarConexao();
                    repositorioCarg.excluir(cargoSelecionado.cargoId);
                    //
                    Toast toast = Toast.makeText(MostraCargo.this, "Cargo deletado com sucesso!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    //
                    finish();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(MostraCargo.this, "não=" + arg1, Toast.LENGTH_SHORT).show();
                }
            });
            alerta = builder.create();
            alerta.show();
        }


        //Cria o gerador do AlertDialog

        //define o titulo

        //define a mensagem

        //define um botão como positivo

        //define um botão como negativo.

        //cria o AlertDialog


    }

    @Override
    protected void onResume() {
        super.onResume();
        onRestart();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                cargoSelecionado = (Cargo) data.getSerializableExtra("cargo_atualizado");

                criarConexao();
                cargoEncontrado = repositorioCarg.buscarCargo(cargoSelecionado.cargoId);

                cargoNomeTextView = findViewById(R.id.cargoNome);

                cargoNomeTextView.setText(cargoEncontrado.cargoNome);

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
