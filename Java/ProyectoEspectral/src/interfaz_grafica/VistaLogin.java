

package interfaz_grafica;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Antonio
 */


public class VistaLogin {
    //Atributos clase Vista Login
    private JFrame frame;
    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JLabel lblCrearCuenta;
    
    public VistaLogin() {
        crearVentana();  //Metodo para crear la ventana
        JPanel panel = configurarPanel(); // Método para configurar el panel de la vista
        crearComponentes();        // Método para crear cada uno de los componentes
        agregarComponentes(panel); // Método que agrega los componentes al panel
    }

    //Método para crear la ventana principal
    private void crearVentana() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650); 
        frame.setLocationRelativeTo(null); // Centrar la vista en el centro de la pantalla
    }

    //Método para configurar el panel
    private JPanel configurarPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); //se crea un layout que permite organizar los elementos gráficos dentro de la vista
        panel.setBackground(new Color(34, 40, 49)); // Se establece el color de fondo
        return panel;
    }

    //Método para crear todos los componentes de la interfaz grafica
    private void crearComponentes() {
        txtUsuario = crearCampoTexto("Usuario");
        txtContraseña = crearCampoContraseña("Contraseña");
        btnLogin = crearBoton("Iniciar sesión");
        lblCrearCuenta = crearEtiqueta("¿No tienes cuenta? Crea una ahora");
        
    }

    //Método para crear los textos que se encuentran encima de los labels
    private JTextField crearCampoTexto(String texto) {
        JTextField campo = new JTextField(20);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(new Color(58, 68, 80));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return campo;
    }

    //Método para crear el campo de la contraseña
    private JPasswordField crearCampoContraseña(String texto) {
        JPasswordField campo = new JPasswordField(20);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(new Color(58, 68, 80));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return campo;
    }

    //Método para crear el botón
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

    //Método para crear la etiqueta para acceder a la ventana de registro de usuario
    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(255, 99, 71)); //Color de etiqeta rojo
        etiqueta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        return etiqueta;
    }

    //Método para agregar los elementos gráficos al panel
    private void agregarComponentes(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); //Se establece un espacio para separar los componentes alrededor
        gbc.fill = GridBagConstraints.HORIZONTAL; //los componentes se colocan  verticalmente
        gbc.weightx = 1.0;

        /*se crean las etiquetas Usuario y Contraseña que se encontrarán encima de los campos correspondientes para
        introducir texto
        */
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(Color.WHITE);  //El texto a diferencia del label para acceder al registro, irá en blanco
        lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblContraseña = new JLabel("Contraseña");
        lblContraseña.setForeground(Color.WHITE);  //El texto a diferencia del label para acceder al registro, irá en blanco
        lblContraseña.setHorizontalAlignment(SwingConstants.CENTER);

        /*
        Se añaden los elementos creados anteriormente, se dispondrán en el centro del eje horizontal
        y se irán colocando segun la distancia establecida en el eje vertical, uno debajo de otro
        */
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsuario, gbc);

        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);

        gbc.gridy = 2;
        panel.add(lblContraseña, gbc);

        gbc.gridy = 3;
        panel.add(txtContraseña, gbc);

        gbc.gridy = 4;
        panel.add(btnLogin, gbc);

        gbc.gridy = 5;
        panel.add(lblCrearCuenta, gbc);
        
     

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
        if(mensaje.contains("Error")){
            JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(frame, mensaje, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } 
    }
    

    //Métodos de acceso a los elementos gráficos
    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JLabel getLblCrearCuenta() {
        return lblCrearCuenta;
    }

    public JTextField getUsuario() {
        return txtUsuario;
    }

    public JPasswordField getContraseña() {
        return txtContraseña;
    }

    //Métodos para mostrar o cerrar la vista
    public void cerrar() {
        frame.setVisible(false);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
