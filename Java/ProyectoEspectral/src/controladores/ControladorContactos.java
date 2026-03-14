
package controladores;

import entidades.Asociacion;
import entidades.Contacto;
import entidades.Sesion;
import entidades.Usuario;
import logica_bd.GestorBDAsociacion;
import logica_bd.GestorBDContactos;
import logica_bd.GestorBDUsuarios;
import interfaz_grafica.VistaAgregarContacto;
import interfaz_grafica.VistaContactos;
import interfaz_grafica.VistaEditarContacto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Antonio
 */

public class ControladorContactos {
    private Sesion sesionActual;
    private GestorBDAsociacion gestorBDAsociacion;
    private GestorBDContactos gestorBDContactos;
    private VistaContactos vistaContactos;

    //Constructor de la clase controlador Contactos
    public ControladorContactos(Sesion sesionActual, VistaContactos vistaContactos) {
        this.sesionActual = sesionActual;
        this.gestorBDAsociacion = new GestorBDAsociacion();
        this.gestorBDContactos = new GestorBDContactos();
        this.vistaContactos = vistaContactos;

        //Se asignan los listeners a los botones y se muestran los contactos disponibles
        asignarListeners();

        mostrarContactos();
    }

    //Método para asignar los listeners correspondientes a cada uno de los botones
    private void asignarListeners() {

        this.vistaContactos.getBotonAgregarContacto().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarAgregarContacto();
            }
        });

        //Eliminamos los listeners al cargar la vista por si se hubiera quedado alguno en escucha
        for (ActionListener al : vistaContactos.getBotonEditarContacto().getActionListeners()) {
            vistaContactos.getBotonEditarContacto().removeActionListener(al);
        }

        this.vistaContactos.getBotonEditarContacto().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarContactoSeleccionado();
            }
        });

        //Eliminamos los listeners al cargar la vista por si se hubiera quedado alguno en escucha
        for (ActionListener al : vistaContactos.getBotonEliminarContacto().getActionListeners()) {
            vistaContactos.getBotonEliminarContacto().removeActionListener(al);
        }

        this.vistaContactos.getBotonEliminarContacto().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarContactoSeleccionado();
            }
        });

    }

    //Método para mostrar los contactos disponibles
    public void mostrarContactos() {
        //Obtenemos el id de usuario desde la sesión actual
        int idUsuario = sesionActual.getIdUsuario();
        //Obtenemos un ArrayList del total de asociaciones disponibles para ese usuario en concreto desde la base de datos
        ArrayList<Asociacion> asociaciones = gestorBDAsociacion.listarAsociacionesPorIdUsuario(idUsuario);

        //Creamos un array bidimensional e introducimos los datos del total de contactos obtenidos en las asociacioens
        String[][] datos = new String[asociaciones.size()][4];
        for (int i = 0; i < asociaciones.size(); i++) {
            int idContacto = asociaciones.get(i).getIdContactoAsociado();
            datos[i][0] = gestorBDContactos.obtenerNombrePorId(idContacto);
            datos[i][1] = gestorBDContactos.obtenerCorreoPorId(idContacto);
            datos[i][2] = gestorBDContactos.obtenerTelefonoPorId(idContacto);
            datos[i][3] = gestorBDContactos.obtenerIpPorId(idContacto);
        }

        //Actualizamos la tabla con los datos obtenidos
        actualizarTabla(datos);
    }

    //Método para actualizar la tabla
    private void actualizarTabla(String[][] datos) {
        
        DefaultTableModel tablaContactos = vistaContactos.getModeloTabla();//Se obtiene el modelo de la tabla 
        tablaContactos.setRowCount(0); ////Se resetea el número de filas, mejor dicho se elimina

        //cada fila representa un array unidimensional dentro del array bidimensional que se le pasa por parámetros
        for (String[] fila : datos) {
            boolean estaVacia = true;
            for (String celda : fila) {//Dentro del array unidimensional revisamos cada uno de los datos, que corresponderán a cada una de las celdas
                if (celda != null && !celda.toString().trim().isEmpty()) {//Si la celda no es nula ni está vacía, se marca como no vacía 
                    estaVacia = false;
                    break;
                }
            }
            if (!estaVacia) {
                tablaContactos.addRow(fila);//se añade la fila
            }
        }

        tablaContactos.fireTableDataChanged();//Se informa a la tabla de que los datos han cambiado
    }
 

    //Método para limpiar la selección
    private void limpiarSeleccion() {
        vistaContactos.getTablaContactos().clearSelection();
    }

    //Método para mostrar los contactos disponibles
    private void mostrarVistaContactos() {
        mostrarContactos();
        vistaContactos.setVisible(true);
    }

    //Método para mostrar la ventana de agregar contacto, se crea una instancia de la vista y de su controlador
    private void mostrarAgregarContacto() {
        VistaAgregarContacto agregarVista = new VistaAgregarContacto();
        ControladorAgregarContacto controladorAgregar = new ControladorAgregarContacto(sesionActual, agregarVista, vistaContactos);
        agregarVista.mostrar();
    }

    //Método para mostrar la ventana de editar contacto, se crea una instancia de la vista y de su controlador, se le pasa un contacto por parámetro
    private void editarContacto(Contacto contacto) {
        VistaEditarContacto editarVista = new VistaEditarContacto();
        ControladorEditarContacto controladorEditar = new ControladorEditarContacto(sesionActual, editarVista, vistaContactos);
        editarVista.mostrar();
    }

    //Método para poder seleccionar un contacto y editarlo
    private void editarContactoSeleccionado() {
        //Se obtiene el nombre del contacto
        String nombreContacto = vistaContactos.getNombreContactoSeleccionado();

        //Si se pulsa editar sin haber seleccionado un contacto se mostrará un mensaje de aviso
        if (nombreContacto == null) {
            vistaContactos.mostrarMensaje("Atención, seleccione un contacto para editar");
            return;
        }

        //Obtenemos el id de contacto a través del nombre que seleccionamos, es posible hacerlo de esta manera ya que el campo nombre en la base de datos se ha configurado como UNIQUE
        int idContacto = gestorBDContactos.obtenerIdPorNombre(nombreContacto);
        //Obtenemos el contacto a través del id
        Contacto contacto = gestorBDContactos.obtenerContactoPorId(idContacto);

        //Creamos la opciones si y no
        String[] opciones = {"Sí", "No"};

        //Mostramos la ventana para poder confirmar o no
        int respuesta = JOptionPane.showOptionDialog(
            null,
            "¿Está seguro de editar el contacto con nombre: " + nombreContacto + "?",
            "Confirmar edición",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[1] //valor por defecto
        );

        //Si el usuario pulsa si, se procede a editar el contacto
        if (respuesta == 0) {
            editarContacto(contacto);
        }
    }

    //Método para eliminar el contacto seleccionado
    private void eliminarContactoSeleccionado() {
        //Se obtiene el nombre del contacto seleccionado
        String nombreContacto = vistaContactos.getNombreContactoSeleccionado();

        //Si no se ha seleccionado ningún contacto, se muestra un mensaje de aviso 
        if (nombreContacto == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un contacto para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = {"Sí", "No"};

        int respuesta = JOptionPane.showOptionDialog(
            null,
            "¿Está seguro de eliminar el contacto con nombre: " + nombreContacto + "?",
            "Confirmar eliminación",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[1]
        );

        //Si se selecciona si 
        if (respuesta == 0) {
            //Obtenemos el id de contacto 
            int idContacto = gestorBDContactos.obtenerIdPorNombre(nombreContacto);

            //Creamos una instancia del gestor de base de datos de usuarios
            GestorBDUsuarios gestorBDUsuarios = new GestorBDUsuarios();
            ArrayList<Usuario> listaUsuarios = new ArrayList<>(); //obtenemos un array list con el total de usuarios
            listaUsuarios = gestorBDUsuarios.listarUsuarios(); //listamos todos los usuarios
            /*Se accede al método para poder borrar el contacto de la tabla, si el usuario es el único que tiene ese contacto se borrará de la tabla, si hay más usuarios que lo 
            tengan agregado se procede a borrar solo la asociación
            */
            boolean sePuedeBorrarContactoDeTabla = borrarContactoTabla(listaUsuarios, idContacto, gestorBDAsociacion);

            if (sePuedeBorrarContactoDeTabla) {
                gestorBDContactos.borrarContactoPorId(idContacto);
                vistaContactos.mostrarMensaje("Contacto eliminado correctamente");
            }

            boolean contactoEliminado;
            contactoEliminado = gestorBDAsociacion.borrarAsociacionPorUsuarioYContacto(sesionActual.getIdUsuario(), idContacto);

            //Si el contacto se ha eliminado correctamente se muestra un mensaje por pantalla y se procede a mostrar la vistaContactos de nuevo
            if (contactoEliminado) {
                
                vistaContactos.getTablaContactos().getSelectionModel().setValueIsAdjusting(true); //Se desactiva la selección de manera momentánea
                limpiarSeleccion();
                mostrarVistaContactos();
                vistaContactos.getTablaContactos().getSelectionModel().setValueIsAdjusting(false); //Se vuelve a poder seleccionar
            } else {
                vistaContactos.mostrarMensaje("Error al eliminar el contacto");
            }
        }
    }

    /*hacemos un método para comprobar si este contacto solo tiene relación con un usuario o tiene con más usuarios
    en el caso de que sea el último usuario con el que tiene relación lo eliminaremos de la tabla contactos, en caso de que no, permanecerá en ella,
    pues todavía podría ser contacto de algún usuario.
    */
    private boolean borrarContactoTabla(ArrayList<Usuario> usuarios, int idContacto, GestorBDAsociacion gestorBDAsociacion) {
        int contadorRelacion = 0;

        for (Usuario usuario : usuarios) {
            int idUsuario = usuario.getIdUsuario();
            if (gestorBDAsociacion.existeRelacionPorUsuarioYContacto(idUsuario, idContacto)) {
                contadorRelacion++;
                if (contadorRelacion > 1) {
                    return false;
                }
            }
        }

        return contadorRelacion == 1;
    }
}
