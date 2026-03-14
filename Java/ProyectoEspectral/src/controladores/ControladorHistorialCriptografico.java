
package controladores;

import entidades.AccionCIFDES;
import entidades.Sesion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import interfaz_grafica.VistaHistorialCriptografico;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica_bd.GestorBDAccionCIFDES;


/**
 *
 * @author Antonio
 */

public class ControladorHistorialCriptografico {

    private Sesion sesionActual;
    private VistaHistorialCriptografico vistaHistorialCriptografico;
    private GestorBDAccionCIFDES gestorBdAcciones;

    //Constructor de la clase
    public ControladorHistorialCriptografico(Sesion sesionActual, VistaHistorialCriptografico vistaHistorialCriptografico) {
        this.sesionActual = sesionActual;
        this.vistaHistorialCriptografico = vistaHistorialCriptografico;
        this.gestorBdAcciones = new GestorBDAccionCIFDES();

        //Agregamos el listener al botón para poder eliminar el historial
        asignarListener();
        mostrarHistorialCriptografico();
    }

    //Método para asignar el listener al botón de eliminar historial
    private void asignarListener() {
        vistaHistorialCriptografico.getBotonLimpiarHistorial().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarHistorialCriptografico();
            }
        });
    }

    //Método para  poder mostrar el historial de cifrados y descifrados
    public void mostrarHistorialCriptografico() {
        //Obtenemos el id de usuario de la sesión actual
        int idUsuario = sesionActual.getIdUsuario();

        //Listamos las acciones de cifrado o descifrado que existen para este usuario
        ArrayList<AccionCIFDES> acciones = gestorBdAcciones.listarAccionesPorUsuario(idUsuario);

        //Número de acciones obtenidas
        System.out.println("Número de acciones obtenidas: " + acciones.size());

        if (acciones.isEmpty()) {
            System.out.println("No se encontraron acciones para el usuario.");
        }

        //Se crea un array para los datos de la tabla
        String[][] datos = new String[acciones.size()][4];

        //Obtenemos los datos
        for (int i = 0; i < acciones.size(); i++) {
            AccionCIFDES a = acciones.get(i);
            datos[i][0] = a.getNombreUsuarioAccion();
            datos[i][1] = a.getNombreArchivoAccion();
            datos[i][2] = a.getTipoAccion();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            datos[i][3] = dateFormat.format(a.getFecha()); //Se formatea la fecha y la hora
        }

        //Se actualiza la tabla
        actualizarTabla(datos);
    }

    //Método para eliminar el historial
    private void eliminarHistorialCriptografico() {
        String[] opciones = {"Sí", "No"};
        int respuesta = JOptionPane.showOptionDialog(
                null,
                "¿Está seguro de eliminar todas acciones de cifrado-descifrado?",
                "Confirmar eliminación",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]
        );

        //Si se selecciona que si, se obtiene el id de usuario y se cuenta si los cifrados son iguales a los decifrados, solo entonces se podrá borrar
        if (respuesta == 0) {
            int idUsuario = sesionActual.getIdUsuario();

            int totalCifrados = gestorBdAcciones.contarAcciones(idUsuario, "Cifrado");
            int totalDescifrados = gestorBdAcciones.contarAcciones(idUsuario, "Descifrado");
            System.out.println("Total Cifrados: " + totalCifrados);
            System.out.println("Total Descifrados: " + totalDescifrados);

            if (totalCifrados == totalDescifrados && totalCifrados > 0) {
                gestorBdAcciones.eliminarTodasLasAccionesPorUsuario(idUsuario);
                vistaHistorialCriptografico.mostrarMensaje("Se eliminó todo el historial del usuario");
                System.out.println("Historial eliminado.");
            } else {
                vistaHistorialCriptografico.mostrarMensaje("Error, no se eliminó el historial, ya que los ciclo cifrado-descifrado no está completos");
                System.out.println("El historial no se eliminó.");
            }

            mostrarHistorialCriptografico();
        }
    }

    //Método para actualizar la tabla
    private void actualizarTabla(String[][] datos) {
        DefaultTableModel tablaAcciones = vistaHistorialCriptografico.getModeloTabla();//Se obtiene el modelo de la tabla 
        tablaAcciones.setRowCount(0);//Se resetea el número de filas, mejor dicho se elimina

        for (String[] fila : datos) {//cada fila representa un array unidimensional dentro del array bidimensional que se le pasa por parámetros
            boolean estaVacia = true;
            for (String celda : fila) {//Dentro del array unidimensional revisamos cada uno de los datos, que corresponderán a cada una de las celdas
                if (celda != null && !celda.trim().isEmpty()) {//Si la celda no es nula ni está vacía, se añade una fila
                    estaVacia = false;
                    break;
                }
            }
            if (!estaVacia) {
                tablaAcciones.addRow(fila);//se añade la fila
            }
        }

        tablaAcciones.fireTableDataChanged();//Se informa a la tabla de que los datos han cambiado
    }
    
}
