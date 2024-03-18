package Pez.PecesRio;

import Pez.Pez;
import Pez.tipos.Carnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Pejerrey` representa a un pez de la especie Pejerrey que habita en ambientes de río.
 * Extiende la clase abstracta `Carnivoro` e implementa la interfaz `DeRio`.
 */
public class Pejerrey extends Carnivoro implements DeRio {

    /**
     * Constructor de la clase Pejerrey.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie Pejerrey.
     */
    public Pejerrey(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.PEJERREY);
    }

    public Pejerrey(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.PEJERREY);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }
    /**
     * Crea un nuevo pez Pejerrey con el sexo especificado.
     * 
     * @param sex  El sexo del nuevo pez (true para masculino, false para femenino).
     * @return     Una nueva instancia de la clase Pejerrey.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Pejerrey(sex, pezDato);
    }
}
