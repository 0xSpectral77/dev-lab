
package controladores;

import entidades.AccionCIFDES;
import entidades.AccionEnvioRecp;
import entidades.Asociacion;
import entidades.Sesion;
import entidades.Usuario;
import logica_bd.GestorBDAccionCIFDES;
import logica_bd.GestorBDAccionEnvioRecp;
import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDSesiones;
import logica_bd.GestorBDUsuarios;
import interfaz_grafica.VistaEditarCuenta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Pattern;


/**
 *
 * @author Antonio
 */

public class ControladorEditarCuenta {
    private Sesion sesionActual;
    private VistaEditarCuenta vistaEditarCuenta;
    private GestorBDUsuarios gestorBDUsuarios;
    private Usuario usuarioAEditar;
    
 //Constructor de la clase
 public ControladorEditarCuenta (Sesion sesionActual, VistaEditarCuenta vistaEditarCuenta) {
        this.sesionActual = sesionActual;
        this.vistaEditarCuenta = vistaEditarCuenta;
        this.gestorBDUsuarios = new GestorBDUsuarios();
        this.usuarioAEditar = gestorBDUsuarios.obtenerUsuarioPorId(sesionActual.getIdUsuario());
       
        //Mostrar los datos del contacto en los campos a cumplimentar
        mostrarDatosCuenta(sesionActual.getIdUsuario(), gestorBDUsuarios);
        

        //Asignar el listener al botón de guardar los cambios
        asignarListener();
            
    }
 
