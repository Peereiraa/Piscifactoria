package Pez;

import java.util.Random;

import Registro.Log;
import Tanques.Tanque;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

import javax.management.ValueExp;

/**
 * La clase `Pez` representa un pez en un sistema de piscifactorías.
 * Cada pez tiene una edad, un ciclo de vida, un género (hembra o macho),
 * una capacidad para reproducirse, un estado de vida, un estado de
 * alimentación,
 * un estado de adultez, y un conjunto de datos específicos del tipo de pez.
 */
public class Pez {

    protected int edad = 0;
    protected int ciclodevida;
    protected final boolean sexo; // true es Hombre
    protected boolean fertil = false; // false NO es fertil
    protected boolean vivo = true; // true es que esta Vivo
    protected boolean alimentado = false; // false es que no esta Alimentado
    protected boolean adulto = false; // false es que no es Adulto
    protected PecesDatos pezDato;
    protected static Log log = Log.getInstance();

    /**
     * Constructor de la clase `Pez`.
     * 
     * @param sexo    Género del pez. `true` para macho, `false` para hembra.
     * @param pezDato Datos específicos del tipo de pez.
     */
    public Pez(boolean sexo, PecesDatos pezDato) {
        this.sexo = sexo;
        this.edad = 0;
        this.pezDato = pezDato;
        this.ciclodevida = pezDato.getCiclo();

    }

    public Pez(int edad, boolean sexo, boolean fertil, boolean vivo, boolean alimentado, boolean adulto, int ciclodevida) {
        this.edad = edad;
        this.sexo = sexo;
        this.fertil = fertil;
        this.vivo = vivo;
        this.alimentado = alimentado;
        this.adulto = adulto;
        this.ciclodevida = ciclodevida;

    }

