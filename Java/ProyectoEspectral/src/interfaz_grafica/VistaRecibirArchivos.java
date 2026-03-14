
package interfaz_grafica;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Antonio
 */

public class VistaRecibirArchivos extends JPanel {

    //Atributos vistaRecibirArchivos
    private JButton botonRecibirArchivo;
    private JButton botonCerrarConexion;

    //Constructor VistaRecibirArchivos
    public VistaRecibirArchivos() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54)); // Gris oscuro suave
        crearTitulo();
        crearPanelPrincipal();
    }

    //Método para crear el título que se mostrará en la parte superior de la vista
    private void crearTitulo() {
        JLabel tituloVista = new JLabel("Recibir Archivos", JLabel.CENTER);
        tituloVista.setFont(new Font("Arial", Font.BOLD, 24));
        tituloVista.setForeground(Color.WHITE);
        tituloVista.setPreferredSize(new Dimension(getWidth(), 50));
        tituloVista.setOpaque(true);
        tituloVista.setBackground(new Color(45, 52, 54)); // Fondo igual al de la vista
        add(tituloVista, BorderLayout.NORTH);
    }

    //Método para crear el panel principal con los botones
    private void crearPanelPrincipal() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(45, 52, 54));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        crearBotones();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(botonRecibirArchivo, gbc);

        gbc.gridx = 1; 
        panelPrincipal.add(botonCerrarConexion, gbc);

        
        add(panelPrincipal, BorderLayout.CENTER);
    }

    //Método para crear los botones
    private void crearBotones() {
        botonRecibirArchivo = new JButton("Recibir Archivo");
        configurarBoton(botonRecibirArchivo);

        botonCerrarConexion = new JButton("Cerrar Conexión");
        configurarBoton(botonCerrarConexion);
    }

    //
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 16)); // Aumento del tamaño de fuente
        boton.setBackground(new Color(66, 133, 244)); 
        boton.setForeground(Color.WHITE); 
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(58, 68, 80), 2, true));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(200, 40)); // Botón más grande
    }

    //Métodos para acceder a los componentes gráficos
    public JButton getRecibirButton() {
        return botonRecibirArchivo;
    }

    public JButton getDetenerButton() {
        return botonCerrarConexion; 
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
