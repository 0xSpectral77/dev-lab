
package entidades;

/**
 *
 * @author Antonio
 */
public class Usuario {
    //Atributos de la clase usuario
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private String email;
    private int telefono;
    private String ip; // Nuevo campo
    
    //Constructor por defecto
     public Usuario() {
       
    }
    
    //Constructor con parámetros
    public Usuario(int idUsuario, String nombreUsuario, String contrasenia, String email, int telefono, String ip) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.email = email;
        this.telefono = telefono;
        this.ip = ip;
    }

    //Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getIp() {
        return ip;
    }

    //Setters
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
