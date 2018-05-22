package com.example.mathe.controlefuncionarios.Objetos;

import java.io.Serializable;

/**
 * Created by mathe on 14/05/2018.
 */

public class Cargo implements Serializable {
    public int cargoId;
    public String cargoNome;

    public Cargo(){
        cargoId = 0;
    }

    @Override
    public String toString() {
        return cargoNome.toString();
    }
}