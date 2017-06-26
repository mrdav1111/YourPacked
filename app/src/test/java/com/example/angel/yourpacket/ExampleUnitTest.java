package com.example.angel.yourpacket;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void equalPaquete() throws Exception{
        Paquete hola = new Paquete("1063968");
        Paquete adios = new Paquete("1063968");

        assertEquals(hola.equals(adios),true);
    }
}