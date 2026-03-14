
package controladores;

import entidades.AccionCIFDES;
import entidades.AccionEnvioRecp;
import entidades.Archivo;
import entidades.Sesion;
import logica_bd.GestorBDAccionEnvioRecp;
import logica_bd.GestorBDArchivos;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDAccionCIFDES;
import logica_comunicacion.Servidor;
import interfaz_grafica.VistaRecibirArchivos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Antonio
 */

public class ControladorRecibirArchivo {

    private VistaRecibirArchivos vistaRecibir;

    private int puerto = 5000;
    private Servidor servidor;
    private ControladorHistorialTransferencias controladorHistorialTransferencias;
    private Sesion sesionActual;
    private Archivo archivoGuardado;

    //Consctructor de la clase
    public ControladorRecibirArchivo(Sesion sesionActual, VistaRecibirArchivos vistaRecibir, ControladorHistorialTransferencias controladorHistorialTransferencias) {
        this.sesionActual = sesionActual;
        this.vistaRecibir = vistaRecibir;
        this.controladorHistorialTransferencias = controladorHistorialTransferencias;

        //Se asignan los listeners a los botones de iniciar y detener servidor
        asignarListeners();
    }

    //Método para asignar los diferentes listeners
    private void asignarListeners() {
        vistaRecibir.getRecibirButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarServidor();
            }
        });

        vistaRecibir.getDetenerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerServidor();
            }
        });
    }

    //Método para iniciar el servidor
    private void iniciarServidor() {
        vistaRecibir.getRecibirButton().setEnabled(false); //El botón para abrir la conexión se desactiva
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() { //El servidor se va a ejecutar en un segundo plano, para no hacer que UI se bloquee mientras se recibe el archivo
            @Override
            protected Void doInBackground() {
                try {
                    //Estas son las intrucciones que se van a ejecutar en segundo plano
                    String nombreUsuarioSistema = System.getProperty("user.name"); //Obtenemos el nombre de usuario del pc en cuestion
                    String nombreUsuarioApp = sesionActual.getNombreUsuario(); //Obtenemos el nombre de usuario de la app
                    String rutaDestino = "C:\\Users\\" + nombreUsuarioSistema + "\\Documents\\Espectral\\usuarios\\" + nombreUsuarioApp + "\\archivos_recibidos"; //Guardamos la ruta donde se guardaran los archivos en una variables
                    File directorioDestino = new File(rutaDestino); //Se crea la carpeta en cuestión
                    if (!directorioDestino.exists() && !directorioDestino.mkdirs()) { //Si no es posible crearla se muestra un mensaje de error
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                vistaRecibir.mostrarMensaje("No se ha podido crear la carpeta de destino");
                            }
                        });
                        return null;
                    }

                    //Se inicia el servidor y se pasa por parámetro el puerto, la ruta de destino y el nombre de usuario
                    servidor = new Servidor(puerto, rutaDestino, nombreUsuarioApp);
                    vistaRecibir.mostrarMensaje("Se va a proceder a iniciar la conexión.");

                    //Se establece el callback que avisará al servidor de que el archivo se ha recibido correctamente
                    servidor.setArchivoRecibidoCallback(new Runnable() {
                        @Override
                        public void run() {
                            //Cuando el archivo se ha recibido correctamente se ejecutará todo el código siguiente
                            File destinoArchivo = servidor.obtenerArchivo(); //Se obtiene el archivo recibido

                            if (destinoArchivo != null && destinoArchivo.exists()) {
                                //Se obtienen los datos del archivo
                                int tamanioArchivoRecibido = servidor.getTamanioArchivo();
                                String nombreArchivoRecibido = servidor.getNombreArchivo();
                                String extArchivoRecibido = servidor.getExtArchivo();
                                String hashArchivoRecibido = servidor.getHashBase();
                                String hashArchivoCifradoRecibido = servidor.getHashCifrado();
                                String claveBase64ArchivoRecibido = servidor.getClaveBase64();
                                boolean estaCifradoArchivoRecibido = servidor.getEstaCifrado();
                                String nombreEmisor = servidor.getNombreEmisor();
                                String nombreReceptor = servidor.getNombreReceptor();

                                //Se guarda el registro del archivo en la base de datos
                                archivoGuardado = new Archivo(0, destinoArchivo.getAbsolutePath(), tamanioArchivoRecibido, nombreArchivoRecibido, extArchivoRecibido, hashArchivoRecibido, hashArchivoCifradoRecibido, claveBase64ArchivoRecibido, estaCifradoArchivoRecibido);

                                GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
                                //Guarda en la base de datos
                                if (gestorBDArchivos.archivoEstaEnBaseDeDatos(destinoArchivo.getAbsolutePath())) {
                                    gestorBDArchivos.actualizarArchivo(archivoGuardado);
                                } else {
                                    gestorBDArchivos.insertarArchivo(archivoGuardado);
                                }

                                //Se procede a registrar la acción de recepción con los datos correspondientes
                                GestorBDContactos gestorbdContactos = new GestorBDContactos();
                                int idEmisor = gestorbdContactos.obtenerIdPorNombre(nombreEmisor);
                                GestorBDAccionEnvioRecp gestorBDEnvioRecp = new GestorBDAccionEnvioRecp();
                                AccionEnvioRecp accionEnvioRecp = new AccionEnvioRecp(0, sesionActual.getIdUsuario(), sesionActual.getNombreUsuario(), idEmisor, nombreEmisor, archivoGuardado.getIdArchivo(), archivoGuardado.getNombreArchivo(), "Recepción", obtenerFechaHoraActual());
                                gestorBDEnvioRecp.insertarAccionEnvioRecp(accionEnvioRecp);

                                //Se establece quien lo ha cifrado para poder pasárselo al receptor
                                String nuevoCifrador = "Cifrado por " + nombreEmisor;
                                //Se inserta la acción y se muestra el historial
                                AccionCIFDES accionRealizadaPorEmisor = new AccionCIFDES(0,sesionActual.getIdUsuario(), sesionActual.getNombreUsuario(), archivoGuardado.getIdArchivo(), nombreArchivoRecibido, obtenerFechaHoraActual(), nuevoCifrador);

                                GestorBDAccionCIFDES gestorBDAccionCIFDES = new GestorBDAccionCIFDES();
                                gestorBDAccionCIFDES.insertarAccionCIDFES(accionRealizadaPorEmisor);

                                controladorHistorialTransferencias.mostrarHistorialTransferencias();

                                //Cuando el mensaje se ha recibido correctamente, se muestra un mensaje de éxito por pantalla
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        vistaRecibir.mostrarMensaje("Archivo recibido correctamente, recuerde cerrar la conexión");
                                    }
                                });
                            } else { //Si no se ha podido recibir, se muestra un mensaje de error por pantalla
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                         vistaRecibir.mostrarMensaje("Error al recibir el archivo");
                                    }
                                });
                            }
                        }
                    });

                    //Se inicia el servidor 
                    servidor.iniciarServidor();

                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                             vistaRecibir.mostrarMensaje("Error inesperado");
                        }
                    });
                }

                return null;
            }
        };

        worker.execute();
    }

    //Método para detener el servidor
    private void detenerServidor() {
        try {
            if (servidor != null) {
                servidor.cerrarServidor();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                         vistaRecibir.mostrarMensaje("Servidor detenido correctamente");
                    }
                });
                vistaRecibir.getRecibirButton().setEnabled(true);
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                     vistaRecibir.mostrarMensaje("Error al detener el servidor");
                }
            });
        }
    }

    //Método para obtener la fecha y hora actual
    private Date obtenerFechaHoraActual() {
    Calendar calendar = Calendar.getInstance();
    return calendar.getTime();  
}
}
