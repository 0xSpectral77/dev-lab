
package interfaz_grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *
 * @author Antonio
 */

public class VistaPrincipal {
    //Atributos de la clase VistaPrincipal
    private JFrame frame;
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;
    private CardLayout cardLayout;
    private JButton botonSeleccionado;
    
    private JButton btnCifrar;
    private JButton btnDescifrar;
    private JButton btnHistorialCifrados;
    private JButton btnEnviar;
    private JButton btnRecibir;
    private JButton btnHistorialTransferencias;
    private JButton btnContactos;
    private JButton btnHistorialSesiones;
    private JButton btnEditarPerfil;
    private JButton btnCerrarSesion;

    //Método constructor de la VistaPrincipal
    public VistaPrincipal() {
        crearVentana();                
        JPanel mainPanel = configurarPanelPrincipal(); 
        panelIzquierdo = configurarPanelIzquierdo();  
        panelDerecho = configurarPanelDerecho();      
        agregarBotonesMenu();          
        configurarVistaInicial();       
        agregarComponentesAlPanel(mainPanel);  
    }

    // Método para crear la ventana principal
    private void crearVentana() {
        frame = new JFrame("Espectral - Menú Principal");
        frame.setSize(850, 850);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
       
    }

    //Método para configurar el panel principal con un diseño de BorderLayout
    private JPanel configurarPanelPrincipal() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    //Método para configurar el panel donde se encuentran los botones correspondientes a las diferentes opciones, que está situado a la izqueirda
    private JPanel configurarPanelIzquierdo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 40, 49));
        return panel;
    }

    //Método para configurar el panel que contendrá la vistas en la parte derecha, para ello se utilizará un CardLayout
    private JPanel configurarPanelDerecho() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        return panel;
    }

    //Método para crear y agregar al panel izquierdo cada uno de los botones para acceder a las diferentes vistas 
    private void agregarBotonesMenu() {
        
        btnCifrar = crearBotonMenu("Cifrar Archivos");
        btnDescifrar = crearBotonMenu("Descifrar Archivos");
        btnHistorialCifrados = crearBotonMenu("Historial Criptográfico");
        btnEnviar = crearBotonMenu("Enviar Archivos");
        btnRecibir = crearBotonMenu("Recibir Archivos");
        btnHistorialTransferencias = crearBotonMenu("Historial de Transferencias");
        btnContactos = crearBotonMenu("Contactos");
        btnHistorialSesiones = crearBotonMenu("Historial de Sesiones");
        btnEditarPerfil = crearBotonMenu ("Editar Perfil");
        btnCerrarSesion = crearBotonMenu("Cerrar Sesión");

        //Configuramos el GridBagConstraints para hacer que los componentes se adapten al tamaño de la interfaz
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; //en este caso se expandirá o contraerá horizontalmente
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;

        //Creamos un Array JButton para almacenar todos los botones
        JButton[] botones = {
            btnCifrar, btnDescifrar, btnHistorialCifrados, btnEnviar, btnRecibir,
            btnHistorialTransferencias, btnContactos, btnHistorialSesiones, btnEditarPerfil, btnCerrarSesion
        };

        /*
        Por cada botón contenido en el array, lo añadimos al panel izquierdo y le añadimos un ActionListener a cada 
        uno, le asignamos la función para cambiar el Botón seleccionado, desarrollada más abajo.
        */
        for (int i = 0; i < botones.length; i++) {
            gbc.gridy = i;
            panelIzquierdo.add(botones[i], gbc);

        botones[i].addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cambiarBotonSeleccionado((JButton) e.getSource()); // Cambiar el color cuando se hace clic
        }
    });
        }
    }

    //Método para crear cada uno de los botones del panel izquierdo
    private JButton crearBotonMenu(String texto) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(58, 68, 80));
        button.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setOpaque(true);
        button.setBorderPainted(true);

        /*
        Método para implementar un Mouse Listener que se activa cuando el usuario pasa el ratón por encima de
        un botón. Es necesario configurar el color cuando el mouse está dentro del botón y cuando está fuera,
        para que vuelva a su color original
        */
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(66, 133, 244));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != botonSeleccionado) {
                    button.setBackground(new Color(58, 68, 80));
                }
            }
        });

        return button;
    }

    //Método para configurar la vista que se mostrará la primera vez que se acceda a la vista principal, que será la vista para cifrar archivos
    private void configurarVistaInicial() {
        cardLayout.show(panelDerecho, "Cifrar Archivos");
        botonSeleccionado = btnCifrar;
        botonSeleccionado.setBackground(new Color(66, 133, 244));
    }

    //Método para agregar tanto los paneles izquierdo (el de los botones), como el panel derecho (el de las vistas) al panel principal.
    private void agregarComponentesAlPanel(JPanel mainPanel) {
        mainPanel.add(panelIzquierdo, BorderLayout.WEST);
        mainPanel.add(panelDerecho, BorderLayout.CENTER);
        frame.add(mainPanel);
    }

    //Método para cambiar de botón seleccionado
    private void cambiarBotonSeleccionado(JButton button) {
        // Cambiar el color de fondo del botón previamente seleccionado (si hay uno)
        if (botonSeleccionado != null) {
            botonSeleccionado.setBackground(new Color(58, 68, 80));//Color en estado normal
        }

        // Establecer el nuevo botón como seleccionado
        botonSeleccionado = button;
        botonSeleccionado.setBackground(new Color(66, 133, 244)); //Color que tiene el botón cuando está seleccionado
    }

    //Método para mostrar la ventana
    public void mostrar() {
        frame.setVisible(true);
    }

    //Método para cerrar la ventana
    public void cerrar() {
        frame.setVisible(false);
    }

    //Métodos para acceder a los elementos gráficos
    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanelIzquierdo() {
        return panelIzquierdo;
    }

    public JPanel getPanelDerecho() {
        return panelDerecho;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JButton getBtnCifrar() {
        return btnCifrar;
    }

    public JButton getBtnDescifrar() {
        return btnDescifrar;
    }

    public JButton getBtnHistorialCifrados() {
        return btnHistorialCifrados;
    }

    public JButton getBtnEnviar() {
        return btnEnviar;
    }

    public JButton getBtnRecibir() {
        return btnRecibir;
    }

    public JButton getBtnHistorialTransferencias() {
        return btnHistorialTransferencias;
    }

    public JButton getBtnContactos() {
        return btnContactos;
    }

    public JButton getBtnHistorialSesiones() {
        return btnHistorialSesiones;
    }

    public JButton getBtnEditarPerfil() {
        return btnEditarPerfil;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }
}
