

package interfaz_grafica;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Antonio
 */  

public class VistaEditarCuenta extends JPanel {
    //Atributos de la clase VistaEditarCuenta
    private JTextField txtUsuario, txtEmail, txtIp, txtTelefono;
    private JPasswordField txtContraseña, txtConfirmarContraseña;
    private JButton botonGuardarCambios;
    private JLabel lblUsuario, lblContraseña, lblConfirmarContraseña, lblEmail, lblIp, lblTelefono;

    //Constructor VistaEditarCuenta
    public VistaEditarCuenta() {
        inicializarPanelPrincipal();
        agregarTitulo();
        crearComponentes();
        construirFormulario();
    }

    //Inicializar el panel principal
    private void inicializarPanelPrincipal() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 52, 54));
    }

    //Método para crear el título y agregarlo panel
    private void agregarTitulo() {
        JLabel titulo = new JLabel("Editar Cuenta", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setPreferredSize(new Dimension(getWidth(), 60));
        add(titulo, BorderLayout.NORTH);
    }

    //Método para crear todos los componentes, campos de texto, etiquetas y botón para guardar los cambios
    private void crearComponentes() {
        txtUsuario = crearCampoTexto();
        txtContraseña = crearCampoContraseña();
        txtConfirmarContraseña = crearCampoContraseña();
        txtEmail = crearCampoTexto();
        txtIp = crearCampoTexto();
        txtTelefono = crearCampoTexto();
        botonGuardarCambios = crearBoton("Guardar Cambios");
        lblUsuario = crearEtiqueta("Usuario", false);
        lblContraseña = crearEtiqueta("Contraseña", false);
        lblConfirmarContraseña = crearEtiqueta("Confirmar Contraseña", false);
        lblEmail = crearEtiqueta("Email", false);
        lblIp = crearEtiqueta("IP", false);
        lblTelefono = crearEtiqueta("Teléfono", false);
    }

    /*
    Método para crear un panel que funcionará como un formulario donde el usuario rellenará los campos 
    para editar los datos de su cuenta, al panel se le agregan los elementos gráficos creados
    */
    private void construirFormulario() {
    JPanel panelFormulario = new JPanel(new GridBagLayout());
    panelFormulario.setBackground(getBackground());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    int y = 0;

   //añadimos cada uno de los elementos gráficos creados anteriormente al panel del formulario
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 1;
    panelFormulario.add(lblUsuario, gbc);

    gbc.gridx = 1;
    panelFormulario.add(txtUsuario, gbc);
    y++;

    gbc.gridx = 0;
    gbc.gridy = y;
    panelFormulario.add(lblContraseña, gbc);

    gbc.gridx = 1;
    panelFormulario.add(txtContraseña, gbc);
    y++;

    gbc.gridx = 0;
    gbc.gridy = y;
    panelFormulario.add(lblConfirmarContraseña, gbc);

    gbc.gridx = 1;
    panelFormulario.add(txtConfirmarContraseña, gbc);
    y++;

    gbc.gridx = 0;
    gbc.gridy = y;
    panelFormulario.add(lblEmail, gbc);

    gbc.gridx = 1;
    panelFormulario.add(txtEmail, gbc);
    y++;

    gbc.gridx = 0;
    gbc.gridy = y;
    panelFormulario.add(lblIp, gbc);

    gbc.gridx = 1;
    panelFormulario.add(txtIp, gbc);
    y++;

    gbc.gridx = 0;
    gbc.gridy = y;
    panelFormulario.add(lblTelefono, gbc);

    gbc.gridx = 1;
    panelFormulario.add(txtTelefono, gbc);
    y++;

    
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 2;
    panelFormulario.add(botonGuardarCambios, gbc);

   //añadimos el panel del formulario al panel principal contenedor y lo centramos
    JPanel container = new JPanel(new GridBagLayout());
    container.setBackground(getBackground());
    container.add(panelFormulario);

    add(container, BorderLayout.CENTER);
}

    //Método para crear los campos de texto, con sus características
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField(20);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(new Color(58, 68, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        return campo;
    }

    //método para crear los campos de contraseña con sus características
    private JPasswordField crearCampoContraseña() {
        JPasswordField campo = new JPasswordField(20);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(new Color(58, 68, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        campo.setHorizontalAlignment(SwingConstants.CENTER);
        return campo;
    }
    

    //Método para crear el botón con sus características
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(66, 133, 244));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /*
    Método para crear las diferentes etiquetas que se mostrarán encima de los campos a cumplimentar
    */
    private JLabel crearEtiqueta(String texto, boolean esRojo) {
        JLabel etiqueta = new JLabel(texto);
        /*
        La etiqueta para regresar a la pantalla de login será de color rojo, para poder tener un método que 
        permita crear todas las etiquetas de la vista realizamos un condicional para diferenciar entre la etiqueta roja
        y el resto que serán blancas
        */
        if (esRojo) { //La etiqueta para regresar a la pantalla de login será de color rojo y mostrará un curso tipo enlace
            etiqueta.setForeground(new Color(255, 99, 71)); 
            etiqueta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            etiqueta.setForeground(Color.WHITE); //Las demás etiquetas que se encuentran encima de cada campo a rellenar serán blancas
        }
      
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        return etiqueta;
    }


    //MNétodos para acceder a los elementos gráficos creados
    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtContraseña() {
        return txtContraseña;
    }

    public JPasswordField getTxtConfirmarContraseña() {
        return txtConfirmarContraseña;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtIp() {
        return txtIp;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JButton getBotonGuardarCambios() {
        return botonGuardarCambios;
    }

    // Mensajes al usuario
    public void mostrarMensaje(String mensaje) {
        if (mensaje.contains("Error")) {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
