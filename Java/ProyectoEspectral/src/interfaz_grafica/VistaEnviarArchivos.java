
package interfaz_grafica;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.JTableHeader;


/**
 *
 * @author Antonio
 */


public class VistaEnviarArchivos extends JPanel {

    //Atributos de la clase VistaEnviarArchivos correspondientes a cada uno de los elementos gráficos
    private JTextField rutaArchivoSeleccionado;
    private JComboBox<String> contactosComboBox;
    private JLabel zonaArrastre;
    private JButton botonEnviar, botonLimpiar, botonBuscar;
    private JTable tablaArchivos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;

    //Constructor de la clase VistaEnviarArchivos
    public VistaEnviarArchivos() {
        configurarPanel();             
        inicializarComponentes();      
        agregarComponentes();          
    }

    //Método para configurar el panel principal
    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54));
    }

    //Método para inicializar cada uno de los componentes que aparecen en la vista
    private void inicializarComponentes() {
        rutaArchivoSeleccionado = crearCampoRutaArchivo();
        contactosComboBox = new JComboBox<>();
        zonaArrastre = crearZonaArrastre();
        botonEnviar = crearBoton("Enviar");
        botonLimpiar = crearBoton("Limpiar");
        botonBuscar = crearBoton("Buscar Archivo");

        crearTablaArchivos();
    }

    //Método para agregar cada uno de los paneles creados, con sus respectivos componentes, al panel principal
    private void agregarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(new Color(45, 52, 54));

        panelPrincipal.add(crearPanelBusqueda());
        panelPrincipal.add(crearPanelArrastre());
        panelPrincipal.add(scrollPane);
        panelPrincipal.add(crearPanelContactos());
        panelPrincipal.add(crearPanelBotones());

        add(crearTitulo("Enviar Archivos"), BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
    }

    //Método para crear el campo que mostrará la ruta absoluta del archivo seleccionado
    private JTextField crearCampoRutaArchivo() {
        JTextField campo = new JTextField(30);
        campo.setFont(new Font("Arial", Font.PLAIN, 16));
        campo.setBackground(new Color(58, 68, 80));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return campo;
    }

    //Método para crear el título mostrado en la parte superior de la vista
    private JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(45, 52, 54));
        label.setPreferredSize(new Dimension(getWidth(), 50));
        return label;
    }

    //Método para crear botones de enviar archivo, buscar archivo y limpiar archivo
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(new Color(66, 133, 244));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(58, 68, 80), 2, true));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(200, 40));
        return boton;
    }

    //Método para crear la zona habilitada para poder arrastrar archivos
    private JLabel crearZonaArrastre() {
        JLabel label = new JLabel("Arrastra y suelta un archivo aquí", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(58, 68, 80));
        label.setPreferredSize(new Dimension(600, 150));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        return label;
    }

    //Método para crear la tabla que albergará la información de los archivos seleccionados
    private void crearTablaArchivos() {
        String[] columnas = {"Nombre de archivo", "Tamaño (KB)", "Extensión"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaArchivos = new JTable(modeloTabla);
        configurarEstiloTabla();

        scrollPane = new JScrollPane(tablaArchivos);
        scrollPane.setPreferredSize(new Dimension(600, 150));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2));
    }

    // Método para configurar el estilo de la tabla
    private void configurarEstiloTabla() {
        tablaArchivos.setFillsViewportHeight(true);
        tablaArchivos.setBackground(new Color(58, 68, 80));
        tablaArchivos.setForeground(Color.WHITE);
        tablaArchivos.setGridColor(new Color(66, 133, 244));
        tablaArchivos.setSelectionBackground(new Color(66, 133, 244));
        tablaArchivos.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaArchivos.getTableHeader();
        header.setBackground(new Color(45, 52, 54));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /*
        Usamos un cell renderer para darle a las celdas que muestran la información del archivo la apariencia que queremos:
        -Texto centrado
        -Colo de letra blanco
        -Fondo igual al de la tabla
        -Configurar el comportamiento cuando una celda está seleccionada para que cambie a color azul
        -Le añadimos un borde azul a las celdas
        */
        DefaultTableCellRenderer renderizadorCeldas = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

           
            Color fondo;
            if (isSelected) {
                fondo = new Color(66, 133, 244);
            } else {
                fondo = new Color(58, 68, 80);
            }
            label.setBackground(fondo);

            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1, true));

            return label;
           }
        };

        for (int i = 0; i < tablaArchivos.getColumnCount(); i++) {
            tablaArchivos.getColumnModel().getColumn(i).setCellRenderer(renderizadorCeldas);
          }
        }
 
    //Método para crear el panel de búsqueda de archivos
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBackground(new Color(45, 52, 54));
        panel.add(rutaArchivoSeleccionado);
        panel.add(botonBuscar);
        return panel;
    }

    //Método para crear el panel correspondiente al área para arrastrar archivos
    private JPanel crearPanelArrastre() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 52, 54));
        panel.add(zonaArrastre, BorderLayout.CENTER);
        return panel;
    }

    //Método para crear el panel correspondiente al combo box para seleccionar contacto al que enviar archivo
    private JPanel crearPanelContactos() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 52, 54));

        JLabel contactoLabel = new JLabel("Seleccionar contacto:", JLabel.CENTER);
        contactoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        contactoLabel.setForeground(Color.WHITE);
        panel.add(contactoLabel);

        contactosComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        contactosComboBox.setBackground(new Color(58, 68, 80));
        contactosComboBox.setForeground(Color.WHITE);
        panel.add(contactosComboBox);

        return panel;
    }

    //Método para crear el panel que albergará los botones
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 52, 54));
        panel.add(botonEnviar);
        panel.add(botonLimpiar);
        return panel;
    }

    //Métodos para acceder a elementos gráficos
    public JTextField getRutaArchivoSeleccionado() {
        return rutaArchivoSeleccionado;
    }

    public JComboBox<String> getContactosComboBox() {
        return contactosComboBox;
    }

    public JButton getBotonEnviar() {
        return botonEnviar;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JButton getBotonBuscar() {
        return botonBuscar;
    }

    public JLabel getZonaArrastre() {
        return zonaArrastre;
    }

    public JTable getTablaArchivos() {
        return tablaArchivos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Método para mostrar mensajes
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