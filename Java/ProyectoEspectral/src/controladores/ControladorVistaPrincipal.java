

package controladores;


import interfaz_grafica.VistaPrincipal;
import interfaz_grafica.VistaCifrarArchivos;
import interfaz_grafica.VistaContactos;
import interfaz_grafica.VistaDescifrarArchivos;
import interfaz_grafica.VistaEnviarArchivos;
import interfaz_grafica.VistaHistorialCriptografico;
import interfaz_grafica.VistaHistorialSesiones;
import interfaz_grafica.VistaHistorialTransferencias;
import interfaz_grafica.VistaRecibirArchivos;
import interfaz_grafica.VistaEditarCuenta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import entidades.Sesion;
import logica_bd.GestorBDSesiones;

/**
 *
 * @author Antonio
 */
public class ControladorVistaPrincipal {

    private Sesion sesionActual;
    private VistaPrincipal vistaPrincipal;

 
    //Constructor de la clase
    public ControladorVistaPrincipal(Sesion sesionActual, VistaPrincipal vistaPrincipal) {
        this.sesionActual = sesionActual;
        this.vistaPrincipal = vistaPrincipal;
        

        //Se procede a inicializar todas las vistas y controladores
        inicializarVistasYControladores();
    }

    //Método para inicializar todas las vistas y controladores
    private void inicializarVistasYControladores() {
        
        VistaCifrarArchivos vistaCifrar = new VistaCifrarArchivos();
        VistaDescifrarArchivos vistaDescifrarArchivos = new VistaDescifrarArchivos();
        VistaHistorialCriptografico vistaHistorialCriptografico = new VistaHistorialCriptografico();
        VistaEnviarArchivos vistaEnviarArchivos = new VistaEnviarArchivos();
        VistaRecibirArchivos vistaRecibirArchivos = new VistaRecibirArchivos();
        VistaHistorialTransferencias vistaHistorialTransferencias = new VistaHistorialTransferencias();
        VistaHistorialSesiones vistaHistorialSesiones = new VistaHistorialSesiones();
        VistaContactos vistaContactos = new VistaContactos();
        VistaEditarCuenta vistaEditarCuenta = new VistaEditarCuenta();
       

        ControladorHistorialCriptografico controladorHistorialCriptografico = new ControladorHistorialCriptografico(sesionActual, vistaHistorialCriptografico);
        ControladorContactos controladorContactos = new ControladorContactos(sesionActual, vistaContactos);
        ControladorHistorialSesiones controladorHistorialSesiones = new ControladorHistorialSesiones(sesionActual, vistaHistorialSesiones);
        ControladorHistorialTransferencias controladorHistorialTransferencias = new ControladorHistorialTransferencias(sesionActual, vistaHistorialTransferencias);
        ControladorVistaEnviarArchivo controladorVistaEnviar = new ControladorVistaEnviarArchivo(sesionActual, vistaEnviarArchivos, controladorHistorialTransferencias);
        ControladorEditarCuenta controladorEditarCuenta = new ControladorEditarCuenta(sesionActual, vistaEditarCuenta);
        ControladorCifrado controladorCifrado = new ControladorCifrado(sesionActual, vistaCifrar, controladorHistorialCriptografico);
        ControladorDescifrado controladorDescifrado = new ControladorDescifrado(sesionActual, vistaDescifrarArchivos, controladorHistorialCriptografico);
        ControladorRecibirArchivo controladorVistaRecibir = new ControladorRecibirArchivo(sesionActual, vistaRecibirArchivos, controladorHistorialTransferencias);

        //Las vistas se añadirán al panel derecho, añadiendo sus nombres
        vistaPrincipal.getPanelDerecho().add(vistaCifrar, "Cifrar Archivos");
        vistaPrincipal.getPanelDerecho().add(vistaDescifrarArchivos, "Descifrar Archivos");
        vistaPrincipal.getPanelDerecho().add(vistaHistorialCriptografico, "Historial Criptográfico");
        vistaPrincipal.getPanelDerecho().add(vistaEnviarArchivos, "Enviar Archivos");
        vistaPrincipal.getPanelDerecho().add(vistaRecibirArchivos, "Recibir Archivos");
        vistaPrincipal.getPanelDerecho().add(vistaHistorialTransferencias, "Historial de Transferencias");
        vistaPrincipal.getPanelDerecho().add(vistaHistorialSesiones, "Historial de Sesiones");
        vistaPrincipal.getPanelDerecho().add(vistaContactos, "Contactos");
        vistaPrincipal.getPanelDerecho().add(vistaEditarCuenta, "Editar Cuenta");

        //Se asignan los listeners a los diferentes botones del panel izquierdo
        asignarListeners();
    }

