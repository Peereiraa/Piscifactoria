package Comun;

/**
 * Clase que representa el sistema de monedas en el simulador.
 */

public class Monedas {

    /**
     * Instancia única del monedero (patrón Singleton).
     */
    public static Monedas monedero = null;
    protected int monedas;

    /**
     * Constructor privado para asegurar la creación de una única instancia del monedero.
     */
    private Monedas() {
        monedas = 10000;
    }

    /**
     * Obtiene la instancia única del monedero (implementación del patrón Singleton).
     *
     * @return La instancia única del monedero.
     */
    public static Monedas getInstance() {
        if (monedero == null) {
            monedero = new Monedas();
        }
        return monedero;
    }

    /**
     * Ingresa monedas al monedero.
     *
     * @param capacidad La cantidad de monedas a ingresar.
     */
    public void ingresar(int capacidad) {
        this.monedas += capacidad;
    }

    /**
     * Realiza un pago restando monedas del monedero.
     *
     * @param cantidad La cantidad de monedas a pagar.
     */
    public void pagar(int cantidad) {
        if (this.monedas >= cantidad) {
            this.monedas -= cantidad;
            System.out.println("Pago realizado con exito");
        } else {
            System.out.println("No tienes suficientes momendas para comprar");
        }
    }

    /**
     * Obtiene la cantidad actual de monedas en el monedero.
     *
     * @return La cantidad actual de monedas.
     */
    public int getMonedas() {
        return monedas;
    }

    /**
     * Establece la cantidad de monedas en el monedero.
     *
     * @param monedas La nueva cantidad de monedas.
     */
    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    /**
     * Obtiene la instancia única del monedero.
     *
     * @return La instancia única del monedero.
     */
    public static Monedas getMonedero() {
        return monedero;
    }

    /**
     * Establece la instancia única del monedero.
     *
     * @param monedero La nueva instancia única del monedero.
     */
    public static void setMonedero(Monedas monedero) {
        Monedas.monedero = monedero;
    }

}
