package Comun;

import java.util.HashMap;
import java.util.Map;

public class Gestion {
    private Map<String, Integer> compras;
    private Map<String, Integer> ventas;

    public Gestion(){
        compras = new HashMap<>();
        ventas = new HashMap<>();
        
    }

    public void registrarCompras(String nombrePez, int cantidad){
        compras.put(nombrePez, compras.getOrDefault(nombrePez, 0) + cantidad);
    }

    public void registrarVenta(String nombrePez, int cantidad){ 
        ventas.put(nombrePez, ventas.getOrDefault(nombrePez, 0) + cantidad);
    }


    public int getCantidadComprada(String nombrePez){
        return compras.getOrDefault(nombrePez, 0);
    }

    public int getCantidadVendida(String nombrePez){ 
        return ventas.getOrDefault(nombrePez, 0);
    }

    
}
