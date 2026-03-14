
package logica_bd;

import entidades.AccionCIFDES;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Antonio
 */

public class GestorBDAccionCIFDES extends GestorBDGeneral {

    //Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDAccionCIFDES() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método para verificar que la TablaAccionCIFDES existe
    public boolean verificarExistenciaTablaAccionCIFDES() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='accionCIFDES';";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la tabla 'accionCIFDES': " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    //Método para crear la TablaAcciónCIFDES
    public void creacionTablaAccionCIFDES() {
        String sql = "CREATE TABLE IF NOT EXISTS accionCIFDES ("
                   + "idAccion INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "idUsuario INTEGER NOT NULL, "
                   + "nombreUsuario TEXT NOT NULL, "
                   + "idArchivo INTEGER NOT NULL, "
                   + "nombreArchivo TEXT NOT NULL, "
                   + "tipoAccion TEXT NOT NULL, "
                   + "fecha TIMESTAMP NOT NULL, "
                   + "FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE, "
                   + "FOREIGN KEY (idArchivo) REFERENCES archivos(idArchivo) ON DELETE CASCADE ON UPDATE CASCADE"
                   + ");";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'accionCIFDES' creada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla accionCIFDES: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    
    /*
    MÉTODOS CREATE
    */
    
    //Método para insertar una Acción de cifrado o descifrado
    public void insertarAccionCIDFES(AccionCIFDES accionCifDes) {
        String sql = "INSERT INTO accionCIFDES (idUsuario, nombreUsuario, idArchivo, nombreArchivo, tipoAccion, fecha) "
                   + "VALUES (?, ?, ?, ?, ?, ?);";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, accionCifDes.getIdUsuario());
                pstmt.setString(2, accionCifDes.getNombreUsuarioAccion());
                pstmt.setInt(3, accionCifDes.getIdArchivo());
                pstmt.setString(4, accionCifDes.getNombreArchivoAccion());
                pstmt.setString(5, accionCifDes.getTipoAccion());
                pstmt.setTimestamp(6, new Timestamp(accionCifDes.getFecha().getTime()));
                pstmt.executeUpdate();
                System.out.println("Acción insertada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar acción: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    
    /*
    MÉTODOS READ
    */
    public ArrayList<AccionCIFDES> listarAccionesCIFDES() {
        ArrayList<AccionCIFDES> accionesCifDes = new ArrayList<>();
        String sql = "SELECT * FROM accionCIFDES";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    AccionCIFDES accionCifDes = new AccionCIFDES(
                            rs.getInt("idAccion"),
                            rs.getInt("idUsuario"),
                            rs.getString("nombreUsuario"),
                            rs.getInt("idArchivo"),
                            rs.getString("nombreArchivo"),
                            new Date(rs.getTimestamp("fecha").getTime()),
                            rs.getString("tipoAccion")
                          
                    );
                    accionesCifDes.add(accionCifDes );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener accionesEnvioRecp: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return accionesCifDes;
    }
    
    
    
    //Método para Listar las acciones por idUsuario
    public ArrayList<AccionCIFDES> listarAccionesPorUsuario(int idUsuario) {
        ArrayList<AccionCIFDES> listaAcciones = new ArrayList<>();
        String sql = "SELECT idAccion, idUsuario, nombreUsuario, idArchivo, nombreArchivo, tipoAccion, fecha "
                   + "FROM accionCIFDES WHERE idUsuario = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int idAccion = rs.getInt("idAccion");
                        String nombreUsuario = rs.getString("nombreUsuario");
                        int idArchivo = rs.getInt("idArchivo");
                        String nombreArchivo = rs.getString("nombreArchivo");
                        String tipoAccion = rs.getString("tipoAccion");
                        Date fecha = rs.getTimestamp("fecha");

                        AccionCIFDES accion = new AccionCIFDES(0,idUsuario, nombreUsuario, idArchivo, nombreArchivo, fecha, tipoAccion);
                        accion.setIdAccionCIFDES(idAccion);
                        listaAcciones.add(accion);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar acciones por usuario: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return listaAcciones;
    }
    
    //Método para Obtener la última acción de cifrado, pasando el id de archivo por parámetro
    public AccionCIFDES obtenerUltimaAccionCifrado(int idArchivo) {
        String sql = "SELECT idUsuario, nombreUsuario, idArchivo, nombreArchivo, tipoAccion, fecha "
                   + "FROM accionCIFDES "
                   + "WHERE idArchivo = ? AND tipoAccion = 'Cifrado' "
                   + "ORDER BY fecha DESC LIMIT 1;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idArchivo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int idUsuario = rs.getInt("idUsuario");
                        String nombreUsuario = rs.getString("nombreUsuario");
                        String nombreArchivo = rs.getString("nombreArchivo");
                        String tipoAccion = rs.getString("tipoAccion");
                        Date fecha = rs.getTimestamp("fecha");

                        return new AccionCIFDES(0,idUsuario, nombreUsuario, idArchivo, nombreArchivo, fecha, tipoAccion);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la última acción de cifrado: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return null;
    }
    
    //Método que devuelve la cantidad de acciones cifrado o descifrado que tiene un usuario
    public int contarAcciones(int idUsuario, String tipoAccion) {
        String sql;

        if (tipoAccion.equalsIgnoreCase("cifrado") || tipoAccion.equalsIgnoreCase("cifrado por")) {
            sql = "SELECT COUNT(*) FROM accionCIFDES WHERE idUsuario = ? AND tipoAccion LIKE ?";
            tipoAccion = "cifrado%";
        } else if (tipoAccion.equalsIgnoreCase("descifrado")) {
            sql = "SELECT COUNT(*) FROM accionCIFDES WHERE idUsuario = ? AND tipoAccion LIKE ?";
        } else {
            return 0;
        }

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setString(2, tipoAccion);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al contar acciones: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return 0;
    }
    
    /*
    MÉTODOS UPDATE
    */
    
    //Método para actualizar una lista de accionesCIFDES
    public boolean actualizarListaAccionesCIFDES(ArrayList<AccionCIFDES> accionesCifDes) {
        String sql = "UPDATE accionCIFDES SET "
                   + "idUsuario = ?, "
                   + "nombreUsuario = ?, "
                   + "idArchivo = ?, "
                   + "nombreArchivo = ?, "
                   + "tipoAccion = ?, "
                   + "fecha = ? "
                   + "WHERE idAccion = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                for (AccionCIFDES accion : accionesCifDes) {
                    pstmt.setInt(1, accion.getIdUsuario());
                    pstmt.setString(2, accion.getNombreUsuarioAccion());
                    pstmt.setInt(3, accion.getIdArchivo());
                    pstmt.setString(4, accion.getNombreArchivoAccion());
                    pstmt.setString(5, accion.getTipoAccion());
                    pstmt.setTimestamp(6, new Timestamp(accion.getFecha().getTime()));
                    pstmt.setInt(7, accion.getIdAccionCIFDES());

                    pstmt.addBatch(); //Agregamos a un lote que se ejecutará posteriormente
                }

                pstmt.executeBatch();//El lote se ejecuta
                System.out.println("Lista de acciones CIFDES actualizada correctamente.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la lista de acciones CIFDES: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    /*
    MÉTODOS DELETE
    */
    
        //Método para eliminar todas las acciones por idUsuario
    public boolean eliminarTodasLasAccionesPorUsuario(int idUsuario) {
        String sql = "DELETE FROM accionCIFDES WHERE idUsuario = ?";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                int filasEliminadas = pstmt.executeUpdate();
                return filasEliminadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar todas las acciones del usuario: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }

}


