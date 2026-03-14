
package logica_bd;

import java.io.File;
import java.sql.*;

/**
 *
 * @author Antonio
 */

public class GestorBDGeneral {

    //Instancia de conection con acceso protected ya que de esta clase heredarán el resto de gestores de base de datos
    protected Connection conexion;

    //Constante estática utilizada para localizar la ruta donde se localiza la carpeta documentos del usuario del pc
    private static final String RUTA_DOCUMENTOS = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Espectral";
    //Constante estática utilizada para nombrar al archivo que contendrá la base de datos
    private static final String NOMBRE_BASE_DE_DATOS = "base_de_datos.db";
    //Constante estática que guarda la ruta donde se alojará el archivo de base de datos
    private static final String RUTA_BASE_DE_DATOS = RUTA_DOCUMENTOS + File.separator + NOMBRE_BASE_DE_DATOS;

    //Método para crear la conexión con la base de datos
    public Connection crearConexion(String rutaBD) throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBD);
        }
        return conexion;
    }

    //Método booleano que indica si la base de datos existe
    public boolean existeBaseDeDatos(String rutaBD) {
        File baseDeDatos = new File(rutaBD);
        return baseDeDatos.exists(); //Se comprueba que el archivo de la base de datos ya existe
    }

    //Método que devuelve un string con la ruta de la base de datos definida previamente
    public String obtenerRutaBaseDeDatos() {
        return RUTA_BASE_DE_DATOS;
    }

    //Método utilizado para cerrar la conexión con la base de datos
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    //Método para crear la base de datos, proporcionando una ruta por parámetro
    public void crearBaseDeDatos(String rutaBD) {
        File baseDeDatos = new File(rutaBD);

        //Verificación para comprobar si la base de datos existe
        if (!baseDeDatos.exists()) {
            //Se crea el directorio que contendrá el archivo de la base de datos
            File directorio = new File(baseDeDatos.getParent());
            if (!directorio.exists()) {
                directorio.mkdirs();//Si ese directorio no existe se creará
            }
            
            //Con un bloque try-cath se verifica que se pueda establecer la conexión con la base de datos
            try {
                
                conexion = crearConexion(rutaBD);
                System.out.println("Base de datos creada correctamente en: " + rutaBD);
            } catch (SQLException e) {
                System.out.println("Error al crear la base de datos: " + e.getMessage()); //Si no se puede establecer la conexión se gestiona la excepción
            }
        } else {
            //Si la base de datos ya existe se muestra un mensaje por consola
            System.out.println("La base de datos ya existe en la ruta: " + rutaBD);
        }
    }
    
    //Método para obtener la ruta "Documentos" del usuario de un pc
    public String obtenerRutaDocumentos() {
        return RUTA_DOCUMENTOS;
    }

}