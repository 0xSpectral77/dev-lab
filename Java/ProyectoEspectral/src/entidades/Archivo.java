
package entidades;

/**
 *
 * @author Antonio
 */


public class Archivo {
    private int idArchivo;
    private String rutaArchivo;
    private int tamanio;
    private String nombreArchivo;
    private String ext;
    private String hashArchivoBase;
    private String hashArchivoCifrado;
    private String claveBase64;
    private boolean estaCifrado;

    //Constructor
    public Archivo(int idArchivo, String rutaArchivo, int tamanio, String nombreArchivo, String ext, String hashArchivoBase, String hashArchivoCifrado, String claveBase64, boolean estaCifrado) {
        this.idArchivo = idArchivo;
        this.rutaArchivo = rutaArchivo;
        this.tamanio = tamanio;
        this.nombreArchivo = nombreArchivo;
        this.ext = ext;
        this.hashArchivoBase = hashArchivoBase;
        this.hashArchivoCifrado = hashArchivoCifrado;
        this.claveBase64 = claveBase64;
        this.estaCifrado = estaCifrado;
    }
    //Constructor vacío
     public Archivo()
     {
         
     }
    
    

    //Getters
    public int getIdArchivo() {
        return idArchivo;
    }

   
    public String getRutaArchivo() {
        return rutaArchivo;
    }

    
    public int getTamanio() {
        return tamanio;
    }

    
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    
    public String getExt() {
        return ext;
    }


    
    public String getHashArchivoBase() {
        return hashArchivoBase;
    }

   
    public String getHashArchivoCifrado() {
        return hashArchivoCifrado;
    }

  
    public String getClaveBase64() {
        return claveBase64;
    }

    
    public boolean getEstaCifrado(){
        return estaCifrado;
    }
    
    //Setters
    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }


    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }


    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

 
    public void setHashArchivoBase(String hashArchivoBase) {
        this.hashArchivoBase = hashArchivoBase;
    }

    public void setHashArchivoCifrado(String hashArchivoCifrado) {
        this.hashArchivoCifrado = hashArchivoCifrado;
    }

    public void setClaveBase64(String claveBase64) {
        this.claveBase64 = claveBase64;
    }

    public void setEstaCifrado(boolean estaCifrado){
        this.estaCifrado = estaCifrado;
    }
    
    
}
