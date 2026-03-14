
package logica_bd;

import entidades.Sesion;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * 
 * @author Antonio
 */

public class GestorBDSesiones extends GestorBDGeneral {

    //Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDSesiones() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método para crear la tabla de sesiones
    public void creacionTablaSesiones() {
        String sql = "CREATE TABLE IF NOT EXISTS sesiones ("
                + "idSesion INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "idUsuario INTEGER NOT NULL, "
                + "nombreUsuario TEXT NOT NULL, "
                + "fechaInicio TIMESTAMP NOT NULL, "
                + "fechaFin TIMESTAMP NOT NULL, "
                + "FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'sesiones' creada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    //Método para verificar que la tabla sesiones existe
    public boolean verificarExistenciaTablaSesiones() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='sesiones';";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia de tabla: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    /*
    MÉTODOS CREATE
    */
    //Método encargado de inserta una instancia de sesión que se le pasa por parámetro en la tabla sesiones
    public int insertarSesion(Sesion sesion) {
        String sql = "INSERT INTO sesiones (idUsuario, nombreUsuario, fechaInicio, fechaFin) VALUES (?, ?, ?, ?)";
        int idGenerado = 0;

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, sesion.getIdUsuario());
                pstmt.setString(2, sesion.getNombreUsuario());
                pstmt.setTimestamp(3, new Timestamp(sesion.getFechaInicio().getTime()));
                pstmt.setTimestamp(4, new Timestamp(sesion.getFechaFin().getTime()));

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            idGenerado = rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar sesión: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return idGenerado;
    }
    /*
    MÉTODOS READ
    */
    
    // Método para listar todas las sesiones
    public ArrayList<Sesion> listarSesiones() {
        String sql = "SELECT * FROM sesiones";
        ArrayList<Sesion> sesiones = new ArrayList<>();

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    sesiones.add(new Sesion(
                            rs.getInt("idSesion"),
                            rs.getInt("idUsuario"),
                            rs.getString("nombreUsuario"),
                            rs.getTimestamp("fechaInicio"),
                            rs.getTimestamp("fechaFin")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar todas las sesiones: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return sesiones;
    }

    
    //Método para listar las sesiones de un usuario
    public ArrayList<Sesion> listarSesionesPorIdUsuario(int idUsuario) {
        String sql = "SELECT * FROM sesiones WHERE idUsuario = ?";
        ArrayList<Sesion> sesiones = new ArrayList<>();

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        sesiones.add(new Sesion(
                                rs.getInt("idSesion"),
                                idUsuario,
                                rs.getString("nombreUsuario"),
                                rs.getTimestamp("fechaInicio"),
                                rs.getTimestamp("fechaFin")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar sesiones: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return sesiones;
    }
    
    
    /*
    MÉTODOS UPDATE
    */
    //Método encargado de actualizar la fecha de fin de sesión pasando por parámetro un id de sesión y una nueva fechaFin 
    public void actualizarFechaFinSesion(int idSesion, Date fechaFin) {
        String sql = "UPDATE sesiones SET fechaFin = ? WHERE idSesion = ?";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                if (fechaFin != null) {
                    pstmt.setTimestamp(1, new Timestamp(fechaFin.getTime()));
                } else {
                    pstmt.setNull(1, Types.TIMESTAMP);
                }
                pstmt.setInt(2, idSesion);

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    System.out.println("Fecha de fin actualizada.");
                } else {
                    System.out.println("Sesión no encontrada.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar fecha de fin: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    //Método para actulizar una lista de sesiones que se le pasan por parámetro mediante un ArrayList
    public boolean actualizarListaSesionesPorUsuario(ArrayList<Sesion> sesiones) {
        String sql = "UPDATE sesiones SET idUsuario = ?, nombreUsuario = ?, fechaInicio = ?, fechaFin = ? WHERE idSesion = ?";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                for (Sesion sesion : sesiones) {
                    pstmt.setInt(1, sesion.getIdUsuario());
                    pstmt.setString(2, sesion.getNombreUsuario());
                    pstmt.setTimestamp(3, new Timestamp(sesion.getFechaInicio().getTime()));
                    pstmt.setTimestamp(4, new Timestamp(sesion.getFechaFin().getTime()));
                    pstmt.setInt(5, sesion.getIdSesion());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                System.out.println("Sesiones actualizadas correctamente.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar sesiones: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    
    /*
    MÉTODOS DELETE
    */
    
    //Método para eliminar una sesión por id de sesión
    public boolean eliminarSesionPorId(int idSesion) {
        String sql = "DELETE FROM sesiones WHERE idSesion = ?";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idSesion);
                int filas = pstmt.executeUpdate();

                if (filas > 0) {
                    System.out.println("Sesión eliminada correctamente.");
                    return true;
                } else {
                    System.out.println("Sesión no encontrada.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar sesión: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
}


