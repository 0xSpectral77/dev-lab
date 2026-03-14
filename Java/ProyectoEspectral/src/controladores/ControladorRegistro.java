
package controladores;

import interfaz_grafica.VistaRegistro;
import interfaz_grafica.VistaLogin;
import logica_bd.GestorBDUsuarios;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDGeneral;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 *
 * @author Antonio
 */
public class ControladorRegistro {
    private VistaRegistro registroVista;
    private VistaLogin loginVista;
    private GestorBDUsuarios gestorBDUsuarios;
    private GestorBDGeneral gestorBDGeneral;
    private GestorBDContactos gestorBDContactos;

    //Constructor de la clase
    public ControladorRegistro(VistaRegistro registroVista, VistaLogin loginVista) {
        this.registroVista = registroVista;
        this.loginVista = loginVista;
        this.gestorBDUsuarios = new GestorBDUsuarios();
        this.gestorBDGeneral = new GestorBDGeneral();
         this.gestorBDContactos = new GestorBDContactos();
         
        agregarListenersABotones();
    }
    
    //Método para agregar los listeners al botón y al label
    private void agregarListenersABotones() {
        this.registroVista.getBtnRegistrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        
        });
        
        this.registroVista.getLblLogin().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registroVista.cerrar();
                loginVista.mostrar();
            }
        });
    }

    //Método para registrar a un usuario
    private void registrarUsuario() {
        //Se obtienen los datos desde los campos de texto de la vista
        String usuario = registroVista.getUsuario().getText().trim();
        formatearTexto(usuario);
        String contraseña = new String(registroVista.getContraseña().getPassword()).trim();
        String confirmarContraseña = new String(registroVista.getConfirmarContraseña().getPassword()).trim();
        String email = registroVista.getEmail().getText().trim();
        formatearTexto(email);
        String telefonoStr = registroVista.getTelefono().getText().trim();
        String ip = registroVista.getIp().getText().trim();
        
        //Creamos un ArrayList para poder validar los errores
        ArrayList<String> posiblesErrores = validacionCampos(usuario, contraseña, confirmarContraseña, email, telefonoStr, ip);
        
        //Si hay errores se muestra un mensaje con los posibles errores
        if (!posiblesErrores.isEmpty()) {
            registroVista.mostrarMensaje(String.join("\n", posiblesErrores));
            return;
        }

        
        int comprobanteTelefono = Integer.parseInt(telefonoStr);
        String contraseniaBase64 = obtenerContraseniaBase64(contraseña);
        Usuario nuevoUsuario = new Usuario(0, usuario, contraseniaBase64, email, comprobanteTelefono, ip);
        gestorBDUsuarios.insertarUsuario(nuevoUsuario);

        //Si se registra al usuario se crea una carpeta con su nombre
        crearCarpetaUsuario(usuario);

        registroVista.mostrarMensaje("Usuario registrado exitosamente.");
        registroVista.cerrar();
        loginVista.mostrar();
    }
    
    //Método para validar los campos
    private ArrayList<String> validacionCampos(String usuario, String contraseña, String confirmarContraseña, String email, String telefonoStr, String ip) {
    ArrayList<String> errores = new ArrayList<>();

    if (usuario.isEmpty() || contraseña.isEmpty() || email.isEmpty() || telefonoStr.isEmpty()|| ip.isEmpty()) {
        errores.add("Rellena todos los campos.");
        return errores; // No tiene sentido seguir validando si falta información básica
    }

 
    if (!contraseña.equals(confirmarContraseña)) {
        errores.add("Error, las contraseñas no coinciden.");
    } else if (!esContraseñaSegura(contraseña)) {
        errores.add("Error, la contraseña no es segura debe contener al menos: \n"
                + "8 carácteres alfanuméricos \n"
                + "una letra mayúscula \n"
                + "una letra minúscula \n"
                + "un símbolo especial (`*, ?, ;, :, <, >, (, ), [, ], {, }, |, \\, /, @, #, $, %, ^, &, +, =, !`)");
    }

    if (!esEmailValido(email)) {
        errores.add("\nError, el email no es válido.");
    }

    if (!esTelefonoValido(telefonoStr)) {
        errores.add("\nError, el número de teléfono no es válido.");
    }
    
    if (!esIpValida(ip)) {
        errores.add("\nError, la IP  no es válida.");
    }

    
    if (errores.isEmpty()) {
        if (gestorBDUsuarios.existeUsuarioPorNombre(usuario) || gestorBDContactos.existeNombreContacto(usuario)  ) {
            errores.add("Error, el nombre ya está en uso.");
        }else if(gestorBDUsuarios.existeUsuarioPorTelefono(telefonoStr) || gestorBDContactos.existeTelefono(telefonoStr) ) {
            errores.add("Error, el número de teléfono ya está en uso.");
        }else if(gestorBDUsuarios.existeUsuarioPorEmail(email) || gestorBDContactos.existeEmail(email) ){
            errores.add("Error, la dirección de correo electrónico ya está en uso.");
        }
    }

    return errores;
}

    //Métodos para validar los campos de texto mediante expresiones regulares
    private boolean esContraseñaSegura(String contraseña) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[*,?;:<>()\\[\\]{}|\\\\/@#$%^&+=!]).{8,}$";
        return Pattern.compile(regex).matcher(contraseña).matches();
    }
    
    private boolean esEmailValido(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regex).matcher(email).matches();
    }
       
    private boolean esTelefonoValido(String telefono) {
    // Nuevo regex que acepta tanto el formato de 9 dígitos seguidos como con espacios o guiones opcionales
    String regex = "^(\\d{9}|\\d{3}[\\s\\-]?\\d{3}[\\s\\-]?\\d{3})$";
    return Pattern.compile(regex).matcher(telefono).matches();
}
    
    private boolean esIpValida(String ip) {
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.compile(regex).matcher(ip).matches();
    }
    
    //Método para formatear la primera letra en mayúscula
    private void formatearTexto(String texto){
        texto = texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
       
    }
      
    //Método para obtener la contraseña en Base64
    private String obtenerContraseniaBase64(String contrasenia) {
       
       return Base64.getEncoder().encodeToString(contrasenia.getBytes());
    }
    
    //Método para crea la carpeta del usuario en cuestión
    private void crearCarpetaUsuario(String usuario) {
        File carpetaUsuarios = new File(new File(gestorBDGeneral.obtenerRutaBaseDeDatos()).getParent(), "usuarios");
        File carpetaNuevoUsuario = new File(carpetaUsuarios, usuario);

        if (!carpetaUsuarios.exists()) {
            carpetaUsuarios.mkdirs();
        }
        
        if (!carpetaNuevoUsuario.exists() && carpetaNuevoUsuario.mkdirs()) {
            System.out.println("Carpeta creada para el usuario: " + usuario);
        } else {
            System.out.println("La carpeta del usuario ya existe: " + usuario);
        }
    }
}
