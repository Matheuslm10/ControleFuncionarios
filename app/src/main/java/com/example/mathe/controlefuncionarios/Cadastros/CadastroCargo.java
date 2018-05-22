package com.example.mathe.controlefuncionarios.Cadastros;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mathe.controlefuncionarios.BD.CargoRepositorio;
import com.example.mathe.controlefuncionarios.BD.DadosOpenHelper;
import com.example.mathe.controlefuncionarios.Objetos.Cargo;
import com.example.mathe.controlefuncionarios.R;


public class CadastroCargo extends AppCompatActivity {

    private EditText edtCargoNome;

    private Cargo cargo, cargoSelecionado;
    private CargoRepositorio cargoRepositorio;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cargo);
        setTitle("Cadastro de Cargo");
        cargo = new Cargo();


        //para caso seja alteracao de dados:
        Intent it = getIntent();
        cargoSelecionado = (Cargo) it.getSerializableExtra("cargo_selecionado");

        edtCargoNome = findViewById(R.id.edtCargoNome);

        if(cargoSelecionado != null){
            setTitle("Alteração de Dados");
            edtCargoNome.setText(cargoSelecionado.cargoNome);
            cargo = cargoSelecionado;
        }
        criarConexao();

    }

    public void criarConexao(){
        try{
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();

            cargoRepositorio = new CargoRepositorio(conexao);

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    public void onClickConfirmar(View v){
        if(getTitle()!="Alteração de Dados"){
            if(validaCampos() == false){
                try{
                    cargoRepositorio.inserir(cargo);
                    //
                    Toast toast = Toast.makeText(CadastroCargo.this, "Cargo adicionado com sucesso!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    //
                    finish();

                }catch (SQLException ex){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Erro XXXXXX");
                    dlg.setMessage(ex.getMessage());
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                }
            }
        }else{
            if(validaCampos() == false){
                try{
                    cargoRepositorio.alterar(cargo);
                    //
                    Toast toast = Toast.makeText(CadastroCargo.this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    //
                    onBackPressed();

                }catch (SQLException ex){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Erro XXXXXX");
                    dlg.setMessage(ex.getMessage());
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra("cargo_atualizado", cargo);
        setResult(1, it);
        super.onBackPressed();
    }

    public void onClickCancelar(View v){
        onBackPressed();
    }

    private boolean isCampoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private boolean validaCampos(){
        boolean res = false;

        String cargoNome = edtCargoNome.getText().toString();

        cargo.cargoNome = cargoNome;

        if(res = isCampoVazio(cargoNome)){
            edtCargoNome.requestFocus();
        }

        if(res){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campos em branco!");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }

        return res;
    }
}
