package Pez.tipos;

import java.util.Random;

import Pez.Pez;
import Tanques.Tanque;
import propiedades.PecesDatos;

/**
 * La clase abstracta `OmnivoroVoraz` es una extensión de la clase `Pez` y representa a un pez omnívoro voraz.
 * Proporciona implementaciones específicas para la alimentación de peces omnívoros voraces.
 */
public abstract class OmnivoroVoraz extends Pez {

    /**
     * Constructor de la clase OmnivoroVoraz.
     * 
     * @param sexo      El sexo del pez (true para masculino, false para femenino).
     * @param pezDatos  Los datos específicos de la especie omnívora voraz.
     */
    public OmnivoroVoraz(boolean sexo, PecesDatos pezDatos) {
        super(sexo, pezDatos);
    }

    /**
     * Implementación específica para la alimentación de peces omnívoros voraces.
     * Un pez omnívoro voraz puede alimentarse de comida disponible en el tanque o de otros peces muertos en el tanque,
     * consumiendo 2 de comida.
     * 
     * @param t El tanque en el que vive el pez omnívoro voraz.
     */
    @Override
    public void alimentar(Tanque<? extends Pez> t) {
        Random rd = new Random();
        int random = rd.nextInt(2);
        int random2 = rd.nextInt(4);

        Pez pezMuerto = t.obtenerPezMuerto();
        Pez pezMuerto2 = t.obtenerPezMuerto();
        
        if (random2 != 0) {
            if (pezMuerto != null && pezMuerto2 != null) {
                if (random == 0) {
                    System.out.println("El pez omnívoro voraz se ha alimentado de un pez muerto");
                    this.alimentado = true;
                } else {
                    t.comerPezMuerto(pezMuerto);
                    t.comerPezMuerto(pezMuerto2);
                    System.out.println("El pez omnívoro voraz se ha alimentado de un pez muerto entero y se eliminó");
                    this.alimentado = true;
                }

            }else{
                if (t.getCantidadComida() > 0) {
                    t.setCantidadComida(t.getCantidadComida() - 2);
                    this.alimentado = true;
                } else {
                    this.alimentado = false;
                }
            }

            
        }else{
            this.alimentado = true;
        }

    }
}
