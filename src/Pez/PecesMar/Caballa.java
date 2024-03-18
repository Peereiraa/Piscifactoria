package Pez.PecesMar;

import Pez.Pez;
import Pez.tipos.Carnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Caballa` representa a un pez de la especie Caballa en un sistema de piscifactorías marinas.
 * Es un pez carnívoro que implementa la interfaz `DeMar`.
 */
public class Caballa extends Carnivoro implements DeMar {

    /**
     * Constructor de la clase `Caballa`.
     * 
     * @param sexo Género del pez. `true` para macho, `false` para hembra.
     * @param pezDatos Datos específicos del tipo de pez (en este caso, de la Caballa).
     */
    public Caballa(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.CABALLA);
    }

    public Caballa(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.CABALLA);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }

    /**
     * Crea un nuevo pez Caballa con el género especificado.
     * 
     * @param sex Género del nuevo pez. `true` para macho, `false` para hembra.
     * @return Nuevo pez Caballa creado.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Caballa(sex, pezDato);
    }
}
