package com.example.mathe.controlefuncionarios.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by mathe on 05/05/2018.
 */

public class DadosOpenHelper extends SQLiteOpenHelper {


    public String getCreateTableFuncionario(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS funcionario(            		        	   ");
        sql.append("		funcId INTEGER PRIMARY KEY AUTOINCREMENT,       		           ");
        sql.append("		cargoId INTEGER REFERENCES cargo (cargoId) NOT NULL DEFAULT (''),  ");
        sql.append("		funcNome VARCHAR(200) NOT NULL DEFAULT (''),    		           ");
        sql.append("		salario VARCHAR(200) NOT NULL DEFAULT ('')    			           ");
        sql.append("	)                                                   		      	   ");

        return sql.toString();
    }

    public String getCreateTableCargo(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS cargo(                    ");
        sql.append("		cargoId INTEGER PRIMARY KEY AUTOINCREMENT,       ");
        sql.append("		cargoNome VARCHAR(200) NOT NULL DEFAULT ('')     ");
        sql.append("	)                                                    ");

        return sql.toString();
    }

    public DadosOpenHelper(Context context) {
        //
        super(context, "Empresa", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        db.execSQL(getCreateTableCargo());
        db.execSQL(getCreateTableFuncionario());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
