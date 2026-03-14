

package entidades;

import java.util.Date;

/**
 *
 * @author Antonio
 */
public class AccionCIFDES {

    
    private int idAccionCIFDES;
    private int idUsuario;
    private String nombreUsuarioAccion;
    private int idArchivo;
    private String nombreArchivoAccion;
    private Date fecha;
    private String tipoAccion;

    //Constructor de la clase
    public AccionCIFDES(int idAccionCIFDES, int idUsuario, String nombreUsuarioAccion, int idArchivo, String nombreArchivoAccion, Date fecha, String tipoAccion) {
        this.idAccionCIFDES = idAccionCIFDES;
        this.idUsuario = idUsuario;
        this.nombreUsuarioAccion = nombreUsuarioAccion;
        this.idArchivo = idArchivo;
        this.nombreArchivoAccion = nombreArchivoAccion;
        this.tipoAccion = tipoAccion;
        this.fecha = fecha;
    }

    //Getters
    public int getIdAccionCIFDES() {
        return idAccionCIFDES;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuarioAccion() {
        return nombreUsuarioAccion;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public String getNombreArchivoAccion() {
        return nombreArchivoAccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    //Setters
    public void setIdAccionCIFDES(int idAccionCIFDES) {
        this.idAccionCIFDES = idAccionCIFDES;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuarioAccion(String nombreUsuarioAccion) {
        this.nombreUsuarioAccion = nombreUsuarioAccion;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public void setNombreArchivoAccion(String nombreArchivoAccion) {
        this.nombreArchivoAccion = nombreArchivoAccion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }
}