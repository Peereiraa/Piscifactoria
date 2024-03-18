package Partida;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Comun.AlmacenCentral;
import Comun.Monedas;
import Comun.Simulador;
import Pez.PecesMar.*;
import Pez.PecesMixtos.Dorada;
import Pez.PecesMixtos.LubinaEuropea;
import Pez.PecesRio.*;
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
import propiedades.AlmacenPropiedades;

public class Cargar {

    Simulador s;
    static Monedas m = Monedas.getInstance();

    protected static Log log = Log.getInstance();


    public Cargar(Simulador s) {
        this.s = s;
    }

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

    private JsonObject leerArchivoJSON(String rutaArchivo) throws IOException {
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(new FileReader(rutaArchivo));
    }

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

    private void cargarAlmacen(JsonObject jsonObject, Simulador simulador) {
        JsonObject edificios = jsonObject.getAsJsonObject("edificios");
        JsonObject almacen = edificios.getAsJsonObject("almacen");

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

    private Tanque<? extends Pez> crearTanque(JsonObject tanqueObj, int capacidadPiscifactoria, Piscifactoria p) {
        int numPeces = tanqueObj.get("num").getAsInt();
        JsonObject datos = tanqueObj.getAsJsonObject("datos");

        // Crear un nuevo tanque con la capacidad y la piscifactoría especificadas
        Tanque<? extends Pez> t = new Tanque<>(capacidadPiscifactoria, numPeces, p);

        String pezDato = tanqueObj.get("pez").getAsString();


        // Verificar si hay un array de peces en el objeto JSON
        if (tanqueObj.has("peces") && tanqueObj.get("peces").isJsonArray()) {
            JsonArray pecesArray = tanqueObj.getAsJsonArray("peces");

            // Iterar sobre el array de peces y agregarlos al tanque
            for (JsonElement pezElement : pecesArray) {
                JsonObject pezObj = pezElement.getAsJsonObject();
                int edad = pezObj.get("edad").getAsInt();
                boolean sexo = pezObj.get("sexo").getAsBoolean();
                boolean vivo = pezObj.get("vivo").getAsBoolean();
                boolean maduro = pezObj.get("maduro").getAsBoolean();
                boolean fertil = pezObj.get("fertil").getAsBoolean();
                int ciclo = pezObj.get("ciclo").getAsInt();
                boolean alimentado = pezObj.get("alimentado").getAsBoolean();


                switch (pezDato){
                    case "Besugo":
                        t.addFish(new Besugo(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Caballa":
                        t.addFish(new Caballa(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Robalo":
                        t.addFish(new Robalo(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Rodaballo":
                        t.addFish(new Rodaballo(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Sargo":
                        t.addFish(new Sargo(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Lubina Europea":
                        t.addFish(new LubinaEuropea(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Carpa":
                        t.addFish(new Carpa(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Carpa Plateada":
                        t.addFish(new Carpa_Plateada(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Lucio del Norte":
                        t.addFish(new LucioDelNorte(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Pejerrey":
                        t.addFish(new Pejerrey(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                    case "Perca Europea":
                        t.addFish(new PercaEuropea(edad, sexo, vivo, maduro, fertil, ciclo, alimentado));
                        break;
                }


            }
        }

        // Devolver el tanque creado con los peces agregados
        return t;
    }

}