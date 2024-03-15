package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DAOPedidos {
    private Connection conexion;
    private PreparedStatement statementInsertarPedido;
    private PreparedStatement statementListarPedidos;
    private PreparedStatement statementActualizarPedido;

    private PreparedStatement statementObtenerIdClientes;
    private PreparedStatement statementObtenerIdPeces;


    private Random random;

    public DAOPedidos() {
        this.conexion = Conexion.obtenerConexion();
        prepararStatements();
        random = new Random();

    }

    private void prepararStatements() {
        try {
            statementInsertarPedido = conexion.prepareStatement("INSERT INTO pedido (id_cliente, id_pez, cantidad_solicitada, cantidad_enviada) VALUES (?, ?, ?, ?)");
            statementListarPedidos = conexion.prepareStatement("SELECT pedido.numero_referencia, cliente.nombre AS nombre_cliente, pez.nombre AS nombre_pez, pedido.cantidad_solicitada, pedido.cantidad_enviada FROM pedido JOIN cliente ON pedido.id_cliente = cliente.id JOIN pez ON pedido.id_pez = pez.id WHERE pedido.cantidad_enviada < pedido.cantidad_solicitada ORDER BY pez.nombre");
            statementActualizarPedido = conexion.prepareStatement("UPDATE pedido SET cantidad_enviada = ? WHERE numero_referencia = ?");
            statementObtenerIdClientes = conexion.prepareStatement("SELECT id FROM cliente");
            statementObtenerIdPeces = conexion.prepareStatement("SELECT id FROM pez");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insertarPedido() {
        try {
            statementInsertarPedido.setInt(1, obtenerIdAleatorioCliente());
            statementInsertarPedido.setInt(2, obtenerIdAleatorioPez());
            statementInsertarPedido.setInt(3, random.nextInt(41) + 10);
            statementInsertarPedido.setInt(4, 0);
            statementInsertarPedido.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
        return idAleatorio;
    }

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
            e.printStackTrace();
        }
        return idAleatorio;
    }

    public List<String> listarPedidos() {
        List<String> listaPedidos = new ArrayList<>();
        try {
            ResultSet resultSet = statementListarPedidos.executeQuery();
            while (resultSet.next()) {
                int numeroReferencia = resultSet.getInt("numero_referencia");
                String nombreCliente = resultSet.getString("nombre_cliente");
                String nombrePez = resultSet.getString("nombre_pez");
                int cantidadSolicitada = resultSet.getInt("cantidad_solicitada");
                int cantidadEnviada = resultSet.getInt("cantidad_enviada");
                double porcentajeEnviado = ((double) cantidadEnviada / cantidadSolicitada) * 100;

                listaPedidos.add("[" + numeroReferencia + "] " + nombreCliente + ": " + nombrePez + " (" + String.format("%.2f", porcentajeEnviado) + "%)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPedidos;
    }

    public void actualizarCantidadEnviada(int numeroReferencia, int cantidadEnviada) {
        try {
            statementActualizarPedido.setInt(1, cantidadEnviada);
            statementActualizarPedido.setInt(2, numeroReferencia);
            statementActualizarPedido.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
