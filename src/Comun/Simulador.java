package Comun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import Pez.Pez;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;
import Pez.PecesMar.Besugo;
import Pez.PecesMar.Caballa;
import Pez.PecesMar.Rodaballo;
import Pez.PecesMar.Robalo;
import Pez.PecesMar.Sargo;
import Pez.PecesMixtos.Dorada;
import Pez.PecesMixtos.LubinaEuropea;
import Pez.PecesRio.Carpa;
import Pez.PecesRio.Carpa_Plateada;
import Pez.PecesRio.LucioDelNorte;
import Pez.PecesRio.Pejerrey;
import Pez.PecesRio.PercaEuropea;
import Piscifactorias.Piscifactoria;
import Piscifactorias.PiscifactoriaMar;
import Piscifactorias.PiscifactoriaRio;
import Tanques.Tanque;

/**
 * Esta clase representa un simulador para gestionar una piscifactoría.
 * Permite realizar diversas operaciones relacionadas con la simulación,
 * como la creación de piscifactorías, el manejo de días pasados, la selección
 * de tipos de peces, etc.
 *
 * @author Pablo
 * @version 1.0
 */

public class Simulador {
    static Scanner sc = new Scanner(System.in);
    protected int diasPasados;
    protected static Monedas monedas = Monedas.getInstance();
    protected Pez escogerPez;

    /**
     * Arreglo de nombres de peces disponibles en la simulación.
     */

    protected String[] peces = { AlmacenPropiedades.BESUGO.getNombre(), AlmacenPropiedades.CABALLA.getNombre(),
            AlmacenPropiedades.ROBALO.getNombre(),
            AlmacenPropiedades.RODABALLO.getNombre(), AlmacenPropiedades.SARGO.getNombre(),
            AlmacenPropiedades.DORADA.getNombre(),
            AlmacenPropiedades.LUBINA_EUROPEA.getNombre(), AlmacenPropiedades.CARPA_PLATEADA.getNombre(),
            AlmacenPropiedades.CARPA.getNombre(),
            AlmacenPropiedades.LUCIO_NORTE.getNombre(), AlmacenPropiedades.PEJERREY.getNombre(),
            AlmacenPropiedades.PERCA_EUROPEA.getNombre() };

    /**
     * Lista de piscifactorías en la simulación.
     */

    protected ArrayList<Piscifactoria> piscifactorias;
    protected String nombrePartida;
    protected String nombrePisci;
    protected Gestion g;

    /**
     * Constructor por defecto de la clase Simulador.
     * Inicializa la lista de piscifactorías y realiza la inicialización
     * necesaria para la simulación.
     */
    public Simulador() {
        piscifactorias = new ArrayList<>();
        g = new Gestion();
        init();
    }

    /**
     * Inicializa la simulación estableciendo valores iniciales.
     * Este método debe ser llamado después de la creación de un objeto
     * Simulador para preparar el entorno de simulación. Crea la primera
     * piscifactoria con los valores iniciales de Rio y de 25 de comida
     */
    public void init() {
        System.out.println("Introduce nombre de partida: ");
        nombrePartida = sc.nextLine();
        System.out.println("Introduce nombre de la piscifactoria: ");
        nombrePisci = sc.nextLine();
        Piscifactoria p = new PiscifactoriaRio(nombrePisci);
        piscifactorias.add(p);
        p.setComidaActual(25);

    }

    /**
     * Muestra el menú de opciones para la simulación y realiza las acciones
     * correspondientes según la opción seleccionada por el usuario.
     */

