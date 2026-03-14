
package controladores;

import entidades.Sesion;
import logica_bd.GestorBDSesiones;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import interfaz_grafica.VistaHistorialSesiones;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *
 * @author Antonio
 */

public class ControladorHistorialSesiones {

    private Sesion sesionActual;
    private VistaHistorialSesiones vistaHistorialSesiones;
    private GestorBDSesiones gestorBdSesiones;

    //Constructor de la clase
    public ControladorHistorialSesiones(Sesion sesionActual, VistaHistorialSesiones vistaHistorialSesiones) {
        this.sesionActual = sesionActual;
        this.vistaHistorialSesiones = vistaHistorialSesiones;
        this.gestorBdSesiones = new GestorBDSesiones();

        //Agregar el listener al botón para eliminar sesiones
        this.vistaHistorialSesiones.getBotonEliminarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarHistorialSesiones();
            }
        });

        //Mostramos el historial de sesiones
        mostrarHistorialSesiones();
    }

    //Método para mostrar el historial de sesiones
    public void mostrarHistorialSesiones() {
        //Se obtiene el id de usuario de la sesión actual y se listan las sesiones por id de usuario
        int idUsuario = sesionActual.getIdUsuario();
        ArrayList<Sesion> sesiones = gestorBdSesiones.listarSesionesPorIdUsuario(idUsuario);

        //Creamos un array para los datos que se mostrarán en la tabla
        String[][] datos = new String[sesiones.size()][3];

        for (int i = 0; i < sesiones.size(); i++) {
            Sesion s = sesiones.get(i);
            datos[i][0] = s.getNombreUsuario();
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaInicioFormateada = dateFormat1.format(s.getFechaInicio());
            datos[i][1] = fechaInicioFormateada;

            //Si la fecha de fin es nulo o si es igual a la de inicio, para las sesiones que aún no han finalizado, se mostrará en la tercera columna sesión en curso
            if (s.getFechaFin() == null || s.getFechaFin().equals(s.getFechaInicio())) {
                datos[i][2] = "En curso";
            } else { //En caso contrario se mostrará la fecha de fin 
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaFinFormateada = dateFormat2.format(s.getFechaFin());
                datos[i][2] = fechaFinFormateada;
            }
        }

        //Actualizamos la tabla
        actualizarTabla(datos);
    }
    
    //Método para eliminar el historial de sesiones, eliminaremos todas excepto la en curso, para no perder su registro
    private void eliminarHistorialSesiones() {
        String[] opciones = {"Sí", "No"};
        int confirmacion = JOptionPane.showOptionDialog(
                null,
                "¿Está seguro de eliminar todas las sesiones excepto la actual?",
                "Confirmar eliminación",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]
        );

        //Si se selecciona que si, se obtienen las sesiones por id usuario
        if (confirmacion == 0) {
            ArrayList<Sesion> sesiones = gestorBdSesiones.listarSesionesPorIdUsuario(sesionActual.getIdUsuario());
            //Por cada sesión dentro del ArrayList obtenido, si la fecha de fin de sesión no es nula y no es igual a la fecha de inicio, se elimina de la lista
            for (Sesion sesion : sesiones) {
                if (sesion.getFechaFin() != null && !sesion.getFechaFin().equals(sesion.getFechaInicio())) {
                    gestorBdSesiones.eliminarSesionPorId(sesion.getIdSesion());
                }
            }

            //Se muestra un mensaje para confirmar la eliminación y se refresca el historial 
            vistaHistorialSesiones.mostrarMensaje("Historial de sesiones eliminado, excepto la sesión en curso");
            mostrarHistorialSesiones();
        }
    }

    //Método para actualizar la tabla
    private void actualizarTabla(String[][] datos) {
        DefaultTableModel tablaSesiones = vistaHistorialSesiones.getTableModel();//Se obtiene el modelo de la tabla
        tablaSesiones.setRowCount(0);//Se resetea el número de filas, mejor dicho se elimina

        for (String[] fila : datos) {//cada fila representa un array unidimensional dentro del array bidimensional que se le pasa por parámetros
            boolean estaVacia = true;
            for (String celda : fila) {//Dentro del array unidimensional revisamos cada uno de los datos, que corresponderán a cada una de las celdas
                if (celda != null && !celda.trim().isEmpty()) {//Si la celda no es nula ni está vacía, se añade una fila
                    estaVacia = false;
                    break;
                }
            }
            if (!estaVacia) {
                tablaSesiones.addRow(fila);//se añade la fila
            }
        }

        tablaSesiones.fireTableDataChanged();//Se informa a la tabla de que los datos han cambiado
    }
    
}
