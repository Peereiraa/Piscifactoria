package Pez.PecesRio;

import Pez.Pez;
import Pez.tipos.OmnivoroVoraz;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Carpa` representa a un pez de la especie Carpa que habita en ambientes de río.
 * Extiende la clase abstracta `OmnivoroVoraz` e implementa la interfaz `DeRio`.
 */
public class Carpa extends OmnivoroVoraz implements DeRio {

    /**
     * Constructor de la clase Carpa.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Carpa.
     */
    public Carpa(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.CARPA);
    }

    /**
     * Crea un nuevo pez Carpa con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Carpa.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Carpa(sex, pezDato);
    }
}
