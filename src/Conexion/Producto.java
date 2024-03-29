package Conexion;

/**
 * Esta clase representa un producto en el sistema, con su número de referencia, cliente asociado, pez asociado,
 * cantidad solicitada y cantidad enviada.
 */
public class Producto {

    private int numero_referencia; // Número de referencia del producto
    private String id_cliente; // Identificador del cliente asociado al producto
    private String id_pez; // Identificador del pez asociado al producto
    private int cantidad_solicitada; // Cantidad solicitada del producto
    private int cantidad_enviada; // Cantidad enviada del producto

    /**
     * Constructor de la clase Producto.
     *
     * @param numero_referencia   Número de referencia del producto.
     * @param id_cliente          Identificador del cliente asociado al producto.
     * @param id_pez              Identificador del pez asociado al producto.
     * @param cantidad_solicitada Cantidad solicitada del producto.
     * @param cantidad_enviada    Cantidad enviada del producto.
     */
    public Producto(int numero_referencia, String id_cliente, String id_pez, int cantidad_solicitada, int cantidad_enviada) {
        this.numero_referencia = numero_referencia;
        this.id_cliente = id_cliente;
        this.id_pez = id_pez;
        this.cantidad_solicitada = cantidad_solicitada;
        this.cantidad_enviada = cantidad_enviada;
    }

    public int getNumero_referencia() {
        return numero_referencia;
    }

    public void setNumero_referencia(int numero_referencia) {
        this.numero_referencia = numero_referencia;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getId_pez() {
        return id_pez;
    }

    public void setId_pez(String id_pez) {
        this.id_pez = id_pez;
    }

    public int getCantidad_solicitada() {
        return cantidad_solicitada;
    }

    public void setCantidad_solicitada(int cantidad_solicitada) {
        this.cantidad_solicitada = cantidad_solicitada;
    }

    public int getCantidad_enviada() {
        return cantidad_enviada;
    }

    public void setCantidad_enviada(int cantidad_enviada) {
        this.cantidad_enviada = cantidad_enviada;
    }

    /**
     * Calcula el porcentaje de la cantidad enviada respecto a la cantidad solicitada.
     *
     * @return El porcentaje de la cantidad enviada respecto a la cantidad solicitada.
     */
    public double porcentajeEnviado(){
        return ((double) cantidad_enviada / cantidad_solicitada) * 100;
    }

    /**
     * Devuelve una representación en cadena del producto.
     *
     * @return Una cadena que representa el producto.
     */
    @Override
    public String toString() {
        return "[" + numero_referencia + "] " + id_cliente + ": " + id_pez +  " (" + String.format("%.2f", porcentajeEnviado()) + "%)";
    }


}
