package Comun;

import java.util.HashMap;
import java.util.Map;


/**
 * Clase que gestiona las transacciones de compra y venta de peces.
 */
public class Gestion {
    private Map<String, Integer> compras;
    private Map<String, Integer> ventas;

    /**
     * Constructor de la clase Gestion.
     * 
     * Inicializa los mapas de compras y ventas.
     */
    public Gestion(){
        compras = new HashMap<>();
        ventas = new HashMap<>();
        
    }

    /**
     * Registra una compra de peces.
     * 
     * @param nombrePez El nombre del pez comprado.
     * @param cantidad La cantidad de peces comprados.
     */
    public void registrarCompras(String nombrePez, int cantidad){
        compras.put(nombrePez, compras.getOrDefault(nombrePez, 0) + cantidad);
    }

    /**
     * Registra una venta de peces.
     * 
     * @param nombrePez El nombre del pez vendido.
     * @param cantidad La cantidad de peces vendidos.
     */
    public void registrarVenta(String nombrePez, int cantidad){ 
        ventas.put(nombrePez, ventas.getOrDefault(nombrePez, 0) + cantidad);
    }

    /**
     * Obtiene la cantidad total comprada de un tipo de pez.
     * 
     * @param nombrePez El nombre del pez del cual obtener la cantidad comprada.
     * @return La cantidad total comprada del pez especificado.
     */
    public int getCantidadComprada(String nombrePez){
        return compras.getOrDefault(nombrePez, 0);
    }

    /**
     * Obtiene la cantidad total vendida de un tipo de pez.
     * 
     * @param nombrePez El nombre del pez del cual obtener la cantidad vendida.
     * @return La cantidad total vendida del pez especificado.
     */
    public int getCantidadVendida(String nombrePez){ 
        return ventas.getOrDefault(nombrePez, 0);
    }

    
}
