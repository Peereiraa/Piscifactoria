package Registro;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

/**
 * La clase Registro gestiona el registro de eventos y transcripciones en archivos de registro y transcripción respectivamente.
 * Permite registrar eventos con marca de tiempo y transcribir texto en archivos correspondientes.
 */
public class Registro {

    private static Registro instance;
    private BufferedWriter writerLog;
    private BufferedWriter writerTranscripcion;

    
    private Registro() {
    }

    /**
     * Obtiene la única instancia de la clase `Registro`.
     *
     * @return La instancia única de `Registro`.
     */
    public static Registro getInstance() {
        if (instance == null) {
            instance = new Registro();
        }
        return instance;
    }

    /**
     * Registra un evento junto con su marca de tiempo en el archivo de registro y transcribe el texto en el archivo correspondiente.
     *
     * @param nombrePartida El nombre de la partida asociada al evento.
     * @param texto         El texto del evento que se registrará.
     */
    public void registrar(String nombrePartida, String texto) {
        try {
            if (writerLog == null) {
                OutputStream outputStream = new FileOutputStream("logs/" + nombrePartida + ".log", true);
                writerLog = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            }
            if (writerTranscripcion == null) {
                OutputStream outputStream = new FileOutputStream("transcripciones/" + nombrePartida + ".tr", true);
                writerTranscripcion = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
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

    /**
     * Cierra los escritores de archivos de registro y transcripción.
     */
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