    //Método para asignar cada uno de los listeners y cargar la vista correspondiente en cada caso
    private void asignarListeners() {
        
        
        vistaPrincipal.getBtnCifrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Cifrar Archivos");
            }
        });

        vistaPrincipal.getBtnDescifrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Descifrar Archivos");
            }
        });

        vistaPrincipal.getBtnHistorialCifrados().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Historial Criptográfico");
            }
        });

        vistaPrincipal.getBtnEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Enviar Archivos");
            }
        });

        vistaPrincipal.getBtnRecibir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Recibir Archivos");
            }
        });

        vistaPrincipal.getBtnHistorialTransferencias().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Historial de Transferencias");
            }
        });

        vistaPrincipal.getBtnContactos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Contactos");
            }
        });

        vistaPrincipal.getBtnHistorialSesiones().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Historial de Sesiones");
            }
        });
        
        vistaPrincipal.getBtnEditarPerfil().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarNuevaVista("Editar Perfil");
            }
        });


        vistaPrincipal.getBtnCerrarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion(sesionActual);
                vistaPrincipal.cerrar();
                ControladorLogin controladorLogin = new ControladorLogin();
                controladorLogin.mostrarVistaLogin();
            }
        });

        //Método para poder cerrar sesión pulsando la "X" de la ventana
        vistaPrincipal.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSesion(sesionActual);
                System.exit(0);
            }
        });
    }

    //Método para cargar cada una de las nuevas vistas, pasando el nombre por parámetro
    private void cargarNuevaVista(String nombreVista) {
    
     //Se eliminan las vistas del panel 
    vistaPrincipal.getPanelDerecho().removeAll();

   //Se utiliza una sentencia switch para cargar en cada caso la vista correspondiente, con su controlador y se la añade al panel derecho
   switch (nombreVista) {
    case "Cifrar Archivos":
        VistaCifrarArchivos nuevaVistaCifrar = new VistaCifrarArchivos();
        ControladorCifrado controladorCifrar = new ControladorCifrado(sesionActual, nuevaVistaCifrar, new ControladorHistorialCriptografico(sesionActual, new VistaHistorialCriptografico()));
        vistaPrincipal.getPanelDerecho().add(nuevaVistaCifrar, nombreVista);
        break;

    case "Descifrar Archivos":
        VistaDescifrarArchivos nuevaVistaDescifrar = new VistaDescifrarArchivos();
        ControladorDescifrado controladorDescifrar = new ControladorDescifrado(sesionActual, nuevaVistaDescifrar, new ControladorHistorialCriptografico(sesionActual, new VistaHistorialCriptografico()));
        vistaPrincipal.getPanelDerecho().add(nuevaVistaDescifrar, nombreVista);
        break;

    case "Historial Criptográfico":
        VistaHistorialCriptografico nuevaVistaHistorialCripto = new VistaHistorialCriptografico();
        ControladorHistorialCriptografico controladorHistorialCripto = new ControladorHistorialCriptografico(sesionActual, nuevaVistaHistorialCripto);
        vistaPrincipal.getPanelDerecho().add(nuevaVistaHistorialCripto, nombreVista);
        break;

    case "Enviar Archivos":
        VistaEnviarArchivos nuevaVistaEnviar = new VistaEnviarArchivos();
        ControladorVistaEnviarArchivo controladorVistaEnviar = new ControladorVistaEnviarArchivo(sesionActual, nuevaVistaEnviar, new ControladorHistorialTransferencias(sesionActual, new VistaHistorialTransferencias()));
        vistaPrincipal.getPanelDerecho().add(nuevaVistaEnviar, nombreVista);
        break;

    case "Recibir Archivos":
        VistaRecibirArchivos nuevaVistaRecibir = new VistaRecibirArchivos();
        ControladorRecibirArchivo controladorVistaRecibir = new ControladorRecibirArchivo(sesionActual, nuevaVistaRecibir, new ControladorHistorialTransferencias(sesionActual, new VistaHistorialTransferencias()));
        vistaPrincipal.getPanelDerecho().add(nuevaVistaRecibir, nombreVista);
        break;

    case "Historial de Transferencias":
        VistaHistorialTransferencias nuevaVistaHistorialTransferencias = new VistaHistorialTransferencias();
        ControladorHistorialTransferencias controladorHistorialTransferencias = new ControladorHistorialTransferencias(sesionActual, nuevaVistaHistorialTransferencias);
        vistaPrincipal.getPanelDerecho().add(nuevaVistaHistorialTransferencias, nombreVista);
        break;

    case "Contactos":
        VistaContactos nuevaVistaContactos = new VistaContactos();
        ControladorContactos controladorContactos = new ControladorContactos(sesionActual, nuevaVistaContactos);
        vistaPrincipal.getPanelDerecho().add(nuevaVistaContactos, nombreVista);
        break;

    case "Historial de Sesiones":
        VistaHistorialSesiones nuevaVistaHistorialSesiones = new VistaHistorialSesiones();
        ControladorHistorialSesiones controladorHistorialSesiones = new ControladorHistorialSesiones(sesionActual, nuevaVistaHistorialSesiones);
        vistaPrincipal.getPanelDerecho().add(nuevaVistaHistorialSesiones, nombreVista);
        break;
    
    case "Editar Perfil":
       
        VistaEditarCuenta nuevaVistaEditarCuenta = new VistaEditarCuenta();
        ControladorEditarCuenta controladorEditarCuenta = new ControladorEditarCuenta(sesionActual, nuevaVistaEditarCuenta);
        vistaPrincipal.getPanelDerecho().add(nuevaVistaEditarCuenta, nombreVista);
        break;
}
    //Se muestra la vista en el panel
    vistaPrincipal.getCardLayout().show(vistaPrincipal.getPanelDerecho(), nombreVista);

    //Se actuliza el panel 
    vistaPrincipal.getPanelDerecho().revalidate();
    vistaPrincipal.getPanelDerecho().repaint();
}


    //Método para cerrar Sesión
    private void cerrarSesion(Sesion sesionActual) {
        Date fechaFin = obtenerFechaHoraActual();
        GestorBDSesiones gestorBdSesiones = new GestorBDSesiones();
        gestorBdSesiones.actualizarFechaFinSesion(sesionActual.getIdSesion(), fechaFin);
        VistaHistorialSesiones vistaHistorialSesiones = new VistaHistorialSesiones();
        
        ControladorHistorialSesiones controladorHistorialSesiones = new ControladorHistorialSesiones(sesionActual, vistaHistorialSesiones);
        controladorHistorialSesiones.mostrarHistorialSesiones();
    }

    //Método para obtener la fecha y hora actual
    private Date obtenerFechaHoraActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime(); 
    }
}