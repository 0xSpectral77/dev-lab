
package logica_bd;

import entidades.AccionEnvioRecp;
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

public class GestorBDAccionEnvioRecp extends GestorBDGeneral {

    //Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDAccionEnvioRecp() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método para verificar si existe la tabla AccionEnvioRecp
    public boolean verificarExistenciaTablaAccionEnvioRecp() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='accionEnvioRecp';";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la tabla 'accionEnvioRecp': " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }

    //Método para la creación de la tabla acciónEnvioRecp
    public void creacionTablaAccionEnvioRecp() {
        String sql = "CREATE TABLE IF NOT EXISTS accionEnvioRecp (" +
                "idAccion INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idUsuario INTEGER NOT NULL, " +
                "nombreUsuario TEXT NOT NULL, " +
                "idContacto INTEGER NOT NULL, " +
                "nombreContacto TEXT NOT NULL, " +
                "idArchivo INTEGER NOT NULL, " +
                "nombreArchivo TEXT NOT NULL, " +
                "tipoAccion TEXT NOT NULL, " +
                "fecha TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (idContacto) REFERENCES contactos(idContacto) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (idArchivo) REFERENCES archivos(idArchivo) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'accionEnvioRecp' creada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla accionEnvioRecp: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    
    /*
    MÉTODOS CREATE
    */
    //Método para insertar una accionEnvioRecp
    public void insertarAccionEnvioRecp(AccionEnvioRecp accionEnvioRecp) {
        String sql = "INSERT INTO accionEnvioRecp (idUsuario, nombreUsuario, idContacto, nombreContacto , idArchivo, nombreArchivo, tipoAccion, fecha) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, accionEnvioRecp.getIdUsuarioEnvioRecp());
                pstmt.setString(2, accionEnvioRecp.getNombreUsuarioEnvioRecp());
                pstmt.setInt(3, accionEnvioRecp.getIdContactoEnvioRecp());
                pstmt.setString(4, accionEnvioRecp.getNombreContactoEnvioRecp());
                pstmt.setInt(5, accionEnvioRecp.getIdArchivoEnvioRecp());
                pstmt.setString(6, accionEnvioRecp.getNombreArchivoEnvioRecp());
                pstmt.setString(7, accionEnvioRecp.getTipoAccion());
                pstmt.setTimestamp(8, new Timestamp(accionEnvioRecp.getFecha().getTime()));

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
    //Método para listar todas las accionesEnvioRecp
    public ArrayList<AccionEnvioRecp> listarAccionesEnvioRecp() {
        ArrayList<AccionEnvioRecp> accionesEnvioRecp = new ArrayList<>();
        String sql = "SELECT * FROM accionEnvioRecp";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    AccionEnvioRecp accion = new AccionEnvioRecp(
                            rs.getInt("idAccion"),
                            rs.getInt("idUsuario"),
                            rs.getString("nombreUsuario"),
                            rs.getInt("idContacto"),
                            rs.getString("nombreContacto"),
                            rs.getInt("idArchivo"),
                            rs.getString("nombreArchivo"),
                            rs.getString("tipoAccion"),
                            new Date(rs.getTimestamp("fecha").getTime())
                    );
                    accionesEnvioRecp.add(accion);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener accionesEnvioRecp: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return accionesEnvioRecp;
    }
    
    //Método para listar las acciones por id de usuario
    public ArrayList<AccionEnvioRecp> listarAccionesPorIdUsuario(int idUsuario) {
        ArrayList<AccionEnvioRecp> acciones = new ArrayList<>();
        String sql = "SELECT * FROM accionEnvioRecp WHERE idUsuario = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        AccionEnvioRecp accion = new AccionEnvioRecp(
                                rs.getInt("idAccion"),
                                idUsuario,
                                rs.getString("nombreUsuario"),
                                rs.getInt("idContacto"),
                                rs.getString("nombreContacto"),
                                rs.getInt("idArchivo"),
                                rs.getString("nombreArchivo"),
                                rs.getString("tipoAccion"),
                                new Date(rs.getTimestamp("fecha").getTime())
                        );
                        acciones.add(accion);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar acciones por ID de usuario: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return acciones;
    }
    
    //Método para obtener la última AccionEnvioRecp
    public AccionEnvioRecp obtenerUltimaAccionEnvioRecp(int idUsuario) {
        String sql = "SELECT * FROM accionEnvioRecp WHERE idUsuario = ? ORDER BY fecha DESC LIMIT 1";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new AccionEnvioRecp(
                                rs.getInt("idAccion"),
                                idUsuario,
                                rs.getString("nombreUsuario"),
                                rs.getInt("idContacto"),
                                rs.getString("nombreContacto"),
                                rs.getInt("idArchivo"),
                                rs.getString("nombreArchivo"),
                                rs.getString("tipoAccion"),
                                new Date(rs.getTimestamp("fecha").getTime())
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la última acción de envío y recepción: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return null;
    }

    /*
    MÉTODOS UPDATE
    */
    //Método para actualizar una lista de accionesEnvio Recp que se le pasa por parámetro
    public boolean actualizarListaAccionesEnvioRecp(ArrayList<AccionEnvioRecp> accionesEnvioRecp) {
        String sql = "UPDATE accionEnvioRecp SET " +
                "idUsuario = ?, nombreUsuario = ?, idContacto = ?, nombreContacto = ?, " +
                "idArchivo = ?, nombreArchivo = ?, tipoAccion = ?, fecha = ? " +
                "WHERE idAccion = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                for (AccionEnvioRecp accion : accionesEnvioRecp) {
                    pstmt.setInt(1, accion.getIdUsuarioEnvioRecp());
                    pstmt.setString(2, accion.getNombreUsuarioEnvioRecp());
                    pstmt.setInt(3, accion.getIdContactoEnvioRecp());
                    pstmt.setString(4, accion.getNombreContactoEnvioRecp());
                    pstmt.setInt(5, accion.getIdArchivoEnvioRecp());
                    pstmt.setString(6, accion.getNombreArchivoEnvioRecp());
                    pstmt.setString(7, accion.getTipoAccion());
                    pstmt.setTimestamp(8, new Timestamp(accion.getFecha().getTime()));
                    pstmt.setInt(9, accion.getIdAccionEnvioRecp());

                    pstmt.addBatch(); //Añadimos la acción a un lote
                }

                pstmt.executeBatch(); //Ejecutamos el lote para poder actualizar las acciones
                System.out.println("Lista de acciones de envío/recepción actualizada correctamente.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la lista de acciones de envío/recepción: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }

    //Método para actualizar el nombre de contacto por id
    public boolean actualizarNombreContactoPorId(int idContacto, String nuevoNombre) {
        String sql = "UPDATE accionEnvioRecp SET nombreContacto = ? WHERE idContacto = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nuevoNombre);
                pstmt.setInt(2, idContacto);

                int filasActualizadas = pstmt.executeUpdate();

                if (filasActualizadas > 0) {
                    System.out.println("Nombre del contacto actualizado correctamente para idContacto " + idContacto);
                    return true;
                } else {
                    System.out.println("No se encontró ninguna acción con idContacto " + idContacto);
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar el nombre del contacto: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    /*
    MÉTODOS DELETE
    */
    
    //Método para eliminar las acciones o transferencias por id de usuario
    public void eliminarAccionesEnvioRecpPorIdUsuario(int idUsuario) {
        String sql = "DELETE FROM accionEnvioRecp WHERE idUsuario = ?";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Se eliminaron " + filasAfectadas + " acciones de transferencias del usuario con id " + idUsuario + ".");
                } else {
                    System.out.println("No se encontraron acciones de transferencias para el usuario con id " + idUsuario + ".");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al acciones de transferencias por idUsuario: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
}






   

