

package Main;

import controladores.ControladorBD;
import controladores.ControladorLogin;
import entidades.AccionCIFDES;
import entidades.AccionEnvioRecp;
import entidades.Archivo;
import entidades.Asociacion;
import entidades.Contacto;
import entidades.Sesion;
import entidades.Usuario;
import logica_bd.GestorBDAccionEnvioRecp;
import logica_bd.GestorBDAccionCIFDES;
import logica_bd.GestorBDArchivos;
import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDSesiones;
import logica_bd.GestorBDUsuarios;
import java.util.ArrayList;

/**
 *
 * @author Antonio
 */

public class Main {
    //Método main de la aplicación
    public static void main(String[] args) {
        ControladorBD controladorBD = new ControladorBD(); //Se crea una instacia del controlador de BD
        ControladorLogin controladorLogin = new ControladorLogin();//Se crea una instacia del controlador de login, que cargará la ventana principal

        //Mediante consola se pueden realizar comprobaciones del contenido de las distintas tablas de la base de datos
        mostrarUsuarios();
        mostrarContactos();
        mostrarAsociaciones();
        mostrarArchivos();
        mostrarAccionesCIFDES();
        mostrarAccionesEnvioRecp();
        mostrarSesiones();
    }

    /*
     * Métodos estáticos creados para realizar comprobaciones de la base de datos cuando se inicia la aplicación
     */
    
