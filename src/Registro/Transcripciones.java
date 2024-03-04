package Registro;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * La clase Transcripciones gestiona la transcripción de texto en un archivo de registro.
 * Permite escribir texto en un archivo de transcripciones, así como cerrar el archivo cuando ya no se necesita.
 */
public class Transcripciones {

    private static Transcripciones instance;
    private BufferedWriter writer;

    protected static Log log = Log.getInstance();

    
    private Transcripciones() {
    }

    /**
     * Obtiene la única instancia de la clase `Transcripciones`.
     *
     * @return La instancia única de `Transcripciones`.
     */
    public static Transcripciones getInstance() {
        if (instance == null) {
            instance = new Transcripciones();
        }
        return instance;
    }

    /**
     * Transcribe un texto en el archivo de transcripciones.
     *
     * @param nombrePartida El nombre de la partida asociada al texto.
     * @param texto         El texto que se transcribirá en el archivo.
     */
    public void transcripcion(String nombrePartida, String texto) {
        try {
            if (writer == null) {
                OutputStream outputStream = new FileOutputStream("transcripciones/" + nombrePartida + ".tr", true);
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            }
            writer.write(texto + "\n");
            writer.flush(); 
        } catch (IOException e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Cierra el escritor del archivo de transcripciones.
     */
    public void cerrar() {
        try {
            if (writer != null) {
                writer.close();
                writer = null; 
            }
        } catch (IOException e) {
            log.logError(e.getMessage());
        }
    }
}
