package Registro;

import Comun.Simulador;

public class Registro {
    private static Registro instance;

    Simulador s;

    private Registro(){

    }

    public static Registro getInstance(){
        if(instance == null){
            instance = new Registro();
        }

        return instance;
    }

    public Registro(Simulador s){
        this.s = s;
    }

    public void registrar(String texto){
        Log log = new Log(s);

        Transcripciones t = new Transcripciones(s);
        log.log(texto);
        t.transcripcion(texto);
    }
}
