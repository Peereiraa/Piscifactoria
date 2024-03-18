package Conexion;

import Registro.Log;
import Registro.Registro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import Registro.Transcripciones;

public class Conexion {
    private static final String SERVER = "localhost";
    private static final String PORT_NUMBER = "3306";
    private static final String DATABASE = "piscifactoria";
    private static final String USERNAME = "admin_pisci";
    private static final String PASSWORD = "abc123.";
    private static Connection conexion = null;

    protected static Log log = Log.getInstance();
    protected static Registro registro = Registro.getInstance();
    protected static Transcripciones tr = Transcripciones.getInstance();

    /**
     * Obtiene una conexión a la base de datos.
     * Si la conexión aún no se ha establecido, se crea una nueva conexión
     * utilizando las credenciales proporcionadas.
     * Utiliza las constantes definidas para el nombre de usuario, la contraseña, el
     * servidor, el número de puerto y la base de datos.
     * Si la conexión ya está establecida, se devuelve la conexión existente.
     * Se manejan excepciones si la conexión no puede ser establecida.
     *
     * @return La conexión a la base de datos.
     */
    public static Connection obtenerConexion() {
        if (conexion == null) {
            Properties propsConexion = new Properties();
            propsConexion.put("user", USERNAME);
            propsConexion.put("password", PASSWORD);
            try {
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://" + SERVER + ":" + PORT_NUMBER + "/" + DATABASE
                                + "?rewriteBatchedStatements=true",
                        propsConexion);
            } catch (SQLException e) {

                log.logError(e.getMessage());
            }
            return conexion;
        } else {
            return conexion;
        }
    }

    /**
     * Cierra la conexión a la base de datos, si está abierta.
     * Si la conexión no es nula, se cierra utilizando el método close().
     * Se maneja una excepción SQLException en caso de que ocurra un error al cerrar
     * la conexión.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            log.logError(e.getMessage());

        }
    }
}