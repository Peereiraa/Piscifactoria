package Registro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Registro {

    private static Registro instance;
    private BufferedWriter writerLog;
    private BufferedWriter writerTranscripcion;

    private Registro() {
    }

    public static Registro getInstance() {
        if (instance == null) {
            instance = new Registro();
        }
        return instance;
    }

    public void registrar(String nombrePartida, String texto) {
        try {
            if (writerLog == null) {
                writerLog = new BufferedWriter(new FileWriter("logs/" + nombrePartida + ".log", true));
            }
            if (writerTranscripcion == null) {
                writerTranscripcion = new BufferedWriter(
                        new FileWriter("transcripciones/" + nombrePartida + ".tr", true));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            String currentTime = formatter.format(System.currentTimeMillis());
            writerLog.write(currentTime + " " + texto + "\n");
            writerLog.flush();

            writerTranscripcion.write(texto + "\n");
            writerTranscripcion.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        try {
            if (writerLog != null) {
                writerLog.close();
                writerLog = null; 
            }
            if (writerTranscripcion != null) {
                writerTranscripcion.close();
                writerTranscripcion = null; 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
