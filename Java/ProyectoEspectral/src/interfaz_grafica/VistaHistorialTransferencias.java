
package interfaz_grafica;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Antonio
 */
 
public class VistaHistorialTransferencias extends JPanel {

    private JTable transferenciasTable;
    private DefaultTableModel tableModel;
    private JButton limpiarHistorialButton; 

    //Constructor de la clase
    public VistaHistorialTransferencias() {
        configurarVista(); 
        crearComponentesGráficos();
    }

    //Método para organizar la estructura de la vista y elegir el color de fondo
    private void configurarVista() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); // Gris oscuro suave, igual al fondo de VistaHistorialCriptografico
    }

    // Método para crear los componentes gráficos
    private void crearComponentesGráficos() {
        crearTitulo();         
        crearTabla();         
        crearPanelInferior(); 
    }

    //Método para crear el título de la vista que aparece en la parte superior de ella
    private void crearTitulo() {
        JLabel titleLabel = new JLabel("Historial de Transferencias", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente similar a VistaHistorialCriptografico
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Márgenes adicionales
        add(titleLabel, BorderLayout.NORTH);
    }

    //Método para crear la tabla de la vista
    private void crearTabla() {
        String[] nombreColumnas = {"Nombre Usuario", "Contacto", "Nombre de Archivo", "Fecha", "Tipo de Acción"};
        tableModel = new DefaultTableModel(nombreColumnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  //No se permite la edición en ninguna celda
            }
        };
        transferenciasTable = new JTable(tableModel);

        // Configurar los estilos de la tabla
        configurarTablaEstilos();

        // Agregar la tabla dentro de un JScrollPane
        JScrollPane scrollPane = new JScrollPane(transferenciasTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2)); // Bordes del JScrollPane
        add(scrollPane, BorderLayout.CENTER);
    }

    //Método para configurar la tabla en cuanto a aspecto estético
    private void configurarTablaEstilos() {
        transferenciasTable.setFillsViewportHeight(true);
        transferenciasTable.setBackground(new Color(58, 68, 80)); // Fondo gris más oscuro para la tabla
        transferenciasTable.setForeground(Color.WHITE);
        transferenciasTable.setGridColor(new Color(66, 133, 244)); // Color de las líneas de la tabla
        transferenciasTable.setSelectionBackground(new Color(66, 133, 244)); // Color de fondo para las filas seleccionadas
        transferenciasTable.setSelectionForeground(Color.WHITE); // Color de texto para las filas seleccionadas

       //Encabezados personalizados de la tabla
        JTableHeader header = transferenciasTable.getTableHeader();
        header.setBackground(new Color(45, 52, 54)); 
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Configuración adicional de la tabla, selección de celfas, alto de fila, fuente y modo de selección única
        transferenciasTable.setCellSelectionEnabled(true);
        transferenciasTable.setRowHeight(35);
        transferenciasTable.setFont(new Font("Arial", Font.PLAIN, 14));
        transferenciasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Aplicamos el renderizador de celdas para centrar el texto y aplicar estilos a las celdas
        DefaultTableCellRenderer renderizadorCeldas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                //Texto centrado en horizontal y en vertical
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);

                //El fondo de la celda cambia si está seleccionada o no
                if (isSelected) {
                    label.setBackground(new Color(66, 133, 244));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(58, 68, 80));
                    label.setForeground(Color.WHITE);
                }

                //Bordes de las celdas
                label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1, true)); 

                return label;
            }
        };

        //Asignar el renderizador a cada columna
        for (int i = 0; i < transferenciasTable.getColumnCount(); i++) {
            transferenciasTable.getColumnModel().getColumn(i).setCellRenderer(renderizadorCeldas);
        }
    }

    //Método que crea el panel inferior, donde está el botón para limpiar el historial
    private void crearPanelInferior() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(45, 52, 54)); 
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20)); 

        limpiarHistorialButton = new JButton("Limpiar Historial");
        configurarBoton(limpiarHistorialButton); 
        bottomPanel.add(limpiarHistorialButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Método auxiliar para configurar el botón
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(66, 133, 244));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(58, 68, 80), 2, true));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 30)); // Ajustar tamaño adecuado para el botón
    }

    //Métodos para acceder a los elementos gráficos
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getLimpiarHistorialButton() {
        return limpiarHistorialButton;
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
