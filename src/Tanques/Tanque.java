package Tanques;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import Pez.*;
import Piscifactorias.Piscifactoria;
import Registro.Log;

/**
 * Clase que representa un tanque en una piscifactoría, que alberga peces.
 * 
 * @param <T> Tipo de pez que puede contener el tanque (extiende la clase Pez).
 */
public class Tanque<T extends Pez> {

    /**
     * Array de peces muertos
     */
    protected ArrayList<Pez> muertos;

    /**
     * Array de peces vivos
     */
    protected ArrayList<Pez> peces;
    protected int numTanque;
    protected String tipo;
    protected int espacio;
    protected int pecesVivos;
    protected int pecesMuertos;
    protected Piscifactoria p;

    protected static Log log = Log.getInstance();

    Scanner sc = new Scanner(System.in);

    /**
     * Constructor de la clase Tanque.
     * 
     * @param espacio   La capacidad del tanque.
     * @param numTanque El número de identificación del tanque.
     * @param p         La piscifactoría a la que pertenece el tanque.
     */
    public Tanque(int espacio, int numTanque, Piscifactoria p) {
        this.p = p;
        this.numTanque = numTanque;
        this.espacio = espacio;
        this.muertos = new ArrayList<>();
        this.peces = new ArrayList<>();
        if (this.espacio == 25) {
            this.tipo = "rio";
        } else if (this.espacio == 100) {
            this.tipo = "mar";
        }
    }

