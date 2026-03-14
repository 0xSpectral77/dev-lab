
package logica_bd;

import entidades.Contacto;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Antonio
 */

public class GestorBDContactos extends GestorBDGeneral {

    //Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDContactos() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método para verificar la existencia de la tabla contactos
    public boolean verificarExistenciaTablaContactos() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='contactos';";
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

    //Método para crear la tabla contactos
    public void creacionTablaContactos() {
        String sql = "CREATE TABLE IF NOT EXISTS contactos ("
                   + "idContacto INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "nombreContacto TEXT UNIQUE NOT NULL, "
                   + "ip TEXT NOT NULL, "
                   + "email TEXT UNIQUE NOT NULL, "
                   + "telefono INTEGER UNIQUE NOT NULL);";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'contactos' creada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    /*
    MÉTODOS CREATE
    */
     //Método para insertar una instancia del Objeto contacto, recibida por parámetro
    public void insertarContacto(Contacto contacto) {
        String sql = "INSERT INTO contactos (nombreContacto, ip, email, telefono) VALUES (?, ?, ?, ?);";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, contacto.getNombreContacto());
                pstmt.setString(2, contacto.getIp());
                pstmt.setString(3, contacto.getEmail());
                pstmt.setString(4, contacto.getTelefono());
                pstmt.executeUpdate();
                System.out.println("Contacto insertado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    /*
    MÉTODOS READ
    */
    
    public ArrayList<Contacto> listarContactos() {
        ArrayList<Contacto> listaContactos = new ArrayList<>();
        String sql = "SELECT * FROM contactos;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("idContacto");
                    String nombre = rs.getString("nombreContacto");
                    String ip = rs.getString("ip");
                    String email = rs.getString("email");
                    String telefono = rs.getString("telefono");

                    Contacto contacto = new Contacto(id, nombre, ip, email, telefono);
                    listaContactos.add(contacto);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los contactos: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return listaContactos;
    }

     //Método para obtener el correo por ID
    public String obtenerCorreoPorId(int idContacto) {
        String sql = "SELECT email FROM contactos WHERE idContacto = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener correo del contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }
    
    //Método para obtener el teléfono por ID
    public String obtenerTelefonoPorId(int idContacto) {
        String sql = "SELECT telefono FROM contactos WHERE idContacto = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getString("telefono");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener teléfono del contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }
    
    //Método para obtener la IP por ID
    public String obtenerIpPorId(int idContacto) {
        String sql = "SELECT ip FROM contactos WHERE idContacto = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getString("ip");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener IP del contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }
    
    //Método para obtener el ID de un contacto recibiendo el correo por parámetro, el correo tiene parámetro UNIQUE en la tabla
    public int obtenerIdPorCorreo(String correo) {
        String sql = "SELECT idContacto FROM contactos WHERE email = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, correo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getInt("idContacto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ID del contacto por correo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return -1;
    }
    
    //Método para obtener el nombre de un contacto, recibiendo por parámetro su ID
    public String obtenerNombrePorId(int idContacto) {
        String sql = "SELECT nombreContacto FROM contactos WHERE idContacto = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getString("nombreContacto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener nombre del contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }

    //Método para obtener el id de un Contacto recibiendo su nombre por parámetro
    public int obtenerIdPorNombre(String nombreContacto) {
        String sql = "SELECT idContacto FROM contactos WHERE nombreContacto = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getInt("idContacto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ID del contacto por nombre: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return -1;
    }

    //Método para obtener la IP de un Contacto mediante el nombre recibido por parámetro
    public String obtenerIpContactoPorNombre(String nombreContacto) {
        String sql = "SELECT ip FROM contactos WHERE nombreContacto = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return rs.getString("ip");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener IP del contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }

    //Método para verificar la existencia de un contacto pasandole un nombre por parámetro
    public boolean existeNombreContacto(String nombreContacto) {
        String sql = "SELECT COUNT(*) FROM contactos WHERE nombreContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el nombre de usuario: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return false;
    }

    //Método para verifica la existencia de un registro de email, pasándole un email por parámetro
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM contactos WHERE email = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el email: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return false;
    }

    //Método para verificar si existe un teléfono, pasándole un teléfono por parámetro
    public boolean existeTelefono(String telefono) {
        String sql = "SELECT COUNT(*) FROM contactos WHERE telefono = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, telefono);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el teléfono: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return false;
    }

    //Método para obtener un contacto pasándole un id por parámetro
    public Contacto obtenerContactoPorId(int idContacto) {
        String sql = "SELECT * FROM contactos WHERE idContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idContacto);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String nombre = rs.getString("nombreContacto");
                        String ip = rs.getString("ip");
                        String email = rs.getString("email");
                        String telefono = rs.getString("telefono");
                        return new Contacto(idContacto, nombre, ip, email, telefono);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener contacto por ID: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return null;
    }
    /*
    MÉTODOS UPDATE
    */
     //Método para actualizar un contacto recibido por parámetro
    public boolean actualizarContacto(Contacto contacto) {
        String sql = "UPDATE contactos SET nombreContacto = ?, ip = ?, email = ?, telefono = ? WHERE idContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, contacto.getNombreContacto());
                pstmt.setString(2, contacto.getIp());
                pstmt.setString(3, contacto.getEmail());
                pstmt.setString(4, contacto.getTelefono());
                pstmt.setInt(5, contacto.getIdContacto());
                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Contacto actualizado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el contacto con el id proporcionado.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar contacto: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }
    /*
    MÉTODOS DELETE
    */
    
     //Método para borrar un contacto por ID
    public void borrarContactoPorId(int idContacto) {
        String sql = "DELETE FROM contactos WHERE idContacto = ?;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idContacto);
                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Contacto con id " + idContacto + " borrado correctamente.");
                } else {
                    System.out.println("No se encontró un contacto con id " + idContacto + " para eliminar.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar contacto: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
}
