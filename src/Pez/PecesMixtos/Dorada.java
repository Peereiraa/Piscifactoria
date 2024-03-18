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

    public Dorada(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.DORADA);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
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
