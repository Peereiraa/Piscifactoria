package Pez.PecesMar;

import Pez.Pez;
import Pez.tipos.Carnivoro;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

/**
 * La clase `Besugo` representa a un pez de la especie Besugo en un sistema de piscifactorías marinas.
 * Es un pez carnívoro que implementa la interfaz `DeMar`.
 */
public class Besugo extends Carnivoro implements DeMar {

    /**
     * Constructor de la clase `Besugo`.
     * 
     * @param sexo Género del pez. `true` para macho, `false` para hembra.
     * @param pezDatos Datos específicos del tipo de pez (en este caso, del Besugo).
     */
    public Besugo(boolean sexo, PecesDatos pezDatos) {
        super(sexo, AlmacenPropiedades.BESUGO);
    }

    public Besugo(int edad, boolean sexo, boolean vivo, boolean adulto, boolean fertil, int ciclo, boolean alimentado) {
        super(sexo, AlmacenPropiedades.BESUGO);
        this.edad = edad;
        this.vivo = vivo;
        this.fertil = fertil;
        this.adulto = adulto;
        this.alimentado = alimentado;
        this.ciclodevida = ciclo;
    }

    /**
     * Crea un nuevo pez Besugo con el género especificado.
     * 
     * @param sex Género del nuevo pez. `true` para macho, `false` para hembra.
     * @return Nuevo pez Besugo creado.
     */
    @Override
    public Pez crearPez(boolean sex) {
        return new Besugo(sex, pezDato);
    }
}
