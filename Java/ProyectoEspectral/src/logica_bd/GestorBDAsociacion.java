
package logica_bd;

import entidades.Asociacion;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 *
 * @author Antonio
 */ 

public class GestorBDAsociacion extends GestorBDGeneral {

    //Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDAsociacion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método que verifica la existencia de la tabla asociación
    public boolean verificarExistenciaTablaAsociacion() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='asociacion';";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la tabla: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }

    //Método encargado de crear la tabla asociación
    public void creacionTablaAsociacion() {
        String sql = "CREATE TABLE IF NOT EXISTS asociacion ("
                   + "idAsociacion INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "idUsuario INTEGER NOT NULL, "
                   + "nombreUsuarioAsociado TEXT NOT NULL, "
                   + "idContacto INTEGER NOT NULL, "
                   + "nombreContactoAsociado TEXT NOT NULL, "
                   + "FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE, "
                   + "FOREIGN KEY (idContacto) REFERENCES contactos(idContacto) ON DELETE CASCADE ON UPDATE CASCADE"
                   + ");";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'asociacion' creada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla 'asociacion': " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    
     /*
    MÉTODOS CREATE
    */
    
    //Método para inserta una asociación en la tabla asociacion
    public void insertarAsociacion(Asociacion asociacion) {
        String sql = "INSERT INTO asociacion (idUsuario, nombreUsuarioAsociado, idContacto, nombreContactoAsociado) "
                   + "VALUES (?, ?, ?, ?);";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, asociacion.getIdUsuarioAsociado());
                pstmt.setString(2, asociacion.getNombreUsuarioAsociado());
                pstmt.setInt(3, asociacion.getIdContactoAsociado());
                pstmt.setString(4, asociacion.getNombreContactoAsociado());
                pstmt.executeUpdate();
                System.out.println("Asociación insertada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar asociación: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    /*
    MÉTODOS READ
    */
    
    //Método para listar las asociaciones por idUsuario
    public ArrayList<Asociacion> listarAsociacionesPorIdUsuario(int idUsuario) {
        ArrayList<Asociacion> asociaciones = new ArrayList<>();
        String sql = "SELECT * FROM asociacion WHERE idUsuario = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int idAsociacion = rs.getInt("idAsociacion");
                        String nombreUsuarioAsociado = rs.getString("nombreUsuarioAsociado");
                        int idContacto = rs.getInt("idContacto");
                        String nombreContactoAsociado = rs.getString("nombreContactoAsociado");

                        Asociacion asociacion = new Asociacion(idAsociacion, idUsuario, nombreUsuarioAsociado, idContacto, nombreContactoAsociado);
                        asociaciones.add(asociacion);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener asociaciones por ID de usuario: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return asociaciones;
    }
    
    //Método para listar todas las asociaciones, devuelve un ArrayList
    public ArrayList<Asociacion> listarAsociaciones() {
        ArrayList<Asociacion> asociaciones = new ArrayList<>();
        String sql = "SELECT * FROM asociacion;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int idAsociacion = rs.getInt("idAsociacion");
                    int idUsuario = rs.getInt("idUsuario");
                    String nombreUsuarioAsociado = rs.getString("nombreUsuarioAsociado");
                    int idContacto = rs.getInt("idContacto");
                    String nombreContactoAsociado = rs.getString("nombreContactoAsociado");

                    Asociacion asociacion = new Asociacion(idAsociacion, idUsuario, nombreUsuarioAsociado, idContacto, nombreContactoAsociado);
                    asociaciones.add(asociacion);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener asociaciones: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return asociaciones;
    }
    
    //Método que devuelve una asociación, pasandole por parámetro el idUsuario y el idContacto
    public Asociacion obtenerAsociacionPorUsuarioYContacto(int idUsuario, int idContacto) {
        String sql = "SELECT * FROM asociacion WHERE idUsuario = ? AND idContacto = ? LIMIT 1;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int idAsociacion = rs.getInt("idAsociacion");
                        String nombreUsuarioAsociado = rs.getString("nombreUsuarioAsociado");
                        String nombreContactoAsociado = rs.getString("nombreContactoAsociado");

                        return new Asociacion(idAsociacion, idUsuario, nombreUsuarioAsociado, idContacto, nombreContactoAsociado);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la asociación: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }
    
    //Método que verifica si existe una asociación entre un usuario y un contacto, pasándole los  nombre de Usuario y Contacto
    public boolean existeAsociacionPorUsuarioYContacto(String nombreUsuarioAsociado, String nombreContactoAsociado) {
        String sql = "SELECT 1 FROM asociacion WHERE nombreUsuarioAsociado = ? AND nombreContactoAsociado = ? LIMIT 1;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreUsuarioAsociado);
                pstmt.setString(2, nombreContactoAsociado);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la asociación: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    //Método para verificar si existe una relación por usuario y contacto, pero esta vez recibiendo los ids de Usuario y Contacto
    public boolean existeRelacionPorUsuarioYContacto(int idUsuario, int idContacto) {
        String sql = "SELECT 1 FROM asociacion WHERE idUsuario = ? AND idContacto = ? LIMIT 1;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la relación entre usuario y contacto: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }


    /*
    MÉTODOS UPDATE
    */
    
    //Método para actualizar el nombre del contacto asociado, pasando por parámetro el idUsuario, el idContacto y el nuevo nombre del contacto
    public boolean actualizarNombreContactoAsociado(int idUsuario, int idContacto, String nombreNuevoContacto) {
        String sql = "UPDATE asociacion SET nombreContactoAsociado = ? WHERE idUsuario = ? AND idContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreNuevoContacto);
                pstmt.setInt(2, idUsuario);
                pstmt.setInt(3, idContacto);
                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el nombre del contacto: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    //Método para actualizar una lista de asociaciones recibida por parámetro
    public boolean actualizarListaAsociaciones(ArrayList<Asociacion> asociaciones) {
        String sql = "UPDATE asociacion SET "
                   + "nombreUsuarioAsociado = ?, "
                   + "idContacto = ?, "
                   + "nombreContactoAsociado = ? "
                   + "WHERE idAsociacion = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                for (Asociacion asociacion : asociaciones) {
                    pstmt.setString(1, asociacion.getNombreUsuarioAsociado());
                    pstmt.setInt(2, asociacion.getIdContactoAsociado());
                    pstmt.setString(3, asociacion.getNombreContactoAsociado());
                    pstmt.setInt(4, asociacion.getIdAsociacion());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                System.out.println("Lista de asociaciones actualizada correctamente.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la lista de asociaciones: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    //Método para actualizar el nombre de un contacto asociado, recibiendo por parámetro el id de contacto y el nombre del nuevo Contacto
    public boolean actualizarNombreContactoAsociadoPorIdContacto(int idContacto, String nuevoNombreContacto) {
        String sql = "UPDATE asociacion SET nombreContactoAsociado = ? WHERE idContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nuevoNombreContacto);
                pstmt.setInt(2, idContacto);
                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar nombre de contacto asociado: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    /*
    MÉTODOS DELETE
    */
    //Método booleano para borrar las asociaciones, recibiendo por parámetro un id de usuario y un id de contacto
    public boolean borrarAsociacionPorUsuarioYContacto(int idUsuario, int idContacto) {
        String sql = "DELETE FROM asociacion WHERE idUsuario = ? AND idContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                pstmt.setInt(2, idContacto);
                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar asociación: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
}
