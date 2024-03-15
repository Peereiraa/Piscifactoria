package Recompensas;

import Comun.AlmacenCentral;
import Comun.Monedas;
import Comun.Simulador;
import Piscifactorias.Piscifactoria;
import Piscifactorias.PiscifactoriaMar;
import Piscifactorias.PiscifactoriaRio;
import Registro.Log;
import Registro.Registro;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import Registro.Transcripciones;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class Rewards {

    protected static Log log = Log.getInstance();
    protected static Transcripciones tr = Transcripciones.getInstance();
    protected static Monedas monedas = Monedas.getInstance();

    private AlmacenCentral ac;

    private static Simulador sc;

    private ArrayList<Piscifactoria> piscifactorias;

    public Rewards(Simulador simulador){
        ac = new AlmacenCentral();
        sc = simulador;
    }
    public Document generarXML(){
        Document document = DocumentHelper.createDocument();
        Element raiz = document.addElement("reward");
        raiz.addElement("name").addText("");
        raiz.addElement("origin").addText("Diego");
        raiz.addElement("desc").addText("");
        raiz.addElement("rarity").addText("");
        raiz.addElement("give");
        raiz.addElement("quantity").addText("1");
        return document;

    }

    private String buscarNombre(File archivo) {
        SAXReader reader = null;
        String nombre = "";
        try {
            reader = new SAXReader();
            Document document = reader.read(archivo);
            Element raiz = document.getRootElement();
            for (Element elemento : raiz.elements()) {
                if ("name".equals(elemento.getName())) {
                    nombre = elemento.getText();
                }
            }
        } catch (Exception e) {
            log.logError("Error al buscar el nombre del archivo XML");
        }
        return nombre;
    }


    public void guardarDocumentoXML(Document document, String nombreArchivo) {
        XMLWriter writer = null;
        try {
            writer = new XMLWriter(
                    new OutputStreamWriter(new FileOutputStream(nombreArchivo), "UTF-8"),
                    OutputFormat.createPrettyPrint());
            writer.write(document);
            writer.flush();
            log.log(sc.getNombrePartida(), "Recompensa creada");
        } catch (IOException e) {
            log.logError("Error al guardar el XML");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                log.logError("Error al cerrar el XMLWriter");
            }
        }
    }


    public void actualizarArchivoXML(String nombreArchivo){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("rewards/" + nombreArchivo + ".xml");
            Element quantityElement = document.getRootElement().element("quantity");
            if (quantityElement != null) {
                int cantidadActual = Integer.parseInt(quantityElement.getTextTrim());
                quantityElement.setText(Integer.toString(cantidadActual + 1));
                guardarDocumentoXML(document, "rewards/" + nombreArchivo + ".xml");
            } else {
                log.logError("Error al modificar la etiqueta quantity del archivo rewards");
            }
        } catch (Exception e) {
            log.logError("Error al actualizar el archivo rewards");
        }

    }

    public boolean existeArchivo(String nombre) {
        File carpeta = new File("rewards/");
        if (carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile() && archivo.getName().startsWith(nombre)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void generarComida(int numero) {
        if (existeArchivo("comida_" + numero)) {
            actualizarArchivoXML("comida_" + numero);
        } else {
            Document document = generarXML();
            String nombre = "";
            String descripcion = " unidades de pienso multipropósito para todo tipo de peces.";
            int foodType = 0;
            int rarity = 0;
            Element raiz = document.getRootElement();
            switch (numero) {
                case 1:
                    nombre = "I";
                    foodType = 50;
                    descripcion = foodType + descripcion;
                    rarity = 0;
                    break;
                case 2:
                    nombre = "II";
                    foodType = 100;
                    descripcion = foodType + descripcion;
                    rarity = 1;
                    break;
                case 3:
                    nombre = "III";
                    foodType = 250;
                    descripcion = foodType + descripcion;
                    rarity = 2;
                    break;
                case 4:
                    nombre = "IV";
                    foodType = 500;
                    descripcion = foodType + descripcion;
                    rarity = 3;
                    break;
                case 5:
                    nombre = "V";
                    foodType = 1000;
                    descripcion = foodType + descripcion;
                    rarity = 4;
                    break;
            }
            for (Element elemento : raiz.elements()) {
                if ("name".equals(elemento.getName())) {
                    elemento.setText("Comida general " + nombre);
                } else if ("desc".equals(elemento.getName())) {
                    elemento.setText(descripcion);
                } else if ("rarity".equals(elemento.getName())) {
                    elemento.setText(Integer.toString(rarity));
                } else if ("give".equals(elemento.getName())) {
                    Element give = elemento;
                    give.addElement("food").addAttribute("type", "general").addText(Integer.toString(foodType));
                }
            }
            guardarDocumentoXML(document, "rewards/" + "comida_" + numero + ".xml");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa comida_" + nombre + " creada");
        }
    }

    public void generarMonedas(int numero) {
        if (existeArchivo("monedas_" + numero)) {
            actualizarArchivoXML("monedas_" + numero);
        } else {
            Document document = generarXML();
            String nombre = "";
            String descripcion = " monedas";
            int coins = 0;
            int rarity = 0;
            Element raiz = document.getRootElement();
            switch (numero) {
                case 1:
                    nombre = "I";
                    coins = 100;
                    descripcion = coins + descripcion;
                    rarity = 0;
                    break;
                case 2:
                    nombre = "II";
                    coins = 300;
                    descripcion = coins + descripcion;
                    rarity = 1;
                    break;
                case 3:
                    nombre = "III";
                    coins = 500;
                    descripcion = coins + descripcion;
                    rarity = 2;
                    break;
                case 4:
                    nombre = "IV";
                    coins = 750;
                    descripcion = coins + descripcion;
                    rarity = 3;
                    break;
                case 5:
                    nombre = "V";
                    coins = 1000;
                    descripcion = coins + descripcion;
                    rarity = 4;
                    break;
            }
            for (Element elemento : raiz.elements()) {
                if ("name".equals(elemento.getName())) {
                    elemento.setText("Monedas " + nombre);
                } else if ("desc".equals(elemento.getName())) {
                    elemento.setText(descripcion);
                } else if ("rarity".equals(elemento.getName())) {
                    elemento.setText(Integer.toString(rarity));
                } else if ("give".equals(elemento.getName())) {
                    Element give = elemento;
                    give.addElement("coins").addText(Integer.toString(coins));
                }
            }
            guardarDocumentoXML(document, "rewards/" + "monedas_" + numero + ".xml");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa monedas_" + nombre + " creada");
        }
    }

    public void generarAlmacen(char letra) {
        if (existeArchivo("almacen_" + Character.toLowerCase(letra))) {
            actualizarArchivoXML("almacen_" + Character.toLowerCase(letra));
        } else {
            Document document = generarXML();
            String descripcion = "Materiales para la construcción de un almacén central. Con la parte A, B, C y D, puedes obtenerlo de forma gratuita.";
            int codigo = 4;
            int rarity = 3;
            Element raiz = document.getRootElement();
            for (Element elemento : raiz.elements()) {
                if ("name".equals(elemento.getName())) {
                    elemento.setText("Almacén central [" + String.valueOf(Character.toUpperCase(letra)) + "]");
                } else if ("desc".equals(elemento.getName())) {
                    elemento.setText(descripcion);
                } else if ("rarity".equals(elemento.getName())) {
                    elemento.setText(Integer.toString(rarity));
                } else if ("give".equals(elemento.getName())) {
                    Element give = elemento;
                    give.addElement("building").addAttribute("code", Integer.toString(codigo))
                            .addText("Almacén central");
                    give.addElement("part").addText(String.valueOf(Character.toUpperCase(letra)));
                    give.addElement("total").addText("ABCD");
                }
            }
            guardarDocumentoXML(document, "rewards/" + "almacen_" + Character.toLowerCase(letra) + ".xml");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa almacen_" + Character.toLowerCase(letra) + " creada");
        }
    }

    public void generarPiscifactoriaMar(char letra) {
        if (existeArchivo("pisci_m_" + Character.toLowerCase(letra))) {
            actualizarArchivoXML("pisci_m_" + Character.toLowerCase(letra));
        } else {
            Document document = generarXML();
            String descripcion = "Materiales para la construcción de una piscifactoría de mar. Con la parte A y B, puedes obtenerla de forma gratuita.";
            int codigo = 1;
            int rarity = 4;
            Element raiz = document.getRootElement();
            for (Element elemento : raiz.elements()) {
                if ("name".equals(elemento.getName())) {
                    elemento.setText("Piscifactoría de mar [" + String.valueOf(Character.toUpperCase(letra)) + "]");
                } else if ("desc".equals(elemento.getName())) {
                    elemento.setText(descripcion);
                } else if ("rarity".equals(elemento.getName())) {
                    elemento.setText(Integer.toString(rarity));
                } else if ("give".equals(elemento.getName())) {
                    Element give = elemento;
                    give.addElement("building").addAttribute("code", Integer.toString(codigo))
                            .addText("Piscifactoría de mar");
                    give.addElement("part").addText(String.valueOf(Character.toUpperCase(letra)));
                    give.addElement("total").addText("AB");
                }
            }
            guardarDocumentoXML(document, "rewards/" + "pisci_m_" + Character.toLowerCase(letra) + ".xml");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa pisci_m_" + Character.toLowerCase(letra) + " creada");
        }
    }

    public void generarPiscifactoriaRio(char letra) {
        if (existeArchivo("pisci_r_" + Character.toLowerCase(letra))) {
            actualizarArchivoXML("pisci_r_" + Character.toLowerCase(letra));
        } else {
            Document document = generarXML();
            String descripcion = "Materiales para la construcción de una piscifactoría de río. Con la parte A y B, puedes obtenerla de forma gratuita.";
            int codigo = 0;
            int rarity = 3;
            Element raiz = document.getRootElement();
            for (Element elemento : raiz.elements()) {
                if ("name".equals(elemento.getName())) {
                    elemento.setText("Piscifactoría de río [" + String.valueOf(Character.toUpperCase(letra)) + "]");
                } else if ("desc".equals(elemento.getName())) {
                    elemento.setText(descripcion);
                } else if ("rarity".equals(elemento.getName())) {
                    elemento.setText(Integer.toString(rarity));
                } else if ("give".equals(elemento.getName())) {
                    Element give = elemento;
                    give.addElement("building").addAttribute("code", Integer.toString(codigo))
                            .addText("Piscifactoría de río");
                    give.addElement("part").addText(String.valueOf(Character.toUpperCase(letra)));
                    give.addElement("total").addText("AB");
                }
            }
            guardarDocumentoXML(document, "rewards/" + "pisci_r_" + Character.toLowerCase(letra) + ".xml");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa pisci_r_" + Character.toLowerCase(letra) + " creada");
        }
    }



    private void reducirQuantity(File archivo) {
        SAXReader reader = null;
        int quantity = 0;
        try {
            reader = new SAXReader();
            Document document = reader.read(archivo);
            Element raiz = document.getRootElement();
            for (Element elemento : raiz.elements()) {
                if ("quantity".equals(elemento.getName())) {
                    quantity = Integer.parseInt(elemento.getText()) - 1;
                    if (quantity == 0) {
                        archivo.delete();
                    } else {
                        elemento.setText(Integer.toString(quantity));
                        guardarDocumentoXML(document, archivo.getPath());
                    }
                }
            }
        } catch (Exception e) {
            log.logError("Error al buscar el nombre del archivo XML");
        }
    }

    private void canjearComida(File archivo) {
        SAXReader reader = null;
        try {
            reader = new SAXReader();
            Document document = reader.read(archivo);
            Element raiz = document.getRootElement();
            Element giveElement = raiz.element("give");
            if (giveElement != null) {
                Element food = giveElement.element("food");
                if (food != null) {
                    int cantidad = Integer.parseInt(food.getText());
                    if (sc.getAlmacenCentral()) {
                        ac.setEspacioOcupado(cantidad);
                    } else {
                        // Lógica casera para repartir equitativamente la comida
                        int piscifactoriasNoLLenas = 0;
                        for (Piscifactoria p : piscifactorias) {
                            if (!p.estaLlena()) {
                                piscifactoriasNoLLenas++;
                            }
                        }
                        if (piscifactoriasNoLLenas > 0) {
                            int comidaPorPiscifactoria = cantidad / piscifactoriasNoLLenas;
                            for (Piscifactoria p : piscifactorias) {
                                if (!p.estaLlena()) {
                                    p.setComidaActual(p.getComidaActual() + comidaPorPiscifactoria);
                                }
                            }
                            ac.setEspacioOcupado(0);
                            ac.setEspacioDisponible(200);
                        }
                    }
                    reducirQuantity(archivo);
                    System.out.println("Comida canjeada. Añadiendo " + cantidad + " de comida");
                    tr.transcripcion(sc.getNombrePartida(), "Recompensa " + archivo.getName() + " usada");
                    log.log(sc.getNombrePartida(), "Recompensa " + archivo.getName() + " usada");
                }
            }
        } catch (Exception e) {
            log.logError("Error al canjear comida");
        }
    }

    public void canjearMonedas(File archivo) {
        SAXReader reader = null;
        try {
            reader = new SAXReader();
            Document document = reader.read(archivo);
            Element raiz = document.getRootElement();
            Element giveElement = raiz.element("give");
            if (giveElement != null) {
                Element coins = giveElement.element("coins");
                if (coins != null) {
                    int cantidad = Integer.parseInt(coins.getText());
                    monedas.ingresar(cantidad);
                    System.out.println("Monedas canjeadas. Añadiendo "+cantidad+" monedas");
                    tr.transcripcion(sc.getNombrePartida(), "Recompensa " + archivo.getName() + " usada");
                    log.log(sc.getNombrePartida(),"Recompensa " + archivo.getName() + " usada");
                }
            }
        } catch (Exception e) {
            log.logError("Error al canjear monedas");
        }
    }

    public void canjearAlmacen() {
        Set<Character> partesTotales = new TreeSet<>(Arrays.asList('A', 'B', 'C', 'D'));
        Set<Character> partesDiponibles = new TreeSet<>();
        ArrayList<String> archivosAlmacen = new ArrayList<>();
        File rewards = new File("rewards");
        if (rewards.exists() && rewards.isDirectory()) {
            File[] archivos = rewards.listFiles();
            if (archivos != null && archivos.length > 0) {
                for (File archivo : archivos) {
                    if (archivo.getName().startsWith("almacen_")) {
                        String cadena = buscarNombre(archivo);
                        archivosAlmacen.add(archivo.getName());
                        char caracter = cadena.charAt(cadena.indexOf("[") + 1);
                        partesDiponibles.add(caracter);
                    }
                }
            }
        }
        if (partesDiponibles.equals(partesTotales)) {
            sc.setAlmacenCentral(true);
            System.out.println("Almacen central canjeado");
            log.log(sc.getNombrePartida(), "Recompensa Almacen central usada");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa Almacen central usada");
            for (String string : archivosAlmacen) {
                reducirQuantity(new File(string));
            }
        } else {
            System.out.println("No tienes todas las partes para canjear el almacen");
            System.out.println("Partes disponibles: " + partesDiponibles);
            System.out.println("Partes necesarias: " + partesTotales);
        }
    }

    public void canjearPisciM() {
        Scanner scanner =new Scanner(System.in);
        Set<Character> partesTotales = new TreeSet<>(Arrays.asList('A', 'B'));
        Set<Character> partesDiponibles = new TreeSet<>();
        ArrayList<String> archivosPiscifactoriaM = new ArrayList<>();
        File rewards = new File("rewards");
        if (rewards.exists() && rewards.isDirectory()) {
            File[] archivos = rewards.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.getName().startsWith("pisci_m_")) {
                        String cadena = buscarNombre(archivo);
                        archivosPiscifactoriaM.add("rewards/"+archivo.getName());
                        char caracter = cadena.charAt(cadena.indexOf("[") + 1);
                        partesDiponibles.add(caracter);
                    }
                }
            }
        }
        if (partesDiponibles.equals(partesTotales)) {
            System.out.println("Indique el nombre de la nueva piscifactoria: ");
            String nombreNuevaPiscifactoria = scanner.nextLine();
            piscifactorias.add(new PiscifactoriaMar(nombreNuevaPiscifactoria));
            System.out.println("Piscifactoria de mar canjeada");
            System.out.println("Añadida nueva piscifactoria de mar " + nombreNuevaPiscifactoria);
            log.log(sc.getNombrePartida(), "Recompensa Piscifactoria de mar usada");
            tr.transcripcion(sc.getNombrePartida(),"Recompensa Piscifactoria de mar usada");
            for (String string : archivosPiscifactoriaM) {
                reducirQuantity(new File(string));
            }
        } else {
            System.out.println("No tienes todas las partes para canjear la piscifactoria de mar");
            System.out.println("Partes disponibles: " + partesDiponibles);
            System.out.println("Partes necesarias: " + partesTotales);
        }
    }

    /**
     * Canjea una recompensa de Piscifactoria río
     */
    public void canjearPisciR() {
        Scanner scanner =new Scanner(System.in);
        Set<Character> partesTotales = new TreeSet<>(Arrays.asList('A', 'B'));
        Set<Character> partesDiponibles = new TreeSet<>();
        ArrayList<String> archivosPiscifactoriaM = new ArrayList<>();
        File rewards = new File("rewards");
        if (rewards.exists() && rewards.isDirectory()) {
            File[] archivos = rewards.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.getName().startsWith("pisci_r_")) {
                        String cadena = buscarNombre(archivo);
                        archivosPiscifactoriaM.add("rewards/"+archivo.getName());
                        char caracter = cadena.charAt(cadena.indexOf("[") + 1);
                        partesDiponibles.add(caracter);
                    }
                }
            }
        }
        if (partesDiponibles.equals(partesTotales)) {
            System.out.println("Indique el nombre de la nueva piscifactoria: ");
            String nombreNuevaPiscifactoria = scanner.nextLine();
            piscifactorias.add(new PiscifactoriaRio(nombreNuevaPiscifactoria));
            System.out.println("Piscifactoria de rio canjeada");
            System.out.println("Añadida nueva piscifactoria de rio " + nombreNuevaPiscifactoria);
            log.log(sc.getNombrePartida(),"Recompensa Piscifactoria de rio usada");
            tr.transcripcion(sc.getNombrePartida(),"Recompensa Piscifactoria de rio usada");
            for (String string : archivosPiscifactoriaM) {
                reducirQuantity(new File(string));
            }
        } else {
            System.out.println("No tienes todas las partes para canjear la piscifactoria de rio");
            System.out.println("Partes disponibles: " + partesDiponibles);
            System.out.println("Partes necesarias: " + partesTotales);
        }
    }

    /**
     * Obtiene el tipo de recompensa que se quiere canjear
     * y se llama a su método correspondiente
     *
     * @param file La recompensa a canjear
     */
    public void canjearRecompensa(File file) {
        String nombre = file.getName();
        if (nombre.startsWith("almacen_")) {
            if (sc.getAlmacenCentral()) {
                System.out.println("El almacen central ya esta activo");
            } else {
                canjearAlmacen();
            }
        } else if (nombre.startsWith("pisci_m_")) {
            canjearPisciM();
        } else if (nombre.startsWith("pisci_r_")) {
            canjearPisciR();
        } else if (nombre.startsWith("tanque_m")) {
            System.out.println();
        } else if (nombre.startsWith("tanque_r")) {
            System.out.println();
        } else {
            System.out.println("Ese archivo no puede ser canjeado");
        }
    }



}
