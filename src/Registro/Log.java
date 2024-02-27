package Registro;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import Comun.Simulador;

public class Log {

    private static Log instance;
    private Simulador s;

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public Log(Simulador s) {
        this.s = s;
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public void log(String texto) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream("logs/" + s.getNombrePartida() + ".log", true), true);
            writer.println(this.getCurrentTime() + " " + texto);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo crear el archivo de registro.");
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    
}

