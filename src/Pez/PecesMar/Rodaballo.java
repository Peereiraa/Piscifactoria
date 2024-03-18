package Pez.PecesMar;

import Pez.Pez;
import Pez.tipos.Carnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Rodaballo` representa a un pez de la especie Rodaballo que pertenece al entorno marino.
 * Extiende la clase abstracta `Carnivoro` e implementa la interfaz `DeMar`.
 */
public class Rodaballo extends Carnivoro implements DeMar {

    /**
     * Constructor de la clase Rodaballo.
     * 
     * @param sexo     El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Rodaballo.
     */
    public Rodaballo(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.RODABALLO);
    }

    public Rodaballo(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.RODABALLO);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }

    /**
     * Crea un nuevo pez Rodaballo con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Rodaballo.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Rodaballo(sex, pezDato);
    }
}
