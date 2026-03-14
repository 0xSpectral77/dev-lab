
package controladores;

import interfaz_grafica.VistaAgregarContacto;
import interfaz_grafica.VistaContactos;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDUsuarios;
import entidades.Contacto;
import entidades.Asociacion;
import entidades.Sesion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


/**
 *
 * @author Antonio
 */


public class ControladorAgregarContacto {
    private Sesion sesionActual;
    private VistaAgregarContacto agregarVista;
    private VistaContactos vistaContactos;
    private GestorBDContactos gestorBDContactos;
    private GestorBDAsociacion gestorBDAsociacion;
    
    //Constructor de la clase
    public ControladorAgregarContacto(Sesion sesionActual,VistaAgregarContacto agregarVista, VistaContactos vistaContactos) {
        this.sesionActual = sesionActual;
        this.agregarVista = agregarVista;
        this.vistaContactos = vistaContactos;
        this.gestorBDContactos = new GestorBDContactos();
        this.gestorBDAsociacion = new GestorBDAsociacion();
      
       //Agregamos los listener a los botones        
       asignarListeners();
        
    }
    
    //Método para agregar listeners a los botones
    private void asignarListeners(){
          agregarVista.getLblAtras().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               cerrarVentaAgregar();
            }
        });
        
        agregarVista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarContacto();
                
            }
        });
        
    }

    //Método para registrar un contacto
    private void registrarContacto() {
    /*
    Obtenemos los nombres del contacto desde los campos de texto y los guardamos en variables 
     */
    String nombreContacto = agregarVista.getNombre().getText().trim();
    formatearTexto(nombreContacto);
    String ip = agregarVista.getIP().getText().trim();
    String email = agregarVista.getCorreo().getText().trim();
    
    String telefonoStr = agregarVista.getTelefono().getText().trim();
    
    //Creamos un ArrayList con posibles errores
    ArrayList<String> posiblesErrores = validacionDatosAgregarContacto(telefonoStr, email,ip, nombreContacto, gestorBDContactos);
        //Si la lista de errores no se encuentra vacía, muestra los errores mediante una ventana
        if (!posiblesErrores.isEmpty()) {
            agregarVista.mostrarMensaje(String.join("\n", posiblesErrores));
            return;
        }

    //Creamos una instacia de contacto y comprobamos si existe una con los datos proporcionados
        Contacto nuevoContacto = new Contacto(0, nombreContacto, ip, email, telefonoStr);
        if (gestorBDContactos.existeNombreContacto(nombreContacto) && !gestorBDAsociacion.existeAsociacionPorUsuarioYContacto(sesionActual.getNombreUsuario(), nombreContacto)){
            //Si el contacto ya existe en la base de datos únicamente deberemos crear una instancia de asociación 
            Asociacion nuevaAsociacion = new Asociacion (0, sesionActual.getIdUsuario(), sesionActual.getNombreUsuario(), gestorBDContactos.obtenerIdPorCorreo(email),nombreContacto);
            gestorBDAsociacion.insertarAsociacion(nuevaAsociacion);

        //Si el contacto no existe se sigue el mismo proceso, pero si se inserta en la base de datos
        }else if (!gestorBDContactos.existeNombreContacto(nombreContacto)) {
            gestorBDContactos.insertarContacto(nuevoContacto);
            Asociacion nuevaAsociacion = new Asociacion (0, sesionActual.getIdUsuario(), sesionActual.getNombreUsuario(), gestorBDContactos.obtenerIdPorCorreo(email),nombreContacto);
            gestorBDAsociacion.insertarAsociacion(nuevaAsociacion);

        }
     agregarVista.mostrarMensaje("Contacto registrado exitosamente.");
     //Se llama al controlador de contactos para mostrar los contactos y actualizar la lista de los mismos, se cierra la ventana para 
     ControladorContactos controladorContactos = new ControladorContactos(sesionActual, vistaContactos);
     controladorContactos.mostrarContactos();
     cerrarVentaAgregar();
    
    }

    
    //función para validar los campos
    private ArrayList<String> validacionDatosAgregarContacto(
        String telefono, String email, String ip, String nombreContacto,
        GestorBDContactos gestorBDContactos) {

        //creamos un ArrayList de errores
    ArrayList<String> errores = new ArrayList<>();
    

    //Primero validamos si hay algún campo que se encuentre vacío
    if (telefono.isEmpty() || email.isEmpty() || ip.isEmpty() || nombreContacto.isEmpty()) {
        errores.add("Rellena todos los campos.");
    }

    //Validamos el formato de cada uno de los datos gracias a las funciones que contienen expresiones regulares, si hay un error lo añadimos al array list
    if (errores.isEmpty()) {
        if (!esTelefonoValido(telefono)) {
            errores.add("Error, el teléfono que has introducido no tiene un formato correcto.");
        }
        if (!esEmailValido(email)) {
            errores.add("Error, el email que has introducido no tiene un formato correcto.");
        }
        if (!esIpValida(ip)) {
            errores.add("Error, la IP que has introducido no tiene un formato correcto.");
        }
    }

    //Creamos instancia del gestor de base de datos para comparar los datos introducidos con los usuarios y contactos existentes
    GestorBDUsuarios gestorBDUsuarios = new GestorBDUsuarios();
    
    if (errores.isEmpty() && !gestorBDAsociacion.existeAsociacionPorUsuarioYContacto(sesionActual.getNombreUsuario(), nombreContacto)) {
        if (gestorBDUsuarios.existeUsuarioPorTelefono(telefono)) {
            errores.add("Error, el teléfono que has introducido ya pertenece a un usuario o a un contacto.");
        }
        if (gestorBDUsuarios.existeUsuarioPorEmail(email)) {
            errores.add("Error, el email que has introducido ya pertenece a un usuario o a un contacto.");
        }
        
        if (gestorBDUsuarios.existeUsuarioPorNombre(nombreContacto) ) {
            errores.add("Error, este nombre ya se encuentra en uso.");
        }
     
    }else if (errores.isEmpty()){
        if (gestorBDContactos.existeTelefono(telefono)) {
            errores.add("Error, el teléfono que has introducido ya pertenece a un usuario o a un contacto.");
        }
        if (gestorBDContactos.existeEmail(email) || gestorBDUsuarios.existeUsuarioPorEmail(email)) {
            errores.add("Error, el email que has introducido ya pertenece a un usuario o a un contacto.");
        }
        
        if (gestorBDContactos.existeNombreContacto(nombreContacto) || gestorBDUsuarios.existeUsuarioPorNombre(nombreContacto) ) {
            errores.add("Error, este nombre ya se encuentra en uso.");
        }
    }
    return errores; //devuelve los errores
  
 }

    //Función con expresión regular para comprobar si una IP introducida tiene un formato IPv4 válido por ejemplo 255.255.255.255
    private boolean esIpValida(String ip) {
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.compile(regex).matcher(ip).matches();
    }

   //Función con expresión regular para comprobar si un email tiene el formato correcto ejemplo@gmail.com
   private boolean esEmailValido(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regex).matcher(email).matches();
    }
    
    //Función con expresión regular para comprobar si un teléfono introducido tiene el formato correcto   
    private boolean esTelefonoValido(String telefono) {
        String regex = "^\\d{3}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3}$";
        return Pattern.compile(regex).matcher(telefono).matches();
    }
    
    //Función para formatear un texto con la primera letra en mayúscula y el resto en minúscula
    private void formatearTexto(String texto){
        texto = texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
       
    }
    
    //Función para cerrar la ventana de agregar contactos
    private void cerrarVentaAgregar() {
        agregarVista.cerrar();
    }
    
    //Función para obtener la fecha y hora 
    private Date obtenerFechaHoraActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}


