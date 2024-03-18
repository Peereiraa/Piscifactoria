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

/**
 * Esta clase representa un gestor de recompensas en el simulador.
 * Permite generar, guardar y canjear distintos tipos de recompensas, como comida, monedas, y construcciones de piscifactorías y almacenes.
 */
public class Rewards {

    /**
     * Clase de registro para la gestión de registros de eventos y errores.
     */
    protected static Log log = Log.getInstance();

    /**
     * Clase para la gestión de transcripciones de eventos.
     */
    protected static Transcripciones tr = Transcripciones.getInstance();

    /**
     * Clase para la gestión de monedas dentro del sistema.
     */
    protected static Monedas monedas = Monedas.getInstance();

    /**
     * Objeto para gestionar el almacén central en el sistema.
     */
    private AlmacenCentral ac;

    /**
     * Objeto para manejar el simulador en el sistema.
     */
    private static Simulador sc;


    /**
     * Constructor de la clase Rewards.
     *
     * @param simulador El simulador al que se asocia el gestor de recompensas.
     */
    public Rewards(Simulador simulador){
        ac = new AlmacenCentral();
        sc = simulador;

    }

    /**
     * Genera un documento XML para representar una recompensa.
     * @return Documento XML generado.
     */
    public Document generarXML(){
        Document document = DocumentHelper.createDocument();
        Element raiz = document.addElement("reward");
        raiz.addElement("name").addText("");
        raiz.addElement("origin").addText("Pablo");
        raiz.addElement("desc").addText("");
        raiz.addElement("rarity").addText("");
        raiz.addElement("give");
        raiz.addElement("quantity").addText("1");
        return document;

    }

    /**
     * Busca el nombre en un archivo XML dado.
     * @param archivo El archivo XML en el que se buscará el nombre.
     * @return El nombre encontrado en el archivo XML.
     */
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

    /**
     * Guarda un documento XML en un archivo.
     * @param document El documento XML a guardar.
     * @param nombreArchivo El nombre del archivo en el que se guardará el documento.
     */
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

    /**
     * Actualiza un archivo XML incrementando la cantidad.
     * @param nombreArchivo El nombre del archivo XML a actualizar.
     */
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

    /**
     * Verifica si existe un archivo con el nombre especificado.
     * @param nombre El nombre del archivo a verificar.
     * @return true si existe el archivo, false de lo contrario.
     */

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

    /**
     * Genera una recompensa de comida según el número dado.
     * @param numero El número que determina el tipo de comida.
     */
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

    /**
     * Genera una recompensa de monedas según el número dado.
     * @param numero El número que determina la cantidad de monedas.
     */
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

    /**
     * Genera una recompensa de un almacén central.
     * @param letra La letra que representa la parte del almacén.
     */
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

    /**
     * Genera una recompensa de una piscifactoría de mar.
     * @param letra La letra que representa la parte de la piscifactoría.
     */
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

    /**
     * Genera una recompensa de una piscifactoría de río.
     * @param letra La letra que representa la parte de la piscifactoría.
     */
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

    /**
     * Reduce la cantidad en un archivo XML.
     * @param archivo El archivo XML en el que se reducirá la cantidad.
     */
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
                        return; // Agregar esta línea para salir del método si la cantidad es cero
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

    /**
     * Canjea una recompensa de comida.
     * @param archivo El archivo XML que representa la recompensa de comida.
     */
    public void canjearComida(File archivo) {
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
                        ac.meterComida(cantidad);
                    } else {

                        int piscifactoriasNoLLenas = 0;
                        for (Piscifactoria p : Simulador.piscifactorias) {
                            if (!p.estaLlena()) {
                                piscifactoriasNoLLenas++;
                            }
                        }
                        if (piscifactoriasNoLLenas > 0) {
                            int comidaPorPiscifactoria = cantidad / piscifactoriasNoLLenas;
                            for (Piscifactoria p : Simulador.piscifactorias) {
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

    /**
     * Canjea una recompensa de monedas.
     * @param archivo El archivo XML que representa la recompensa de monedas.
     */
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

    /**
     * Canjea una recompensa de un almacén central si todas las partes están disponibles.
     */
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
            AlmacenCentral ac = new AlmacenCentral(); // Crear el almacén central
            sc.setAlmacenCentral(ac); // Establecer el almacén central en el simulador
            System.out.println("Almacen central canjeado");
            log.log(sc.getNombrePartida(), "Recompensa Almacen central usada");
            tr.transcripcion(sc.getNombrePartida(), "Recompensa Almacen central usada");
            for (String string : archivosAlmacen) {
                reducirQuantity(new File("rewards/" + string));
            }
        } else {
            System.out.println("No tienes todas las partes para canjear el almacen");
            System.out.println("Partes disponibles: " + partesDiponibles);
            System.out.println("Partes necesarias: " + partesTotales);
        }
    }

    /**
     * Canjea una recompensa de una piscifactoría de mar si todas las partes están disponibles.
     */
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
            Simulador.piscifactorias.add(new PiscifactoriaMar(nombreNuevaPiscifactoria));
            System.out.println("Piscifactoria de mar canjeada");
            System.out.println("Añadida nueva piscifactoria de mar " + nombreNuevaPiscifactoria);
            log.log(sc.getNombrePartida(), "Recompensa Piscifactoria de mar usada");
            tr.transcripcion(sc.getNombrePartida(),"Recompensa Piscifactoria de mar usada");
            for (String string : archivosPiscifactoriaM) {
                reducirQuantity(new File("rewards/"+string));
            }
        } else {
            System.out.println("No tienes todas las partes para canjear la piscifactoria de mar");
            System.out.println("Partes disponibles: " + partesDiponibles);
            System.out.println("Partes necesarias: " + partesTotales);
        }
    }

    /**
     * Canjea una recompensa de una piscifactoría de río si todas las partes están disponibles.
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
            Simulador.piscifactorias.add(new PiscifactoriaRio(nombreNuevaPiscifactoria));
            System.out.println("Piscifactoria de rio canjeada");
            System.out.println("Añadida nueva piscifactoria de rio " + nombreNuevaPiscifactoria);
            log.log(sc.getNombrePartida(),"Recompensa Piscifactoria de rio usada");
            tr.transcripcion(sc.getNombrePartida(),"Recompensa Piscifactoria de rio usada");
            for (String string : archivosPiscifactoriaM) {
                reducirQuantity(new File("rewards/"+string));
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
