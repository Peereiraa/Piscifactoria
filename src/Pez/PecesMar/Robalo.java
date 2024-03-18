package Pez.PecesMar;

import Pez.Pez;
import Pez.tipos.CarnivoroVoraz;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Robalo` representa a un pez de la especie Robalo que pertenece al entorno marino.
 * Extiende la clase abstracta `CarnivoroVoraz` e implementa la interfaz `DeMar`.
 */
public class Robalo extends CarnivoroVoraz implements DeMar {

    /**
     * Constructor de la clase Robalo.
     * 
     * @param sexo     El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Robalo.
     */
    public Robalo(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.ROBALO);
    }

    public Robalo(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.ROBALO);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }

    /**
     * Crea un nuevo pez Robalo con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Robalo.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Robalo(sex, pezDato);
    }
}
