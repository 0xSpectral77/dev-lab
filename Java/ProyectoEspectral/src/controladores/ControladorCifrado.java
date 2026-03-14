
package controladores;

import entidades.AccionCIFDES;
import entidades.Archivo;
import entidades.Sesion;
import logica_bd.GestorBDAccionCIFDES;
import logica_bd.GestorBDArchivos;
import logica_bd.GestorBDUsuarios;
import logica_cifradodes.Cifrado;
import interfaz_grafica.VistaCifrarArchivos;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.SecretKey;



/**
 *
 * @author Antonio
 */

public class ControladorCifrado {
    private VistaCifrarArchivos vistaCifrar;
    private Sesion sesionActual;
    private DefaultTableModel modeloTabla;
    private boolean archivoEstaSeleccionado; 
    private ControladorHistorialCriptografico controladorHistorialCriptografico;
    
    //Constructor de la clase ControladorCifrado
    public ControladorCifrado(Sesion sesionActual, VistaCifrarArchivos vistaCifrar, ControladorHistorialCriptografico controladorHistorialCriptografico) {
        this.sesionActual = sesionActual;
        this.vistaCifrar = vistaCifrar;
        this.modeloTabla = vistaCifrar.getModeloTablaArchivos();
        this.archivoEstaSeleccionado = false;
        this.controladorHistorialCriptografico = controladorHistorialCriptografico;

       
        
        //Asignamos listener y configuramos la zona de arrastrar archivos
        asignarListeners();
        configurarZonaArrastre();
    }

