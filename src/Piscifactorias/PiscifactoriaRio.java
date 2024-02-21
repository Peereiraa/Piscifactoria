package Piscifactorias;

import Pez.Pez;
import Tanques.Tanque;

/**
 * Clase que representa una piscifactoría de tipo río.
 * Extiende la clase base Piscifactoria.
 */

public class PiscifactoriaRio extends Piscifactoria {

    /**
     * Constructor de la clase PiscifactoriaRio.
     *
     * @param nombre El nombre de la piscifactoría de río.
     */

    public PiscifactoriaRio(String nombre) {
        super(nombre);
        this.comidaActual = 25;
        this.comidaMaxima = 25;
        Tanque<? extends Pez> t = new Tanque<>(25, 1, this);
        tanque.add(t);
    }
    
}
