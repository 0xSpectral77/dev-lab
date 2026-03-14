

package controladores;

import interfaz_grafica.VistaLogin;
import logica_bd.GestorBDUsuarios;
import logica_bd.GestorBDSesiones;
import entidades.Usuario;
import entidades.Sesion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaz_grafica.VistaRegistro;
import interfaz_grafica.VistaPrincipal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Antonio
 */

public class ControladorLogin {
    private VistaLogin loginVista;
    private GestorBDUsuarios gestorBDUsuarios;
    private GestorBDSesiones gestorBDSesiones;
    private VistaRegistro registroVista;
    private VistaPrincipal vistaPrincipal;
    private Sesion sesionActual;
 

    //Constructor de la clase
    public ControladorLogin() {
        this.loginVista = new VistaLogin();
        this.gestorBDUsuarios = new GestorBDUsuarios();
        this.gestorBDSesiones = new GestorBDSesiones();
        
        mostrarVistaLogin();

        asignarListeners();
       
    }

    //Método para asignar los listeners
    private void asignarListeners(){
         this.loginVista.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        
        });
        
        
        this.loginVista.getLblCrearCuenta().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarRegistro();
            }
        });
    }
    //Método para mostrar la vista
    public void mostrarVistaLogin() {
        loginVista.mostrar();
    }
    //Método para cerrar la vista
    private void cerrarVistaLogin() {
        loginVista.cerrar();
    }

   
    //Método para poder autenticar al usuario
    private void autenticarUsuario() {
    //Obtenemos los datos del usuario de los campos de texto de la vista
    String usuario = loginVista.getUsuario().getText().trim();
    String contrasenia = obtenerContraseniaBase64(new String(loginVista.getContraseña().getPassword()).trim());


    //Comprobar que los campos no estén vacíos
    if (usuario == null || usuario.isEmpty() || contrasenia == null || contrasenia.isEmpty()) {
        loginVista.mostrarMensaje("Por favor ingrese usuario y contraseña.");
        return;
    }

    //Se obtiene el usuario por nombre, al tener UNIQUE en la tabla de datos no habrá problema
    Usuario usuarioEnBD = gestorBDUsuarios.obtenerUsuarioPorNombreUsuario(usuario);

    //Si no existe el usuario, indicará un mensaje de error
    if (usuarioEnBD == null) {
        loginVista.mostrarMensaje("Error, usuario no registrado.");
    } else if (usuarioEnBD.getContrasenia().equals(contrasenia)) { //si para el usuario en cuestión coincide la contraseña introducida con la suya se podrá iniciar sesión
        loginVista.mostrarMensaje("¡Autenticación exitosa!");
        sesionActual = iniciarSesion(usuarioEnBD.getIdUsuario(), usuarioEnBD.getNombreUsuario());
        if (sesionActual != null) {
            cerrarVistaLogin(); //Se cierra la vista de login
            mostrarVistaPrincipal(); //Se muestra la principal
        } else {
            loginVista.mostrarMensaje("Error al iniciar sesión.");
        }
    } else {
        loginVista.mostrarMensaje("Contraseña incorrecta.");
    }
}
    //Método para iniciar sesión, recibe por parámetro un id Usuario y un nombreUsuario
    private Sesion iniciarSesion(int idUsuario, String nombreUsuario) {
     //Se obtiene la fecha y hora actual 
    Date fechaInicio = obtenerFechaHoraActual();
    //Se crea una instancia del objeto sesión con los datos obtenidos
    Sesion sesionCreada = new Sesion(0, idUsuario, nombreUsuario, fechaInicio, fechaInicio);
    int idGenerado = gestorBDSesiones.insertarSesion(sesionCreada); //Se inserta la sesión

    if (idGenerado > 0) { //Comprobaciones
        sesionCreada.setIdSesion(idGenerado);
        System.out.println("Sesión de usuario: " + nombreUsuario + " con idSesion: " + idGenerado);
    } else {
        System.out.println("Error al generar el id de la sesión.");
        loginVista.mostrarMensaje("Error al iniciar sesión");
        return null;
    }
    
    return sesionCreada; //Devuelve una nueva sesión
}

    //Método para mostrar la vista registro, también inicia su controlador
    private void mostrarRegistro() {
        cerrarVistaLogin();
        registroVista = new VistaRegistro();
        ControladorRegistro controladorRegistro = new ControladorRegistro(registroVista, loginVista);
        registroVista.mostrar();
    }
    
   

    //Método para mostrar la vista principal, iniciando también su controlador
    private void mostrarVistaPrincipal() {
        vistaPrincipal = new VistaPrincipal();
        ControladorVistaPrincipal controladorVistaPrincipal = new ControladorVistaPrincipal(sesionActual, vistaPrincipal);
        vistaPrincipal.mostrar();
    }
    
    //Método para obtener la fecha y hora actual 
    private Date obtenerFechaHoraActual() {
    Calendar calendar = Calendar.getInstance();
    return calendar.getTime();  
}
    //Método para obtener la contraseña en Base64
    private String obtenerContraseniaBase64(String contrasenia) {
       
       return Base64.getEncoder().encodeToString(contrasenia.getBytes());
    }
}
