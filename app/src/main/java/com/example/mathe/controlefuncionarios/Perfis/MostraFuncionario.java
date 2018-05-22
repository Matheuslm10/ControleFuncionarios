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
import com.example.mathe.controlefuncionarios.BD.FuncionarioRepositorio;
import com.example.mathe.controlefuncionarios.Cadastros.CadastroFuncionario;
import com.example.mathe.controlefuncionarios.Objetos.Funcionario;
import com.example.mathe.controlefuncionarios.R;

public class MostraFuncionario extends AppCompatActivity {
    private TextView funcionarioNome, funcionarioCargo, funcionarioSalario;
    public Funcionario selectedFunc, funcionarioEncontrado;
    FuncionarioRepositorio repositorioFunc;
    private SQLiteDatabase conexao;
    private DadosOpenHelper dadosOpenHelper;
    private AlertDialog alerta;
    public boolean primeiraVez = true;
    CargoRepositorio cargoRepositorio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_funcionario);
        setTitle("Perfil de Funcionário");
        //if(primeiraVez){
        //    atualizarDados();
        //}
        atualizarDados();

        final Button altera = findViewById(R.id.btnAlterar);

        altera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MostraFuncionario.this, CadastroFuncionario.class);
                it.putExtra("funcionario_selecionado",selectedFunc);
                startActivityForResult(it, 1);
            }
        });

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
            repositorioFunc = new FuncionarioRepositorio(conexao);
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
        selectedFunc = (Funcionario) it.getSerializableExtra("chave_pessoa");

        criarConexao();
        funcionarioEncontrado = repositorioFunc.buscarFuncionario(selectedFunc.funcId);

        funcionarioNome = findViewById(R.id.funcionarioNome);
        funcionarioCargo = findViewById(R.id.funcionarioCargo);
        funcionarioSalario = findViewById(R.id.funcionarioSalario);

        conectaCargoBD();
        funcionarioNome.setText(funcionarioEncontrado.funcNome);
        funcionarioCargo.setText((cargoRepositorio.buscarCargo(funcionarioEncontrado.cargoId)).cargoNome);
        funcionarioSalario.setText(funcionarioEncontrado.salario);

    }

    public void onClickDeletar(View v){
        verificaDelecao();

    }

    private void verificaDelecao() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção!");
        //define a mensagem
        builder.setMessage("Tem certeza que deseja deletar este funcionário?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                criarConexao();
                repositorioFunc.excluir(selectedFunc.funcId);
                //
                Toast toast = Toast.makeText(MostraFuncionario.this, "Funcionário deletado com sucesso!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                //
                finish();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MostraFuncionario.this, "não=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRestart();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                selectedFunc = (Funcionario) data.getSerializableExtra("funcionario_atualizado");

                criarConexao();
                funcionarioEncontrado = repositorioFunc.buscarFuncionario(selectedFunc.funcId);

                funcionarioNome = findViewById(R.id.funcionarioNome);
                funcionarioCargo = findViewById(R.id.funcionarioCargo);
                funcionarioSalario = findViewById(R.id.funcionarioSalario);

                conectaCargoBD();
                funcionarioNome.setText(funcionarioEncontrado.funcNome);
                funcionarioCargo.setText((cargoRepositorio.buscarCargo(funcionarioEncontrado.cargoId)).cargoNome);
                funcionarioSalario.setText(funcionarioEncontrado.salario);

                break;
            case 2:
                atualizarDados();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    */
}
