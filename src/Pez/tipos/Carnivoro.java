package Pez.tipos;

import java.util.Random;

import Pez.Pez;
import Tanques.Tanque;
import propiedades.PecesDatos;

/**
 * La clase abstracta `Carnivoro` es una extensión de la clase `Pez` y representa a un pez de la especie carnívora.
 * Proporciona implementaciones específicas para la alimentación de peces carnívoros.
 */
public abstract class Carnivoro extends Pez {

    /**
     * Constructor de la clase Carnivoro.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie carnívora.
     */
    public Carnivoro(boolean sexo, PecesDatos pezDatos) {
        super(sexo, pezDatos);
    }

    /**
     * Implementación específica para la alimentación de peces carnívoros.
     * Un pez carnívoro se puede alimentar de un pez muerto, de parte de un pez muerto o de la comida disponible en el tanque.
     * 
     * @param t El tanque en el que vive el pez carnívoro.
     */
    @Override
    public void alimentar(Tanque<? extends Pez> t) {
        Random rd = new Random();
        int random = rd.nextInt(2);

        Pez pezMuerto = t.obtenerPezMuerto();

        if (pezMuerto != null) {
            if (random == 0) {
                System.out.println("El pez carnívoro se ha alimentado de un pez muerto");
                this.alimentado = true;
            } else {
                t.comerPezMuerto(pezMuerto);
                System.out.println("El pez carnívoro se ha alimentado de un pez muerto entero y se eliminó");
                this.alimentado = true;
            }
        }else{
            if (t.getCantidadComida() > 0) {
                t.setCantidadComida(t.getCantidadComida() - 1);
                this.alimentado = true;
            } else {
                this.alimentado = false;
            }
        }

        
    }
}
