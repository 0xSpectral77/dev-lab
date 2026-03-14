
package controladores;

import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDSesiones;
import logica_bd.GestorBDGeneral;
import logica_bd.GestorBDAccionEnvioRecp;
import logica_bd.GestorBDArchivos;
import logica_bd.GestorBDUsuarios;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDAccionCIFDES;


/**
 *
 * @author Antonio
 */

public class ControladorBD {
    private GestorBDGeneral gestorGeneral;
    private GestorBDAccionCIFDES gestorBDAccionCIFDES;
    private GestorBDAccionEnvioRecp gestorBDAccionEnvioRecp;
    private GestorBDArchivos gestorBDArchivos;
    private GestorBDAsociacion gestorBDAsociacion;
    private GestorBDContactos gestorBDContactos;
    private GestorBDSesiones gestorBDSesiones;
    private GestorBDUsuarios gestorBDUsuarios;
    
    //Constructor de la clase
    public ControladorBD() {
        gestorGeneral = new GestorBDGeneral();
        /*
        En el constructor verificamos que la base de datos existe, de los contrario la creamos, así como las tablas
        */
        verificarYCrearBaseDeDatos();
        crearTablaUsuariosSiNoExiste();
        crearTablaSesionesSiNoExiste();
        crearTablaContactosSiNoExiste();
        crearTablaAsociacionesSiNoExiste();
        crearTablaAccionCIFDESSiNoExiste();
        crearTablaAccionEnvioRecpSiNoExiste();
        crearTablaArchivosSiNoExiste();
              
    }

    //Método para verificar si existe la base de datos
    public void verificarYCrearBaseDeDatos() {
        //Se obtiene la ruta donde se encuentra la base de datos
        String rutaBaseDeDatos = gestorGeneral.obtenerRutaBaseDeDatos();

        //Se verifica si ya existe
        if (!gestorGeneral.existeBaseDeDatos(rutaBaseDeDatos)) {
            //Si no, se crea
            gestorGeneral.crearBaseDeDatos(rutaBaseDeDatos);
        } else {
           
            System.out.println("La base de datos ya existe en la ruta: " + rutaBaseDeDatos);
        }
    }
    
    //Creación de las diferentes tablas
    public void crearTablaUsuariosSiNoExiste() {
         gestorBDUsuarios = new GestorBDUsuarios();
        if (!gestorBDUsuarios.verificarExistenciaTablaUsuarios()) {
            gestorBDUsuarios.creacionTablaUsuarios();
        } else {
            System.out.println("La tabla 'usuarios' ya existe.");
        }
    }
    
    public void crearTablaSesionesSiNoExiste() {
        gestorBDSesiones = new GestorBDSesiones();
        if (!gestorBDSesiones.verificarExistenciaTablaSesiones()) {
            gestorBDSesiones.creacionTablaSesiones();
        } else {
            System.out.println("La tabla 'sesiones' ya existe.");
        }
    }
    
    public void crearTablaContactosSiNoExiste() {
        gestorBDContactos = new GestorBDContactos();
        if(!gestorBDContactos.verificarExistenciaTablaContactos()){
        gestorBDContactos.creacionTablaContactos(); 
        } else {
            System.out.println("La tabla 'Contactos' ya existe.");
        }
    }
    
    public void crearTablaAsociacionesSiNoExiste() {
        gestorBDAsociacion = new GestorBDAsociacion();
        if(!gestorBDAsociacion.verificarExistenciaTablaAsociacion()){
        gestorBDAsociacion.creacionTablaAsociacion();
        } else {
            System.out.println("La tabla 'Asociaciones' ya existe.");
        }
    }
    
    public void crearTablaAccionCIFDESSiNoExiste() {
        gestorBDAccionCIFDES = new GestorBDAccionCIFDES();
        if(!gestorBDAccionCIFDES.verificarExistenciaTablaAccionCIFDES()){
        gestorBDAccionCIFDES.creacionTablaAccionCIFDES();
        } else {
            System.out.println("La tabla 'Asociaciones' ya existe.");
        }
    }
    
    public void crearTablaAccionEnvioRecpSiNoExiste() {
        gestorBDAccionEnvioRecp = new GestorBDAccionEnvioRecp();
        if(!gestorBDAccionEnvioRecp.verificarExistenciaTablaAccionEnvioRecp()){
        gestorBDAccionEnvioRecp.creacionTablaAccionEnvioRecp();
        } else {
            System.out.println("La tabla 'EnvioRecp' ya existe.");
        }
    }
    
    public void crearTablaArchivosSiNoExiste() {
        gestorBDArchivos = new GestorBDArchivos();
        if(!gestorBDArchivos.verificarExistenciaTablaArchivos()){
        gestorBDArchivos.creacionTablaArchivos();
        } 
        else {
            System.out.println("La tabla 'Archivos' ya existe.");
        }
    }
}
