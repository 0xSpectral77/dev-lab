
package entidades;

import java.util.Date;

/**
 * @author Antonio
 */
public class Sesion {
    //Atributos clase Sesion
    private int idSesion;
    private int idUsuario;
    private String nombreUsuario;
    private Date fechaInicio;
    private Date fechaFin;

    //Constructor
    public Sesion(int idSesion, int idUsuario, String nombreUsuario, Date fechaInicio, Date fechaFin) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    //Constructor para sesiones en curso
    public Sesion(int idSesion, int idUsuario, String nombreUsuario, Date fechaInicio) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null; //La fecha fin es nula cuando se inicia la sesión
    }

    //Getters
    public int getIdSesion() {
        return idSesion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    

    public Date getFechaInicio() {
        return fechaInicio;
    }

    

    public Date getFechaFin() {
        return fechaFin;
    }
    
    //Setters
    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }


    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }


    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
   
}
