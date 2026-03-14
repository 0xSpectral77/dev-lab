

package controladores;

import entidades.AccionEnvioRecp;
import entidades.Archivo;
import entidades.Asociacion;
import entidades.Sesion;
import logica_bd.GestorBDAccionEnvioRecp;
import logica_bd.GestorBDArchivos;
import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDContactos;
import logica_comunicacion.Cliente;
import interfaz_grafica.VistaEnviarArchivos;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Antonio
 */
public class ControladorVistaEnviarArchivo {

    private Sesion sesionActual;
    private DefaultTableModel modeloTabla;
    private VistaEnviarArchivos vistaEnviar;
    private boolean archivoHaSidoSeleccionado;
    private ControladorHistorialTransferencias controladorHistorialTransferencias;
    private Archivo archivoAEnviar = new Archivo();
  

    //Constructor de la clase
    public ControladorVistaEnviarArchivo(Sesion sesionActual, VistaEnviarArchivos vistaEnviar, ControladorHistorialTransferencias controladorHistorialTransferencias ) {
        this.sesionActual = sesionActual;
        this.vistaEnviar = vistaEnviar;
        this.controladorHistorialTransferencias = controladorHistorialTransferencias;
        this.archivoHaSidoSeleccionado = false;
        this.modeloTabla = vistaEnviar.getModeloTabla();
        
        //Se asignan los diferentes listeners, se configura la zona de arrastrar los archivos y se cargan los contactos en el combo box
        asignarListeners();
        configurarZonaArrastrar();
        cargarContactos();
    }

