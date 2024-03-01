package Partida;

public class Edificio {
    private boolean disponible;
    private int capacidad;
    private Comida comida;

    public Edificio(boolean disponible, int capacidad, Comida comida){
        this.disponible = disponible;
        this.capacidad = capacidad;
        this.comida = comida;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Comida getComida() {
        return comida;
    }

    public void setComida(Comida comida) {
        this.comida = comida;
    }

    

    
}
