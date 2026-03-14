

package interfaz_grafica;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Antonio
 */


public class VistaHistorialCriptografico extends JPanel {

    private JTable tablaCripto;
    private DefaultTableModel modeloTabla;
    private JButton botonLimpiarHistorial; 

    public VistaHistorialCriptografico() {
        configurarVista(); 
        crearComponentesGráficos();
    }

    //Método para organizar la estructura de la vista elegir el color de fondo
    private void configurarVista() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); 
    }

    // Método para crear los componentes de la vista
    private void crearComponentesGráficos() {
        crearTitulo();         
        crearTabla();         
        crearPanelInferior(); 
    }

    //Método para crear el título de la vista que aparece en la parte superior de ella
    private void crearTitulo() {
        JLabel titleLabel = new JLabel("Historial Criptográfico", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    //Método para crear la tabla de la vista
    private void crearTabla() {
        String[] nombreColumnas = {"Nombre Usuario", "Nombre de Archivo", "Tipo de Acción", "Fecha"};
        modeloTabla = new DefaultTableModel(nombreColumnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { //configuramos que la celda no sea editable
                return false;
            }
        };
        tablaCripto = new JTable(modeloTabla);

        // Configurar los estilos de la tabla
        configurarTablaEstilos();

        // Agregar la tabla dentro de un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tablaCripto);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2));
        add(scrollPane, BorderLayout.CENTER);
    }

    //Método para configurar la tabla en cuanto a aspecto estético
    private void configurarTablaEstilos() {
        tablaCripto.setFillsViewportHeight(true);
        tablaCripto.setBackground(new Color(58, 68, 80));  // Fondo gris más oscuro para la tabla
        tablaCripto.setForeground(Color.WHITE);
        tablaCripto.setGridColor(new Color(66, 133, 244));
        tablaCripto.setSelectionBackground(new Color(66, 133, 244));
        tablaCripto.setSelectionForeground(Color.WHITE);

        //Configuramos los cabeceros de la tabla
        JTableHeader header = tablaCripto.getTableHeader();
        header.setBackground(new Color(45, 52, 54));  // Fondo gris oscuro suave
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Configuramos la altura de las filas, fuente
        tablaCripto.setRowHeight(35);
        tablaCripto.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaCripto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Se configura la seleccion para que selecciones una fila entera

        //Se configura un renderizador de celdas personalizado
        DefaultTableCellRenderer renderizadorCeldas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);

                //Cambiamos el color de la celda seleccionada
                if (isSelected || tablaCripto.getSelectedRow() == row) {
                    label.setBackground(new Color(66, 133, 244));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(58, 68, 80)); 
                    label.setForeground(Color.WHITE);
                }

                label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1, true));
                return label;
            }
        };

        //El renderizador de celdas se añade a todas las columnas
        for (int i = 0; i < tablaCripto.getColumnCount(); i++) {
            tablaCripto.getColumnModel().getColumn(i).setCellRenderer(renderizadorCeldas);
        }

        //Se añade un action listener para hacer que la fila se seleccione e
        tablaCripto.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tablaCripto.repaint(); 
            }
        });
    }

    //Método que crea el panel inferior, donde el botón para limpiar el historial
    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(45, 52, 54));
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));

        //Creamos el botón para limpiar el historial y lo añadimos al panel
        botonLimpiarHistorial = new JButton("Limpiar Historial");
        configurarBoton(botonLimpiarHistorial);
        panelInferior.add(botonLimpiarHistorial);

        // Añadir el panel inferior a la vista
        add(panelInferior, BorderLayout.SOUTH);
    }

    //Método para configurar el botón
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(66, 133, 244));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(58, 68, 80), 2, true));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 30)); 
    }

    //Métodos para acceder a los elementos gráficos
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JButton getBotonLimpiarHistorial() {
        return botonLimpiarHistorial;
    }
    
    //Método para mostrar mensajes
    public void mostrarMensaje(String mensaje) {
        if (mensaje.contains("Error")) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } else if (mensaje.contains("Atención")) {
            JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