    //Método para configurar los diferentes listeners
    private void asignarListeners() {
        this.vistaEnviar.getBotonBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBotonBuscarArchivo();
            }
        
        });
        
        this.vistaEnviar.getBotonLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarArchivo();
            }
        
        });
        
        this.vistaEnviar.getBotonEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarArchivoSeleccionado();
            }
        
        });
    }

    //Método para utilizar el botón para buscar archivos
    private void activarBotonBuscarArchivo() {
        try {
            FileDialog navegadorArchivo = new FileDialog((Frame) null, "Seleccionar un archivo", FileDialog.LOAD);
            navegadorArchivo.setVisible(true);

            if (navegadorArchivo.getFile() != null) {
                String rutaArchivoSeleccionado = navegadorArchivo.getDirectory() + navegadorArchivo.getFile();
                File archivo = new File(rutaArchivoSeleccionado);
                archivoHaSidoSeleccionado = true;
                actualizarVistaConArchivo(archivo);
            } else {
                vistaEnviar.getRutaArchivoSeleccionado().setText("");  
            }
        } catch (Exception e) {
            vistaEnviar.mostrarMensaje("Error al seleccionar el archivo");
        }
    }

    //Método para actualizar la tabla con los datos del archivo
    private void actualizarVistaConArchivo(File archivo) {
        try {
            if (archivo.exists() && archivo.isFile()) {
                archivoHaSidoSeleccionado = true;
                vistaEnviar.getRutaArchivoSeleccionado().setText(archivo.getAbsolutePath());
                String nombreArchivo = archivo.getName();
                long tamanioArchivo = archivo.length() / 1024; //Se divido entre 1024 para que muestre los KiloBytes
                String tamanioFormateado = String.valueOf(tamanioArchivo);
                String extensionArchivo = obtenerExtension(archivo);

                modeloTabla.setRowCount(0);
                String[] datosTabla = new String[] {nombreArchivo, tamanioFormateado, extensionArchivo};
                modeloTabla.addRow(datosTabla); //Se añade una fila

                int indiceTabla = modeloTabla.getRowCount() - 1;
                vistaEnviar.getTablaArchivos().setRowSelectionInterval(indiceTabla, indiceTabla);
                modeloTabla.fireTableDataChanged();
            } else {
                vistaEnviar.mostrarMensaje("Error, archivo inválido");
            }
        } catch (Exception e) {
            vistaEnviar.mostrarMensaje("Error, al mostrar la vista con el archivo");
        }
    }
    //Método para obtener la extensión de un archivo 
    private String obtenerExtension(File archivo) {
        try {
            String nombreArchivo = archivo.getName();
            int indice = nombreArchivo.lastIndexOf('.');
            if (indice == -1){
                return " ";
            }else return  nombreArchivo.substring(indice + 1);
        } catch (Exception e) {
            vistaEnviar.mostrarMensaje("Error al obtener la extensión del archivo");
            return "";
        }
    }

    //Método para limpiar un archivo 
    private void limpiarArchivo() {
        try {
            if (!archivoHaSidoSeleccionado) {
                vistaEnviar.mostrarMensaje("Aviso, no hay archivo seleccionado para limpiar");
                return;
            }
            modeloTabla.setRowCount(0);  //Se limpia la tabla
            vistaEnviar.getRutaArchivoSeleccionado().setText("");  //Se limpia el label de al lado del botón buscar
            archivoHaSidoSeleccionado = false;  //Resetear la variable
        } catch (Exception e) {
            vistaEnviar.mostrarMensaje("Error al limpiar el archivo");
        }
    }
    
    

    //Método para configurar la zona de arrastre de archivos
    private void configurarZonaArrastrar() {
        //Accedemos al componente gráfico, copiamos el archivo, y sobrescribimos el método drop para ejecutarlo cuando se suelte el archivo
        new DropTarget(vistaEnviar.getZonaArrastre(), DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    //Se extrae directamente el primer archivo que se ha arrastrado
                    File archivo = ((List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0);
                    actualizarVistaConArchivo(archivo); //Se actualiza la vista con el único archivo permitido
                } catch (Exception e) {
                    e.printStackTrace();
                    vistaEnviar.mostrarMensaje("Error al procesar el archivo");
                }
            }
        });
    }


    //Método para cargar los contactos en el Combo Box
    private void cargarContactos() {
        try {
            //Se limpian los contactos antes de añadir ninguno
            vistaEnviar.getContactosComboBox().removeAllItems();

            //Instancia del gestor de Base de datos de asociaciones
             GestorBDAsociacion gestorBDAsociacion = new GestorBDAsociacion();
            //Se obtienen los contactos que tiene el usuario agregados
            ArrayList<Asociacion> asociaciones = gestorBDAsociacion.listarAsociacionesPorIdUsuario(sesionActual.getIdUsuario());

            //se procede a añadir cada contacto al combo box
            if (asociaciones.isEmpty()) {
                vistaEnviar.getContactosComboBox().addItem("No hay ningún contacto disponible");
            } else {
                for (Asociacion asociacion : asociaciones) {
                    vistaEnviar.getContactosComboBox().addItem(asociacion.getNombreContactoAsociado());
                }
            }

            //Actualizamos la vista
            vistaEnviar.getContactosComboBox().revalidate();
            vistaEnviar.getContactosComboBox().repaint();
        } catch (Exception e) {
           vistaEnviar.mostrarMensaje("Error al mostrar los contactos");
        }
    }

    //Método para enviar el archivo seleccionado
    private void enviarArchivoSeleccionado() {
    try {
        //Se comprueba que haya un archivo seleccionado, de lo contrario se mostrará un mensaje de aviso
        if (!archivoHaSidoSeleccionado) {
            vistaEnviar.mostrarMensaje("Atención, no hay ningún archivo seleccionado para enviar");
            return;
        }

        //Se guarda la ruta absoluta del archivo en una variable y se crea una instancia del archivo
        String rutaArchivo = vistaEnviar.getRutaArchivoSeleccionado().getText();
        File archivo = new File(rutaArchivo);

        //Si el archivo no existe, o la ruta proporcionada no apunta a un archivo se mostrará un mensaje de error
        if (!archivo.exists() || !archivo.isFile()) {
            vistaEnviar.mostrarMensaje("Error, el archivo seleccionado no es válido");
            return;
        }

        //Se comprueba si el contacto ha sido seleccionado
        String nombreContactoSeleccionado = (String) vistaEnviar.getContactosComboBox().getSelectedItem(); //Se guarda en una variable el nombre del contacto seleccionado
        if (nombreContactoSeleccionado == null || nombreContactoSeleccionado.equals("No hay contactos disponibles")) { //En caso de que el contacto seleccionado sea nulo, se indicará que no hay contactos disponibles
            vistaEnviar.mostrarMensaje("Error, seleccione un contacto válido");
            return;
        }

        //Se verifica que existe relación entre el usuario y el contacto seleccionado
        GestorBDAsociacion gestorBDAsociacion = new GestorBDAsociacion();
        GestorBDContactos gestorBDContactos = new GestorBDContactos();
        int idContactoSeleccionado = gestorBDContactos.obtenerIdPorNombre(nombreContactoSeleccionado);
        boolean hayRelacion = gestorBDAsociacion.existeRelacionPorUsuarioYContacto(sesionActual.getIdUsuario(), idContactoSeleccionado);
        
        //Si no existiera relación, se muestra un mensaje de error
        if (!hayRelacion) {
            vistaEnviar.mostrarMensaje("Error, no existe relación con ese contacto");
            return;
        }

        //Se obtiene la IP del contacto al cual se le quiere enviar el archivo
        String servidorIP = gestorBDContactos.obtenerIpContactoPorNombre(nombreContactoSeleccionado);
        int puerto = 5000; //Puerto establecido por defecto

        //Creamos una instancia de cliente, pasándole por parámetro la ip del receptor y el puerto 5000
        Cliente cliente = new Cliente(servidorIP, puerto);

        //Se verifica que la conexión se encuentre activa,es decir que el receptor haya abierto la conexión, en caso de que no lo esté, se mostrará un error
        if (!cliente.esConexionActiva()) {
            vistaEnviar.mostrarMensaje("Error, no se pudo establecer conexión con el servidor del contacto");
            return;
        }

        //Se obtiene el archivo que se va a enviar mediante consulta a la base de datos
        GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
        Archivo archivoAEnviar = gestorBDArchivos.obtenerArchivoPorRutaAbsoluta(archivo.getAbsolutePath());

        //Se envia el archivo y se verifica si la transferencia se ha realizado correctamente
        boolean transferenciaExitosa = cliente.enviarArchivoyDatos(
                archivoAEnviar.getTamanio(),
                archivoAEnviar.getNombreArchivo(),
                archivoAEnviar.getExt(),
                archivoAEnviar.getHashArchivoBase(),
                archivoAEnviar.getHashArchivoCifrado(),
                archivoAEnviar.getClaveBase64(),
                archivoAEnviar.getEstaCifrado(),
                sesionActual.getNombreUsuario(),
                nombreContactoSeleccionado,
                archivo
        );
        //Si la transferencia no resulta existosa se muestra un mensaje de error por pantalla

        if (!transferenciaExitosa) {
            vistaEnviar.mostrarMensaje("Error, hubo un problema al enviar el archivo");
            return;
        }

        //Si la transferencia es satisfactoria se crea una nueva instancia de AccionEnvioRecp con los datos obtenidos
        AccionEnvioRecp accionEnvio = new AccionEnvioRecp(
                0,
                sesionActual.getIdUsuario(),
                sesionActual.getNombreUsuario(),
                idContactoSeleccionado,
                nombreContactoSeleccionado,
                archivoAEnviar.getIdArchivo(),
                archivoAEnviar.getNombreArchivo(),
                "Envío",
                obtenerFechaHoraActual()
        );
        GestorBDAccionEnvioRecp gestorAccion = new GestorBDAccionEnvioRecp();
        gestorAccion.insertarAccionEnvioRecp(accionEnvio);

        
        controladorHistorialTransferencias.mostrarHistorialTransferencias();

       
        vistaEnviar.mostrarMensaje("Archivo enviado correctamente");
    } catch (Exception e) {
        
        vistaEnviar.mostrarMensaje("Error al enviar el archivo");
        e.printStackTrace(); 
    }
}

   //Método para obtener la fecha y hora actual
    private Date obtenerFechaHoraActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
