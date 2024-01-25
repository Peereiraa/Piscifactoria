package Pez.tipos;

import java.util.Random;

import Pez.Pez;
import Tanques.Tanque;
import propiedades.PecesDatos;

/**
 * La clase abstracta `Omnivoro` es una extensión de la clase `Pez` y representa a un pez de la especie omnívora.
 * Proporciona implementaciones específicas para la alimentación de peces omnívoros.
 */
public abstract class Omnivoro extends Pez {

    /**
     * Constructor de la clase Omnivoro.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie omnívora.
     */
    public Omnivoro(boolean sexo, PecesDatos pezDatos) {
        super(sexo, pezDatos);
    }

    /**
     * Implementación específica para la alimentación de peces omnívoros.
     * Un pez omnívoro puede alimentarse de comida disponible en el tanque o de otros peces muertos en el tanque.
     * 
     * @param t El tanque en el que vive el pez omnívoro.
     */
    @Override
    public void alimentar(Tanque<? extends Pez> t) {
        Random rd = new Random();
        int random = rd.nextInt(2);
        int random2 = rd.nextInt(4);

        Pez pezMuerto = t.obtenerPezMuerto();

        if (random2 == 0) {
            if (pezMuerto != null) {

                if (random == 0) {
                    System.out.println("El pez omnívoro se ha alimentado de un pez muerto");
                    this.alimentado = true;
                } else {
                    t.comerPezMuerto(pezMuerto);
                    System.out.println("El pez omnívoro se ha alimentado de un pez muerto entero y se eliminó");
                    this.alimentado = true;
                }
            }

            if (t.getCantidadComida() > 0) {
                t.setCantidadComida(t.getCantidadComida() - 1);
                this.alimentado = true;
            } else {
                this.alimentado = false;
            }
        }

        this.alimentado = false;
    }
}