    //Función para asignar los listeners a cada botón de la vista
    private void asignarListeners() {
        this.vistaCifrar.getBotonBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBotonBuscarArchivo();
            }
        
        });
        
        this.vistaCifrar.getBotonEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarArchivo();
            }
        
        });
        
        this.vistaCifrar.getBotonCifrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cifrarArchivoSeleccionado();
            }
        
        });
        
    }

    //Función para buscar un archivo mediante un FileDialog
    private void activarBotonBuscarArchivo() {
        //Mostramos el FileDialog
        FileDialog navegadorArchivo = new FileDialog((Frame) null, "Seleccionar un archivo", FileDialog.LOAD);
        navegadorArchivo.setVisible(true);

        //Si seleccionamos un archivo, se creará una instancia del mismo y se actualizará la interfaz gráfica con los datos del mismo
        if (navegadorArchivo.getFile() != null) {
            String rutaArchivoSeleccionado = navegadorArchivo.getDirectory() + navegadorArchivo.getFile();
            File archivoSeleccionado = new File(rutaArchivoSeleccionado);
            archivoEstaSeleccionado = true;
            actualizarVistaConArchivo(archivoSeleccionado);
        } else { //Si no, el label donde se muestra la ruta permanecerá igual
            vistaCifrar.getRutaArchivoSeleccionado().setText("");
        }
    }

    //Método para actualizar la vista con los datos del archivo
    private void actualizarVistaConArchivo(File archivo) {
        //Si el archivo existe y es un archivo, obtenemos los datos del mismo y los añadimos a la tabla
        if (archivo.exists() && archivo.isFile()) {
            archivoEstaSeleccionado = true;
            vistaCifrar.getRutaArchivoSeleccionado().setText(archivo.getAbsolutePath());
            String nombreArchivo = archivo.getName();
            String tamanioArchivo = Long.toString(archivo.length() / 1024) + " KB";
            String extension = obtenerExtension(archivo);

            //Se limpia la tabla
            modeloTabla.setRowCount(0);
            modeloTabla.addRow(new String []{nombreArchivo, tamanioArchivo, extension}); //se añade una fila con los campos correspondientes

            //Se obtiene el número de filas y se selecciona la fila añadida
            int indice = modeloTabla.getRowCount() - 1;
            vistaCifrar.getTablaArchivos().setRowSelectionInterval(indice, indice);
            modeloTabla.fireTableDataChanged();
        } else {
            vistaCifrar.mostrarMensaje("Error, archivo inválido");
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

    //Método para limpiar de la selección el archivo
    //Si no hay ningún archivo seleccionado se muestra una ventana de aviso
    private void limpiarArchivo() {
        if (!archivoEstaSeleccionado) {
            vistaCifrar.mostrarMensaje("Aviso, no hay ningún archivo seleccionado");
            return;
        }
        //Se limpia la tabla
        modeloTabla.setRowCount(0);
        vistaCifrar.getRutaArchivoSeleccionado().setText(""); //Limpiamos el label de la ruta de archivo
        archivoEstaSeleccionado = false; //volvemos al estado false, ya que no hay nigún archivo seleccinado
    }

    //Método para configurar la zona de arrastre de archivos
        private void configurarZonaArrastre() {
        //Accedemos al componente gráfico, copiamos el archivo, y sobrescribimos el método drop para ejecutarlo cuando se suelte el archivo
        new DropTarget(vistaCifrar.getZonaArrastre(), DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    //Se extrae el archivo que se ha arrastrado (único archivo)
                    File archivo = ((List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0);
                    actualizarVistaConArchivo(archivo); // Se actualiza la vista con el archivo
                } catch (Exception e) {
                    e.printStackTrace();
                    vistaCifrar.mostrarMensaje("Error al procesar el archivo");
                }
            }
        });
    }

    //Método para cifrar un archivo seleccionado
   private void cifrarArchivoSeleccionado() {
      //Si no hay ningún archivo seleccionado lanzamos una ventana de error
    if (!archivoEstaSeleccionado) {
        vistaCifrar.mostrarMensaje("Error, no hay ningún archivo seleccionado para cifrar");
        return;
    }

    //Obtenemos la ruta de archivo del campo de ruta
    String rutaArchivo = vistaCifrar.getRutaArchivoSeleccionado().getText();
    //Creamos una instancia de archivo
    File archivo = new File(rutaArchivo);

    //Si el archivo no existe, mostramos una ventana de error
    if (!archivo.exists()) {
        vistaCifrar.mostrarMensaje("Error, el archivo no existe en la ruta especificada");
        return;
    }

    //Creamos instancias del gestor de base de datos de archivos para verificar que el archivo se encuentraen la base de datos
    GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
    Cifrado cifrado = new Cifrado(archivo, gestorBDArchivos); //Creamos una instancia de la clase cifrado
    
  
    
    //Si el archivo existe en la base de datos y su estado es que se encuentra cifrado, mostraremos un mensaje de error
    if(gestorBDArchivos.archivoEstaEnBaseDeDatos(rutaArchivo) && gestorBDArchivos.archivoEstaCifrado(rutaArchivo))
    {
       vistaCifrar.mostrarMensaje("Error, el archivo ya está cifrado, no se puede volver a cifrar");
        return;  
    }else
    {
        try {
        //Creamos una contraseña y obtenemos los hashes del archivo, tanto el de archivo original como el de archivo cifrado
        SecretKey claveSecreta = cifrado.generarClaveAES();
        String claveBase64 = cifrado.obtenerClaveSecretaBase64(claveSecreta);
        String hashArchivoBase = cifrado.obtenerHashArchivo(archivo);

        //Ciframos el archivo junto con la clave secreata creada
        cifrado.cifrarArchivo(archivo, claveSecreta);
        String hashArchivoCifrado = cifrado.obtenerHashArchivo(archivo);

        //Si el archivo ya existía en la base de datos, pero su estado no era cifrado, se actualiza la clave secreta
        if (gestorBDArchivos.archivoEstaEnBaseDeDatos(rutaArchivo)) {
        
            boolean actualizado = gestorBDArchivos.actualizarClaveSecretaPorRuta(rutaArchivo, claveBase64);
            if (actualizado) {
                System.out.println("Clave secreta actualizada correctamente.");
            } else {
                System.out.println("Error al actualizar la clave secreta.");
            }
        } else {
            //Si no existe, se crea una instancia de Archivo y se inserta en la base de datos
            Archivo archivoBD = new Archivo(0, archivo.getAbsolutePath(), (int) archivo.length() / 1024, archivo.getName(), obtenerExtension(archivo), hashArchivoBase, hashArchivoCifrado, claveBase64, true);
            gestorBDArchivos.insertarArchivo(archivoBD);
            System.out.println("Archivo cifrado insertado en la base de datos.");
        }
        //Se registra la acción de cifrado en la base de datos
        registrarAccionCifrado(archivo.getAbsolutePath(), "Cifrado");
        controladorHistorialCriptografico.mostrarHistorialCriptografico();
        vistaCifrar.mostrarMensaje("Archivo cifrado exitosamente: " + rutaArchivo);

    } catch (Exception e) {
        vistaCifrar.mostrarMensaje("Error al cifrar el archivo");
        e.printStackTrace();
    }
    }
    
}

   //Método para registrar la acción de cifrado, mediante la ruta absoluta de un archivo y el tipo de acción, en este caso "cifrado"
    private void registrarAccionCifrado(String rutaAbsoluta, String tipoAccion) {
        //Se obtiene la fecha y hora actual, se obtiene y el id de usuario desde la instancia de sesion y se obtiene el nombre de usuario consultando a la base de datos
        Date fechaActual = obtenerFechaHoraActual();
        int idUsuario = sesionActual.getIdUsuario();
        GestorBDUsuarios gestorBDUsuarios = new GestorBDUsuarios();
        String nombreUsuario = gestorBDUsuarios.obtenerNombreUsuarioPorId(idUsuario);

        //obtenemos el archivo a registrar desde la base de datos
        GestorBDArchivos gestorBDArchivos = new GestorBDArchivos();
        Archivo archivoARegistrar = gestorBDArchivos.obtenerArchivoPorRutaAbsoluta(rutaAbsoluta);

        //Si el archivo a registrar no devuelve ningún valor, es que no se encuentra en la base de datos
        if (archivoARegistrar == null) {
            vistaCifrar.mostrarMensaje("Error, no se encontró el archivo en la base de datos");
            return;
        }

        //Obtenemos el id del archivo, creamos una instancia de accion cifrado-descifrado y la insertamos en la base de datos
        int idArchivo = archivoARegistrar.getIdArchivo();

        AccionCIFDES accionCifDes = new AccionCIFDES(0,idUsuario, nombreUsuario, idArchivo, archivoARegistrar.getNombreArchivo(), fechaActual, tipoAccion);

        GestorBDAccionCIFDES gestorBDAccionCIFDES = new GestorBDAccionCIFDES();
        gestorBDAccionCIFDES.insertarAccionCIDFES(accionCifDes);
        controladorHistorialCriptografico.mostrarHistorialCriptografico();
    }
    
    //Método para obtener la fecha y hora actual
    private Date obtenerFechaHoraActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
