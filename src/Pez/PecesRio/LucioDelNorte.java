package Pez.PecesRio;

import Pez.Pez;
import Pez.tipos.CarnivoroActivo;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `LucioDelNorte` representa a un pez de la especie Lucio del Norte que habita en ambientes de río.
 * Extiende la clase abstracta `CarnivoroActivo` e implementa la interfaz `DeRio`.
 */
public class LucioDelNorte extends CarnivoroActivo implements DeRio {

    /**
     * Constructor de la clase LucioDelNorte.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Lucio del Norte.
     */
    public LucioDelNorte(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.LUCIO_NORTE);
    }

    /**
     * Crea un nuevo pez Lucio del Norte con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase LucioDelNorte.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new LucioDelNorte(sex, pezDato);
    }
}
