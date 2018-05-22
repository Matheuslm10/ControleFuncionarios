package com.example.mathe.controlefuncionarios.Objetos;

import java.io.Serializable;

/**
 * Created by mathe on 05/05/2018.
 */

public class Funcionario implements Serializable {
    public int funcId;
    public String funcNome;
    public int cargoId;
    public String salario;

    public Funcionario(){
        funcId = 0;
    }

    @Override
    public String toString() {
        return funcNome.toString();
    }
}
