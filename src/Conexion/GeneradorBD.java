package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import Comun.Simulador;
import propiedades.AlmacenPropiedades;

/**
 * Esta clase se encarga de generar la estructura de la base de datos y de insertar datos de prueba.
 * También proporciona métodos para preparar los statements de inserción y para obtener una instancia única del generador.
 */
public class GeneradorBD {

    private PreparedStatement statementClientes;
    private PreparedStatement statementPeces;
    private PreparedStatement statementPedidos;


    private static GeneradorBD instancia = null;
    private Connection conexion;

    String[] telefonos = {"912345678", "611223344", "643987654", "910987654", "678123456",
            "662334455", "955667788", "933445566", "976123456", "981112233"};
    String[] nifs = {"A12345678", "B23456789", "C34567890", "D45678901", "E56789012",
            "F67890123", "G78901234", "H89012345", "J90123456", "K01234567"};
    String[] nombresEmpresas = {"Innovatech Solutions", "Global Dynamics", "TechNova", "BrightWave Innovations", "InnovaCorp",
            "FutureScape Enterprises", "NexGen Solutions", "Skylink Technologies", "Stratix Innovations", "Horizon Enterprises"};
    public static String[] nCientifico = { AlmacenPropiedades.BESUGO.getCientifico(), AlmacenPropiedades.CABALLA.getCientifico(),
            AlmacenPropiedades.ROBALO.getCientifico(),
            AlmacenPropiedades.RODABALLO.getCientifico(), AlmacenPropiedades.SARGO.getCientifico(),
            AlmacenPropiedades.DORADA.getCientifico(),
            AlmacenPropiedades.LUBINA_EUROPEA.getCientifico(), AlmacenPropiedades.CARPA_PLATEADA.getCientifico(),
            AlmacenPropiedades.CARPA.getCientifico(),
            AlmacenPropiedades.LUCIO_NORTE.getCientifico(), AlmacenPropiedades.PEJERREY.getCientifico(),
            AlmacenPropiedades.PERCA_EUROPEA.getCientifico() };

    /**
     * Constructor de la clase GeneradorBD.
     * Este constructor inicializa la conexión a la base de datos y prepara los
     * statements para operaciones de inserción y selección.
     */
    public GeneradorBD() {
        this.conexion = Conexion.obtenerConexion();
        prepararStatementsInserts();

    }

    /**
     * Método estático que obtiene una instancia única del Generador utilizando el patrón
     * Singleton.
     *
     * @return La instancia única del GeneradorBD.
     */
    public static GeneradorBD obtenerInstancia() {
        if (instancia == null) {
            instancia = new GeneradorBD();
        }
        return instancia;
    }

    /**
     * Crea las tablas en la base de datos.
     */
    public void crearTablas() {
        Statement statement = null;
        try {
            statement = conexion.createStatement();
            try {
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS cliente (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(255) NOT NULL, nif VARCHAR(20) UNIQUE NOT NULL, telefono VARCHAR(20));");
            } catch (SQLException e) {
                System.out.println("No se pudo crear la tabla cliente");
            }

            try {
                statement.execute(
                        "CREATE TABLE pez (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(255) NOT NULL, nombre_cientifico VARCHAR(255) NOT NULL);");
            } catch (SQLException e) {
                System.out.println("No se pudo crear la tabla pez");
            }
            try {
                statement.execute(
                        "CREATE TABLE pedido (numero_referencia INT AUTO_INCREMENT PRIMARY KEY, id_cliente INT, id_pez INT, cantidad_solicitada INT, cantidad_enviada INT,"
                                +
                                "FOREIGN KEY (id_cliente) REFERENCES cliente(id), FOREIGN KEY (id_pez) REFERENCES pez(id));");
            } catch (SQLException e) {
                System.out.println("No se pudo crear la tabla pedido");
            }

        } catch (SQLException e) {
            System.out.println("Las tablas no se crearon correctamente en la BD");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("No se pudo cerrar el statement correctamente");
            }
        }
    }

    /**
     * Prepara los statements de inserción para las tablas de la base de datos.
     * Estos statements de inserción se utilizan para insertar registros en las
     * tablas de la base de datos.
     * Si ocurre algún error al preparar los statements, se imprime un mensaje de
     * error correspondiente.
     */
    public void prepararStatementsInserts() {

        try {
            try {
                statementClientes = conexion.prepareStatement("INSERT INTO cliente (nombre, nif,telefono) VALUES (?,?,?);");
            } catch (SQLException e) {
                System.out.println("No se pudo insertar en cliente");
            }
            try {
                statementPeces = conexion
                        .prepareStatement("INSERT INTO pez (nombre, nombre_cientifico) VALUES (?,?);");
            } catch (SQLException e) {
                System.out.println("No se pudo insertar en pez");
            }
            try {
                statementPedidos = conexion.prepareStatement("INSERT INTO `fruteria` (nombre) VALUES (?);");
            } catch (SQLException e) {
                System.out.println("No se pudo insertar en fruteria");
            }


        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Cierra los statements y la conexión.
     */
    public void cerrar() {
        try {
            if (statementClientes != null) {
                statementClientes.close();
            }
            if (statementPeces != null) {
                statementPeces.close();
            }
            if (statementPedidos != null) {
                statementPedidos.close();
            }
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar los recursos: " + e.getMessage());
        }
    }

    /**
     * Inserta registros de clientes en la base de datos.
     */
    public void insertarClientes(){
        try {
            for (int i = 0; i < 10; i++) {
                statementClientes.setString(1,nombresEmpresas[i]);
                statementClientes.setString(2,nifs[i]);
                statementClientes.setString(3,telefonos[i]);
                statementClientes.addBatch();
            }
            statementClientes.executeBatch();

            System.out.println("Clientes insertados correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar clientes: " + e.getMessage());
        }
    }

    /**
     * Inserta registros de peces en la base de datos.
     */
    public void insertarPeces(){
        try {
            for (int i = 0; i < Simulador.peces.length; i++) {
                statementPeces.setString(1,Simulador.peces[i]);
                statementPeces.setString(2,nCientifico[i]);
                statementPeces.addBatch();
            }
            statementPeces.executeBatch();

            System.out.println("Peces insertados correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar peces: " + e.getMessage());
        }
    }





}