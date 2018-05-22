package com.example.mathe.controlefuncionarios.BD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mathe.controlefuncionarios.Objetos.Funcionario;

import java.util.ArrayList;

/**
 * Created by mathe on 05/05/2018.
 */

public class FuncionarioRepositorio {

    private SQLiteDatabase conexao;

    public FuncionarioRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(Funcionario funcionario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("funcNome", funcionario.funcNome);
        contentValues.put("cargoId", funcionario.cargoId);
        contentValues.put("salario", funcionario.salario);

        conexao.insertOrThrow("funcionario", null, contentValues);

    }

    public void excluir(int funcId){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(funcId);

        conexao.delete("funcionario", "funcId = ?", parametros);
    }
    public void alterar(Funcionario funcionario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("funcNome", funcionario.funcNome);
        contentValues.put("cargoId", funcionario.cargoId);
        contentValues.put("salario", funcionario.salario);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(funcionario.funcId);

        conexao.update("funcionario", contentValues, "funcId = ?", parametros);
    }

    public ArrayList<Funcionario> buscarTodos(){
        ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT funcId, cargoId, funcNome, salario FROM funcionario;");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            do{
                Funcionario func = new Funcionario();

                func.funcId = resultado.getInt( resultado.getColumnIndexOrThrow("funcId"));
                func.funcNome = resultado.getString(resultado.getColumnIndexOrThrow("funcNome"));
                func.cargoId = resultado.getInt(resultado.getColumnIndexOrThrow("cargoId"));
                func.salario = resultado.getString(resultado.getColumnIndexOrThrow("salario"));

                funcionarios.add(func);
            }while(resultado.moveToNext());
        }
        return funcionarios;
    }

    public Funcionario buscarFuncionario(int funcId){
        Funcionario funcionario = new Funcionario();

        //StringBuilder sql = new StringBuilder();
        //sql.append(" SELECT funcId, funcNome, cargoId, salario FROM funcionario WHERE funcId = ?");

        String where = "funcId = "+funcId;
        String[] colunas = {"funcId", "cargoId", "funcNome", "salario"};
        //String[] parametros = new String[1];
        //parametros[0] = String.valueOf(funcId);

        Cursor resultado = conexao.query("funcionario", colunas, where, null, null, null, null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();

            funcionario.funcId = resultado.getInt(resultado.getColumnIndexOrThrow("funcId"));
            funcionario.funcNome = resultado.getString(resultado.getColumnIndexOrThrow("funcNome"));
            funcionario.cargoId = resultado.getInt(resultado.getColumnIndexOrThrow("cargoId"));
            funcionario.salario = resultado.getString(resultado.getColumnIndexOrThrow("salario"));

            return funcionario;
        }
        return null;
    }
}
