package Pez.tipos;

import java.util.Random;

import Pez.Pez;
import Tanques.Tanque;
import propiedades.PecesDatos;

/**
 * La clase abstracta `CarnivoroActivo` es una extensión de la clase `Pez` y representa a un pez de la especie carnívora activa.
 * Proporciona implementaciones específicas para la alimentación de peces carnívoros activos.
 */
public abstract class CarnivoroActivo extends Pez {

    /**
     * Constructor de la clase CarnivoroActivo.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie carnívora activa.
     */
    public CarnivoroActivo(boolean sexo, PecesDatos pezDatos) {
        super(sexo, pezDatos);
    }

    /**
     * Implementación específica para la alimentación de peces carnívoros activos.
     * Un pez carnívoro activo puede alimentarse de un pez muerto, de parte de un pez muerto o de la comida disponible en el tanque.
     * Además, un pez carnívoro activo tiene un 50% de comer 2 de alimento ese día.
     * 
     * @param t El tanque en el que vive el pez carnívoro activo.
     */
    @Override
    public void alimentar(Tanque<? extends Pez> t) {
        Random rd = new Random();
        int random = rd.nextInt(2);
        int random2 = rd.nextInt(2);

        Pez pezMuerto = t.obtenerPezMuerto();
        Pez pezMuerto2 = t.obtenerPezMuerto();

        if (random2 == 0) {

            if (pezMuerto != null) {
                if (random == 0) {
                    System.out.println("El pez carnívoro activo se ha alimentado de un pez muerto");
                    this.alimentado = true;
                } else {
                    t.comerPezMuerto(pezMuerto);
                    System.out.println("El pez carnívoro activo se ha alimentado de un pez muerto entero y se eliminó");
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
        }else{
            if (pezMuerto != null) {
                if (random == 0) {
                    System.out.println("El pez carnívoro activo se ha alimentado de dos peces muerto");
                    this.alimentado = true;
                } else {
                    t.comerPezMuerto(pezMuerto);
                    t.comerPezMuerto(pezMuerto2);
                    System.out.println("El pez carnívoro activo se ha alimentado de dos peces muertos entero y se eliminó");
                    this.alimentado = true;
                }
            }else{
                if (t.getCantidadComida() > 1) {
                    t.setCantidadComida(t.getCantidadComida() - 2);
                    this.alimentado = true;
                } else {
                    this.alimentado = false;
                }
            }
        }
    }
}
