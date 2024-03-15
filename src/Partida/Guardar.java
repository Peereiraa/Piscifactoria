package Partida;

import Comun.AlmacenCentral;
import Pez.Pez;
import Piscifactorias.Piscifactoria;
import Piscifactorias.PiscifactoriaRio;
import Registro.Log;
import Tanques.Tanque;
import com.google.gson.Gson;

import Comun.Monedas;
import Comun.Simulador;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import estadisticas.Estadisticas;

import java.io.*;
import java.util.ArrayList;

/**
 * La clase Guardar se utiliza para guardar el estado actual del simulador en un archivo JSON.
 * Contiene métodos para serializar los datos del simulador y escribirlos en un archivo.
 */
public class Guardar {

    /**
     * El simulador cuyo estado se va a guardar.
     */
    protected Simulador s;

    /**
     * El almacén central del simulador.
     */
    protected AlmacenCentral ac;

    /**
     * Instancia de la clase Monedas para gestionar las monedas del simulador.
     */
    Monedas monedas = Monedas.getInstance();

    /**
     * Array de peces implementados en el simulador.
     */
    String[] pecesImp;

    protected static Log log = Log.getInstance();

    /**
     * Estadísticas de los peces implementados en el simulador.
     */
    Estadisticas estadisticas;

    /**
     * El número de días pasados en el simulador.
     */
    int diasPasados;

    /**
     * El nombre de la partida.
     */
    String nombrePartida;

    /**
     * La cantidad de monedas en el simulador.
     */
    int cantidadMonedas;

    /**
     * Indica si el almacén central está disponible o no.
     */
    boolean disponible;

    /**
     * La capacidad máxima del almacén central.
     */
    int capacidad;

    /**
     * La cantidad de comida almacenada en el almacén central.
     */
    int comida;

    /**
     * Lista de piscifactorias en el simulador.
     */
    ArrayList<Piscifactoria> piscifactorias;

    /**
     * Constructor de la clase Guardar.
     *
     * @param s El simulador cuyo estado se va a guardar.
     */
    public Guardar(Simulador s){
        this.s = s;
        ac = new AlmacenCentral();
    }

    /**
     * Guarda el estado actual del simulador en un archivo JSON.
     */
    public void save(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        pecesImp = s.getPeces();
        estadisticas = new Estadisticas(pecesImp);
        diasPasados = s.getDiasPasados();
        cantidadMonedas = monedas.getMonedas();
        disponible = s.getAlmacenCentral();
        capacidad = ac.getEspacioMaximo();
        comida = ac.getEspacioOcupado();
        piscifactorias = s.getPiscifactorias();
        nombrePartida = s.getNombrePartida();

        JsonObject partida = new JsonObject();
        JsonArray pecesArray = new JsonArray();
        JsonObject edificios = new JsonObject();
        JsonObject almacen = new JsonObject();

        for(String pez : pecesImp){
            pecesArray.add(pez);
        }

        JsonArray piscifactoriasArray = new JsonArray();

        for(Piscifactoria piscifactoria : piscifactorias){
            JsonObject piscifactoriaJSON = new JsonObject();
            piscifactoriaJSON.addProperty("nombre", piscifactoria.getNombre());
            if(piscifactoria instanceof PiscifactoriaRio){
                piscifactoriaJSON.addProperty("tipo", 0);
            }else{
                piscifactoriaJSON.addProperty("tipo", 1);
            }

            piscifactoriaJSON.addProperty("capacidad", piscifactoria.getComidaMaxima());

            JsonObject comidaJSON = new JsonObject();
            comidaJSON.addProperty("general", piscifactoria.getComidaActual());
            piscifactoriaJSON.add("comida", comidaJSON);


            JsonArray tanquesArray = new JsonArray();

            for(Tanque<? extends Pez> tanque : piscifactoria.getTanque()){
                JsonObject tanqueJ = new JsonObject();
                if(!tanque.getPeces().isEmpty()){
                    tanqueJ.addProperty("pez", tanque.getPeces().get(0).getPezDato().getNombre());
                }else{
                    tanqueJ.addProperty("pez", "nombre");
                }
                tanqueJ.addProperty("num", tanque.getNumTanque());

                JsonObject datosJSON = new JsonObject();
                datosJSON.addProperty("vivos", tanque.pecesVivos());
                datosJSON.addProperty("maduros", tanque.macho());
                datosJSON.addProperty("fertiles", tanque.fertil());
                tanqueJ.add("datos", datosJSON);

                JsonArray pecesJSON = new JsonArray();
                for(Pez p : tanque.getPeces()){
                    JsonObject pezJ = new JsonObject();
                    pezJ.addProperty("edad", p.getEdad());
                    pezJ.addProperty("sexo", p.getSexo());
                    pezJ.addProperty("vivo", p.isVivo());
                    pezJ.addProperty("maduro", p.getAdulto());
                    pezJ.addProperty("fertil", p.isFertil());
                    pezJ.addProperty("ciclo", p.getCiclodevida());
                    pezJ.addProperty("alimentado", p.getAlimentado());

                    JsonObject extraObj = new JsonObject();
                    pezJ.add("extra", extraObj);

                    pecesJSON.add(pezJ);


                }
                tanqueJ.add("peces", pecesJSON);

                tanquesArray.add(tanqueJ);

            }

            piscifactoriaJSON.add("tanques", tanquesArray);
            piscifactoriasArray.add(piscifactoriaJSON);

        }
        partida.add("implementados", pecesArray);
        partida.addProperty("empresa", nombrePartida);
        partida.addProperty("dia", diasPasados);
        partida.addProperty("monedas", cantidadMonedas);
        partida.addProperty("orca", estadisticas.exportarDatos(pecesImp));
        edificios.add("almacen", almacen);
        almacen.addProperty("disponible", disponible);
        almacen.addProperty("capacidad", capacidad);
        almacen.addProperty("comida", comida);
        partida.add("edificios", edificios);
        partida.add("piscifactoria", piscifactoriasArray);


        Writer writer = null;

        String savesFolderPath = "src/saves"; // Carpeta donde guardar los archivos .save
        String filePath = savesFolderPath + File.separator + nombrePartida + ".save"; // Ruta completa del archivo

        // Ahora guardamos el archivo en la ruta especificada
        try {
            File savesFolder = new File(savesFolderPath);
            if (!savesFolder.exists()) {
                savesFolder.mkdirs(); // Crea la carpeta si no existe
            }

            String absoluteFilePath = savesFolder.getAbsolutePath() + File.separator + nombrePartida + ".save";
            writer = new OutputStreamWriter(new FileOutputStream(absoluteFilePath), "UTF-8");
            gson.toJson(partida, writer);
            System.out.println("Datos guardados " + absoluteFilePath);
        } catch (IOException e) {
            log.logError(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.logError(e.getMessage());
                }
            }
        }
    }
}