    //Método para mostrar todas las AccionesCIFDES que existen en la base de datos
    public static void mostrarAccionesCIFDES() {
        GestorBDAccionCIFDES gestorBDAccionCIFDES = new GestorBDAccionCIFDES();
        ArrayList<AccionCIFDES> acciones = gestorBDAccionCIFDES.listarAccionesCIFDES();

        if (acciones.isEmpty()) {
            System.out.println("No hay acciones de cifrado/descifrado registradas en la base de datos.");
        } else {
            System.err.println("Lista de acciones de Cifrado/Descifrado:"); // Simplemente es para que la línea resalte en la consola, no porque haya que señalar un error
            for (AccionCIFDES accion : acciones) {
                System.out.println("ID Acción: " + accion.getIdAccionCIFDES() +
                        ", Usuario: " + accion.getNombreUsuarioAccion() +
                        ", ID Archivo: " + accion.getIdArchivo() +
                        ", Archivo: " + accion.getNombreArchivoAccion() +
                        ", Tipo de Acción: " + accion.getTipoAccion() +
                        ", Fecha: " + accion.getFecha());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    //Método para mostrar todas las AccionesEnvioRecp que existen en la base de datos
    public static void mostrarAccionesEnvioRecp() {
        GestorBDAccionEnvioRecp gestorBDAccionEnvioRecp = new GestorBDAccionEnvioRecp();
        ArrayList<AccionEnvioRecp> acciones = gestorBDAccionEnvioRecp.listarAccionesEnvioRecp();

        if (acciones.isEmpty()) {
            System.out.println("No hay acciones de envío/recepción registradas en la base de datos.");
        } else {
            System.err.println("Lista de acciones de Envío/Recepción:");
            for (AccionEnvioRecp accion : acciones) {
                System.out.println("ID Acción: " + accion.getIdAccionEnvioRecp() +
                        ", Usuario: " + accion.getNombreUsuarioEnvioRecp() +
                        ", Contacto: " + accion.getNombreContactoEnvioRecp() +
                        ", ID Archivo: " + accion.getIdArchivoEnvioRecp() +
                        ", Archivo: " + accion.getNombreArchivoEnvioRecp() +
                        ", Tipo de Acción: " + accion.getTipoAccion() +
                        ", Fecha: " + accion.getFecha());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    //Método para mostrar todos los archivos que existen en la base de datos
    public static void mostrarArchivos() {
        GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
        ArrayList<Archivo> archivos = gestorBDArchivos.listarArchivos();

        if (archivos.isEmpty()) {
            System.out.println("No hay archivos registrados en la base de datos.");
        } else {
            System.err.println("Lista de archivos registrados:");
            for (Archivo archivo : archivos) {
                System.out.println("ID Archivo: " + archivo.getIdArchivo());
                System.out.println("Ruta: " + archivo.getRutaArchivo());
                System.out.println("Nombre: " + archivo.getNombreArchivo());
                System.out.println("Extensión: " + archivo.getExt());
                System.out.println("Tamaño: " + archivo.getTamanio() + " KB");
                System.out.println("Hash Base: " + archivo.getHashArchivoBase());
                System.out.println("Hash Cifrado: " + archivo.getHashArchivoCifrado());
                System.out.println("Clave Base64: " + archivo.getClaveBase64());

                if (archivo.getEstaCifrado()) {
                    System.out.println("Cifrado: Sí");
                } else {
                    System.out.println("Cifrado: No");
                }

                System.out.println("--------------------------------------------------");
            }
        }
    }

    //Método para mostrar todas las Asociaciones que existen en la base de datos
    public static void mostrarAsociaciones() {
        GestorBDAsociacion gestorBDAsociacion = new GestorBDAsociacion();
        ArrayList<Asociacion> asociaciones = gestorBDAsociacion.listarAsociaciones();

        if (asociaciones.isEmpty()) {
            System.out.println("No hay asociaciones registradas en la base de datos.");
        } else {
            System.err.println("Lista de asociaciones registradas:");
            for (Asociacion asociacion : asociaciones) {
                System.out.println("ID Asociación: " + asociacion.getIdAsociacion());
                System.out.println("ID Usuario: " + asociacion.getIdUsuarioAsociado());
                System.out.println("Nombre de Usuario Asociado: " + asociacion.getNombreUsuarioAsociado());
                System.out.println("ID Contacto: " + asociacion.getIdContactoAsociado());
                System.out.println("Nombre de Contacto Asociado: " + asociacion.getNombreContactoAsociado());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    //Método para mostrar todos los Contactos que existen en la base de datos
    public static void mostrarContactos() {
        GestorBDContactos gestorBDContactos = new GestorBDContactos();
        ArrayList<Contacto> contactos = gestorBDContactos.listarContactos();

        if (contactos.isEmpty()) {
            System.out.println("No hay contactos registrados en la base de datos.");
        } else {
            System.err.println("Lista de contactos registrados:");
            for (Contacto contacto : contactos) {
                System.out.println("ID: " + contacto.getIdContacto());
                System.out.println("Nombre: " + contacto.getNombreContacto());
                System.out.println("IP: " + contacto.getIp());
                System.out.println("Email: " + contacto.getEmail());
                System.out.println("Teléfono: " + contacto.getTelefono());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    //Método para mostrar todas las Sesiones que existen en la base de datos
    public static void mostrarSesiones() {
        GestorBDSesiones gestorBDSesiones = new GestorBDSesiones();
        ArrayList<Sesion> sesiones = gestorBDSesiones.listarSesiones();

        if (sesiones.isEmpty()) {
            System.out.println("No hay sesiones registradas en la base de datos.");
        } else {
            System.err.println("Lista de sesiones registradas:");
            for (Sesion sesion : sesiones) {
                System.out.println("ID Sesión: " + sesion.getIdSesion() +
                        ", ID Usuario: " + sesion.getIdUsuario() +
                        ", Nombre Usuario: " + sesion.getNombreUsuario() +
                        ", Fecha Inicio: " + sesion.getFechaInicio() +
                        ", Fecha Fin: " + sesion.getFechaFin());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    //Método para mostrar todos los Usuarios que existen en la base de datos
    public static void mostrarUsuarios() {
        GestorBDUsuarios gestorBDUsuarios = new GestorBDUsuarios();
        ArrayList<Usuario> usuarios = gestorBDUsuarios.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en la base de datos.");
        } else {
            System.err.println("Lista de usuarios registrados:");
            for (Usuario usuario : usuarios) {
                System.out.println("ID: " + usuario.getIdUsuario() +
                        ", Nombre: " + usuario.getNombreUsuario() +
                        ", Contraseña (Base64): " + usuario.getContrasenia() +
                        ", Email: " + usuario.getEmail() +
                        ", Teléfono: " + usuario.getTelefono() +
                        ", IP: " + usuario.getIp());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}
