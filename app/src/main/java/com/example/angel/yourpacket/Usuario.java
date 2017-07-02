package com.example.angel.yourpacket;

/**
 * Created by angel on 2/7/2017.
 */

public class Usuario {
    String nombre;
    int tipo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Usuario(){


    }

    public Usuario(String nombre, int tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
}
