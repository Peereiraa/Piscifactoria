package Pez.PecesMixtos;

import Pez.Pez;
import Pez.PecesMar.DeMar;
import Pez.PecesRio.DeRio;
import Pez.tipos.Carnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `LubinaEuropea` representa a un pez de la especie Lubina Europea que puede habitar tanto en ambientes marinos como de río.
 * Extiende la clase abstracta `Carnivoro` e implementa las interfaces `DeMar` y `DeRio`.
 */
public class LubinaEuropea extends Carnivoro implements DeMar, DeRio {

    /**
     * Constructor de la clase LubinaEuropea.
     * 
     * @param sexo     El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Lubina Europea.
     */
    public LubinaEuropea(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.LUBINA_EUROPEA);
    }

    /**
     * Crea un nuevo pez Lubina Europea con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase LubinaEuropea.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new LubinaEuropea(sex, pezDato);
    }
}
