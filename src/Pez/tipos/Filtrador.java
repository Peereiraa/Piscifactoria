package Pez.tipos;

import java.util.Random;

import Pez.Pez;
import Tanques.Tanque;
import propiedades.PecesDatos;

/**
 * La clase abstracta `Filtrador` es una extensión de la clase `Pez` y representa a un pez de la especie filtradora.
 * Proporciona implementaciones específicas para la alimentación de peces filtradores.
 */
public abstract class Filtrador extends Pez {

    /**
     * Constructor de la clase Filtrador.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie filtradora.
     */
    public Filtrador(boolean sexo, PecesDatos pezDatos) {
        super(sexo, pezDatos);
    }

    /**
     * Implementación específica para la alimentación de peces filtradores.
     * Un pez filtrador puede alimentarse de la comida disponible en el tanque.
     * 
     * @param t El tanque en el que vive el pez filtrador.
     */
    @Override
    public void alimentar(Tanque<? extends Pez> t) {
        Random rd = new Random();
        int random = rd.nextInt(2);

        if (random == 0) {
            if (t.getCantidadComida() > 0) {
                t.setCantidadComida(t.getCantidadComida() - 1);
                this.alimentado = true;
            } else {
                this.alimentado = false;
            }
        } else {
            this.alimentado = false;
        }
    }
}
