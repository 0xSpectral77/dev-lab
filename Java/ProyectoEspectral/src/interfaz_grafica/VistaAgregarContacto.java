
package interfaz_grafica;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Antonio
 */

public class VistaAgregarContacto {
    //Atributos clase VistaAgregarContacto
    private JFrame frame;
    private JTextField txtNombre, txtIP, txtCorreo, txtTelefono;
    private JButton botonGuardar;
    private JLabel lblAtras;
    private JLabel lblNombre;
    private JLabel lblIP;
    private JLabel lblCorreo;
    private JLabel lblTelefono;

    
    //Constructor vistaAgregarContacto
    public VistaAgregarContacto() {
        crearVentana();              
        JPanel panel = configurarPanel();  
        crearComponentes();           
        agregarComponentes(panel);    
    }

    //Método para crear la ventana 
    private void crearVentana() {
        frame = new JFrame("Agregar Contacto");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 650); 
        frame.setLocationRelativeTo(null); 
    }

    //Método para configurar el panel
    private JPanel configurarPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 40, 49));
        return panel;
    }

    //Método para crear y configurar los componentes gráficos que aparecen en la vista
    private void crearComponentes() {
        txtNombre = crearCampoTexto("Nombre");
        txtIP = crearCampoTexto("Dirección IP");
        txtCorreo = crearCampoTexto("Correo Electrónico");
        txtTelefono = crearCampoTexto("Teléfono");
        botonGuardar = crearBoton("Guardar Contacto");
        lblAtras = crearEtiqueta("Volver atrás", true); 
        lblNombre = crearEtiqueta("Nombre", false);
        lblIP = crearEtiqueta("Dirección IP", false);
        lblCorreo = crearEtiqueta("Correo Electrónico", false);
        lblTelefono = crearEtiqueta("Teléfono", false);
    }

    // Método para crear los JTextField con el estilo deseado
    private JTextField crearCampoTexto(String texto) {
        JTextField campo = new JTextField(20);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBackground(new Color(58, 68, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return campo;
    }

    // Método para crear el botón con el estilo deseado
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

    // Método para crear la etiqueta con el estilo deseado
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


    //Método para agregar los componentes al panel
    private void agregarComponentes(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); //Se establecen las separaciones entre cada elemento
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

       //Se agregan los elementos en disposición vertical uno detras de otro, teniendo en cuenta las restricciones anteriormente establecidas
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);
        gbc.gridy++;
        panel.add(txtNombre, gbc);
        gbc.gridy++;
        panel.add(lblIP, gbc);
        gbc.gridy++;
        panel.add(txtIP, gbc);
        gbc.gridy++;
        panel.add(lblCorreo, gbc);
        gbc.gridy++;
        panel.add(txtCorreo, gbc);
        gbc.gridy++;
        panel.add(lblTelefono, gbc);
        gbc.gridy++;
        panel.add(txtTelefono, gbc);

        //Ajuste de posición para botón guardar
        gbc.gridy++;
        gbc.insets.top = 20;
        panel.add(botonGuardar, gbc);

        //Ajuste de posición para el enlace para volver atrás
        gbc.insets.top = 10;
        gbc.gridy++;
        panel.add(lblAtras, gbc);

        //se añade el panel dentro del contenedor centrado
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(34, 40, 49));
        container.add(panel);

        //Se agrega el contenedor al frame y se hace visible
        frame.add(container);
        frame.setVisible(true);
    }

    //Método para mostrar un mensaje de error o éxito
    public void mostrarMensaje(String mensaje) {
        if(mensaje.contains("Error")){
            JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(frame, mensaje, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } 
    }

    //Métodos para acceder a los elementos gráficos
    public JTextField getNombre() {
        return txtNombre;
    }

    public JTextField getIP() {
        return txtIP;
    }

    public JTextField getCorreo() {
        return txtCorreo;
    }

    public JTextField getTelefono() {
        return txtTelefono;
    }

    public JLabel getLblAtras() {
        return lblAtras;
    }

    public JButton getBtnGuardar() {
        return botonGuardar;
    }

    //Métodos para cerrar o mostrar la vista
    public void cerrar() {
        frame.setVisible(false);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
