package Conexion;

import Registro.Log;
import Registro.Registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Registro.Transcripciones;

/**
 * Esta clase gestiona las operaciones relacionadas con los pedidos en la base de datos.
 * Proporciona métodos para insertar, actualizar, listar y eliminar pedidos,
 * así como para obtener la cantidad de pedidos en la base de datos.
 */
public class DAOPedidos {
    private Connection conexion;
    private PreparedStatement statementInsertarPedido;
    private PreparedStatement statementListarPedidos;
    private PreparedStatement statementActualizarPedido;

    private PreparedStatement statementObtenerIdClientes;
    private PreparedStatement statementObtenerIdPeces;

    private PreparedStatement statementUpdateCantidad;
    private PreparedStatement statementSelectCantidad;

    private PreparedStatement statementBorrarPedido;

    protected static Log log = Log.getInstance();
    protected static Registro registro = Registro.getInstance();
    protected static Transcripciones tr = Transcripciones.getInstance();




    private Random random;

    /**
     * Constructor de la clase DAOPedidos.
     * Establece la conexión a la base de datos y prepara las declaraciones SQL.
     */
    public DAOPedidos() {
        this.conexion = Conexion.obtenerConexion();
        prepararStatements();
        random = new Random();

    }

    /**
     * Prepara las declaraciones SQL utilizadas en la clase.
     */
    private void prepararStatements() {
        try {
            statementInsertarPedido = conexion.prepareStatement("INSERT INTO pedido (id_cliente, id_pez, cantidad_solicitada, cantidad_enviada) VALUES (?, ?, ?, ?)");
            statementListarPedidos = conexion.prepareStatement("SELECT pedido.numero_referencia, cliente.nombre AS nombre_cliente, pez.nombre AS nombre_pez, pedido.cantidad_solicitada, pedido.cantidad_enviada FROM pedido JOIN cliente ON pedido.id_cliente = cliente.id JOIN pez ON pedido.id_pez = pez.id WHERE pedido.cantidad_enviada < pedido.cantidad_solicitada ORDER BY numero_referencia");
            statementActualizarPedido = conexion.prepareStatement("UPDATE pedido SET cantidad_enviada = ? WHERE numero_referencia = ?");
            statementObtenerIdClientes = conexion.prepareStatement("SELECT id FROM cliente");
            statementObtenerIdPeces = conexion.prepareStatement("SELECT id FROM pez");
            statementUpdateCantidad = conexion.prepareStatement("UPDATE pedido SET cantidad_enviada = ? where numero_referencia = ?;");
            statementSelectCantidad = conexion.prepareStatement("SELECT cantidad_solicitada,cantidad_enviada FROM pedido;");
            statementBorrarPedido = conexion.prepareStatement("DELETE FROM pedido WHERE numero_referencia = ?");
        } catch (SQLException e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Inserta un nuevo pedido en la base de datos.
     */
    public void insertarPedido() {
        try (PreparedStatement statementInsertarPedido = conexion.prepareStatement("INSERT INTO pedido (id_cliente, id_pez, cantidad_solicitada, cantidad_enviada) VALUES (?, ?, ?, ?)")) {
            statementInsertarPedido.setInt(1, obtenerIdAleatorioCliente());
            statementInsertarPedido.setInt(2, obtenerIdAleatorioPez());
            statementInsertarPedido.setInt(3, random.nextInt(41) + 10);
            statementInsertarPedido.setInt(4, 0);
            statementInsertarPedido.executeUpdate();
        } catch (SQLException e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Obtiene la cantidad de pedidos desde la base de datos.
     *
     * @return El resultado de la consulta SQL como un conjunto de resultados ResultSet.
     */
    public ResultSet getCantidad() {
        ResultSet resultSet = null;
        try {
            resultSet = statementSelectCantidad.executeQuery();
        } catch (SQLException e) {
            log.logError(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                log.logError(e.getMessage());
            }
        }
        return resultSet;
    }

    /**
     * Obtiene un ID de cliente aleatorio desde la base de datos.
     *
     * @return ID de cliente aleatorio.
     */
    private int obtenerIdAleatorioCliente() {
        int idAleatorio = 0;
        try {
            ResultSet resultSet = statementObtenerIdClientes.executeQuery();
            List<Integer> idsClientes = new ArrayList<>();
            while (resultSet.next()) {
                idsClientes.add(resultSet.getInt("id"));
            }
            if (!idsClientes.isEmpty()) {
                idAleatorio = idsClientes.get(random.nextInt(idsClientes.size()));
            }
        } catch (SQLException e) {
            log.logError(e.getMessage());
        }
        return idAleatorio;
    }

    /**
     * Obtiene un ID de pez aleatorio desde la base de datos.
     *
     * @return ID de pez aleatorio.
     */
    private int obtenerIdAleatorioPez() {
        int idAleatorio = 0;
        try {
            ResultSet resultSet = statementObtenerIdPeces.executeQuery();
            List<Integer> idsPeces = new ArrayList<>();
            while (resultSet.next()) {
                idsPeces.add(resultSet.getInt("id"));
            }
            if (!idsPeces.isEmpty()) {
                idAleatorio = idsPeces.get(random.nextInt(idsPeces.size()));
            }
        } catch (SQLException e) {
            log.logError(e.getMessage());
        }
        return idAleatorio;
    }


    /**
     * Obtiene una lista de pedidos desde la base de datos.
     *
     * @return Lista de pedidos.
     */
    public List<Producto> productosSeleccionar(){
        List<Producto> listaPedidos = new ArrayList<>();
        try {
            ResultSet resultSet = statementListarPedidos.executeQuery();
            while (resultSet.next()) {
                int numeroReferencia = resultSet.getInt("numero_referencia");
                String idCliente = resultSet.getString("nombre_cliente");
                String idPez = resultSet.getString("nombre_pez");
                int cantidadSolicitada = resultSet.getInt("cantidad_solicitada");
                int cantidadEnviada = resultSet.getInt("cantidad_enviada");


                listaPedidos.add(new Producto(numeroReferencia, idCliente, idPez, cantidadSolicitada, cantidadEnviada));
            }
        } catch (SQLException e) {
            log.logError(e.getMessage());
        }
        return listaPedidos;
    }

    /**
     * Añade una cantidad de pez a un pedido específico en la base de datos.
     *
     * @param cantidadNueva Cantidad de pez a añadir.
     * @param numPedido     Número de referencia del pedido.
     */
    public void añadirPezPedido(int cantidadNueva,int numPedido){
        try {
            // Suponiendo que tienes una variable llamada 'cantidadNueva' que contiene la nueva cantidad enviada
            int nuevaCantidadEnviada = cantidadNueva;


            statementUpdateCantidad.setInt(1, nuevaCantidadEnviada);
            statementUpdateCantidad.setInt(2,numPedido);
            statementUpdateCantidad.executeUpdate();

            System.out.println("Cantidad enviada actualizada correctamente en la tabla 'pedido'.");
        } catch (SQLException e) {
            System.out.println("Error al intentar actualizar la cantidad enviada en la tabla 'pedido'.");
            log.logError(e.getMessage());
        }
    }

    /**
     * Actualiza la cantidad enviada de un pedido en la base de datos.
     *
     * @param numeroReferencia Número de referencia del pedido.
     * @param cantidadEnviada  Nueva cantidad enviada.
     */
    public void actualizarCantidadEnviada(int numeroReferencia, int cantidadEnviada) {
        try {
            statementActualizarPedido.setInt(1, cantidadEnviada);
            statementActualizarPedido.setInt(2, numeroReferencia);
            statementActualizarPedido.executeUpdate();
        } catch (SQLException e) {
            log.logError(e.getMessage());
        }
    }

    /**
     * Elimina un pedido de la base de datos.
     *
     * @param numeroReferencia Número de referencia del pedido a eliminar.
     */
    public void eliminarPedido(int numeroReferencia){
        try{
            statementBorrarPedido.setInt(1, numeroReferencia);
            statementBorrarPedido.executeQuery();
        }catch (SQLException e){
            log.logError(e.getMessage());
        }
    }


}
