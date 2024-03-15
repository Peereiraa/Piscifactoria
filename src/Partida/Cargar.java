package Partida;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Comun.AlmacenCentral;
import Comun.Monedas;
import Comun.Simulador;
import Piscifactorias.Piscifactoria;
import Piscifactorias.PiscifactoriaMar;
import Piscifactorias.PiscifactoriaRio;
import Registro.Log;
import Tanques.Tanque;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Pez.Pez;



/**
 * Clase encargada de cargar los datos de una partida desde un archivo JSON.
 */
public class Cargar {

    Simulador s;
    static Monedas m = Monedas.getInstance();

    protected static Log log = Log.getInstance();

    public Cargar(Simulador s) {
        this.s = s;
    }

    /**
     * Carga los datos de una partida desde un archivo JSON y los asigna al simulador.
     *
     * @param rutaArchivo La ruta del archivo JSON.
     */
    public void cargarPartida(String rutaArchivo) {
        try {
            JsonObject jsonObject = leerArchivoJSON(rutaArchivo);

            cargarDatosGenerales(jsonObject, s);

            cargarAlmacen(jsonObject, s);

            cargarPiscifactorias(jsonObject, s);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee un archivo JSON y lo convierte en un objeto JsonObject.
     *
     * @param rutaArchivo La ruta del archivo JSON a leer.
     * @return El objeto JsonObject que representa el contenido del archivo JSON.
     * @throws IOException Si ocurre un error de lectura del archivo.
     */
    private JsonObject leerArchivoJSON(String rutaArchivo) throws IOException {
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(new FileReader(rutaArchivo));
    }

    /**
     * Carga los datos generales de la partida desde el objeto JsonObject y los asigna al simulador.
     *
     * @param jsonObject El objeto JsonObject que contiene los datos de la partida.
     * @param simulador  El simulador al que se asignarán los datos cargados.
     */
    private void cargarDatosGenerales(JsonObject jsonObject, Simulador simulador) {
        try {
            String empresa = jsonObject.get("empresa").getAsString();
            int dia = jsonObject.get("dia").getAsInt();
            int monedas = jsonObject.get("monedas").getAsInt();

            simulador.setNombrePartida(empresa);
            simulador.setDiasPasados(dia);

            m.setMonedas(monedas);
        } catch (Exception e) {
            log.logError(e.getMessage());
        }
    }


    /**
     * Carga los datos del almacén desde el objeto JsonObject y los asigna al simulador.
     *
     * @param jsonObject El objeto JsonObject que contiene los datos de la partida.
     * @param simulador  El simulador al que se asignarán los datos cargados.
     */
    private void cargarAlmacen(JsonObject jsonObject, Simulador simulador) {
        JsonObject edificios = jsonObject.getAsJsonObject("edificios");
        JsonObject almacen = edificios.getAsJsonObject("almacen");

        // Verificar si el atributo "disponible" está presente y no es nulo
        boolean almacenDisponible = false;
        JsonElement disponibleElement = almacen.get("disponible");
        if (disponibleElement != null && !disponibleElement.isJsonNull() && disponibleElement.isJsonPrimitive()) {
            almacenDisponible = disponibleElement.getAsBoolean();
        }

        if (almacenDisponible) {
            int capacidadAlmacen = almacen.get("capacidad").getAsInt();
            JsonObject comidaAlmacen = almacen.getAsJsonObject("comida");
            int cantidadComidaAlmacen = comidaAlmacen.get("general").getAsInt();

            AlmacenCentral ac = new AlmacenCentral();
            ac.setEspacioOcupado(cantidadComidaAlmacen);
            ac.setEspacioMaximo(capacidadAlmacen);

            simulador.setAlmacenCentral(ac);
        }
    }


    /**
     * Carga los datos de las piscifactorías desde el objeto JsonObject y los asigna al simulador.
     *
     * @param jsonObject El objeto JsonObject que contiene los datos de la partida.
     * @param simulador  El simulador al que se asignarán los datos cargados.
     */
    private void cargarPiscifactorias(JsonObject jsonObject, Simulador simulador) {
        JsonArray piscifactoriasArray = jsonObject.getAsJsonArray("piscifactoria");
        if (piscifactoriasArray != null) {
            ArrayList<Piscifactoria> piscifactorias = new ArrayList<>();
            for (JsonElement element : piscifactoriasArray) {
                JsonObject piscifactoriaObj = element.getAsJsonObject();
                Piscifactoria piscifactoria = crearPiscifactoria(piscifactoriaObj);
                piscifactorias.add(piscifactoria);
            }
            simulador.setPiscifactorias(piscifactorias);
        } else {
            System.out.println("La clave 'piscifactorias' no está presente o su valor es null en el archivo JSON.");
        }
    }


    /**
     * Crea una piscifactoría a partir de un objeto JsonObject que contiene sus datos.
     *
     * @param piscifactoriaObj El objeto JsonObject que contiene los datos de la piscifactoría.
     * @return La piscifactoría creada.
     */
    private Piscifactoria crearPiscifactoria(JsonObject piscifactoriaObj) {
        String nombrePiscifactoria = piscifactoriaObj.get("nombre").getAsString();
        int tipoPiscifactoria = piscifactoriaObj.get("tipo").getAsInt();
        int capacidadPiscifactoria = piscifactoriaObj.get("capacidad").getAsInt();
        JsonObject comidaPiscifactoria = piscifactoriaObj.getAsJsonObject("comida");
        int cantidadComidaPiscifactoria = comidaPiscifactoria.get("general").getAsInt();

        Piscifactoria p;
        if (tipoPiscifactoria == 0) {
            p = new PiscifactoriaRio(nombrePiscifactoria);
        } else {
            p = new PiscifactoriaMar(nombrePiscifactoria);
        }

        JsonArray tanquesArray = piscifactoriaObj.getAsJsonArray("tanques");
        ArrayList<Tanque<? extends Pez>> tanques = new ArrayList<>();
        if (tanquesArray != null) {
            for (JsonElement tanqueElement : tanquesArray) {
                JsonObject tanqueObj = tanqueElement.getAsJsonObject();
                Tanque<? extends Pez> tanque = crearTanque(tanqueObj, capacidadPiscifactoria, p);
                tanques.add(tanque);
            }
        }
        p.setTanque(tanques);

        return p;
    }

    /**
     * Crea un tanque a partir de un objeto JsonObject que contiene sus datos.
     *
     * @param tanqueObj              El objeto JsonObject que contiene los datos del tanque.
     * @param capacidadPiscifactoria La capacidad de la piscifactoría a la que pertenece el tanque.
     * @param p                      La piscifactoría a la que pertenece el tanque.
     * @return El tanque creado.
     */
    private Tanque<? extends Pez> crearTanque(JsonObject tanqueObj, int capacidadPiscifactoria, Piscifactoria p) {
        int numPeces = tanqueObj.get("num").getAsInt();
        JsonObject datos = tanqueObj.getAsJsonObject("datos");

        JsonArray pecesArray = tanqueObj.getAsJsonArray("peces");
        ArrayList<Pez> pecesTanque = new ArrayList<>();
        if (pecesArray != null) {
            for (JsonElement pece : pecesArray) {
                JsonObject pezObj = pece.getAsJsonObject();
                // Aquí debes extraer los datos de cada pez del JSON
                int edad = pezObj.get("edad").getAsInt();
                boolean sexo = pezObj.get("sexo").getAsBoolean();
                boolean vivo = pezObj.get("vivo").getAsBoolean();
                boolean maduro = pezObj.get("maduro").getAsBoolean();
                boolean fertil = pezObj.get("fertil").getAsBoolean();
                int ciclo = pezObj.get("ciclo").getAsInt();
                boolean alimentado = pezObj.get("alimentado").getAsBoolean();

                // Aquí creas un nuevo objeto Pez con los datos extraídos
                Pez pez = new Pez(edad, sexo, fertil, vivo, alimentado, maduro, ciclo);

                // Agregas el pez al ArrayList de peces del tanque
                pecesTanque.add(pez);
            }
        }

        // Creas el tanque con los peces cargados
        Tanque<? extends Pez> t = new Tanque<>(numPeces, capacidadPiscifactoria, p);
        t.setPeces(pecesTanque);



        return t;
    }
}