    /**
     * Muestra el estado del tanque, incluyendo la ocupación, cantidad de peces
     * vivos, peces alimentados, peces adultos, y otros detalles.
     */
    public void showStatus() {
        try {
            int sexo = macho();
            System.out.println("=============== Tanque " + numTanque + " ===============\r");
            System.out.println("Ocupación: " + peces.size() + "/" + this.getEspacio() + " ["
                    + (this.getEspacio() != 0 ? (peces.size() / this.getEspacio()) * 100 : 0) + "%]");
            System.out.println("Peces vivos: " + pecesVivos() + "/" + peces.size() + " ["
                    + (peces.size() != 0 ? (pecesVivos() / peces.size()) * 100 : 0) + "%]");
            System.out.println("Peces alimentados: " + pezAlimentado() + "/" + peces.size() + " ["
                    + (peces.size() != 0 ? (pezAlimentado() / peces.size()) * 100 : 0) + "%]");
            System.out.println("Peces adultos: " + pezAdulto() + "/" + peces.size() + " ["
                    + (peces.size() != 0 ? (pezAdulto() / peces.size()) * 100 : 0) + "%]");
            System.out.println("Hembras / Machos: " + sexo + " / " + (peces.size() - sexo) + " ["
                    + (peces.size() != 0 ? (sexo / peces.size()) * 100 : 0) + "%]");
            System.out.println("Fértiles: " + fertil() + " / " + pecesVivos() + " ["
                    + (pecesVivos() != 0 ? (fertil() / pecesVivos()) * 100 : 0) + "%]");
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }


    public int eliminarPecesMaduros(int max){
        int pecesAMandar = 0;

        for(int i = 0; i < peces.size() && i < max; i++){
            if(peces.get(i).getAdulto()){
                pecesAMandar++;
                peces.remove(i);
            }
        }

        return pecesAMandar;
    }

    /**
     * Obtiene el número total de peces en el tanque (vivos y muertos).
     * 
     * @return El número total de peces en el tanque.
     */
    public int pecesTotales() {

        return peces.size() + muertos.size();
    }

    /**
     * Elimina un pez del tanque y lo agrega a la lista de peces muertos.
     * 
     * @param p El pez que ha muerto y debe ser eliminado del tanque.
     */
    public void pezMuere(Pez p) {
        try {
            Iterator<Pez> pezIterator = peces.iterator();
            while (pezIterator.hasNext()) {
                Pez pez = pezIterator.next();
                if (pez.equals(p)) {
                    pezIterator.remove();
                    break;
                }
            }
            this.muertos.add(p);
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Obtiene la cantidad de peces vivos en el tanque.
     * 
     * @return La cantidad de peces vivos en el tanque.
     */
    public int pecesVivos() {

        return peces.size();
    }

    /**
     * Obtiene la cantidad de peces alimentados en el tanque.
     * 
     * @return La cantidad de peces alimentados en el tanque.
     */
    public int pezAlimentado() {
        int pecesAlimentados = 0;

        for (Pez p : peces) {
            if (p.getAlimentado()) {
                pecesAlimentados++;
            }
        }

        return pecesAlimentados;
    }

    /**
     * Obtiene la cantidad de peces adultos en el tanque.
     * 
     * @return La cantidad de peces adultos en el tanque.
     */
    public int pezAdulto() {
        int pecesAdultos = 0;

        for (Pez p : peces) {
            if (p.getAdulto()) {
                pecesAdultos++;
            }
        }

        return pecesAdultos;
    }

    /**
     * Obtiene la cantidad de peces machos en el tanque.
     * 
     * @return La cantidad de peces machos en el tanque.
     */
    public int macho() {
        int machos = 0;
        for (Pez p : peces) {
            if (p.getSexo()) {
                machos++;
            }

        }

        return machos;
    }

    /**
     * Obtiene la cantidad de peces hembras en el tanque.
     * 
     * @return La cantidad de peces hembras en el tanque.
     */
    public int hembra() {
        int hembras = 0;
        for (Pez p : peces) {
            if (!p.getSexo()) {
                hembras++;
            }
        }

        return hembras;
    }

    /**
     * Obtiene la cantidad de peces fértiles en el tanque.
     * 
     * @return La cantidad de peces fértiles en el tanque.
     */
    public int fertil() {
        int fertiles = 0;
        for (Pez p : peces) {
            if (p.isFertil()) {
                fertiles++;
            }

        }

        return fertiles;
    }

    /**
     * Obtiene un pez muerto del tanque, si hay alguno.
     * 
     * @return El primer pez muerto en la lista, o null si no hay peces muertos.
     */
    public Pez obtenerPezMuerto() {
        if (muertos.size() > 0) {
            return muertos.get(0);
        } else {
            return null;
        }

    }

    /**
     * Obtiene un pez fértil del tanque, según el sexo especificado.
     * 
     * @param sexo El sexo del pez que se está buscando (true para macho, false para
     *             hembra).
     * @return Un pez fértil del tanque o null si no hay peces fértil del género
     *         especificado.
     */
    public Pez obtenerPezFertil(boolean sexo) {
        Random rd = new Random();
        List<Pez> pecesFertiles = peces.stream().filter(p -> p.getSexo() == sexo && p.isFertil())
                .collect(Collectors.toList());
        if (pecesFertiles.size() == 0) {
            return null;
        }
        int random = rd.nextInt(pecesFertiles.size());
        return pecesFertiles.get(random);

    }

    /**
     * Obtiene la cantidad de espacio libre en el tanque (sin contar los peces
     * muertos).
     * 
     * @return La cantidad de espacio libre en el tanque.
     */
    public int espacioLibreTanque() {
        return this.espacio - peces.size() - muertos.size();
    }

    /**
     * Elimina un pez muerto de la lista de peces muertos.
     * 
     * @param p El pez muerto que se eliminará de la lista.
     */
    public void comerPezMuerto(Pez p) {
        muertos.remove(p);
    }

    /**
     * Elimina un pez vivo de la lista de peces en el tanque.
     * 
     * @param p El pez que se eliminará de la lista de peces vivos en el tanque.
     */
    public void removerPezVivo(Pez p) {
        peces.remove(p);
    }

    /**
     * Añade un pez a la lista de peces en el tanque.
     * 
     * @param p El pez que se añadirá al tanque.
     */
    public void añadirLosHijos(Pez p) {
        this.peces.add(p);
    }

    /**
     * Vacía el tanque, eliminando todos los peces y peces muertos.
     */
    public void vaciarTanque() {
        peces.clear();
        muertos.clear();
    }

    /**
     * Simula el paso de un día en el tanque, haciendo que los peces crezcan y se
     * reproduzcan.
     * 
     * @param comida La cantidad de comida disponible para los peces.
     */
    public void nextDay(int comida) {
        try {
            for (int i = 0; i < peces.size(); i++) {
                Pez pezActual = peces.get(i);
                if (pezActual != null) {
                    pezActual.grow(this, comida);
                    pezActual.reproducir(this);
                }
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Limpia la lista de peces muertos en el tanque.
     */
    public void limpiarPecesMuertos() {
        muertos.clear();
    }

    /**
     * Establece la cantidad de comida disponible en la piscifactoría asociada al
     * tanque.
     * 
     * @param comida La cantidad de comida disponible.
     */
    public void setCantidadComida(int comida) {
        if (p != null) {
            p.setComidaActual(comida);
        } else {
            System.out.println("Error: La instancia de Piscifactoria no está inicializada.");
        }
    }

    public void sellFish(List<Pez> pecesAVender){
        for(Pez pez : pecesAVender){
            peces.remove(pez);
        }
    }

    /**
     * Obtiene la cantidad de comida disponible en la piscifactoría asociada al
     * tanque.
     * 
     * @return La cantidad de comida disponible.
     */
    public int getCantidadComida() {
        return p.getComidaActual();
    }

    /**
     * Verifica si hay espacio disponible en el tanque para agregar más peces.
     * 
     * @return true si hay espacio disponible, false de lo contrario.
     */
    public boolean hayEspacio() {
        if (this.espacio > peces.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Agrega un pez al tanque.
     * 
     * @param p El pez a agregar al tanque.
     */
    public void addFish(Pez p) {
        this.peces.add(p);
    }

    /**
     * Obtiene la lista de peces en el tanque.
     * 
     * @return La lista de peces en el tanque.
     */
    public ArrayList<Pez> getPeces() {
        return peces;
    }

    /**
     * Establece la lista de peces en el tanque.
     * 
     * @param peces La nueva lista de peces para establecer en el tanque.
     */
    public void setPeces(ArrayList<Pez> peces) {
        this.peces = peces;
    }

    /**
     * Obtiene el número identificador del tanque.
     * 
     * @return El número identificador del tanque.
     */
    public int getNumTanque() {
        return numTanque;
    }

    /**
     * Muestra el estado de cada pez en el tanque.
     */
    public void mostrarEstadoPeces() {
        for (Pez p : peces) {
            p.showStatus();
        }
    }

    /**
     * Obtiene el tipo de tanque (rio o mar).
     * 
     * @return El tipo de tanque.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de tanque (rio o mar).
     * 
     * @param tipo El nuevo tipo de tanque.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Establece el número identificador del tanque.
     * 
     * @param numTanque El nuevo número identificador del tanque.
     */
    public void setNumTanque(int numTanque) {
        this.numTanque = numTanque;
    }

    /**
     * Obtiene el espacio disponible en el tanque.
     * 
     * @return El espacio disponible en el tanque.
     */
    public int getEspacio() {
        return espacio;
    }

    /**
     * Establece el espacio disponible en el tanque.
     * 
     * @param espacio El nuevo espacio disponible en el tanque.
     */
    public void setEspacio(int espacio) {
        this.espacio = espacio;
    }

    /**
     * Obtiene la lista de peces muertos en el tanque.
     * 
     * @return La lista de peces muertos en el tanque.
     */
    public ArrayList<Pez> getMuertos() {
        return muertos;
    }

    /**
     * Verifica si el tanque está vacío.
     * 
     * @return `true` si el tanque está vacío, `false` si contiene peces.
     */
    public boolean isEmpty() {
        if (peces.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
