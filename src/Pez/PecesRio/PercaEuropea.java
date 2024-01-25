package Pez.PecesRio;

import Pez.Pez;
import Pez.tipos.CarnivoroActivo;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `PercaEuropea` representa a un pez de la especie Perca Europea que habita en ambientes de río.
 * Extiende la clase abstracta `CarnivoroActivo` e implementa la interfaz `DeRio`.
 */
public class PercaEuropea extends CarnivoroActivo implements DeRio {

    /**
     * Constructor de la clase PercaEuropea.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Perca Europea.
     */
    public PercaEuropea(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.PERCA_EUROPEA);
    }

    /**
     * Crea un nuevo pez Perca Europea con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase PercaEuropea.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new PercaEuropea(sex, pezDato);
    }
}
