package Comun;

    /**
     * Clase que representa el almacén central en el simulador.
     */
public class AlmacenCentral {

    protected int espacioMaximo;
    protected int espacioOcupado;
    protected int espacioDisponible;
    protected static int precio = 2000;

    /**
     * Constructor por defecto que inicializa la capacidad máxima del almacén central.
     */
    public AlmacenCentral() {
        this.espacioMaximo = 200;
        this.espacioDisponible = 200;
    }

    /**
     * Almacena una cantidad de comida en el almacén central.
     *
     * @param cantidad La cantidad de comida a almacenar.
     */
    public void meterComida(int cantidad){
        if(this.espacioDisponible >= cantidad){
            this.espacioOcupado += cantidad;
        } else{
            this.espacioOcupado += this.espacioDisponible;
            
        }
        this.espacioDisponible = this.espacioMaximo - this.espacioOcupado;
    }

    /**
     * Obtiene la capacidad máxima del almacén central.
     *
     * @return La capacidad máxima del almacén central.
     */
    public int getEspacioMaximo() {
        return espacioMaximo;
    }

    /**
     * Establece la capacidad máxima del almacén central.
     *
     * @param espacioMaximo La nueva capacidad máxima del almacén central.
     */
    public void setEspacioMaximo(int espacioMaximo) {
        this.espacioMaximo = espacioMaximo;
    }

    /**
     * Obtiene el espacio actualmente ocupado en el almacén central.
     *
     * @return El espacio actualmente ocupado en el almacén central.
     */
    public int getEspacioOcupado() {
        return espacioOcupado;
    }

    /**
     * Establece el espacio ocupado en el almacén central.
     *
     * @param espacioOcupado El nuevo espacio ocupado en el almacén central.
     */
    public void setEspacioOcupado(int espacioOcupado) {
        this.espacioOcupado = espacioOcupado;
    }

    /**
     * Obtiene el espacio disponible en el almacén central.
     *
     * @return El espacio disponible en el almacén central.
     */
    public int getEspacioDisponible() {
        int espacio = espacioMaximo - espacioOcupado;
        return espacio;
    }

    /**
     * Establece el espacio disponible en el almacén central.
     *
     * @param espacioDisponible El nuevo espacio disponible en el almacén central.
     */
    public void setEspacioDisponible(int espacioDisponible) {
        this.espacioDisponible = espacioDisponible;
    }

}
