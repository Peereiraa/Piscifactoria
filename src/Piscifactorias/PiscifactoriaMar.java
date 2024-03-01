package Piscifactorias;

import Pez.Pez;
import Tanques.Tanque;

/**
 * Clase que representa una piscifactoría de tipo mar.
 * Extiende la clase base Piscifactoria.
 */
public class PiscifactoriaMar extends Piscifactoria {

    /**
     * Constructor de la clase PiscifactoriaMar.
     *
     * @param nombre El nombre de la piscifactoría de mar.
     */
    public PiscifactoriaMar(String nombre) {
        super(nombre);
        this.comidaActual = 100;
        this.comidaMaxima = 100;
        Tanque<? extends Pez> t = new Tanque<>(100, 1,this);
        tanque.add(t);
    }
    

}