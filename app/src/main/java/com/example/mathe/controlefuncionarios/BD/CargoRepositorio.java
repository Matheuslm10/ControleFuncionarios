package com.example.mathe.controlefuncionarios.BD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.mathe.controlefuncionarios.Objetos.Cargo;
import com.example.mathe.controlefuncionarios.Objetos.Funcionario;
import com.example.mathe.controlefuncionarios.Perfis.MostraCargo;

import java.util.ArrayList;

/**
 * Created by mathe on 05/05/2018.
 */

public class CargoRepositorio {

    private SQLiteDatabase conexao;

    public CargoRepositorio(SQLiteDatabase conexao){
        this.conexao = conexao;
    }

    public void inserir(Cargo cargo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("cargoNome", cargo.cargoNome);

        conexao.insertOrThrow("cargo", null, contentValues);

    }
    public void excluir(int cargoId){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cargoId);

        conexao.delete("cargo", "cargoId = ?", parametros);
    }
    public void alterar(Cargo cargo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("cargoNome", cargo.cargoNome);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cargo.cargoId);

        conexao.update("cargo", contentValues, "cargoId = ?", parametros);
    }

    public ArrayList<Cargo> buscarTodos(){
        ArrayList<Cargo> cargos = new ArrayList<Cargo>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cargoId, cargoNome FROM cargo;");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            do{
                Cargo cargo = new Cargo();

                cargo.cargoId = resultado.getInt( resultado.getColumnIndexOrThrow("cargoId"));
                cargo.cargoNome = resultado.getString(resultado.getColumnIndexOrThrow("cargoNome"));
                cargos.add(cargo);

            }while(resultado.moveToNext());
        }
        return cargos;
    }

    public Cargo buscarCargo(int cargoId){
        Cargo cargo = new Cargo();

        String where = "cargoId = "+cargoId;
        String[] colunas = {"cargoId", "cargoNome"};

        Cursor resultado = conexao.query("cargo", colunas, where, null, null, null, null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();

            cargo.cargoId = resultado.getInt(resultado.getColumnIndexOrThrow("cargoId"));
            cargo.cargoNome = resultado.getString(resultado.getColumnIndexOrThrow("cargoNome"));

            return cargo;
        }
        return null;
    }

    public int buscaPorNome(String nomeDoCargo){
        int idEncontrado;

        String sql = "select cargoId from cargo where cargo.cargoNome = '"+nomeDoCargo+"';";
        Cursor resultado = conexao.rawQuery(sql, null);
        resultado.moveToFirst();
        idEncontrado = resultado.getInt(resultado.getColumnIndexOrThrow("cargoId"));
        return idEncontrado;
    }

    public int consultaQtd(int cargoId){
        StringBuilder sql = new StringBuilder();
        sql.append("	select count(funcionario.cargoId) as 'qtd'                                  ");
        sql.append("	from cargo join funcionario                                                 ");
        sql.append("	where cargo.cargoId = funcionario.cargoId and cargo.cargoId = "+cargoId+";  ");

        int qtd = 0;
        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        resultado.moveToFirst();
        qtd = resultado.getInt(resultado.getColumnIndexOrThrow("qtd"));
        return qtd;
    }
}
