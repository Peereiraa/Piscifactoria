package Registro;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import Comun.Simulador;

public class Transcripciones {

    private static Transcripciones instance;

    Simulador s;

    private Transcripciones() {

    }

    public static Transcripciones getInstance() {
        if (instance == null) {
            instance = new Transcripciones();
        }

        return instance;
    }

    public Transcripciones(Simulador s) {
        this.s = s;
    }

    public void transcripcion(String texto){
        BufferedWriter writer = null;
        try{
            String ruta = "transcripciones/"+s.getNombrePartida()+".tr";
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta, true),"UTF-8"));
            writer.write(texto + "\n");
            writer.flush();
    }catch(IOException e){
        e.printStackTrace();
    }finally{
        if(writer != null){
            try{
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
}