    /**
     * Muestra el estado del pez, incluyendo su edad, sexo, estado de vida,
     * alimentación, adultez y fertilidad.
     */
    public void showStatus() {
        try {
            System.out.println("--------------- " + pezDato.getNombre() + " ---------------");
            System.out.println("Edad: " + this.edad + " dias");
            System.out.println("Sexo: " + (this.sexo == true ? "H" : "M"));
            System.out.println("Vivo: " + (this.vivo ? "Si" : "No"));
            System.out.println("Alimentado: " + (this.alimentado ? "Si" : "No"));
            System.out.println("Adulto: " + (this.adulto ? "Si" : "No"));
            System.out.println("Fértil: " + (this.fertil ? "Si" : "No"));
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Hace que el pez crezca un día, realizando la alimentación y verificando su
     * supervivencia.
     * 
     * @param t      Tanque en el que se encuentra el pez.
     * @param comida Cantidad de comida disponible en el tanque.
     */
    public void grow(Tanque<? extends Pez> t, int comida) {
        try {
            if (this.isVivo()) {
                Random rd = new Random();
                int random = rd.nextInt(2);
                this.alimentar(t);
                this.edad++;
                verificarFertilidad();

                if (!this.alimentado && random == 0) {
                    this.vivo = false;
                    t.pezMuere(this);
                }
            } else {
                this.fertil = false;
                this.adulto = false;
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Realiza la acción de alimentar al pez. (Método a implementar según las
     * necesidades específicas de cada tipo de pez)
     * 
     * @param t Tanque en el que se encuentra el pez.
     */

    public void alimentar(Tanque<? extends Pez> t) {

    }

    /**
     * Crea un nuevo pez del mismo tipo y con el género especificado.
     * 
     * @param sex Género del nuevo pez. `true` para macho, `false` para hembra.
     * @return Nuevo pez creado.
     */

    public Pez crearPez(boolean sex) {
        return new Pez(sex, pezDato);
    }

    /**
     * Verifica si el pez ha alcanzado la edad de madurez para ser fértil.
     */
    public void verificarFertilidad() {
        if (this.edad >= pezDato.getMadurez()) {
            this.fertil = true;
            this.adulto = true;
        } else {
            this.fertil = false;
        }
    }

    /**
     * Reinicia los valores del pez a su estado inicial.
     */
    public void reset() {
        this.edad = 0;
        this.setAdulto(adulto);
        this.setAlimentado(alimentado);
        this.setVivo(true);
        this.setFertil(false);

    }

    /**
     * Realiza la acción de reproducirse, generando nuevos peces si las condiciones
     * son adecuadas.
     * 
     * @param t Tanque en el que se encuentra el pez.
     */
    public void reproducir(Tanque<? extends Pez> t) {
        try {
            if (this.ciclodevida == 0) {
                this.fertil = true;
                this.ciclodevida = pezDato.getCiclo();
            } else {
                // Reducir el ciclo de vida en uno
                this.ciclodevida--;
            }

            if ((this.isFertil() && !this.getSexo())) {
                if (pezDato.getHuevos() <= t.espacioLibreTanque()) {
                    Pez p = t.obtenerPezFertil(true);
                    if (p != null) {
                        for (int i = 0; i < pezDato.getHuevos(); i++) {
                            Pez pezHijo;
                            if (i % 2 == 0) {
                                pezHijo = crearPez(true);
                            } else {
                                pezHijo = crearPez(false);
                            }
                            pezHijo.reset();
                            t.addFish(pezHijo);

                        }
                        this.fertil = false;
                    }
                }
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Incrementa en uno el ciclo de vida del pez.
     */
    public void incrementarCicloDeVida() {
        this.ciclodevida++;
    }

    /**
     * Reinicia el ciclo de vida del pez, estableciéndolo en cero.
     */
    public void resetCicloDeVida() {
        this.ciclodevida = 0;
    }

    /**
     * Obtiene la edad actual del pez.
     * 
     * @return Edad actual del pez en días.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del pez.
     * 
     * @param edad Nueva edad del pez en días.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el género del pez.
     * 
     * @return `true` si el pez es macho, `false` si el pez es hembra.
     */
    public boolean getSexo() {
        return sexo;
    }

    /**
     * Verifica si el pez es fértil.
     * 
     * @return `true` si el pez es fértil, `false` si el pez no es fértil.
     */
    public boolean isFertil() {

        return fertil;
    }

    /**
     * Establece el estado de fertilidad del pez.
     * 
     * @param fertil Nuevo estado de fertilidad del pez.
     */
    public void setFertil(boolean fertil) {
        this.fertil = fertil;
    }

    /**
     * Verifica si el pez está vivo.
     * 
     * @return `true` si el pez está vivo, `false` si el pez está muerto.
     */
    public boolean isVivo() {
        return vivo;
    }

    /**
     * Establece el estado de vida del pez.
     * 
     * @param vivo Nuevo estado de vida del pez.
     */
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    /**
     * Verifica si el pez ha sido alimentado.
     * 
     * @return `true` si el pez ha sido alimentado, `false` si el pez no ha sido
     *         alimentado.
     */
    public boolean getAlimentado() {
        return alimentado;
    }

    /**
     * Establece el estado de alimentación del pez.
     * 
     * @param alimentado Nuevo estado de alimentación del pez.
     */
    public void setAlimentado(boolean alimentado) {
        this.alimentado = alimentado;
    }

    /**
     * Verifica si el pez es adulto.
     * 
     * @return `true` si el pez es adulto, `false` si el pez no es adulto.
     */
    public boolean getAdulto() {
        return adulto;
    }

    /**
     * Establece el estado de adultez del pez.
     * 
     * @param adulto Nuevo estado de adultez del pez.
     */
    public void setAdulto(boolean adulto) {
        this.adulto = adulto;
    }

    /**
     * Obtiene los datos específicos del tipo de pez.
     * 
     * @return Datos específicos del tipo de pez.
     */
    public PecesDatos getPezDato() {
        return pezDato;
    }

    /**
     * Establece los datos específicos del tipo de pez.
     * 
     * @param pezDato Nuevos datos específicos del tipo de pez.
     */
    public void setPezDato(PecesDatos pezDato) {
        this.pezDato = pezDato;
    }

    /**
     * Obtiene el ciclo de vida del pez.
     * 
     * @return Ciclo de vida del pez.
     */
    public int getCiclodevida() {
        return ciclodevida;
    }

    /**
     * Establece el ciclo de vida del pez.
     * 
     * @param ciclodevida Nuevo ciclo de vida del pez.
     */
    public void setCiclodevida(int ciclodevida) {
        this.ciclodevida = ciclodevida;
    }

    public double getPrecioVenta(Pez p) {
        // Obtener el tipo de pez
        String tipoPez = p.getClass().getSimpleName();

        double costo = 0;
        switch (tipoPez) {
            case "Besugo":
                costo = AlmacenPropiedades.BESUGO.getCoste();
                break;
            case "Caballa":
                costo = AlmacenPropiedades.CABALLA.getCoste();
                break;
            case "Robalo":
                costo = AlmacenPropiedades.ROBALO.getCoste();
                break;
            case "Rodaballo":
                costo = AlmacenPropiedades.RODABALLO.getCoste();
                break;
            case "Sargo":
                costo = AlmacenPropiedades.SARGO.getCoste();
                break;
            case "Carpa_Plateada":
                costo = AlmacenPropiedades.CARPA_PLATEADA.getCoste();
                break;
            case "Carpa":
                costo = AlmacenPropiedades.CARPA.getCoste();
                break;
            case "LucioDelNorte":
                costo = AlmacenPropiedades.LUCIO_NORTE.getCoste();
                break;
            case "Pejerrey":
                costo = AlmacenPropiedades.PEJERREY.getCoste();
                break;
            case "PercaEuropea":
                costo = AlmacenPropiedades.PERCA_EUROPEA.getCoste();
                break;
            case "Dorada":
                costo = AlmacenPropiedades.DORADA.getCoste();
                break;
            case "LubinaEuropea":
                costo = AlmacenPropiedades.LUBINA_EUROPEA.getCoste();
                break;
            default:
                System.out.println("No se puede determinar el costo del pez.");
                return -1;
        }

        return costo;
    }

}
