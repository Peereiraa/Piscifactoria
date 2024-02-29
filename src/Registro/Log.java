package Registro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Log {

    private static Log instance;
    private BufferedWriter writer;

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void log(String nombrePartida, String texto) {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter("logs/" + nombrePartida + ".log", true));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            String currentTime = formatter.format(System.currentTimeMillis());
            writer.write(currentTime + " " + texto + "\n");
            writer.flush(); // Limpiar el buffer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logError(String errorMessage) {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter("logs/0_errors" + ".log", true));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
            String currentTime = formatter.format(System.currentTimeMillis());
            writer.write(currentTime + " [ERROR] " + errorMessage + "\n");
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
