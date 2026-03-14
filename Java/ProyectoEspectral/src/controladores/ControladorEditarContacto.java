
package controladores;



import entidades.Contacto;
import entidades.Asociacion;
import entidades.Sesion;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDUsuarios;
import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDAccionEnvioRecp;
import interfaz_grafica.VistaEditarContacto;
import interfaz_grafica.VistaContactos;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Antonio
 */

public class ControladorEditarContacto {
    private Sesion sesionActual;
    private VistaEditarContacto editarVista;
    private VistaContactos vistaContactos;
    private GestorBDContactos gestorBDContactos;
    private Contacto contactoSeleccionado;
 
   //Constructor de la clase
    public ControladorEditarContacto(Sesion sesionActual, VistaEditarContacto editarVista, VistaContactos vistaContactos) {
        this.sesionActual = sesionActual;
        this.editarVista = editarVista;
        this.vistaContactos = vistaContactos;
        this.gestorBDContactos = new GestorBDContactos();

        //Mostrar los datos del contacto en los campos a cumplimentar y agregar los listeners
        asignarListeners();
        mostrarDatosContacto();
        

       
    }
    
    //Método para configurar los listeners
    private void asignarListeners(){
         
        editarVista.getLblAtras().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarContactos();
            }
        });

       editarVista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarContacto();
            }
        });
    }

    //Método para actualizar el contacto
    private void actualizarContacto() {
        //Obtenemos los datos de los campos de texto
        String nombreContacto = editarVista.getTxtNombre().getText().trim();
        String ip = editarVista.getTxtIP().getText().trim();
        String email = editarVista.getTxtCorreo().getText().trim();
        String telefonoStr = editarVista.getTxtTelefono().getText().trim();

        //Se validan los datos ingresados
        ArrayList<String> posiblesErrores = validacionDatosEditarContacto(telefonoStr, email, ip, nombreContacto);

        //Si hay errores se mostrará un mensaje de error con ellos
        if (!posiblesErrores.isEmpty()) {
            editarVista.mostrarMensaje(String.join("\n", posiblesErrores));
            return;
        }

        //Se actualizan los datos del contacto, mediante la información obtenida y validada y los setters
        contactoSeleccionado.setNombreContacto(nombreContacto);
        contactoSeleccionado.setIP(ip);
        contactoSeleccionado.setEmail(email);
        contactoSeleccionado.setTelefono(telefonoStr);

        //Si la actualización es exitosa, actualizamos el contacto
        boolean actualizacionExistosa = gestorBDContactos.actualizarContacto(contactoSeleccionado);
       
        //
        /*
        Se actualiza el nombre del contacto en las diferentes asociaciones y en las acciones de envío recepción, tiene sentido al realizarse en red local
        */
        GestorBDAsociacion gestorBDAsociacion = new GestorBDAsociacion();
        gestorBDAsociacion.actualizarNombreContactoAsociadoPorIdContacto(contactoSeleccionado.getIdContacto() ,contactoSeleccionado.getNombreContacto());
        GestorBDAccionEnvioRecp gestorBDAccionEnvioRecp = new GestorBDAccionEnvioRecp();
        gestorBDAccionEnvioRecp.actualizarNombreContactoPorId(contactoSeleccionado.getIdContacto(), contactoSeleccionado.getNombreContacto());
        
        //Listamos por pantalla las asociaciones del usuario
         ArrayList<Asociacion> asociacionesPorUsuario = gestorBDAsociacion. listarAsociacionesPorIdUsuario(sesionActual.getIdUsuario());
         if (asociacionesPorUsuario.isEmpty()) {
            System.out.println("No hay asociaciones para el usuario con ID: " + sesionActual.getIdUsuario());
        }else{
        System.out.println("Asociaciones para el usuario con ID: " + sesionActual.getIdUsuario());
  
}
 
        //Si la actualización ha sido existosa, se muestra un mensaje de éxito, en caso contrario de error
        if (actualizacionExistosa) {
            editarVista.mostrarMensaje("Contacto actualizado exitosamente");
            mostrarContactos();
        } else {
            editarVista.mostrarMensaje("Error al actualizar el contacto");
        }
        ControladorContactos controladorContactos = new ControladorContactos(sesionActual, vistaContactos); //Se vuelven a mostrar los contactos
        controladorContactos.mostrarContactos();
    }

    //Validamos los datos
    private ArrayList<String> validacionDatosEditarContacto(
            String telefono, String email, String ip, String nombreContacto) {
        
        ArrayList<String> errores = new ArrayList<>();

        // 1. Validación de campos vacíos
        if (telefono.isEmpty() || email.isEmpty() || ip.isEmpty() || nombreContacto.isEmpty()) {
            errores.add("Rellena todos los campos.");
        }

        // 2. Validación de formato (solo si no hay errores de campos vacíos)
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
        
    GestorBDUsuarios gestorBDUsuarios = new GestorBDUsuarios();

    if (errores.isEmpty()) {
        if ((gestorBDContactos.existeTelefono(telefono) || gestorBDUsuarios.existeUsuarioPorTelefono(telefono)) && !telefono.equals(contactoSeleccionado.getTelefono())) {
            errores.add("Error, el teléfono que has introducido ya está asignado a otro contacto.");
        }

        if ((gestorBDContactos.existeEmail(email) || gestorBDUsuarios.existeUsuarioPorEmail(email)) && !email.equals(contactoSeleccionado.getEmail())) {
            errores.add("Error, el email que has introducido ya está asignado a otro contacto.");
        }


        if ((gestorBDContactos.existeNombreContacto(nombreContacto) || gestorBDUsuarios.existeUsuarioPorNombre(nombreContacto)) && !nombreContacto.equals(contactoSeleccionado.getNombreContacto())) {
            errores.add("Error, ese nombre ya está en uso por otro contacto o usuario.");
        }
    }

        return errores;
    }

    //Mediante expresiones regulares validamos los datos introducidos en los campos de texto
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

    private void mostrarContactos() {
        editarVista.cerrar();
    }
    
    //Método para mostrar los datos del contacto
    private void mostrarDatosContacto(){
        //Obtenemos los datos del contacto desde la vista contactos, de fila seleccionada en la tabla
         String nombreContacto = vistaContactos.getNombreContactoSeleccionado();
        if (nombreContacto != null) { //Si el nombre no es nulo, se obtiene el id del contacto por nombre y luego se obtiene una instancia de Contacto pasando el id por parámetro en la base de datos
            int idContacto = gestorBDContactos.obtenerIdPorNombre(nombreContacto);
            contactoSeleccionado = gestorBDContactos.obtenerContactoPorId(idContacto);
            
        }
        //Setteamos los campos de textos con la información obtenida del contacto
        editarVista.getTxtNombre().setText(nombreContacto);
        editarVista.getTxtTelefono().setText(contactoSeleccionado.getTelefono());
        editarVista.getTxtCorreo().setText(contactoSeleccionado.getEmail());
        editarVista.getTxtIP().setText(contactoSeleccionado.getIp());
    }
}
