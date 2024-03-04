package Registro;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

/**
 * La clase Log proporciona funcionalidades para registrar eventos y errores en archivos de registro.
 */
public class Log {

    private static Log instance;
    private BufferedWriter writer;

    private Log() {
    }

    /**
     * Obtiene la única instancia de la clase `Log`.
     *
     * @return La instancia única de `Log`.
     */
    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }   

    /**
     * Registra un evento en el archivo de registro asociado a una partida.
     *
     * @param nombrePartida El nombre de la partida asociada al evento.
     * @param texto         El texto del evento que se registrará.
     */
    public void log(String nombrePartida, String texto) {
        try {
            if (writer == null) {
                OutputStream outputStream = new FileOutputStream("logs/" + nombrePartida + ".log", true);
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            String currentTime = formatter.format(System.currentTimeMillis());
            writer.write(currentTime + " " + texto + "\n");
            writer.flush(); 
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    /**
     * Registra un mensaje de error en el archivo de registro de errores generales.
     *
     * @param errorMessage El mensaje de error que se registrará.
     */
    public void logError(String errorMessage) {
        try {
            if (writer == null) {
                OutputStream outputStream = new FileOutputStream("logs/0_errors"+".log", true);
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            String currentTime = formatter.format(System.currentTimeMillis());
            writer.write(currentTime + " [ERROR] " + errorMessage + "\n");
            writer.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra el escritor de archivos de registro.
     */
    public void cerrar() {
        try {
            if (writer != null) {
                writer.close();
                writer = null; 
            }
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }
}
