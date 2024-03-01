package Partida;

import Comun.AlmacenCentral;
import Piscifactorias.Piscifactoria;
import Piscifactorias.PiscifactoriaMar;
import Piscifactorias.PiscifactoriaRio;
import com.google.gson.Gson;

import Comun.Monedas;
import Comun.Simulador;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import estadisticas.Estadisticas;

import java.util.ArrayList;


public class Guardar {
    
    protected Simulador s;
    protected AlmacenCentral ac;
    Monedas monedas = Monedas.getInstance();
    String[] pecesImp;
    Estadisticas estadisticas;
    int diasPasados;
    int cantidadMonedas;
    boolean disponible;
    int capacidad;
    int comida;
    ArrayList<Piscifactoria> piscifactorias;



    public Guardar(Simulador s){
        this.s = s;
    }


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


        }



    
    }
    
    
}
