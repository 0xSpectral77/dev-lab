/**
 *
 * @author Antonio
 */
package logica_bd;

import entidades.Usuario;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Antonio
 */


public class GestorBDUsuarios extends GestorBDGeneral {

    //Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDUsuarios() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método para verificar la existencia de la tabla usuarios
    public boolean verificarExistenciaTablaUsuarios() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='usuarios';";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la tabla: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }

    //Método para crear la tabla Usuarios
    public void creacionTablaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreUsuario TEXT UNIQUE NOT NULL, " +
                "contrasenia TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "telefono INTEGER UNIQUE NOT NULL, " +
                "ip TEXT NOT NULL);";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'usuarios' creada correctamente.");
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
    
    //Método para insertar un usuario en la tabl usuarios
    public void insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombreUsuario, contrasenia, email, telefono, ip) VALUES (?, ?, ?, ?, ?);";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
           
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, usuario.getNombreUsuario());
                pstmt.setString(2, usuario.getContrasenia());
                pstmt.setString(3, usuario.getEmail());
                pstmt.setInt(4, usuario.getTelefono());
                pstmt.setString(5, usuario.getIp());

                int filasInsertadas = pstmt.executeUpdate();
                if (filasInsertadas > 0) {
                  System.out.println("Usuario insertado satisfactoriamente.");
                } else {
                   System.out.println("No se ha podido insertar el usuario.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
    /*
    MÉTODOS READ
    */
    
    //Método para listar todos los usuarios
    public ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios;";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    usuarios.add(new Usuario(
                            rs.getInt("idUsuario"),
                            rs.getString("nombreUsuario"),
                            rs.getString("contrasenia"),
                            rs.getString("email"),
                            rs.getInt("telefono"),
                            rs.getString("ip")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return usuarios;
    }
    
    //Método para obtener el nombre de usuario por Id
    public String obtenerNombreUsuarioPorId(int idUsuario) {
        String nombreUsuario = null;
        String sql = "SELECT nombreUsuario FROM usuarios WHERE idUsuario = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        nombreUsuario = rs.getString("nombreUsuario");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el nombre de usuario por ID: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return nombreUsuario;
    }

    //Método para comprobar si existe un usuario introduciendo un número de teléfono que se le pasará por parámetro
    public boolean existeUsuarioPorTelefono(String telefonoStr) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE telefono = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            int telefono = Integer.parseInt(telefonoStr);
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, telefono);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("El número de teléfono no es válido.");
        } catch (SQLException e) {
            System.out.println("Error al verificar el teléfono: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return false;
    }

    //Método para verificar si exite un usuario introduciendo un email por parámetro
    public boolean existeUsuarioPorEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
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
            System.out.println("Error al verificar email: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return false;
    }

    //Método para comprobar si existe un usuario por nombre de usuario, apunta al método para onbtener un usuario por nombre de usuario
    public boolean existeUsuarioPorNombre(String nombreUsuario) {
        return obtenerUsuarioPorNombreUsuario(nombreUsuario) != null;
    }

    //Método para obtener un usuario por Id
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE idUsuario = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, idUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        usuario = new Usuario(
                                idUsuario,
                                rs.getString("nombreUsuario"),
                                rs.getString("contrasenia"),
                                rs.getString("email"),
                                rs.getInt("telefono"),
                                rs.getString("ip"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por ID: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return usuario;
    }
    
    //Método para obtener un usuario por nombre de usuario
    public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE nombreUsuario = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombreUsuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        usuario = new Usuario(
                                rs.getInt("idUsuario"),
                                nombreUsuario,
                                rs.getString("contrasenia"),
                                rs.getString("email"),
                                rs.getInt("telefono"),
                                rs.getString("ip"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por nombre: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return usuario;
    }
    
    /*
    MÉTODOS UPDATE
    */
    
    //Método para comprobar si es posible actualiza un usuario
    public boolean sePuedeActualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombreUsuario = ?, contrasenia = ?, email = ?, telefono = ?, ip = ? WHERE idUsuario = ?";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, usuario.getNombreUsuario());
                pstmt.setString(2, usuario.getContrasenia());
                pstmt.setString(3, usuario.getEmail());
                pstmt.setInt(4, usuario.getTelefono());
                pstmt.setString(5, usuario.getIp());
                pstmt.setInt(6, usuario.getIdUsuario());

                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Usuario actualizado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el usuario con el id proporcionado.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        } finally {
            cerrarConexion();
        }
    }

    /*
    MÉTODOS DELETE
    */

}
