
package interfaz_grafica;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Antonio
 */ 


public class VistaRegistro {
    //Atributos clase VistaRegistro
    private JFrame frame;
    private JTextField txtUsuario,txtEmail,txtIp,txtTelefono;
    private JPasswordField txtContraseña,txtConfirmarContraseña;
    private JButton btnRegistrar;
    private JLabel lblLogin,lblUsuario,lblContraseña,lblConfirmarContraseña,lblEmail,lblIp,lblTelefono;

    //Constructor clase VistaRegistro
    public VistaRegistro() {
        crearVentana(); //Método para crear la ventana
        JPanel panel = configurarPanel(); //Método para configurar el panel
        crearComponentes();        //Método para crear los componentes gráficos
        agregarComponentes(panel); //Método para agregar los componentes al panel
    }

    //Método apra crear la ventana
    private void crearVentana() {
        frame = new JFrame("Registro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650);
        frame.setLocationRelativeTo(null); //Centramos la ventana en el centro de la pantalla
       
    }

    //Método apra configurar el panel donde se mostrarán los elementos gráficos
    private JPanel configurarPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); //se crea un layout para poder organizar mejor los elementos en la vista
        panel.setBackground(new Color(34, 40, 49)); // Color de fondo
        return panel;
    }

    //Método para crear y configurar cada uno de los elementos gráficos
    private void crearComponentes() {
        //Se crean los campos de texto correspondientes a la vista de registro
        txtUsuario = crearCampoTexto("Usuario");
        txtContraseña = crearCampoContraseña("Contraseña");
        txtConfirmarContraseña = crearCampoContraseña("Confirmar Contraseña");
        txtEmail = crearCampoTexto("Email");
        txtIp = crearCampoTexto("IP");
        txtTelefono = crearCampoTexto("Teléfono");

        //Creamos el botón de registro
        btnRegistrar = crearBoton("Registrar");

        //Creamos cada una de las etiquetas que se mostrarán encima de los campos de texto
        lblUsuario = crearEtiqueta("Usuario", false); 
        lblContraseña = crearEtiqueta("Contraseña", false);
        lblConfirmarContraseña = crearEtiqueta("Confirmar Contraseña", false);
        lblEmail = crearEtiqueta("Email", false);
        lblIp = crearEtiqueta("IP", false); 
        lblTelefono = crearEtiqueta("Teléfono", false); 
        lblLogin = crearEtiqueta("¿Ya tienes cuenta? Inicia sesión", true); 
    }

    //Método para crear los campos de texto que el usuario deberá cumplimentar para registrarse
    private JTextField crearCampoTexto(String texto) {
        JTextField campoTexto = new JTextField(20);
        campoTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        campoTexto.setBackground(new Color(58, 68, 80));
        campoTexto.setForeground(Color.WHITE);
        campoTexto.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //para hacer los campos más estéticos establecemos un borde
        return campoTexto;
    }

    //Método para crear los campos para introducir la contraseña y para confirmarla
    private JPasswordField crearCampoContraseña(String texto) {
        JPasswordField campoContraseña = new JPasswordField(20);
        campoContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        campoContraseña.setBackground(new Color(58, 68, 80));
        campoContraseña.setForeground(Color.WHITE);
        campoContraseña.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //para hacer los campos más estéticos establecemos un borde
        return campoContraseña;
    }

    //Método para crear el botón encargado de registrar al usuario
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

    //Método para agregar los elementos gráficos al panel
    private void agregarComponentes(JPanel panel) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); //Se establece un espacio para separar los componentes alrededor
        constraints.fill = GridBagConstraints.HORIZONTAL; //los componentes se colocan  verticalmente
        constraints.weightx = 1.0;

         /*
        Se añaden los elementos creados anteriormente, se dispondrán en el centro del eje horizontal
        y se irán colocando segun la distancia establecida en el eje vertical, uno debajo de otro
        */
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(lblUsuario, constraints); 

        constraints.gridy = 1;
        panel.add(txtUsuario, constraints);

        constraints.gridy = 2;
        panel.add(lblContraseña, constraints);  

        constraints.gridy = 3;
        panel.add(txtContraseña, constraints);

        constraints.gridy = 4;
        panel.add(lblConfirmarContraseña, constraints); 

        constraints.gridy = 5;
        panel.add(txtConfirmarContraseña, constraints);

        constraints.gridy = 6;
        panel.add(lblEmail, constraints); 

        constraints.gridy = 7;
        panel.add(txtEmail, constraints);
        
        constraints.gridy = 8;
        panel.add(lblIp, constraints); 

        constraints.gridy = 9;
        panel.add(txtIp, constraints);

        constraints.gridy = 10;
        panel.add(lblTelefono, constraints); 

        constraints.gridy = 11;
        panel.add(txtTelefono, constraints);

        constraints.gridy = 12;
        panel.add(btnRegistrar, constraints);

        constraints.gridy = 13;
        panel.add(lblLogin, constraints);  

        //Añadimos el panel con los componentes creados al panel que se le pasa por parámetro
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(34, 40, 49));
        container.add(panel);

         //Se agrega el panel a la ventana y se hace visible
        frame.add(container);
        frame.setVisible(true);
    }
     //Método para mostrar mensajes 
    public void mostrarMensaje(String mensaje) {
        if (mensaje.contains("Error")) {
            JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, mensaje, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } 
    }

    // Métodos para obtener los componentes gráficos que aparecen en la vista
    public JTextField getUsuario() {
        return txtUsuario;
    }

    public JPasswordField getContraseña() {
        return txtContraseña;
    }

    public JPasswordField getConfirmarContraseña() {
        return txtConfirmarContraseña;
    }

    public JTextField getEmail() {
        return txtEmail;
    }

    public JTextField getIp() {
        return txtIp;
    }

    public JTextField getTelefono() {
        return txtTelefono;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JLabel getLblLogin() {
        return lblLogin;
    }

    public void cerrar() {
        frame.setVisible(false);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
