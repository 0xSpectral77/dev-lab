

package logica_bd;

import entidades.Archivo;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 *
 * @author Antonio
 */

public class GestorBDArchivos extends GestorBDGeneral {

    ////Constructor de la clase que crea una conexión a la base de datos si esta es nula o se encuentra cerrada
    public GestorBDArchivos() {
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la conexión: " + e.getMessage());
        }
    }

    //Método booleano para verificar la existencia de la tabla archivos
    public boolean verificarExistenciaTablaArchivos() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='archivos';";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la tabla 'archivos': " + e.getMessage());
        } finally {
            cerrarConexion();
        }
        return false;
    }

    //Método para la creación de la tabla archivos
    public void creacionTablaArchivos() {
        String sql = "CREATE TABLE IF NOT EXISTS archivos (" +
                "idArchivo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "rutaArchivo TEXT NOT NULL, " +
                "tamaño INTEGER NOT NULL, " +
                "nombreArchivo TEXT NOT NULL, " +
                "extensión TEXT NOT NULL, " +
                "hashArchivoBase TEXT NOT NULL, " +
                "hashArchivoCifrado TEXT NOT NULL, " +
                "claveBase64 TEXT NOT NULL, " +
                "estaCifrado BOOLEAN NOT NULL);";
        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'archivos' creada correctamente.");
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
    //Método encargado de insertar un archivo a la tabla archivos, recibe por parámetro una instancia de archivo
    public void insertarArchivo(Archivo archivo) {
        String sql = "INSERT INTO archivos (rutaArchivo, tamaño, nombreArchivo, extensión, hashArchivoBase, hashArchivoCifrado, claveBase64, estaCifrado) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, archivo.getRutaArchivo());
                pstmt.setInt(2, archivo.getTamanio());
                pstmt.setString(3, archivo.getNombreArchivo());
                pstmt.setString(4, archivo.getExt());
                pstmt.setString(5, archivo.getHashArchivoBase());
                pstmt.setString(6, archivo.getHashArchivoCifrado());
                pstmt.setString(7, archivo.getClaveBase64());
                pstmt.setBoolean(8, archivo.getEstaCifrado());
                pstmt.executeUpdate();
                System.out.println("Archivo insertado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar archivo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    /*
    MÉTODOS READ
    */
    
    //Método para listar todos los archivos
    public ArrayList<Archivo> listarArchivos() {
        ArrayList<Archivo> listaArchivos = new ArrayList<>();
        String sql = "SELECT * FROM archivos";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Archivo archivo = new Archivo(
                        rs.getInt("idArchivo"),
                        rs.getString("rutaArchivo"),
                        rs.getInt("tamaño"),
                        rs.getString("nombreArchivo"),
                        rs.getString("extensión"),
                        rs.getString("hashArchivoBase"),
                        rs.getString("hashArchivoCifrado"),
                        rs.getString("claveBase64"),
                        rs.getBoolean("estaCifrado")
                    );
                    listaArchivos.add(archivo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los archivos: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return listaArchivos;
    }

    //Método para verificar si un archivo se encuentra cifrado o no, el archivo se le pasará al método por parámetro
    public boolean verificarEstadoCifrado(File archivo) {
        String sql = "SELECT estaCifrado FROM archivos WHERE rutaArchivo = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, archivo.getAbsolutePath());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("estaCifrado");
                    }
                }
           
 }
        } catch (SQLException e) {
            System.out.println("Error al verificar el estado de cifrado del archivo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }
    
    //Método para que devuelve una instancia de archivo pasándole la ruta absoluta del mismo por parámetro
    public Archivo obtenerArchivoPorRutaAbsoluta(String rutaArchivo) {
        String sql = "SELECT * FROM archivos WHERE rutaArchivo = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, rutaArchivo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Archivo(
                                rs.getInt("idArchivo"),
                                rs.getString("rutaArchivo"),
                                rs.getInt("tamaño"),
                                rs.getString("nombreArchivo"),
                                rs.getString("extensión"),
                                rs.getString("hashArchivoBase"),
                                rs.getString("hashArchivoCifrado"),
                                rs.getString("claveBase64"),
                                rs.getBoolean("estaCifrado")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener archivo por ruta absoluta: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return null;
    }
    
    //Método para verificar el estado cifrado de un archivo, pero esta vez pasándole por parámetro la ruta absoluta
    public boolean archivoEstaCifrado(String rutaArchivo) {
        String sql = "SELECT estaCifrado FROM archivos WHERE rutaArchivo = ?";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, rutaArchivo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("estaCifrado");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el estado del archivo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }
    
    //Método para verificar si un archivo se encuentra en la base de datos
    public boolean archivoEstaEnBaseDeDatos(String rutaArchivo) {
        String sql = "SELECT COUNT(*) FROM archivos WHERE rutaArchivo = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, rutaArchivo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si el archivo está en la base de datos: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }
    /*
    MÉTODOS UPDATE
    */
    //Método para actualizar el estado cifrado de un archivo que se le pasa al método por parámetro
    public boolean actualizarEstadoCifrado(Archivo archivo) {
        String sql = "UPDATE archivos SET estaCifrado = ? WHERE rutaArchivo = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setBoolean(1, archivo.getEstaCifrado());
                pstmt.setString(2, archivo.getRutaArchivo());
                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el estado de cifrado del archivo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }
    
    //Método para cambiar el estado de cifrado de un archivo, proporcionándole la ruta absoluta del mismo por parámetro
    public boolean desactivarCifradoPorRuta(String rutaArchivo) {
        String sql = "UPDATE archivos SET estaCifrado = FALSE WHERE rutaArchivo = ? AND estaCifrado = TRUE;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, rutaArchivo);
                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al desactivar el cifrado del archivo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }
    
    //Método para actualizar la clave secreta de un archivo, al cifrarlo de nuevo, proporcionando por parámetro la ruta del archivo y la nueva clave secreta en formato Base64
    public boolean actualizarClaveSecretaPorRuta(String rutaArchivo, String nuevaClaveBase64) {
        String sql = "UPDATE archivos SET claveBase64 = ? WHERE rutaArchivo = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nuevaClaveBase64);
                pstmt.setString(2, rutaArchivo);
                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la clave secreta del archivo: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }
    
    //Método par actulizar los datos de un archivo en la base de datos
    public boolean actualizarArchivo(Archivo nuevoArchivo) {
        String sql = "UPDATE archivos SET " +
                "tamaño = ?, " +
                "nombreArchivo = ?, " +
                "extensión = ?, " +
                "hashArchivoBase = ?, " +
                "hashArchivoCifrado = ?, " +
                "claveBase64 = ?, " +
                "estaCifrado = ? " +
                "WHERE rutaArchivo = ?;";

        try {
            if (conexion == null || conexion.isClosed()) {
                crearConexion(obtenerRutaBaseDeDatos());
            }

            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, nuevoArchivo.getTamanio());
                pstmt.setString(2, nuevoArchivo.getNombreArchivo());
                pstmt.setString(3, nuevoArchivo.getExt());
                pstmt.setString(4, nuevoArchivo.getHashArchivoBase());
                pstmt.setString(5, nuevoArchivo.getHashArchivoCifrado());
                pstmt.setString(6, nuevoArchivo.getClaveBase64());
                pstmt.setBoolean(7, nuevoArchivo.getEstaCifrado());
                pstmt.setString(8, nuevoArchivo.getRutaArchivo());

                int filasAfectadas = pstmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el archivo por ruta: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return false;
    }

    /*
    MÉTODOS DELETE
    */
   
}










