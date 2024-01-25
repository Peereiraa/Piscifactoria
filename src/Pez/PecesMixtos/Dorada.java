package Pez.PecesMixtos;

import Pez.Pez;
import Pez.PecesMar.DeMar;
import Pez.PecesRio.DeRio;
import Pez.tipos.Omnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Dorada` representa a un pez de la especie Dorada que puede habitar tanto en ambientes marinos como de río.
 * Extiende la clase abstracta `Omnivoro` e implementa las interfaces `DeMar` y `DeRio`.
 */
public class Dorada extends Omnivoro implements DeMar, DeRio {

    /**
     * Constructor de la clase Dorada.
     * 
     * @param sexo     El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Dorada.
     */
    public Dorada(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.DORADA);
    }

    /**
     * Crea un nuevo pez Dorada con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Dorada.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Dorada(sex, pezDato);
    }
}