    public void menu() {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println("---------INFORMACION---------\n");
            System.out.println("Tu monedero: " + monedas.getMonedas());
            System.out.println("Dia: " + this.diasPasados);
            System.out.println("");
            System.out.println("Menú:");
            System.out.println("1. Estado general");
            System.out.println("2. Estado piscifactoría");
            System.out.println("3. Estado tanques");
            System.out.println("4. Informes");
            System.out.println("5. Ictiopedia");
            System.out.println("6. Pasar día");
            System.out.println("7. Comprar comida");
            System.out.println("8. Comprar peces");
            System.out.println("9. Vender peces");
            System.out.println("10. Limpiar tanques");
            System.out.println("11. Vaciar tanque");
            System.out.println("12. Mejorar");
            System.out.println("13. Pasar varios días");
            System.out.println("14. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Opción 1: Estado general");
                    showGeneralStatus();
                    break;
                case 2:
                    System.out.println("Opción 2: Estado piscifactoría");
                    showSpecificStatus();
                    break;
                case 3:
                    System.out.println("Opción 3: Estado tanques");
                    showTankStatus();
                    break;
                case 4:
                    System.out.println("Opción 4: Informes");
                    showStats();
                    break;
                case 5:
                    System.out.println("Opción 5: Ictiopedia");
                    showIctio();
                    break;
                case 6:
                    System.out.println("Opción 6: Pasar día");
                    nextDay();
                    break;
                case 7:
                    System.out.println("Opción 7: Comprar comida");
                    addFood();
                    break;
                case 8:
                    System.out.println("Opción 8: Comprar peces");
                    addFish();
                    break;
                case 9:
                    System.out.println("Opción 9: Vender peces");
                    sell();
                    break;
                case 10:
                    System.out.println("Opción 10: Limpiar tanques");
                    cleanTank();
                    break;
                case 11:
                    System.out.println("Opción 11: Vaciar tanque");
                    emptyTank();
                    break;
                case 12:
                    System.out.println("Opción 12: Mejorar");
                    upgrade();
                    break;
                case 13:
                    System.out.println("Opción 13: Pasar varios días");
                    // Implementar la opción 13: Pasar varios días
                    break;
                case 14:
                    System.out.println("Saliendo del programa.");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción válida.");

            }
        }
    }

    /**
     * Muestra el estado de las piscifactorías, incluyendo la cantidad de peces
     * vivos,
     * la cantidad total de peces, y el espacio total disponible en cada tanque.
     */

    public void menuPisc() {
        int contador = 0;
        System.out.println("--------------------------- Piscifactorías ---------------------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");

        for (Piscifactoria p : piscifactorias) {
            contador++;
            for (Tanque<? extends Pez> t : p.getTanque()) {
                System.out.println(contador + ".- " + p.getNombre() + " [" + t.pecesVivos() + " | " + t.pecesTotales()
                        + " | " + t.getEspacio() + "]");
            }
        }

    }

    /**
     * Permite al usuario seleccionar una piscifactoría a partir de la lista de
     * piscifactorías disponibles.
     * Muestra la lista de piscifactorías y devuelve el índice de la piscifactoría
     * seleccionada.
     * Si se selecciona la opción 0, devuelve -1 indicando ninguna selección válida.
     *
     * @return El índice de la piscifactoría seleccionada o -1 si no se selecciona
     *         ninguna.
     */

    public int selectPisc() {
        menuPisc();
        int opc = sc.nextInt();
        for (opc = 0; opc < piscifactorias.size(); opc++) {
            System.out.println(piscifactorias.get(opc).getNombre());
        }

        if (opc == 0) {
            return -1;
        } else {
            return opc;
        }
    }

    /**
     * Permite al usuario seleccionar un tanque de una piscifactoría específica.
     * Muestra la lista de tanques disponibles y devuelve el índice del tanque
     * seleccionado.
     * Si se selecciona la opción 0, devuelve -1 indicando ninguna selección válida.
     *
     * @param p La piscifactoría para la cual se debe seleccionar un tanque.
     * @return El índice del tanque seleccionado o -1 si no se selecciona ninguno.
     */

    public int selectTank(Piscifactoria p) {
        System.out.println("Seleccione un Tanque");
        System.out.println("-----------Tanque------------");

        for (int i = 0; i < p.getTanque().size(); i++) {
            System.out.println("Tanque: " + (i + 1) + " | " + p.getTanque().get(i).getTipo());
        }
        int opc = sc.nextInt();
        if (opc != 0) {
            return opc - 1;
        } else {
            return -1;
        }
    }

    /**
     * Muestra el estado de los tanques de todas las piscifactorías.
     * El usuario puede seleccionar un tanque específico para ver más detalles.
     */

    public void showTankStatus() {
        for (Piscifactoria p : piscifactorias) {
            int selectedTank = selectTank(p);
            p.estadoPeces(selectedTank);
        }
    }

    public void showStats() {
        System.out.println("Desglose de los peces en el sistema: ");
        System.out.println("-------------------------------------");

        for (int i = 1; i <= 12; i++) {
            PecesDatos pez = null;

            switch (i) {
                case 1:
                    pez = AlmacenPropiedades.BESUGO;
                    break;
                case 2:
                    pez = AlmacenPropiedades.CABALLA;
                    break;
                case 3:
                    pez = AlmacenPropiedades.ROBALO;
                    break;
                case 4:
                    pez = AlmacenPropiedades.RODABALLO;
                    break;
                case 5:
                    pez = AlmacenPropiedades.SARGO;
                    break;
                case 6:
                    pez = AlmacenPropiedades.CARPA_PLATEADA;
                    break;
                case 7:
                    pez = AlmacenPropiedades.CARPA;
                    break;
                case 8:
                    pez = AlmacenPropiedades.LUCIO_NORTE;
                    break;
                case 9:
                    pez = AlmacenPropiedades.PEJERREY;
                    break;
                case 10:
                    pez = AlmacenPropiedades.PERCA_EUROPEA;
                    break;
                case 11:
                    pez = AlmacenPropiedades.DORADA;
                    break;
                case 12:
                    pez = AlmacenPropiedades.LUBINA_EUROPEA;
                    break;
            }

            if (pez != null) {
                String nombrePez = pez.getNombre();
                int cantidadComprada = g.getCantidadComprada(nombrePez);
                System.out.println(pez.getNombre());
                System.out.println("Comprados: " + cantidadComprada);
                System.out.println("Vendidos: ");
            }

        }
    }

    /**
     * Muestra el estado general de todas las piscifactorías, incluyendo el estado
     * de los tanques
     * y el número de días transcurridos en la simulación.
     */

    public void showGeneralStatus() {
        for (int i = 0; i < piscifactorias.size(); i++) {
            piscifactorias.get(i).showStatus(piscifactorias.get(i));
            System.out.println("Dia: " + this.diasPasados);
        }
    }

    /**
     * Muestra el estado específico de una piscifactoría seleccionada por el
     * usuario.
     * Permite al usuario seleccionar una piscifactoría y muestra el estado de todos
     * sus tanques.
     */

    public void showSpecificStatus() {
        System.out.println("Seleccione una piscifactoria");
        int piscifactoriaSeleccionada = selectPisc();

        if (piscifactoriaSeleccionada >= 1 && piscifactoriaSeleccionada <= piscifactorias.size()) {
            Piscifactoria p = piscifactorias.get(piscifactoriaSeleccionada - 1);

            System.out.println("Mostrando el estado de todos los tanques en " + p.getNombre() + ":");

            for (Tanque<? extends Pez> tanque : p.getTanque()) {
                tanque.showStatus();
                System.out.println();
            }
        } else {
            System.out.println("Opción no disponible");
        }
    }

    /**
     * Inicia el proceso de añadir peces a una piscifactoría.
     * Permite al usuario seleccionar un tipo de pez.
     */

    public void addFish() {
        seleccionarPez(menuPeces());
    }

    public void sell() {
        int totalPecesVendidos = 0;
        int totalMonedasGanadas = 0;
    
        // Iterar sobre todas las piscifactorías
        Iterator<Piscifactoria> iterator = piscifactorias.iterator();
        while (iterator.hasNext()) {
            Piscifactoria piscifactoria = iterator.next();
            System.out.println("Piscifactoría " + piscifactoria.getNombre() + ":");
    
            int pecesVendidosEnPiscifactoria = 0;
            int monedasGanadasEnPiscifactoria = 0;
    
        
            for (Tanque<? extends Pez> tanque : piscifactoria.getTanque()) {
                Iterator<Pez> pezIterator = tanque.getPeces().iterator();
                while (pezIterator.hasNext()) {
                    Pez pez = pezIterator.next();
                    if (pez.getAdulto() && pez.isVivo()) {
                        pecesVendidosEnPiscifactoria++;
                        monedasGanadasEnPiscifactoria += pez.getPezDato().getCoste();
                        pezIterator.remove();
                    }
                }
            }
    
            System.out.println(pecesVendidosEnPiscifactoria + " peces vendidos por " + monedasGanadasEnPiscifactoria + " monedas");
    
            // Actualizar el total de peces vendidos y el total de monedas ganadas
            totalPecesVendidos += pecesVendidosEnPiscifactoria;
            totalMonedasGanadas += monedasGanadasEnPiscifactoria;
        }
    }
    
    
    

    /**
     * Muestra información detallada sobre un tipo de pez seleccionado por el
     * usuario.
     * Permite al usuario seleccionar un pez de la lista e imprime sus propiedades,
     * como
     * nombre científico, cantidad de huevos, ciclo de vida, madurez, condiciones
     * óptimas,
     * costo y ganancias en monedas.
     */

    public void showIctio() {
        int opc = 0;
        for (int i = 0; i < this.peces.length; i++) {
            System.out.println(i + ". " + this.peces[i]);
        }

        System.out.println("Pez: ");
        opc = sc.nextInt();
        String nombre = this.peces[opc];
        PecesDatos p = AlmacenPropiedades.getPropByName(nombre);
        System.out.println("---------- PEZ " + p.getNombre() + " ----------");
        System.out.println("Nombre cientifico: " + p.getCientifico());
        System.out.println("Huevos: " + p.getHuevos());
        System.out.println("Ciclo: " + p.getCiclo());
        System.out.println("Madurez: " + p.getMadurez());
        System.out.println("Optimo: " + p.getOptimo());
        System.out.println("Coste: " + p.getCoste());
        System.out.println("Monedas: " + p.getMonedas());

    }

    /**
     * Avanza la simulación al siguiente día, aplicando los cambios correspondientes
     * en todas las piscifactorías.
     */

    public void nextDay() {
        for (Piscifactoria p : piscifactorias) {
            p.nextDay();
            diasPasados++;
        }
    }

    /**
     * Permite al usuario comprar comida para una piscifactoría seleccionada.
     * Muestra la cantidad de monedas disponibles y solicita la cantidad de comida a
     * comprar.
     * La comida comprada se agrega a la piscifactoría seleccionada, y las monedas
     * se deducen.
     */

    public void addFood() {
        int opc = selectPisc();
        if (opc != 0) {
            Piscifactoria p = piscifactorias.get(opc - 1);
            System.out.println("¿Cuanta comida quieres comprar? Pulsa 0 para cancelar");
            System.out.println("Tu monedero: " + monedas.getMonedas());
            int cantidad = sc.nextInt();

            switch (cantidad) {
                case 5:
                    if (cantidad <= p.getComidaMaxima() - p.getComidaActual()) {
                        p.setComidaActual(p.getComidaActual() + 5);
                        System.out.println("Añadiste 5 de comida");
                        monedas.setMonedas(monedas.getMonedas() - 5);

                    } else {
                        System.out.println("No tienes espacio para meter mas comida");
                    }
                    break;

                case 10:
                    if (cantidad <= p.getComidaMaxima() - p.getComidaActual()) {
                        p.setComidaActual(p.getComidaActual() + 10);
                        System.out.println("Añadiste 10 de comida");
                        monedas.setMonedas(monedas.getMonedas() - 10);
                    } else {
                        System.out.println("No tienes espacio para meter mas comida");
                    }
                    break;

                case 25:
                    if (cantidad <= p.getComidaMaxima() - p.getComidaActual()) {
                        p.setComidaActual(p.getComidaActual() + 25);
                        System.out.println("Añadiste 5 de comida");
                        monedas.setMonedas(monedas.getMonedas() - 25);
                    } else {
                        System.out.println("No tienes espacio para meter mas comida");
                    }
                    break;
            }
        } else {
            menu();
        }
    }

    /**
     * Crea y agrega un pez al tanque de la piscifactoría seleccionada según la
     * opción proporcionada.
     * El pez se selecciona según la opción y se verifica si hay fondos suficientes
     * antes de agregarlo.
     *
     * @param opc La opción seleccionada por el usuario para elegir el tipo de pez.
     */

    public void seleccionarPez(int opc) {
        Random rd = new Random();
        boolean sexo = rd.nextBoolean();
        int cantidadComprada = 1;

        switch (opc) {
            case 1:
                escogerPez = new Besugo(sexo, AlmacenPropiedades.BESUGO);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.BESUGO.getCoste()) {
                    System.out.println("BESUGO añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.BESUGO.getCoste());
                    g.registrarCompras(AlmacenPropiedades.BESUGO.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 2:
                escogerPez = new Caballa(sexo, AlmacenPropiedades.CABALLA);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.CABALLA.getCoste()) {
                    System.out.println("CABALLA añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.CABALLA.getCoste());
                    g.registrarCompras(AlmacenPropiedades.CABALLA.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 3:
                escogerPez = new Robalo(sexo, AlmacenPropiedades.ROBALO);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.ROBALO.getCoste()) {
                    System.out.println("ROBALO añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.ROBALO.getCoste());
                    g.registrarCompras(AlmacenPropiedades.ROBALO.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 4:
                escogerPez = new Rodaballo(sexo, AlmacenPropiedades.RODABALLO);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.RODABALLO.getCoste()) {
                    System.out.println("RODABALLO añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.RODABALLO.getCoste());
                    g.registrarCompras(AlmacenPropiedades.RODABALLO.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 5:
                escogerPez = new Sargo(sexo, AlmacenPropiedades.SARGO);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.SARGO.getCoste()) {
                    System.out.println("SARGO añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.SARGO.getCoste());
                    g.registrarCompras(AlmacenPropiedades.SARGO.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 6:
                escogerPez = new Carpa_Plateada(sexo, AlmacenPropiedades.CARPA_PLATEADA);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.CARPA_PLATEADA.getCoste()) {
                    System.out.println("CARPA_PLATEADA añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.CARPA_PLATEADA.getCoste());
                    g.registrarCompras(AlmacenPropiedades.CARPA_PLATEADA.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 7:
                escogerPez = new Carpa(sexo, AlmacenPropiedades.CARPA);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.CARPA.getCoste()) {
                    System.out.println("CARPA añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.CARPA.getCoste());
                    g.registrarCompras(AlmacenPropiedades.CARPA.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 8:
                escogerPez = new LucioDelNorte(sexo, AlmacenPropiedades.LUCIO_NORTE);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.LUCIO_NORTE.getCoste()) {
                    System.out.println("LUCIO_NORTE añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.LUCIO_NORTE.getCoste());
                    g.registrarCompras(AlmacenPropiedades.LUCIO_NORTE.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 9:
                escogerPez = new Pejerrey(sexo, AlmacenPropiedades.PEJERREY);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.PEJERREY.getCoste()) {
                    System.out.println("PEJERREY añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.PEJERREY.getCoste());
                    g.registrarCompras(AlmacenPropiedades.PEJERREY.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 10:
                escogerPez = new PercaEuropea(sexo, AlmacenPropiedades.PERCA_EUROPEA);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.PERCA_EUROPEA.getCoste()) {
                    System.out.println("PERCA_EUROPEA añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.PERCA_EUROPEA.getCoste());
                    g.registrarCompras(AlmacenPropiedades.PERCA_EUROPEA.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 11:
                escogerPez = new Dorada(sexo, AlmacenPropiedades.DORADA);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.DORADA.getCoste()) {
                    System.out.println("DORADA añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.DORADA.getCoste());
                    g.registrarCompras(AlmacenPropiedades.DORADA.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            case 12:
                escogerPez = new LubinaEuropea(sexo, AlmacenPropiedades.LUBINA_EUROPEA);
                if (!comprobarPecesDelTanque(escogerPez)) {
                    return;
                }
                if (monedas.getMonedas() > AlmacenPropiedades.LUBINA_EUROPEA.getCoste()) {
                    System.out.println("LUBINA_EUROPEA añadido correctamente");
                    monedas.setMonedas(monedas.getMonedas() - AlmacenPropiedades.LUBINA_EUROPEA.getCoste());
                    g.registrarCompras(AlmacenPropiedades.LUBINA_EUROPEA.getNombre(), cantidadComprada);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            default:
                System.out.println("Opción inválida");
                break;
        }
    }

    /**
     * Limpia los peces muertos de todos los tanques de la piscifactoría
     * seleccionada por el usuario.
     * El usuario debe seleccionar una piscifactoría válida antes de limpiar los
     * tanques.
     */

    public void cleanTank() {

        int piscifactoriaSeleccionada = selectPisc();
        if (piscifactoriaSeleccionada >= 1 && piscifactoriaSeleccionada <= piscifactorias.size()) {
            Piscifactoria p = piscifactorias.get(piscifactoriaSeleccionada - 1);
            for (Tanque<? extends Pez> t : p.getTanque()) {
                t.limpiarPecesMuertos();

            }
            System.out.println("Se han eliminado los peces muertos de todos los tanques de la piscifactoría "
                    + p.getNombre() + ".");
        } else {
            System.out.println("Piscifactoria no valida");
            menu();
        }

        System.out.println("Se han eliminado todos los peces muertos de todos los tanques");
    }

    /**
     * Vacía todos los peces de un tanque específico de la piscifactoría
     * seleccionada por el usuario.
     * El usuario debe seleccionar una piscifactoría y un tanque válidos antes de
     * vaciar el tanque.
     */

    public void emptyTank() {
        int piscifactoriaSeleccionada = selectPisc();

        if (piscifactoriaSeleccionada >= 1 && piscifactoriaSeleccionada <= piscifactorias.size()) {
            Piscifactoria p = piscifactorias.get(piscifactoriaSeleccionada - 1);
            int tanqueSeleccionado = selectTank(p);

            if (tanqueSeleccionado >= 0 && tanqueSeleccionado < p.getTanque().size()) {
                Tanque<? extends Pez> t = p.getTanque().get(tanqueSeleccionado);
                t.vaciarTanque();
                System.out.println("Se han eliminado todos los peces del tanque " + t.getTipo());
            } else {
                System.out.println("Tanque no válido");
            }
        } else {
            System.out.println("Piscifactoria no válida");
            menu();
        }
    }

    /**
     * Muestra un menú de selección de peces para que el usuario elija un tipo de
     * pez.
     * Devuelve la opción seleccionada por el usuario.
     *
     * @return La opción seleccionada por el usuario para elegir el tipo de pez.
     */

    public int menuPeces() {
        System.out.println("Selecciona un pez para añadir: ");
        System.out.println("----------PECES DE MAR----------");
        System.out.println("1. Besugo");
        System.out.println("2. Caballa");
        System.out.println("3. Róbalo");
        System.out.println("4. Rodaballo");
        System.out.println("5. Sargo");
        System.out.println(" ");
        System.out.println("----------PECES DE RIO----------");
        System.out.println("6. Carpa_Plateada");
        System.out.println("7. Carpa");
        System.out.println("8. Lucio del Norte");
        System.out.println("9. Pejerrey");
        System.out.println("10. Perca Europea");
        System.out.println(" ");
        System.out.println("----------PECES MIXTO----------");
        System.out.println("11. Dorada");
        System.out.println("12. Lubina Europea");

        int opc = sc.nextInt();

        return opc;
    }

    /**
     * Verifica la disponibilidad de tanques y permite al usuario seleccionar un
     * tanque para agregar un pez.
     * Selecciona el tanque disponible y añade el pez al tanque correspondiente.
     *
     * @param p El pez que se va a agregar al tanque.
     */

    public boolean comprobarPecesDelTanque(Pez p) {
        ArrayList<Tanque<? extends Pez>> tanquesDispos = new ArrayList<>();

        for (int i = 0; i < piscifactorias.size(); i++) {
            tanquesDispos.addAll(piscifactorias.get(i).comprobarEspacioTanque(p));
        }

        if (tanquesDispos.size() == 0) {
            System.out.println("No hay tanques disponibles");
            return false; // No hay tanques disponibles
        } else {
            System.out.println("Tanques disponibles:");
            for (int i = 0; i < tanquesDispos.size(); i++) {
                System.out.println((i + 1) + ". " + tanquesDispos.get(i).getTipo());
            }

            System.out.println("Selecciona una opción de los tanques disponibles");
            int opc = sc.nextInt();
            tanquesDispos.get(opc - 1).addFish(p);
            return true; // Hay tanques disponibles
        }
    }

    /**
     * Muestra un menú de opciones para mejorar o comprar edificios en la
     * simulación.
     * Permite al usuario seleccionar diferentes opciones, como comprar una
     * piscifactoría o mejorar tanques.
     */

    public void upgrade() {
        System.out.println("-------------MEJORAR-------------");
        System.out.println("1. Comprar edificios\n");
        System.out.println("(a) Piscifactoria");
        System.out.println("(b) Almacen Central\n");
        System.out.println("-----------------\n");
        System.out.println("2. Mejorar edificios\n");
        System.out.println("(a) Piscifactoria");
        System.out.println("I. Comprar Tanque");
        System.out.println("II. Aumentar almacén de comida\n");
        System.out.println("(b) Almacen Centrar");
        System.out.println("I. Aumentar capacidad\n");
        System.out.println("-----------------\n");
        System.out.println("3. Cancelar");

        int opc = sc.nextInt();

        switch (opc) {
            case 1:
                System.out.println("Selecciona el edificio a comprar: ");
                System.out.println("1. Piscifactoria");
                System.out.println("2. Almacen central");
                int tipoEdificio = sc.nextInt();

                switch (tipoEdificio) {
                    case 1:
                        comprarPiscifactoria();
                        break;

                    case 2:
                        comprarAlmacenCentral();
                        System.out.println("Compraste un Almacen Centrar");

                    default:
                        break;
                }
                break;

            case 2:
                System.out.println("Selecciona el edificio a mejorar: ");
                System.out.println("1. Piscifactoria");
                System.out.println("2. Almacen central");
                int tipoMejorar = sc.nextInt();
                switch (tipoMejorar) {
                    case 1:
                        System.out.println("1. Comprar Tanque");
                        System.out.println("2. Aumentar almacén de comida");
                        int seleccionarOpc = sc.nextInt();

                        switch (seleccionarOpc) {
                            case 1:
                                comprarTanques();
                                break;
                            case 2:
                                Piscifactoria p;
                                p = piscifactorias.get(selectPisc());
                                if (p instanceof PiscifactoriaRio) {
                                    if (monedas.getMonedas() >= 100) {
                                        monedas.setMonedas(monedas.getMonedas() - 100);
                                        if (p.getComidaMaxima() <= 225) {
                                            p.setComidaMaxima(p.getComidaMaxima() + 25);
                                        } else {
                                            System.out.println("Almacen al MAXIMO");
                                        }
                                    }
                                }
                                if (p instanceof PiscifactoriaMar) {
                                    if (monedas.getMonedas() >= 200) {
                                        monedas.setMonedas(monedas.getMonedas() - 200);
                                        if (p.getComidaMaxima() <= 900) {
                                            p.setComidaMaxima(p.getComidaMaxima() + 100);
                                        } else {
                                            System.out.println("Almacen al MAXIMO");
                                        }
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    case 2:
                        System.out.println("Aumentar capacidad de Almacen");
                        if (monedas.getMonedas() >= 100) {
                            monedas.setMonedas(monedas.getMonedas() - 100);

                        }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Permite al usuario comprar una nueva Piscifactoría, solicitando información
     * sobre el nombre y el tipo.
     * Verifica las monedas disponibles y realiza la compra si es posible.
     * Muestra mensajes adecuados según el resultado de la compra.
     */

    public void comprarPiscifactoria() {

        System.out.println("Precios: ");
        System.out.println("1. Piscifactoria de Rio - 500");
        System.out.println("2. Piscifactoria de Mar - 2000");
        System.out.println("¿Quieres comprar una Piscifactoria? (SI/NO)");
        String respuesta = sc.next();

        if (respuesta.equalsIgnoreCase("SI")) {
            System.out.println("Introduce el nombre de la Piscifactoria");
            String nombrePisci = sc.next();
            System.out.println("¿De que tipo quieres la Piscifactoria?");
            System.out.println("1. Rio");
            System.out.println("2. Mar");
            int tipoPisci = sc.nextInt();
            if (tipoPisci == 1) {
                if (monedas.getMonedas() >= 500) {
                    monedas.setMonedas(monedas.getMonedas() - 500);
                    PiscifactoriaRio nuevaPisci = new PiscifactoriaRio(nombrePisci);
                    piscifactorias.add(nuevaPisci);
                    System.out.println("Piscifactoria de Rio añadida CORRECTAMENTE");
                } else {
                    System.out.println("No tienes suficientes monedas para comprar");
                    comprarPiscifactoria();
                }

            } else if (tipoPisci == 2) {
                if (monedas.getMonedas() >= 2000) {
                    monedas.setMonedas(monedas.getMonedas() - 2000);
                    PiscifactoriaMar nuevaPisci = new PiscifactoriaMar(nombrePisci);
                    piscifactorias.add(nuevaPisci);
                    System.out.println("Piscifactoria de Mar añadida CORRECTAMENTE");
                } else {
                    System.out.println("No tienes suficientes monedas para comprar");
                }

            } else {
                System.out.println("Opcion no valida");
                upgrade();
            }
        } else {
            System.out.println("Vuelve pronto!");
            upgrade();
        }
    }

    /**
     * Permite al usuario comprar un nuevo tanque para una Piscifactoría
     * seleccionada.
     * Verifica las monedas disponibles y realiza la compra si es posible.
     * Muestra mensajes adecuados según el resultado de la compra.
     */

    public void comprarTanques() {
        System.out.println("¿Quieres comprar un Tanque? (SI/NO)");
        String respuesta = sc.next();

        if (respuesta.equalsIgnoreCase("SI")) {
            int piscifactoriaSeleccionada = selectPisc();

            if (piscifactoriaSeleccionada >= 1 && piscifactoriaSeleccionada <= piscifactorias.size()) {
                Piscifactoria p = piscifactorias.get(piscifactoriaSeleccionada - 1);

                if (p instanceof PiscifactoriaRio) {
                    if (monedas.getMonedas() >= 150 * p.getTanque().size()) {
                        monedas.setMonedas(monedas.getMonedas() - 150 * p.getTanque().size());
                        p.añadirTanque(new Tanque<>(25, p.getTanque().size(), p));
                        System.out.println("Tanque añadido correctamente.");
                    } else {
                        System.out.println("No tienes suficientes monedas para comprar un tanque.");
                    }
                } else if (p instanceof PiscifactoriaMar) {
                    if (monedas.getMonedas() >= 600 * p.getTanque().size()) {
                        monedas.setMonedas(monedas.getMonedas() - 600 * p.getTanque().size());
                        p.añadirTanque(new Tanque<>(100, p.getTanque().size(), p));
                        System.out.println("Tanque añadido correctamente.");
                    } else {
                        System.out.println("No tienes suficientes monedas para comprar un tanque.");
                    }
                }
            } else {
                System.out.println("Piscifactoria no válida.");
            }
        } else {
            System.out.println("Vuelve pronto!");
            upgrade();
        }
    }

    /**
     * Permite al usuario comprar un Almacén Central.
     * Verifica las monedas disponibles y realiza la compra si es posible.
     * Muestra mensajes adecuados según el resultado de la compra.
     */

    public void comprarAlmacenCentral() {
        System.out.println("¿Quieres comprar el AlmacenCentral? (SI/NO)");
        String respuesta = sc.next();

        if (respuesta.equalsIgnoreCase("SI")) {
            if (monedas.getMonedas() >= AlmacenCentral.precio) {
                monedas.setMonedas(monedas.getMonedas() - AlmacenCentral.precio);
                new AlmacenCentral();

            } else {
                System.out.println("No tienes suficiente dinero para comprar el Almacen");
            }

        } else {
            System.out.println("Vuelva pronto!");
            upgrade();
        }
    }

    /**
     * Método principal que crea una instancia de la clase Simulador y llama al
     * método menu() para iniciar la simulación.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este
     *             caso).
     */

    public static void main(String[] args) {
        Simulador s = new Simulador();
        s.menu();
    }

}
