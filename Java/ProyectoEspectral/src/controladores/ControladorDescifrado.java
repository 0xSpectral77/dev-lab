
package controladores;

import entidades.AccionCIFDES;
import entidades.AccionEnvioRecp;
import entidades.Archivo;
import entidades.Sesion;
import logica_bd.GestorBDArchivos;
import logica_bd.GestorBDAccionCIFDES;
import logica_bd.GestorBDAccionEnvioRecp;
import logica_bd.GestorBDUsuarios;
import logica_cifradodes.Descifrado;
import interfaz_grafica.VistaDescifrarArchivos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author Antonio
 */

public class ControladorDescifrado {
    private VistaDescifrarArchivos vistaDescifrar;
    private Sesion sesionActual;
    private DefaultTableModel modeloTabla;
    private boolean archivoSeleccionado;
    private ControladorHistorialCriptografico controladorHistorialCriptografico;

    //Constructor de la clase
    public ControladorDescifrado(Sesion sesionActual, VistaDescifrarArchivos vista, ControladorHistorialCriptografico controladorHistorialCriptografico) {
        this.sesionActual = sesionActual;
        this.vistaDescifrar = vista;
        this.modeloTabla = vista.getModeloTablaArchivos();
        this.archivoSeleccionado = false;
        this.controladorHistorialCriptografico = controladorHistorialCriptografico;

        //Se asignan los listeners y se configura la zona de arrastre
        asignarListeners();
        configurarZonaArrastrar();
    }

