
package entidades;

import java.util.Date;

/**
 *
 * @author Antonio
 */
public class AccionEnvioRecp {
    //atributos clase AccionEnvioRecp
    private int idAccionEnvioRecp;
    private int idUsuarioEnvioRecp;
    private String nombreUsuarioEnvioRecp;
    private int idContactoEnvioRecp;
    private String nombreContactoEnvioRecp;
    private int idArchivoEnvioRecp;
    private String nombreArchivoEnvioRecp;
    private Date fecha;
    private String tipoAccion;
    
    //Constructor de la clase
    public AccionEnvioRecp(int idAccionEnvioRecp, int idUsuarioEnvioRecp, String nombreUsuarioEnvioRecp, int idContactoEnvioRecp, String nombreContactoEnvioRecp, int idArchivoEnvioRecp,String nombreArchivoEnvioRecp,String tipoAccion, Date fecha) {
        this.idAccionEnvioRecp = idAccionEnvioRecp;
        this.idUsuarioEnvioRecp = idUsuarioEnvioRecp;
        this.nombreUsuarioEnvioRecp = nombreUsuarioEnvioRecp;
        this.idContactoEnvioRecp = idContactoEnvioRecp;
        this.nombreContactoEnvioRecp = nombreContactoEnvioRecp;
        this.idArchivoEnvioRecp = idArchivoEnvioRecp;
        this.nombreArchivoEnvioRecp = nombreArchivoEnvioRecp;
        this.tipoAccion = tipoAccion;
        this.fecha = fecha;
    }
    
    //Getters
   public int getIdAccionEnvioRecp(){
       return idAccionEnvioRecp;
   }
   
   public int getIdUsuarioEnvioRecp() {
       return idUsuarioEnvioRecp;
   }
   
   public String getNombreUsuarioEnvioRecp(){
       return nombreUsuarioEnvioRecp;
   }
   
   public int getIdContactoEnvioRecp() {
       return idContactoEnvioRecp;
   }
   
   public String getNombreContactoEnvioRecp() {
       return nombreContactoEnvioRecp;
   }
   
   public int getIdArchivoEnvioRecp() {
       return idArchivoEnvioRecp;
   }
   
   public String getNombreArchivoEnvioRecp(){
       return nombreArchivoEnvioRecp;
   }

    public String getTipoAccion() {
        return tipoAccion;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    //Setters
    
    public void setidAccionEnvioRecp(int idAccionEnvioRecp) {
        this.idAccionEnvioRecp = idAccionEnvioRecp;
    }
    
    public void setIdUsuarioEnvioRecp(int idUsuarioEnvioRecp) {
        this.idUsuarioEnvioRecp = idUsuarioEnvioRecp;
    }
    
    public void setNombreUsuarioEnvioRecp(String nombreUsuarioEnvioRecp){
        this.nombreUsuarioEnvioRecp = nombreUsuarioEnvioRecp;
    }
    
    public void setIdContactoEnvioRecp(int idContactoEnvioRecp) {
        this.idContactoEnvioRecp = idContactoEnvioRecp;
    }
    
     public void setNombreContactoEnvioRecp(String nombreContactoEnvioRecp){
        this.nombreContactoEnvioRecp = nombreContactoEnvioRecp;
    }
    
    public void setIdArchivoEnvioRecp(int idArchivoEnvioRecp) {
        this.idArchivoEnvioRecp = idArchivoEnvioRecp;
    }
    
    public void setNombreArchivoEnvioRecp(String nombreArchivoEnvioRecp){
        this.nombreArchivoEnvioRecp = nombreArchivoEnvioRecp;
    }
    
    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }
    

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
