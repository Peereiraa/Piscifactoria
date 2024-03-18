package Pez.PecesMar;

import Pez.Pez;
import Pez.tipos.Omnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Sargo` representa a un pez de la especie Sargo que pertenece al entorno marino.
 * Extiende la clase abstracta `Omnivoro` e implementa la interfaz `DeMar`.
 */
public class Sargo extends Omnivoro implements DeMar {

    /**
     * Constructor de la clase Sargo.
     * 
     * @param sexo     El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Sargo.
     */
    public Sargo(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.SARGO);
    }

    public Sargo(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.SARGO);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }

    /**
     * Crea un nuevo pez Sargo con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Sargo.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Sargo(sex, pezDato);
    }
}
