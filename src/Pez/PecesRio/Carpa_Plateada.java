package Pez.PecesRio;

import Pez.Pez;

import Pez.tipos.Filtrador;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Carpa_Plateada` representa a un pez de la especie Carpa Plateada que habita en ambientes de río.
 * Extiende la clase abstracta `Filtrador` e implementa la interfaz `DeRio`.
 */
public class Carpa_Plateada extends Filtrador implements DeRio {

    /**
     * Constructor de la clase Carpa_Plateada.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Carpa Plateada.
     */
    public Carpa_Plateada(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.CARPA_PLATEADA);
    }

    public Carpa_Plateada(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.CARPA_PLATEADA);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }

    /**
     * Crea un nuevo pez Carpa Plateada con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Carpa_Plateada.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Carpa_Plateada(sex, pezDato);
    }
}