 //Metodo para asignar el listener para guardar los cambios
    private void asignarListener(){
        vistaEditarCuenta.getBotonGuardarCambios().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCuenta();
            }
        });
    }
 
    //Método para mostrar los datos de la cuenta, pasando por parámetros una id de usuario y un gestor de base de datos de usuarios
    private void mostrarDatosCuenta(int idUsuario, GestorBDUsuarios gestorBDUsuarios){
        //Obtenemos los datos del usuario y los seteamos en los campos de texto
        vistaEditarCuenta.getTxtUsuario().setText(usuarioAEditar.getNombreUsuario());
        vistaEditarCuenta.getTxtContraseña().setText(decodificarContraseniaBase64(usuarioAEditar.getContrasenia()));
        vistaEditarCuenta.getTxtConfirmarContraseña().setText(decodificarContraseniaBase64(usuarioAEditar.getContrasenia()));
        vistaEditarCuenta.getTxtEmail().setText(usuarioAEditar.getEmail());
        vistaEditarCuenta.getTxtIp().setText(usuarioAEditar.getIp());
        vistaEditarCuenta.getTxtTelefono().setText(String.valueOf(usuarioAEditar.getTelefono()));
    }

    //Método para actualizar los datos de la cuenta
    private void actualizarCuenta() {
        //Obtenemos los datos de lo introducido en los camposd de texto
        String nuevoNombreUsuario = vistaEditarCuenta.getTxtUsuario().getText().trim();
        String contraseñaNueva = new String (vistaEditarCuenta.getTxtContraseña().getPassword()).trim();
        String nuevaConfirmarContraseña = new String(vistaEditarCuenta.getTxtConfirmarContraseña().getPassword()).trim();
        String ip = vistaEditarCuenta.getTxtIp().getText().trim();
        String email = vistaEditarCuenta.getTxtEmail().getText().trim();
        String telefonoStr = vistaEditarCuenta.getTxtTelefono().getText().trim();

        //Validamos los datos ingresados
        ArrayList<String> posiblesErrores = validacionDatosEditarUsuario(nuevoNombreUsuario, contraseñaNueva, nuevaConfirmarContraseña, ip, email, telefonoStr);

        if (!posiblesErrores.isEmpty()) {
            vistaEditarCuenta.mostrarMensaje(String.join("\n", posiblesErrores));
            return;
        }

        //Actualizamos el contacto
        usuarioAEditar.setNombreUsuario(nuevoNombreUsuario);
        usuarioAEditar.setContrasenia(obtenerContraseniaBase64(contraseñaNueva));
        usuarioAEditar.setIp(ip);
        usuarioAEditar.setEmail(email);
        usuarioAEditar.setTelefono(Integer.parseInt(telefonoStr));
       

        boolean actualizacionExistosa = gestorBDUsuarios.sePuedeActualizarUsuario(usuarioAEditar);
        //Si la actualización es existosa, actualizamos las asociaciones,sesiones, las accionesEnvioRecp y las accionesCIFDES en las que ha intervenido el usuario
        if(actualizacionExistosa){
           
           GestorBDAsociacion gestorBDAsociacion = new GestorBDAsociacion();
           ArrayList<Asociacion> asociacionesPorUsuario = gestorBDAsociacion. listarAsociacionesPorIdUsuario(sesionActual.getIdUsuario());
           if (asociacionesPorUsuario.isEmpty()) {
            System.out.println("No hay asociaciones para el usuario, no son necesarias más actualizaciones");
           }else{
             actualizarAsociaciones(asociacionesPorUsuario, nuevoNombreUsuario, gestorBDAsociacion);
           }
           
           GestorBDAccionEnvioRecp gestorBDEnvioRecp = new GestorBDAccionEnvioRecp();
           ArrayList<AccionEnvioRecp> accionesEnvioRecpPorUsuario = gestorBDEnvioRecp.listarAccionesPorIdUsuario(sesionActual.getIdUsuario());
          if (accionesEnvioRecpPorUsuario.isEmpty()) {
            System.out.println("No hay acciones de envío o recepción para el usuario, no son necesarias más actualizaciones");
           }else{
             actualizarAccionesEnvioRecp(accionesEnvioRecpPorUsuario, nuevoNombreUsuario, gestorBDEnvioRecp);
           }
          
           GestorBDAccionCIFDES gestorBDAccionCifDes = new GestorBDAccionCIFDES();
           ArrayList<AccionCIFDES> accionesCifDesPorUsuario = gestorBDAccionCifDes.listarAccionesPorUsuario(sesionActual.getIdUsuario());
           if (accionesCifDesPorUsuario.isEmpty()) {
            System.out.println("No hay acciones de cifrado o descifrado para el usuario, no son necesarias más actualizaciones");
           }else{
             actualizarAccionesCifDes(accionesCifDesPorUsuario, nuevoNombreUsuario, gestorBDAccionCifDes);
           }
           
           GestorBDSesiones gestorBDSesiones = new GestorBDSesiones();
           ArrayList<Sesion> sesionesPorUsuario = gestorBDSesiones.listarSesionesPorIdUsuario(sesionActual.getIdUsuario());
           if(sesionesPorUsuario.isEmpty()){
             System.out.println("No hay sesiones guardadas para el usuario, no son necesarias más actualizaciones");
           }else{
               actualizarSesiones(sesionesPorUsuario, nuevoNombreUsuario, gestorBDSesiones);
           }
           
           vistaEditarCuenta.mostrarMensaje("Usuario actualzado exitosamente");
        }else{
            vistaEditarCuenta.mostrarMensaje("Error, no se ha podido editar los datos del usuario");
        }
}
 
        


    //Método para validar datos
    private ArrayList<String> validacionDatosEditarUsuario(String nombreContacto, String contraseñaNueva, String nuevaConfirmarContraseña, String ip, String email, String telefonoStr) {
        
        ArrayList<String> errores = new ArrayList<>();

        
        if (telefonoStr.isEmpty() || email.isEmpty() || ip.isEmpty() || nombreContacto.isEmpty() || contraseñaNueva.isEmpty() || nuevaConfirmarContraseña.isEmpty())  {
                errores.add("Rellena todos los campos.");
            }

             if (!contraseñaNueva.equals(nuevaConfirmarContraseña)) {
            errores.add("Error, las contraseñas no coinciden.");
        } else if (!esContraseñaSegura(contraseñaNueva)) {
            errores.add("Error, la contraseña no es segura debe contener al menos: \n"
                    + "8 carácteres alfanuméricos \n"
                    + "una letra mayúscula \n"
                    + "una letra minúscula \n"
                    + "un símbolo especial (`*, ?, ;, :, <, >, (, ), [, ], {, }, |, \\, /, @, #, $, %, ^, &, +, =, !`)");
        }

        //Validación del formato de los datos introducidos
        if (errores.isEmpty()) {
            if (!esTelefonoValido(telefonoStr)) {
                errores.add("Error, el teléfono que has introducido no tiene un formato correcto.");
            }
            if (!esEmailValido(email)) {
                errores.add("Error, el email que has introducido no tiene un formato correcto.");
            }
            if (!esIpValida(ip)) {
                errores.add("Error, la IP que has introducido no tiene un formato correcto.");
            }
        }

        //Validación de la existencia de los datos introducidos, para evitar duplicados
    GestorBDContactos gestorBDContactos = new GestorBDContactos();
        if (errores.isEmpty()) {
            if ((gestorBDContactos.existeTelefono(telefonoStr) || gestorBDUsuarios.existeUsuarioPorTelefono(telefonoStr))
            && !telefonoStr.equals(String.valueOf(usuarioAEditar.getTelefono()))) {
            errores.add("Error, el teléfono que has introducido ya está asignado a otro contacto o usuario.");
            }

            if ((gestorBDContactos.existeEmail(email) || gestorBDUsuarios.existeUsuarioPorEmail(email))
                && !email.equals(usuarioAEditar.getEmail())) {
                errores.add("Error, el email que has introducido ya está asignado a otro contacto o usuario.");
            }


            if ((gestorBDContactos.existeNombreContacto(nombreContacto) || gestorBDUsuarios.existeUsuarioPorNombre(nombreContacto))
                && !nombreContacto.equals(usuarioAEditar.getNombreUsuario())) {
                errores.add("Error, ese nombre ya está en uso por otro contacto o usuario.");
              }
        }

      return errores;

    }
    
    
    //Validamos con expresiones regulares los diferentes datos proporcionados por el usuario
    private boolean esIpValida(String ip) {
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.compile(regex).matcher(ip).matches();
    }

    private boolean esEmailValido(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private boolean esTelefonoValido(String telefono) {
        String regex = "^\\d{3}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}$";
        return Pattern.compile(regex).matcher(telefono).matches();
    }
    
    private boolean esContraseñaSegura(String contraseña) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[*,?;:<>()\\[\\]{}|\\\\/@#$%^&+=!]).{8,}$";
        return Pattern.compile(regex).matcher(contraseña).matches();
    }
    
    //Se actualiza el nombre de usuario en todas las acciones en las que se encuentra involucrado
    private void actualizarAsociaciones(ArrayList<Asociacion> asociacionesPorUsuario, String nuevoNombreUsuario, GestorBDAsociacion gestorBDAsociacion){
        for(Asociacion asociacion : asociacionesPorUsuario) {
               asociacion.setNombreUsuarioAsociado(nuevoNombreUsuario);
           } 
        gestorBDAsociacion.actualizarListaAsociaciones(asociacionesPorUsuario);
        
    }
    //Métodos para actualizar cada una de las acciones
    private void actualizarAccionesEnvioRecp(ArrayList<AccionEnvioRecp> accionesEnvioRecpPorUsuario, String nuevoNombreUsuario, GestorBDAccionEnvioRecp gestorBDAccionEnvioRecp){
        for(AccionEnvioRecp accion : accionesEnvioRecpPorUsuario) {
               accion.setNombreUsuarioEnvioRecp(nuevoNombreUsuario);
           } 
        gestorBDAccionEnvioRecp.actualizarListaAccionesEnvioRecp(accionesEnvioRecpPorUsuario);
        
    }
    
    private void actualizarAccionesCifDes(ArrayList<AccionCIFDES> accionesCifDesPorUsuario, String nuevoNombreUsuario, GestorBDAccionCIFDES gestorBDAccionCifDes){
        for(AccionCIFDES accion : accionesCifDesPorUsuario) {
               accion.setNombreUsuarioAccion(nuevoNombreUsuario);
           } 
        gestorBDAccionCifDes.actualizarListaAccionesCIFDES(accionesCifDesPorUsuario);
        
    }

    private void actualizarSesiones(ArrayList<Sesion> sesionesPorUsuario, String nuevoNombreUsuario, GestorBDSesiones gestorBDSesiones){
        for(Sesion sesion : sesionesPorUsuario) {
               sesion.setNombreUsuario(nuevoNombreUsuario);
           } 
        gestorBDSesiones.actualizarListaSesionesPorUsuario(sesionesPorUsuario);
    }
    
    //Metodo para obtener la contraseña en Base64
    private String obtenerContraseniaBase64(String contrasenia) {
       
       return Base64.getEncoder().encodeToString(contrasenia.getBytes());
    }
    
    //Método para decodificar la contraseña desde Base64
    private String decodificarContraseniaBase64(String contraseniaCodificada) {
    byte[] bytesDecodificados = Base64.getDecoder().decode(contraseniaCodificada);
    return new String(bytesDecodificados);
    }
    
}
   

