package interfaz_grafica;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Antonio
 */


public class VistaContactos extends JPanel {

    //Atributos de la clase VistaContactos
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private JButton botonAgregarContacto,botonEliminarContacto,botonEditarContacto;


    //Constructor de la clase VistaContactos
    public VistaContactos() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); 

        crearTitulo();            
        crearTabla();             
        crearPanelInferior();      
    }

    //Método para crear el título de la vista que aparece en la parte superior de la misma
    private void crearTitulo() {
        JLabel titleLabel = new JLabel("Contactos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    //Método para crear y configurar la tabla
    private void crearTabla() {
        String[] nombreColumnas = {"Nombre", "Correo", "Teléfono", "IP"};
        modeloTabla = new DefaultTableModel(nombreColumnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaContactos = new JTable(modeloTabla);
        configurarTabla();//configuramos la tabla en otro método

        //Creación de un scrollpane para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaContactos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2));
        add(scrollPane, BorderLayout.CENTER);
    }

    //Método para configurar el aspecto de la tabla
    private void configurarTabla() {
        tablaContactos.setFillsViewportHeight(true);
        tablaContactos.setBackground(new Color(58, 68, 80));
        tablaContactos.setForeground(Color.WHITE);
        tablaContactos.setGridColor(new Color(66, 133, 244));
        tablaContactos.setSelectionBackground(new Color(66, 133, 244));
        tablaContactos.setSelectionForeground(Color.WHITE);

        //Personalizamos los cabeceros de la tabla
        JTableHeader header = tablaContactos.getTableHeader();
        header.setBackground(new Color(45, 52, 54));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tablaContactos.setRowHeight(35);
        tablaContactos.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        for (int i = 0; i < tablaContactos.getColumnCount(); i++) {
            tablaContactos.getColumnModel().getColumn(i).setCellRenderer(renderizadorCeldas);
        }
        tablaContactos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tablaContactos.repaint();
            }
        });
    }
    
    //Método para crear y configurar el panel inferior con los botones
    private void crearPanelInferior() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(45, 52, 54));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));

        crearBotones();  // Crear los botones para agregar, editar y eliminar contactos

        //Añadir los botones al panel inferior
        bottomPanel.add(botonAgregarContacto);
        bottomPanel.add(botonEditarContacto);
        bottomPanel.add(botonEliminarContacto);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Método para crear los botones
    private void crearBotones() {
        botonAgregarContacto = new JButton("Agregar Contacto");
        configurarBoton(botonAgregarContacto);
        
         botonEditarContacto = new JButton("Editar Contacto");
        configurarBoton(botonEditarContacto);

        botonEliminarContacto = new JButton("Eliminar Contacto");
        configurarBoton(botonEliminarContacto);
    }

    //Método para unificar la configuración de los botones
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(66, 133, 244));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(58, 68, 80), 2, true));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 30)); // Mantener el tamaño adecuado
    }

    //Método para obtener el nombre del contacto seleccionado
    public String getNombreContactoSeleccionado() {
        int filaSeleccionada = tablaContactos.getSelectedRow();
        if (filaSeleccionada != -1) {
            return (String) tablaContactos.getValueAt(filaSeleccionada, 0);
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

    //Métodos para acceder a los botones
    public JButton getBotonAgregarContacto() {
        return botonAgregarContacto;
    }

    public JButton getBotonEditarContacto() {
        return botonEditarContacto;
    }

    public JButton getBotonEliminarContacto() {
        return botonEliminarContacto;
    }

    // Método para obtener el modelo de la tabla
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Método para obtener la tabla de contactos
    public JTable getTablaContactos() {
        return tablaContactos;
    }
}
