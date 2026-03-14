
package entidades;


/**
 *
 * @author Antonio
 */

public class Asociacion {
    private int idAsociacion;
    private int idUsuarioAsociado;
    private String nombreUsuarioAsociado;
    private int idContactoAsociado;
    private String nombreContactoAsociado;

    //Constructor
    public Asociacion(int idAsociacion, int idUsuarioAsociado, String nombreUsuarioAsociado,
                      int idContactoAsociado, String nombreContactoAsociado) {
        this.idAsociacion = idAsociacion;
        this.idUsuarioAsociado = idUsuarioAsociado;
        this.nombreUsuarioAsociado = nombreUsuarioAsociado;
        this.idContactoAsociado = idContactoAsociado;
        this.nombreContactoAsociado = nombreContactoAsociado;

    }

    //Getters
    public int getIdAsociacion() {
        return idAsociacion;
    }

    public int getIdUsuarioAsociado() {
        return idUsuarioAsociado;
    }

    public String getNombreUsuarioAsociado() {
        return nombreUsuarioAsociado;
    }

    public int getIdContactoAsociado() {
        return idContactoAsociado;
    }

    public String getNombreContactoAsociado() {
        return nombreContactoAsociado;
    }


    //Setters
    public void setIdAsociacion(int idAsociacion) {
        this.idAsociacion = idAsociacion;
    }

    public void setIdUsuarioAsociado(int idUsuarioAsociado) {
        this.idUsuarioAsociado = idUsuarioAsociado;
    }

    public void setNombreUsuarioAsociado(String nombreUsuarioAsociado) {
        this.nombreUsuarioAsociado = nombreUsuarioAsociado;
    }

    public void setIdContactoAsociado(int idContactoAsociado) {
        this.idContactoAsociado = idContactoAsociado;
    }

    public void setNombreContactoAsociado(String nombreContactoAsociado) {
        this.nombreContactoAsociado = nombreContactoAsociado;
    }

   
}