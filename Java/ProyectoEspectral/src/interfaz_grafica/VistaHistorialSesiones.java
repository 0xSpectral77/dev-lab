

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


public class VistaHistorialSesiones extends JPanel {

    //Atributos clase VistalHistorial Sesiones
    private JTable tablaSesiones;
    private DefaultTableModel modeloTabla;
    private JButton botonEliminarSesion;

    //Constructor VistaHistorialSesiones
    public VistaHistorialSesiones() {
        configurarVista();
        crearComponentesGraficos();
    }

    //Método para configurar el layout y el colo de fondo de la vista
    private void configurarVista() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54));
    }

    //Método para crear todos los componentes gráficos
    private void crearComponentesGraficos() {
        crearTitulo();
        crearTabla();
        crearPanelInferior();
    }

    //Método que crea el título de la vista que se mostrará en la parte superior de la misma
    private void crearTitulo() {
        JLabel titleLabel = new JLabel("Historial de Sesiones", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    //Método para crear la tabla
    private void crearTabla() {
        String[] columnas = {"Nombre Usuario", "Fecha y Hora Inicio", "Fecha y Hora Fin"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaSesiones = new JTable(modeloTabla);

        configurarTablaEstilos();

        JScrollPane scrollPane = new JScrollPane(tablaSesiones);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2));
        add(scrollPane, BorderLayout.CENTER);
    }

    //Método para configurar la tabla estéticamente
    private void configurarTablaEstilos() {
        tablaSesiones.setFillsViewportHeight(true);
        tablaSesiones.setBackground(new Color(58, 68, 80));
        tablaSesiones.setForeground(Color.WHITE);
        tablaSesiones.setGridColor(new Color(66, 133, 244));
        tablaSesiones.setSelectionBackground(new Color(66, 133, 244));
        tablaSesiones.setSelectionForeground(Color.WHITE);
        tablaSesiones.setRowHeight(35);
        tablaSesiones.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSesiones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Configurar los cabeceros de la tabla
        JTableHeader header = tablaSesiones.getTableHeader();
        header.setBackground(new Color(45, 52, 54));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Renderización personalizada de celdas
        DefaultTableCellRenderer renderizadorCeldas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                //texto centrado en las celdas
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);

                //Si la celda está seleccionada se cambia de color
                if (isSelected) {
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
        
       

        for (int i = 0; i < tablaSesiones.getColumnCount(); i++) {
            tablaSesiones.getColumnModel().getColumn(i).setCellRenderer(renderizadorCeldas);
        }
    }

    //Método para crear el panel inferior con el botón para eliminar el historial de sesiones
    private void crearPanelInferior() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(45, 52, 54));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));

        botonEliminarSesion= new JButton("Limpiar Historial");
        configurarBoton(botonEliminarSesion);
        bottomPanel.add(botonEliminarSesion);

        add(bottomPanel, BorderLayout.SOUTH);
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

    // Métodos de acceso público
    public DefaultTableModel getTableModel() {
        return modeloTabla;
    }

    
    public JTable getTablaSesiones() {
        return tablaSesiones;
    }

    public JButton getBotonEliminarSesion() {
        return botonEliminarSesion;
    }

    public String getFechaInicioSeleccionada() {
        int filaSeleccionada = tablaSesiones.getSelectedRow();
        if (filaSeleccionada != -1) {
            return (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        }
        return null;
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
