package Registro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Transcripciones {

    private static Transcripciones instance;
    private BufferedWriter writer;

    private Transcripciones() {
    }

    public static Transcripciones getInstance() {
        if (instance == null) {
            instance = new Transcripciones();
        }
        return instance;
    }

    public void transcripcion(String nombrePartida, String texto) {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter("transcripciones/" + nombrePartida + ".tr", true));
            }
            writer.write(texto + "\n");
            writer.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        try {
            if (writer != null) {
                writer.close();
                writer = null; 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
