package Pez.tipos;

import java.util.Random;

import Pez.Pez;
import Tanques.Tanque;
import propiedades.PecesDatos;

/**
 * La clase abstracta `CarnivoroVoraz` es una extensión de la clase `Pez` y representa a un pez de la especie carnívora voraz.
 * Proporciona implementaciones específicas para la alimentación de peces carnívoros voraces.
 */
public abstract class CarnivoroVoraz extends Pez {

    /**
     * Constructor de la clase CarnivoroVoraz.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie carnívora voraz.
     */
    public CarnivoroVoraz(boolean sexo, PecesDatos pezDatos) {
        super(sexo, pezDatos);
    }

    /**
     * Implementación específica para la alimentación de peces carnívoros voraces.
     * Un pez carnívoro voraz puede alimentarse de dos peces muertos, de parte de dos peces muertos o de la comida disponible en el tanque.
     * Además, un pez carnívoro voraz come 2 de alimento
     * 
     * @param t El tanque en el que vive el pez carnívoro voraz.
     */
    @Override
    public void alimentar(Tanque<? extends Pez> t) {
        Random rd = new Random();
        int random = rd.nextInt(2);

        Pez pezMuerto = t.obtenerPezMuerto();
        Pez pezMuerto2 = t.obtenerPezMuerto();

        if (pezMuerto != null && pezMuerto2 != null) {
            if (random == 0) {
                System.out.println("El pez carnívoro voraz se ha alimentado de un pez muerto");
                this.alimentado = true;
            } else {
                t.comerPezMuerto(pezMuerto);
                t.comerPezMuerto(pezMuerto2);
                System.out.println("El pez carnívoro voraz se ha alimentado de dos peces muertos enteros y se eliminaron");
                this.alimentado = true;
            }
        }

        if (t.getCantidadComida() > 0) {
            t.setCantidadComida(t.getCantidadComida() - 2);
            this.alimentado = true;
        } else {
            this.alimentado = false;
        }
    }
}
