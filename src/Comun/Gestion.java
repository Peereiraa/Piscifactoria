package Comun;

import java.util.HashMap;
import java.util.Map;

public class Gestion {
    private Map<String, Integer> compras;

    public Gestion(){
        compras = new HashMap<>();
    }

    public void registrarCompras(String nombrePez, int cantidad){
        compras.put(nombrePez, compras.getOrDefault(nombrePez, 0) + cantidad);
    }


    public int getCantidadComprada(String nombrePez){
        return compras.getOrDefault(nombrePez, 0);
    }

    
}
