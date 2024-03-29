package Piscifactorias;

import java.util.ArrayList;

import Pez.Pez;
import Pez.PecesMar.DeMar;
import Pez.PecesRio.DeRio;
import Registro.Log;
import Tanques.*;

/**
 * Clase que representa una piscifactoría en el simulador.
 */
public class Piscifactoria {
    protected String nombre;
    protected int comidaActual;
    protected int comidaMaxima;

    protected static Log log = Log.getInstance();

    /**
     * Lista de tanques en la piscifactoría.
     */
    protected ArrayList<Tanque<? extends Pez>> tanque;

    /**
     * Constructor que inicializa el nombre y la lista de tanques de la
     * piscifactoría.
     *
     * @param nombre El nombre de la piscifactoría.
     */
    public Piscifactoria(String nombre) {
        this.tanque = new ArrayList<>();
        this.nombre = nombre;
    }

    /**
     * Muestra el estado de la piscifactoría.
     *
     * @param p La piscifactoría de la cual mostrar el estado.
     */
    public void showStatus(Piscifactoria p) {
        try {
            for (Tanque<? extends Pez> t : tanque) {
                System.out.println("==============" + p.getNombre() + " ============");
                System.out.println("Comida de la Piscifactoria: " + p.getComidaActual());
                System.out.println("Tanques: " + t.getNumTanque());
                System.out.println("Ocupacion: " + t.pecesVivos() + "/" + t.getEspacio() + " ["
                        + (t.getEspacio() != 0 ? (t.pecesVivos() / t.getEspacio()) * 100 : 0) + "%]");
                System.out.println("Peces vivos: " + t.pecesVivos() + "/" + t.pecesTotales() + " ["
                        + (t.pecesTotales() != 0 ? (t.pecesVivos() / t.pecesTotales()) * 100 : 0) + "%]");
                System.out.println("Peces alimentados: " + t.pezAlimentado() + "/" + t.pecesVivos() + " ["
                        + (t.pecesVivos() != 0 ? (t.pezAlimentado() / t.pecesVivos()) * 100 : 0) + "%]");
                System.out.println("Peces adultos: " + t.pezAdulto() + "/" + t.pecesVivos() + " ["
                        + (t.pecesVivos() != 0 ? (t.pezAdulto() / t.pecesVivos()) * 100 : 0) + "%]");
                System.out.println("Hembras / Machos: " + t.hembra() + "/" + t.macho());
                System.out.println("Fértiles: " + (t.pecesVivos() != 0 ? (t.fertil() / t.pecesVivos()) : 0));
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Añade un tanque a la piscifactoría.
     *
     * @param t El tanque a añadir.
     */
    public void añadirTanque(Tanque<? extends Pez> t) {
        tanque.add(t);
    }

    /**
     * Obtiene la cantidad total de peces vivos en la piscifactoría.
     *
     * @return La cantidad total de peces vivos.
     */
    public int getPecesVivos() {
        int pecesVivos = 0;
        for (Tanque<? extends Pez> t : tanque) {
            pecesVivos += t.pecesVivos();
        }

        return pecesVivos;
    }

    /**
     * Obtiene la cantidad total de peces alimentados en la piscifactoría.
     *
     * @return La cantidad total de peces alimentados.
     */
    public int getPecesAlimentados() {
        int pecesAlimentados = 0;
        for (Tanque<? extends Pez> t : tanque) {
            pecesAlimentados += t.pezAlimentado();
        }

        return pecesAlimentados;
    }

    /**
     * Obtiene el número total de peces (incluyendo todos los estados) en la
     * piscifactoría.
     *
     * @return El número total de peces.
     */
    public int getNumPeces() {
        int numPeces = 0;
        for (Tanque<? extends Pez> t : tanque) {
            numPeces += t.pecesTotales();
        }

        return numPeces;
    }

    /**
     * Obtiene el número total de peces de río vivos en la piscifactoría.
     *
     * @return El número total de peces de río vivos.
     */
    public int getNumeroPecesRio() {
        int totalPecesRio = 0;
        try {
            for (Tanque<? extends Pez> t : tanque) {
                for (Pez pez : t.getPeces()) {
                    if (pez instanceof DeRio && pez.isVivo()) {
                        totalPecesRio++;
                    }
                }
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
        return totalPecesRio;
    }

    /**
     * Obtiene el número total de peces de mar vivos en la piscifactoría.
     *
     * @return El número total de peces de mar vivos.
     */
    public int getNumeroPecesMar() {
        int totalPecesMar = 0;
        try {
            for (Tanque<? extends Pez> t : tanque) {
                for (Pez pez : t.getPeces()) {
                    if (pez instanceof DeMar && pez.isVivo()) {
                        totalPecesMar++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalPecesMar;
    }

    /**
     * Muestra el estado de los peces en un tanque específico.
     *
     * @param i El índice del tanque del cual mostrar el estado.
     */

    public void estadoPeces(int i) {
        tanque.get(i).mostrarEstadoPeces();
    }

    /**
     * Avanza un día en la simulación para todos los tanques de la piscifactoría.
     */
    public void nextDay() {
        for (Tanque<? extends Pez> t : tanque) {
            t.nextDay(comidaActual);
        }

    }

    /**
     * Comprueba el espacio disponible en los tanques para un tipo de pez
     * específico.
     *
     * @param p El pez para el cual se busca espacio.
     * @return Una lista de tanques disponibles para el pez.
     */
    public ArrayList<Tanque<? extends Pez>> comprobarEspacioTanque(Pez p) {
        ArrayList<Tanque<? extends Pez>> tanquesDispos = new ArrayList<>();
        try {
            for (int i = 0; i < tanque.size(); i++) {
                if ((this instanceof PiscifactoriaMar && p instanceof DeMar)
                        || (this instanceof PiscifactoriaRio && p instanceof DeRio)) {
                    if (tanque.get(i).isEmpty()) {
                        tanquesDispos.add(tanque.get(i));
                        System.out.println((i + 1) + " Tanque disponible en la piscifactoria " + this.getNombre());
                    } else if (tanque.get(i).hayEspacio()) {
                        if ((this instanceof PiscifactoriaRio && p instanceof DeRio)
                                || (this instanceof PiscifactoriaMar && p instanceof DeMar)) {
                            String pezAdmitido = this.tanque.get(i).getPeces().get(0).getClass().getName();
                            if (pezAdmitido.equals(p.getClass().getName())) {
                                tanquesDispos.add(this.tanque.get(i));
                                System.out.println(
                                        (i + 1) + " Tanque disponible de la piscifactoria " + this.getNombre());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
        return tanquesDispos;
    }

    /**
     * Verifica si la piscifactoría está vacía.
     *
     * @return `true` si la piscifactoría está vacía, `false` de lo contrario.
     */
    public boolean estaVacio() {
        return tanque.isEmpty();
    }

    /**
     * Obtiene el nombre de la piscifactoría.
     *
     * @return El nombre de la piscifactoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la piscifactoría.
     *
     * @param nombre El nuevo nombre para la piscifactoría.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la lista de tanques en la piscifactoría.
     *
     * @return La lista de tanques en la piscifactoría.
     */
    public ArrayList<Tanque<? extends Pez>> getTanque() {
        return tanque;
    }

    /**
     * Establece la lista de tanques en la piscifactoría.
     *
     * @param tanque La nueva lista de tanques para la piscifactoría.
     */
    public void setTanque(ArrayList<Tanque<? extends Pez>> tanque) {
        this.tanque = tanque;
    }

    /**
     * Obtiene la cantidad actual de comida en la piscifactoría.
     *
     * @return La cantidad actual de comida.
     */
    public int getComidaActual() {
        return comidaActual;
    }

    /**
     * Establece la cantidad actual de comida en la piscifactoría.
     *
     * @param comidaActual La nueva cantidad actual de comida.
     */
    public void setComidaActual(int comidaActual) {
        this.comidaActual = comidaActual;
    }

    /**
     * Obtiene la capacidad máxima de comida que puede almacenar la piscifactoría.
     *
     * @return La capacidad máxima de comida que puede almacenar.
     */
    public int getComidaMaxima() {
        return comidaMaxima;
    }

    /**
     * Establece la capacidad máxima de comida que puede almacenar la piscifactoría.
     *
     * @param comidaMaxima La nueva capacidad máxima de comida.
     */
    public void setComidaMaxima(int comidaMaxima) {
        this.comidaMaxima = comidaMaxima;
    }

    /**
     * Verifica si la piscifactoría está llena de comida.
     *
     * @return `true` si la piscifactoría está llena de comida, `false` de lo
     *         contrario.
     */
    public boolean estaLlena() {
        return comidaActual >= comidaMaxima;
    }

}
