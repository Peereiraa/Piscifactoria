package Comun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import Partida.Cargar;
import Partida.Guardar;
import Pez.Pez;
import Recompensas.Rewards;
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
import Registro.Log;
import Registro.Registro;
import Registro.Transcripciones;
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
    protected static Log log = Log.getInstance();
    protected static Registro registro = Registro.getInstance();
    protected static Transcripciones tr = Transcripciones.getInstance();
    protected Pez escogerPez;

    protected Guardar guardar;
    private Rewards rewards;

    protected ArrayList<String> pecesRio;
    protected ArrayList<String> pecesMar;
    protected ArrayList<String> pecesMixto;

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
    protected AlmacenCentral ac;
    private int numMachos = 0;
    private int numHembras = 0;

    /**
     * Constructor por defecto de la clase Simulador.
     * Inicializa la lista de piscifactorías y realiza la inicialización
     * necesaria para la simulación.
     */
    public Simulador() {
        piscifactorias = new ArrayList<>();
        g = new Gestion();
        rewards = new Rewards(this);
        guardar = new Guardar(this);

        init();
    }

    /**
     * Inicializa la simulación estableciendo valores iniciales.
     * Este método debe ser llamado después de la creación de un objeto
     * Simulador para preparar el entorno de simulación. Crea la primera
     * piscifactoria con los valores iniciales de Rio y de 25 de comida
     */
    public void init() {
        try {

            File logsFolder = new File("logs");
            File transcripcionesFolder = new File("transcripciones");
            File errorLogFile = new File(logsFolder, "0_errors.log");
            File carpeta = new File("rewards");
            if (!carpeta.exists()) {
                carpeta.mkdir();
                rewards.generarComida(1);
                rewards.generarComida(2);
                rewards.generarComida(3);
                rewards.generarComida(4);
                rewards.generarComida(5);

                rewards.generarMonedas(1);
                rewards.generarMonedas(2);
                rewards.generarMonedas(3);
                rewards.generarMonedas(4);
                rewards.generarMonedas(5);

                rewards.generarAlmacen('A');
                rewards.generarAlmacen('B');
                rewards.generarAlmacen('C');
                rewards.generarAlmacen('D');

                rewards.generarPiscifactoriaMar('A');
                rewards.generarPiscifactoriaMar('B');

                rewards.generarPiscifactoriaRio('A');
                rewards.generarPiscifactoriaRio('B');
            }


            if (!logsFolder.exists()) {
                logsFolder.mkdirs();
            }

            if (!transcripcionesFolder.exists()) {
                transcripcionesFolder.mkdirs();
            }

            if (!errorLogFile.exists()) {
                errorLogFile.createNewFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        File srcFolder = new File("src/saves");
        File[] files = srcFolder.listFiles((dir, name) -> name.endsWith(".save"));

        if (files != null && files.length > 0) {
            String rutaArchivo = files[0].getAbsolutePath();
            load();
            System.out.println("Partida cargada exitosamente desde " + rutaArchivo);
            return;
        }

        System.out.println("Introduce nombre de partida: ");
        nombrePartida = sc.nextLine();
        System.out.println("Introduce nombre de la piscifactoria: ");
        nombrePisci = sc.nextLine();
        registro.registrar(nombrePartida, "Inicio de la simulacion " + nombrePartida);
        tr.transcripcion(nombrePartida, "Dinero: " + String.valueOf(monedas.getMonedas()) + " monedas");
        Piscifactoria p = new PiscifactoriaRio(nombrePisci);
        p.setComidaActual(25);
        piscifactorias.add(p);
        pecesRio = new ArrayList<>();
        pecesMar = new ArrayList<>();
        pecesMixto = new ArrayList<>();
        for (int i = 0; i < this.peces.length; i++) {
            String nombrePez = this.peces[i];
            PecesDatos pezDatos = AlmacenPropiedades.getPropByName(nombrePez);
            if (pezDatos.getPiscifactoria().toString().equals("RIO")) {
                pecesRio.add(nombrePez);
            } else if (pezDatos.getPiscifactoria().toString().equals("DOUBLE")) {
                pecesMixto.add(nombrePez);
            } else {
                pecesMar.add(nombrePez);
            }
        }

        tr.transcripcion(nombrePartida, "Rio: ");
        for (String nombre : pecesRio) {
            tr.transcripcion(nombrePartida, "-" + nombre);
        }
        tr.transcripcion(nombrePartida, "Mar: ");
        for (String nombre : pecesMar) {
            tr.transcripcion(nombrePartida, "-" + nombre);
        }
        tr.transcripcion(nombrePartida, "Mixto: ");
        for (String nombre : pecesMixto) {
            tr.transcripcion(nombrePartida, "-" + nombre);
        }
        registro.registrar(nombrePartida, "Piscifactoria Inicial: " + nombrePisci);
        tr.transcripcion(nombrePartida, "----------");

        guardar.save();

    }

    /**
     * Carga una partida desde un archivo .save en la carpeta "src/saves".
     * Si encuentra al menos un archivo .save, carga el primero encontrado.
     *
     * @throws Exception si ocurre algún error durante la carga de la partida.
     */
    public void load() {
        try {
            File srcFolder = new File("src/saves");
            File[] files = srcFolder.listFiles((dir, name) -> name.endsWith(".save"));

            if (files != null && files.length > 0) {
                String rutaArchivo = files[0].getAbsolutePath();
                Cargar cargador = new Cargar(this);
                cargador.cargarPartida(rutaArchivo);
                System.out.println("Partida cargada exitosamente desde " + rutaArchivo);
            } else {
                System.out.println("No se encontraron archivos .save en la carpeta src.");
            }
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }


    public String getNombrePartida() {
        return nombrePartida;
    }



    public String[] getPeces() {
        return peces;
    }

    public boolean getAlmacenCentral(){
        return ac != null;
    }

    public void setAlmacenCentral(boolean tiene){
        if(!tiene){
            ac = new AlmacenCentral();
        }else{
            System.out.println("Ya tienes el almacen");
        }
    }

    public void setAlmacenCentral(AlmacenCentral ac) {
        this.ac = ac;
    }


    public ArrayList<Piscifactoria> getPiscifactorias() {
        return piscifactorias;
    }

    public void setPiscifactorias(ArrayList<Piscifactoria> piscifactorias) {
        this.piscifactorias = piscifactorias;
    }

    public void setPeces(String[] peces) {
        this.peces = peces;
    }

    public int getDiasPasados() {
        return diasPasados;
    }

    public void setDiasPasados(int diasPasados) {
        this.diasPasados = diasPasados;
    }

    public void setNombrePartida(String nombrePartida) {
        this.nombrePartida = nombrePartida;
    }

    public void setNombrePisci(String nombrePisci) {
        this.nombrePisci = nombrePisci;
    }

    public void setAc(AlmacenCentral ac) {
        this.ac = ac;
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
            System.out.println("Almacen Central: " + AlmacenCentral());
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
            System.out.println("14. Recompensas");
            System.out.println("15. Salir");
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
                    pasarVariosDias();
                    break;

                case 14:
                    System.out.println("Opcion 14: Recompensas");
                    canjearRecompensas();
                    break;
                case 15:
                    System.out.println("Saliendo del programa.");
                    salir = true;
                    log.log(nombrePartida, "Cierre de partida");
                    log.cerrar();
                    tr.cerrar();
                    registro.cerrar();
                    guardar.save();
                    break;
                case 98:
                    addPezRandom();
                case 99:
                    monedas.setMonedas(1000);
                    tr.transcripcion(nombrePartida,
                            "Añadidas 1000 monedas mediante la opcion oculta. Monedas actuales, "
                                    + monedas.getMonedas());
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción válida.");
            }
        }

    }

    public void canjearRecompensas(){

        boolean salir = false;

        while(!salir){
            System.out.println("Menú:");
            System.out.println("1. Canjear recompensa");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    // Canjear recompensa
                    System.out.println("Introduce el nombre del archivo XML a canjear: ");
                    String nombreArchivo = sc.next();
                    File archivo = new File("rewards/" + nombreArchivo + ".xml");
                    if (archivo.exists()) {
                        rewards.canjearRecompensa(archivo);
                    } else {
                        System.out.println("El archivo no existe.");
                    }
                    break;
                case 2:

                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
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

        if (opc == 0 || opc > piscifactorias.size()) {
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

    /**
     * Muestra un desglose de los peces en el sistema, incluyendo la cantidad comprada,
     * cantidad vendida y el dinero ganado para cada tipo de pez. Al final, se muestra el
     * total general de dinero ganado en todas las ventas.
     */
    public void showStats() {
        System.out.println("Desglose de los peces en el sistema: ");
        System.out.println("-------------------------------------");

        int totalDineroGanado = 0;

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
                int cantidadVendida = g.getCantidadVendida(nombrePez);
                int dineroGanado = cantidadVendida * pez.getCoste();
                System.out.println(pez.getNombre());
                System.out.println("Comprados: " + cantidadComprada);
                System.out.println("Vendidos: " + cantidadVendida);
                System.out.println("Dinero ganado: " + dineroGanado);

                totalDineroGanado += dineroGanado;
                System.out.println();

            }

        }

        System.out.println("Total general:");
        System.out.println("Dinero ganado en total: " + totalDineroGanado);
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

    /**
     * Permite al usuario vender peces de una piscifactoría seleccionada.
     * Muestra las piscifactorías disponibles y solicita al usuario que elija una.
     * Luego, recorre los tanques de la piscifactoría seleccionada, identifica los peces adultos
     * y vivos, los elimina del tanque, registra la venta de los peces y actualiza el total de monedas.
     * Finalmente, muestra un mensaje con el número total de peces vendidos y las monedas ganadas.
     * También registra la transacción en el registro de la partida.
     */
    public void sell() {

        System.out.println("Selecciona una piscifactoría para vender peces (ingresa el número correspondiente):");
        int indexPiscifactoria = selectPisc();

        if (indexPiscifactoria == -1) {
            System.out.println("Selección de piscifactoría inválida. Venta cancelada.");
            return;
        }

        Piscifactoria piscifactoriaSeleccionada = piscifactorias.get(indexPiscifactoria - 1);

        int totalPecesVendidos = 0;
        int totalMonedasGanadas = 0;

        for (Tanque<? extends Pez> tanque : piscifactoriaSeleccionada.getTanque()) {
            Iterator<Pez> iterator = tanque.getPeces().iterator();
            while (iterator.hasNext()) {
                Pez pez = iterator.next();
                if (pez.getAdulto() && pez.isVivo()) {
                    int ganancia = pez.getPezDato().getCoste() / 2;
                    totalMonedasGanadas += ganancia;
                    iterator.remove();
                    totalPecesVendidos++;
                    g.registrarVenta(pez.getPezDato().getNombre(), 1);
                }
            }
        }

        monedas.setMonedas(monedas.getMonedas() + totalMonedasGanadas);

        System.out.println(
                "Se han vendido " + totalPecesVendidos + " peces por un total de " + totalMonedasGanadas + " monedas.");

        String transcripcion = "Vendidos " + totalPecesVendidos + " peces de la piscifactoría "
                + piscifactoriaSeleccionada.getNombre() + " de forma manual por " + totalMonedasGanadas + " monedas.";
        registro.registrar(nombrePartida, transcripcion);
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
     * Avanza un día en la simulación.
     * Realiza varias acciones:
     * - Distribuye la comida almacenada en el acuario entre las piscifactorías no llenas.
     * - Calcula el número total de peces en el sistema, tanto de río como de mar.
     * - Registra el final del día en el log y transcribe la información correspondiente.
     * - Incrementa el número de días pasados en la simulación.
     */
    public void nextDay() {
        if (ac != null) {
            if (ac.getEspacioOcupado() > 0) {
                int comidaTotal = ac.getEspacioOcupado();
                int piscifactoriasNoLLenas = 0;

                for (Piscifactoria p : piscifactorias) {
                    if (!p.estaLlena()) {
                        piscifactoriasNoLLenas++;
                    }
                }

                if (piscifactoriasNoLLenas > 0) {
                    int comidaPorPiscifactoria = comidaTotal / piscifactoriasNoLLenas;
                    for (Piscifactoria p : piscifactorias) {
                        if (!p.estaLlena()) {
                            p.setComidaActual(p.getComidaActual() + comidaPorPiscifactoria);
                        }
                    }

                    ac.setEspacioOcupado(0);
                    ac.setEspacioDisponible(200);
                }
            }
        }

        for (Piscifactoria p : piscifactorias) {
            p.nextDay();
        }

        int totalPecesRio = 0;
        int totalPecesMar = 0;
        for (Piscifactoria p : piscifactorias) {
            totalPecesRio += p.getNumeroPecesRio();
            totalPecesMar += p.getNumeroPecesMar();
        }

        int totalMonedas = monedas.getMonedas();

        log.log(nombrePartida, "Fin del dia " + diasPasados + ".");
        tr.transcripcion(nombrePartida, "Fin del día " + diasPasados + ".");
        tr.transcripcion(nombrePartida, "Peces actuales, " + totalPecesRio + " de río " + totalPecesMar + " de mar.");
        tr.transcripcion(nombrePartida, "Total de monedas " + totalMonedas);
        tr.transcripcion(nombrePartida, "-------------------------");
        tr.transcripcion(nombrePartida, ">>>Inicio del día " + (diasPasados + 1) + ".");

        // Incrementar el número de días pasados
        diasPasados++;
        guardar.save();
    }

    /**
     * Avanza la simulación durante un número especificado de días.
     *
     */
    public void pasarVariosDias() {
        System.out.println("¿Cuántos días quieres pasar?");
        int dias = sc.nextInt();

        for (int i = 0; i < dias; i++) {
            nextDay();
        }

        System.out.println("Se han pasado " + dias + " días.");
        guardar.save();
    }

    /**
     * Permite al usuario comprar comida para una piscifactoría seleccionada.
     * Muestra la cantidad de monedas disponibles y solicita la cantidad de comida a
     * comprar.
     * La comida comprada se agrega a la piscifactoría seleccionada, y las monedas
     * se deducen.
     */
    public void addFood() {

        if (ac != null) {
            System.out.println("¿Cuanta comida quieres comprar? Pulsa 0 para cancelar");
            System.out.println("Tu monedero: " + monedas.getMonedas());
            int cantidad1 = sc.nextInt();

            switch (cantidad1) {
                case 5:
                case 10:
                case 25:

                    if (monedas.getMonedas() >= cantidad1) {
                        ac.meterComida(cantidad1);
                        System.out.println("Añadiste " + cantidad1 + " de comida al Almacen Central");
                        monedas.setMonedas(monedas.getMonedas() - cantidad1);
                        registro.registrar(nombrePartida, cantidad1 + " de comida comprada por " + cantidad1
                                + " monedas. Se almacena en el almacén central.");
                    } else {
                        System.out.println("No tienes suficientes monedas");
                    }
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } else {
            int opc = selectPisc();
            if (opc != 0) {
                Piscifactoria p = piscifactorias.get(opc - 1);
                System.out.println("¿Cuánta comida quieres comprar? Pulsa 0 para cancelar");
                System.out.println("Tu monedero: " + monedas.getMonedas());
                int cantidad = sc.nextInt();

                switch (cantidad) {
                    case 5:
                    case 10:
                    case 25:
                        if (cantidad <= p.getComidaMaxima() - p.getComidaActual()) {
                            p.setComidaActual(p.getComidaActual() + cantidad);
                            System.out.println("Añadiste " + cantidad + " de comida");
                            monedas.setMonedas(monedas.getMonedas() - cantidad);
                            registro.registrar(nombrePartida, cantidad + " de comida comprada por " + cantidad
                                    + " monedas. Se almacena en la piscifactoría " + p.getNombre() + ".");
                        } else {
                            System.out.println("No tienes espacio para meter más comida");
                        }
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            } else {
                menu();
            }
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
        boolean sexo;
        if (numHembras >= numMachos) {
            sexo = true;
        } else {
            sexo = false;
        }
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.BESUGO.getNombre() + " (" + sexoString + ") comprado por "
                            + AlmacenPropiedades.BESUGO.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.CABALLA.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.CABALLA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.CABALLA.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.CABALLA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.ROBALO.getNombre() + " (" + sexoString + ") comprado por "
                            + AlmacenPropiedades.ROBALO.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.SARGO.getNombre() + " (" + sexoString + ") comprado por "
                            + AlmacenPropiedades.SARGO.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.CARPA_PLATEADA.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.CARPA_PLATEADA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.CARPA.getNombre() + " (" + sexoString + ") comprado por "
                            + AlmacenPropiedades.CARPA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.LUCIO_NORTE.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.LUCIO_NORTE.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.PEJERREY.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.PEJERREY.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.PERCA_EUROPEA.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.PERCA_EUROPEA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.DORADA.getNombre() + " (" + sexoString + ") comprado por "
                            + AlmacenPropiedades.DORADA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
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
                    String sexoString = sexo ? "M" : "H";
                    String transcripcion = AlmacenPropiedades.LUBINA_EUROPEA.getNombre() + " (" + sexoString
                            + ") comprado por " + AlmacenPropiedades.LUBINA_EUROPEA.getCoste()
                            + " monedas.";
                    registro.registrar(nombrePartida, transcripcion);
                } else {
                    System.out.println("No tienes suficientes fondos para comprar el pez");
                }
                break;
            default:
                System.out.println("Opción inválida");
                break;
        }

        if (sexo) {
            numMachos++;
        } else {
            numHembras++;
        }

    }

    /**
     * Agrega un pez aleatorio al tanque.
     * 
     * Este método selecciona aleatoriamente un tipo de pez y agrega cuatro
     * ejemplares
     * de ese tipo al tanque, asegurándose de que haya espacio suficiente y que no
     * se exceda el límite de población del tanque.
     */
    public void addPezRandom() {
        Random rd = new Random();
        boolean sexo;
        if (numHembras >= numMachos) {
            sexo = true;
        } else {
            sexo = false;
        }
        int opcion = rd.nextInt(12) + 1;
        switch (opcion) {
            case 1:
                if (!comprobarPecesDelTanque(escogerPez)) {
                    for (int i = 0; i < 4; i++) {
                        escogerPez = new Besugo(sexo, AlmacenPropiedades.BESUGO);
                    }
                }
            case 2:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Caballa(sexo, AlmacenPropiedades.CABALLA);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 3:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Robalo(sexo, AlmacenPropiedades.ROBALO);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 4:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Rodaballo(sexo, AlmacenPropiedades.RODABALLO);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 5:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Sargo(sexo, AlmacenPropiedades.SARGO);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 6:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Carpa_Plateada(sexo, AlmacenPropiedades.CARPA_PLATEADA);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 7:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Carpa(sexo, AlmacenPropiedades.CARPA);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 8:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new LucioDelNorte(sexo, AlmacenPropiedades.LUCIO_NORTE);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 9:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Pejerrey(sexo, AlmacenPropiedades.PEJERREY);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 10:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new PercaEuropea(sexo, AlmacenPropiedades.PERCA_EUROPEA);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 11:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new Dorada(sexo, AlmacenPropiedades.DORADA);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }
            case 12:
                for (int i = 0; i < 4; i++) {
                    escogerPez = new LubinaEuropea(sexo, AlmacenPropiedades.LUBINA_EUROPEA);
                    if (!comprobarPecesDelTanque(escogerPez)) {
                        return;
                    }
                }

        }
        if (sexo) {
            numMachos++;
        } else {
            numHembras++;
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
            for (int i = 0; i < p.getTanque().size(); i++) {
                Tanque<? extends Pez> t = p.getTanque().get(i);
                t.limpiarPecesMuertos();
                String transcripcion = "Limpiado el tanque " + (i + 1) + " de la piscifactoría " + p.getNombre() + ".";
                registro.registrar(nombrePartida, transcripcion);
            }
            System.out.println("Se han eliminado los peces muertos de todos los tanques de la piscifactoría "
                    + p.getNombre() + ".");
        } else {
            System.out.println("Piscifactoría no válida");
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
                String transcripcion = "Vaciado el tanque " + (tanqueSeleccionado + 1) + " de la piscifactoría "
                        + p.getNombre();
                registro.registrar(nombrePartida, transcripcion);
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
            return false;
        } else {
            System.out.println("Tanques disponibles:");
            for (int i = 0; i < tanquesDispos.size(); i++) {
                System.out.println((i + 1) + ". " + tanquesDispos.get(i).getTipo());
            }

            System.out.println("Selecciona una opción de los tanques disponibles");
            int opc = sc.nextInt();
            tanquesDispos.get(opc - 1).addFish(p);
            return true;
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
                        registro.registrar(nombrePartida, "Comprado el almacén central.");
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
                    registro.registrar(nombrePartida,
                            "Comparada la piscifactoría de río " + nombrePisci + " por 500 monedas.");
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
                    registro.registrar(nombrePartida,
                            "Comparada la piscifactoría de mar " + nombrePisci + " por 2000 monedas.");
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
                        registro.registrar(nombrePartida, "Comprado un tanque número " + p.getTanque().size()
                                + " de la piscifactoría de río " + p.getNombre() + ".");
                    } else {
                        System.out.println("No tienes suficientes monedas para comprar un tanque.");
                    }
                } else if (p instanceof PiscifactoriaMar) {
                    if (monedas.getMonedas() >= 600 * p.getTanque().size()) {
                        monedas.setMonedas(monedas.getMonedas() - 600 * p.getTanque().size());
                        p.añadirTanque(new Tanque<>(100, p.getTanque().size(), p));
                        System.out.println("Tanque añadido correctamente.");
                        registro.registrar(nombrePartida, "Comprado un tanque número " + p.getTanque().size()
                                + " de la piscifactoría de mar " + p.getNombre() + ".");
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
                ac = new AlmacenCentral();

            } else {
                System.out.println("No tienes suficiente dinero para comprar el Almacen");
            }

        } else {
            System.out.println("Vuelva pronto!");
            upgrade();
        }
    }

    public String AlmacenCentral() {
        if (ac != null) {
            return "Si";
        } else {
            return "No";
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
        try {
            s.menu();
        } catch (Exception e) {
            log.logError(e.getMessage());
        }

    }
}
