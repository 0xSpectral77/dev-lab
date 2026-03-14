

package controladores;

import entidades.AccionEnvioRecp;
import entidades.Sesion;
import interfaz_grafica.VistaHistorialTransferencias;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica_bd.GestorBDAccionEnvioRecp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Antonio
 */
public class ControladorHistorialTransferencias {

    private Sesion sesionActual;
    private VistaHistorialTransferencias vistaHistorialTransferencias;
    private GestorBDAccionEnvioRecp gestorBDAccionEnvioRecp = new GestorBDAccionEnvioRecp();

    //Constructor de la clase
    public ControladorHistorialTransferencias(Sesion sesionActual, VistaHistorialTransferencias vistaHistorialTransferencias) {
        this.sesionActual = sesionActual;
        this.vistaHistorialTransferencias = vistaHistorialTransferencias;
        

        // Agregar listener al botón de eliminar transferencia
        this.vistaHistorialTransferencias.getLimpiarHistorialButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarHistorialTransferencias();
            }
        });


        mostrarHistorialTransferencias();
    }

    //Método para mostrar el historial de envíos recepciones
    public void mostrarHistorialTransferencias() {
        int idUsuario = sesionActual.getIdUsuario(); //Se obtiene el idUsuario de la instancia de la sesión actual
        ArrayList<AccionEnvioRecp> transferencias = gestorBDAccionEnvioRecp.listarAccionesPorIdUsuario(idUsuario); //Se listan las transferencias por idUsuario

        //Se crea un array bidimensional para contener los datos de la tabla, tendrá el tamaño del número de asociaciones de la lista y dentro de cada uno, 5 campos
        String[][] datos = new String[transferencias.size()][5];

        //Se recorre el array para asignar los datos de cada transferencia
        for (int i = 0; i < transferencias.size(); i++) {
            AccionEnvioRecp t = transferencias.get(i);
            datos[i][0] = t.getNombreUsuarioEnvioRecp();
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaTransferenciaFormateada = dateFormat1.format(t.getFecha());
            datos[i][1] = t.getNombreContactoEnvioRecp();
            datos[i][2] = t.getNombreArchivoEnvioRecp();
            datos[i][3] = fechaTransferenciaFormateada;
            datos[i][4] = t.getTipoAccion();
        }

        //Se actualiza la tabla con los datos obtenidos
        actualizarTabla(datos);
    }

    //Método para eliminar el historial completo de transferencias, envío y recepción 
    private void eliminarHistorialTransferencias() {
        String[] opciones = {"Sí", "No"};
        int respuesta = JOptionPane.showOptionDialog(
                null,
                "¿Está seguro de eliminar todas las transferencias?",
                "Confirmar eliminación",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]
        );

        //Si se selecciona que si, se listarán las trasferencias por usuario y se eliminarán todas
        if (respuesta == 0) {
            ArrayList<AccionEnvioRecp> transferencias = gestorBDAccionEnvioRecp.listarAccionesPorIdUsuario(sesionActual.getIdUsuario());
            gestorBDAccionEnvioRecp.eliminarAccionesEnvioRecpPorIdUsuario(sesionActual.getIdUsuario());
            vistaHistorialTransferencias.mostrarMensaje("Historial de transferencias eliminado existosamente");
            mostrarHistorialTransferencias();
        }
    }

    //Método para actualizar la tabla
    private void actualizarTabla(String[][] datos) {
        DefaultTableModel tablaTransferencias = vistaHistorialTransferencias.getTableModel();//Se obtiene el modelo de la tabla desde la vistaHistorialTransferencias
        tablaTransferencias.setRowCount(0); //Se resetea el número de filas, mejor dicho se elimina

        for (String[] fila : datos) {//cada fila representa un array unidimensional dentro del array bidimensional que se le pasa por parámetros
            boolean estaVacia = true;
            for (String celda : fila) { //Dentro del array unidimensional revisamos cada uno de los datos, que corresponderán a cada una de las celdas
                if (celda != null && !celda.trim().isEmpty()) { //Si la celda no es nula ni está vacía, se añade una fila
                    estaVacia = false;
                    break;
                }
            }
            if (!estaVacia) {
                tablaTransferencias.addRow(fila);//se añade la fila
            }
        }

        tablaTransferencias.fireTableDataChanged();//Se informa a la tabla de que los datos han cambiado
    }
}