    //Método para asignar los listeners a cada botón
    private void asignarListeners() {
        vistaDescifrar.getBotonBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBotonBuscarArchivo();
            }
        });

        vistaDescifrar.getBotonDescifrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                descifrarArchivo();
            }
        });

        vistaDescifrar.getBotonEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarArchivo();
            }
        });
    }

    //Método para buscar el archivo 
    private void activarBotonBuscarArchivo() {
        FileDialog navegadorArchivo = new FileDialog((Frame) null, "Seleccionar un archivo", FileDialog.LOAD);
        navegadorArchivo.setVisible(true);
        if (navegadorArchivo.getFile() != null) {
            File archivoSeleccionado = new File(navegadorArchivo.getDirectory() + navegadorArchivo.getFile());
            actualizarVistaConArchivo(archivoSeleccionado);
        }
    }

    //Método para actualizar la vista
    private void actualizarVistaConArchivo(File archivo) {
        //Si el archivo existe y es un archivo, obtenemos los datos del mismo y los añadimos a la tabla
        if (archivo.exists() && archivo.isFile()) {
            archivoSeleccionado = true;
            vistaDescifrar.getRutaArchivoSeleccionado().setText(archivo.getAbsolutePath());
            String nombreArchivo = archivo.getName();
            String tamanioArchivo = Long.toString(archivo.length() / 1024) + " KB";
            String extension = obtenerExtension(archivo);

            //See limpia la tabla
            modeloTabla.setRowCount(0);
            modeloTabla.addRow(new String[]{nombreArchivo, tamanioArchivo, extension}); //se añade una fila con los campos correspondientes

            //Se obtiene el número de filas y se selecciona la fila añadida
            int indice = modeloTabla.getRowCount() - 1;
            vistaDescifrar.getTablaArchivos().setRowSelectionInterval(indice, indice);
            modeloTabla.fireTableDataChanged();
        } else {
            vistaDescifrar.mostrarMensaje("Error, archivo inválido"); //Se muestra un mensaje de error en caso de que no exista el archivo o de que no se trate de un archivo
        }
    }

    //Método para obtener la extensión de un archivo
    private String obtenerExtension(File archivo) {
        String nombreArchivo = archivo.getName();
        int indice = nombreArchivo.lastIndexOf('.');

        if (indice == -1) {
            return "";
        } else {
            return nombreArchivo.substring(indice + 1);
        }
    }

    //Método para limpiar un archivo
    private void limpiarArchivo() {
        if (!archivoSeleccionado) {
            JOptionPane.showMessageDialog(vistaDescifrar, "No hay archivo seleccionado", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloTabla.setRowCount(0);
        vistaDescifrar.getRutaArchivoSeleccionado().setText("");
        archivoSeleccionado = false;
    }

    //Método para descifrar un archivo 
    private void descifrarArchivo() {
        //Si el archivo no está seleccionado, se mostrará un mensaje por pantalla
        if (!archivoSeleccionado) {
            vistaDescifrar.mostrarMensaje("Error no hay archivo seleccionado");
            return;
        }

        //Se obtiene la ruta del archivo desde la vista descifrar
        String rutaArchivo = vistaDescifrar.getRutaArchivoSeleccionado().getText();
        // Se crea una instancia de descifrar archivo
        File archivo = new File(rutaArchivo);

        //Si el archivo no existe, se muestra un mensaje de error
        if (!archivo.exists()) {
            vistaDescifrar.mostrarMensaje("Error el archivo no existe");
            return;
        }

        //Se crea una instancia del gestor de base de datos de archivos, se obtiene el archivo en cuestión, si no se puede obtener el archivo se muestra un error
        GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
        Archivo archivoBD = gestorBDArchivos.obtenerArchivoPorRutaAbsoluta(rutaArchivo);
        if (archivoBD == null) {
            vistaDescifrar.mostrarMensaje("Error no se encontró el archivo en la base de datos");
            return;
        }

        //Se crea una instancia de la clase gestora de acciones de cifrado-descifrado
        GestorBDAccionCIFDES gestorBDAccionCIFDES = new GestorBDAccionCIFDES();
        //Se obtiene la última acción de cifrado del archivo
        AccionCIFDES ultimaAccionCifrado = gestorBDAccionCIFDES.obtenerUltimaAccionCifrado(archivoBD.getIdArchivo());

        //Se crea una instancia del gestor de envíos y recepciones
        GestorBDAccionEnvioRecp gestorEnvioRecp = new GestorBDAccionEnvioRecp();
        //Se obtiene la última acción de Envío y Recepción para el usuario en cuestión
        int idUsuarioActual = sesionActual.getIdUsuario();
        AccionEnvioRecp ultimaAccionEnvRecp = gestorEnvioRecp.obtenerUltimaAccionEnvioRecp(idUsuarioActual);

        //Si la última acción de cifrado existe
        if (ultimaAccionCifrado != null) {
            //Se obtiene el id de usuario de la última acción de cifrado
            int idUsuarioCifrado = ultimaAccionCifrado.getIdUsuario();

            //Si el usuario actual es el mismo que cifró el archivo, podrá descifrarlo
            if (idUsuarioCifrado == idUsuarioActual) {
                Descifrado descifrado = new Descifrado(archivo); // instancia de descifrado
                if (descifrado.descifrarArchivo()) {
                    registrarAccionDescifrado(archivo.getAbsolutePath(), "Descifrado");
                    gestorBDArchivos.desactivarCifradoPorRuta(rutaArchivo);
                    controladorHistorialCriptografico.mostrarHistorialCriptografico();
                    vistaDescifrar.mostrarMensaje("Archivo descifrado exitosamente");
                } else {
                    vistaDescifrar.mostrarMensaje("Error, no se pudo descifrar el archivo");
                }
                return;
            }
        }
        //Si un usuario recibe un archivo cifrado también debe poder descifrarlo aunque no lo haya cifrado, si el archivo era para ese usuario
        boolean puedeDescifrarReceptor = receptorPuedeDescifrarArchivo(ultimaAccionEnvRecp, ultimaAccionCifrado);

        //Si el receptor puede descifrar el archivo, lo descifra, en caso contrario mostrará un mensaje de error
        if (puedeDescifrarReceptor && ultimaAccionCifrado == null) {
            Descifrado descifrado = new Descifrado(archivo);
            if (descifrado.descifrarArchivo()) {
                registrarAccionDescifrado(archivo.getAbsolutePath(), "Descifrado");
                gestorBDArchivos.desactivarCifradoPorRuta(rutaArchivo);
                controladorHistorialCriptografico.mostrarHistorialCriptografico();
                vistaDescifrar.mostrarMensaje("Archivo descifrado exitosamente");
            } else {
                vistaDescifrar.mostrarMensaje("Error, no se pudo descifrar el archivo");
            }
        } else {
            vistaDescifrar.mostrarMensaje("Error, no se encontró un registro de cifrado para este archivo");
        }
    }

    //Método para registrar acción de cifrado o descifrado
    private void registrarAccionDescifrado(String rutaAbsoluta, String tipoAccion) {
        Date fechaActual = obtenerFechaHoraActual();
        int idUsuario = sesionActual.getIdUsuario();
        GestorBDUsuarios gestorBDUsuarios = new GestorBDUsuarios();
        String nombreUsuario = gestorBDUsuarios.obtenerNombreUsuarioPorId(idUsuario);

        GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
        Archivo archivoARegistrar = gestorBDArchivos.obtenerArchivoPorRutaAbsoluta(rutaAbsoluta);

        if (archivoARegistrar == null) {
            JOptionPane.showMessageDialog(vistaDescifrar, "No se encontró el archivo original en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idArchivo = archivoARegistrar.getIdArchivo();

        AccionCIFDES accionCifDes = new AccionCIFDES(0, idUsuario, nombreUsuario, idArchivo, archivoARegistrar.getNombreArchivo(), fechaActual, tipoAccion);

        GestorBDAccionCIFDES gestorBDAccionCIFDES = new GestorBDAccionCIFDES();
        gestorBDAccionCIFDES.insertarAccionCIDFES(accionCifDes);
        controladorHistorialCriptografico.mostrarHistorialCriptografico();
    }

    //Método para Obtener fecha y hora actual
    private Date obtenerFechaHoraActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    //Método para averiguar si el receptor puede descifrar el archivo, recibe por parámetros una acción de cifrado y una acción de envío
    private boolean receptorPuedeDescifrarArchivo(AccionEnvioRecp accionEnvioRecp, AccionCIFDES accionCifDes) {
        //Si cualquiera de las dos acciones es nula, se muestra un mensaje de error
        if (accionEnvioRecp == null && accionCifDes == null) {
            JOptionPane.showMessageDialog(vistaDescifrar, "No puedes descifrar este archivo, no tienes permiso", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Si hay una acción de recepción, registrada a nombre del usuario, valdrá true
        if (accionEnvioRecp != null) {
            if (!accionEnvioRecp.getNombreContactoEnvioRecp().equals(accionEnvioRecp.getNombreUsuarioEnvioRecp())
                    && "Recepción".equals(accionEnvioRecp.getTipoAccion())) {
                return true;
            }
        }

        return false; //en caso contrario, false
    }

    //Método para configurar la zona para arrastrar archivos
    private void configurarZonaArrastrar() {
        new DropTarget(vistaDescifrar.getZonaArrastre(), DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    //Se obtiene directamente el primer archivo de la lista (el único que se podrá tratar)
                    File archivo = ((java.util.List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0);
                    actualizarVistaConArchivo(archivo);
                } catch (Exception e) {
                    e.printStackTrace();
                    vistaDescifrar.mostrarMensaje("Error al procesar el archivo");
                }
            }
        });
    }
    
}
