
package entidades;

/**
 *
 * @author Antonio
 */
public class Contacto {
    private int idContacto;
    private String nombreContacto;
    private String ip;
    private String email;
    private String telefono;

    
    

    //Constructor
    public Contacto(int idContacto,String nombreContacto, String ip, String email, String telefono) {
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.ip = ip;
        this.email = email;
        this.telefono = telefono;
    }
    
    
     
    //Getters
    
    public int getIdContacto() {
        return idContacto;
    }
    
    
    public String getNombreContacto() {
        return nombreContacto;
    }
    
    public String getIp() {
        return ip;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    
    //Setters
    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    
    public void setIP(String ip) {
        this.ip = ip;
    }
    
     public void setEmail(String email) {
        this.email = email;
    }
     
      public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
}