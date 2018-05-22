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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mathe.controlefuncionarios.BD.CargoRepositorio;
import com.example.mathe.controlefuncionarios.BD.DadosOpenHelper;
import com.example.mathe.controlefuncionarios.BD.FuncionarioRepositorio;
import com.example.mathe.controlefuncionarios.Objetos.Cargo;
import com.example.mathe.controlefuncionarios.Objetos.Funcionario;
import com.example.mathe.controlefuncionarios.R;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

public class CadastroFuncionario extends AppCompatActivity implements OnItemSelectedListener {

    private EditText edtNome;
    private EditText edtSalario;
    private EditText edtCargo;

    private Funcionario funcionario, selectedFunc;
    private FuncionarioRepositorio funcionarioRepositorio;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private CargoRepositorio cargoRepositorio;
    private String cargoSelecionado = "";
    private boolean algoMudou = false;

    private Spinner sp;
    ArrayList<String> cargos = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_funcionario);
        setTitle("Cadastro de Funcionário");
        funcionario = new Funcionario();

        /////preenchendo spinner (menu dropdown)
        sp = (Spinner) findViewById(R.id.spinner1);
        sp.setOnItemSelectedListener(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,cargos);
        conectaCargoBD();
        ArrayList<Cargo> arrayCargos = cargoRepositorio.buscarTodos();
        for(Cargo c : arrayCargos) {
            cargos.add(c.cargoNome);
        }
        sp.setAdapter(adapter);
        /////

        //para caso seja alteracao de dados:
        Intent it = getIntent();
        selectedFunc = (Funcionario) it.getSerializableExtra("funcionario_selecionado");

        edtNome = findViewById(R.id.edtNome);
        edtSalario = findViewById(R.id.edtSalario);


        if(selectedFunc != null){
            setTitle("Alteração de Dados");
            edtNome.setText(selectedFunc.funcNome);
            edtSalario.setText(selectedFunc.salario);
            //aqui remove o cargo da onde ele ta, para colocar no começo da lista, para que o usuario entenda que eh o cargo atual.
            String nomeDoCargo = (cargoRepositorio.buscarCargo(selectedFunc.cargoId)).cargoNome;
            cargos.remove(nomeDoCargo);
            cargos.add(0, nomeDoCargo);
            sp.setAdapter(adapter);
            //
            funcionario = selectedFunc;
        }
        criarConexao();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String carg = (String) parent.getItemAtPosition(pos);
        this.cargoSelecionado = carg;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public int getCargoSelecionado(){
        int idEncontrado = cargoRepositorio.buscaPorNome(cargoSelecionado);

        return idEncontrado;
    }

    public void conectaCargoBD(){
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

    public void criarConexao(){
        try{
            dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            funcionarioRepositorio = new FuncionarioRepositorio(conexao);

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
                    funcionarioRepositorio.inserir(funcionario);
                    //
                    Toast toast = Toast.makeText(CadastroFuncionario.this, "Funcionário adicionado com sucesso!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    //
                    finish();

                }catch (SQLException ex){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Erro: não foi possível adicionar o funcionário!");
                    dlg.setMessage(ex.getMessage());
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                }
            }
        }else{
            if(validaCampos() == false){
                try{
                    funcionarioRepositorio.alterar(funcionario);
                    algoMudou = true;
                    //
                    Toast toast = Toast.makeText(CadastroFuncionario.this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    //
                    onBackPressed();

                }catch (SQLException ex){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                    dlg.setTitle("Erro: não foi possível alterar os dados!");
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
        it.putExtra("funcionario_atualizado", funcionario);
        setResult(1, it);

        if(algoMudou){
            setResult(1, it);
        }else{
            setResult(2, it);
        }
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

        String funcNome = edtNome.getText().toString();
        String salario = edtSalario.getText().toString();
        int cargoId = getCargoSelecionado();

        funcionario.funcNome = funcNome;
        funcionario.cargoId = cargoId;
        funcionario.salario = salario;

        if(res = isCampoVazio(funcNome)){
            edtNome.requestFocus();
        }else if(res = isCampoVazio(salario)){
            edtSalario.requestFocus();
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
