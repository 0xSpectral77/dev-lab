/**
 *
 * @author Antonio
 */
package logica_cifradodes;

import entidades.Archivo;
import logica_bd.GestorBDArchivos;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

/**
 *
 * @author Antonio
 */

public class Descifrado {

    private File archivoEntrada;
    private SecretKey claveSecreta;
    private GestorBDArchivos gestorBDArchivos;

    //Constructor de la clase Descifrado
    public Descifrado(File archivo) {
        this.archivoEntrada = archivo;
        this.gestorBDArchivos = new GestorBDArchivos();
        this.claveSecreta = obtenerClaveDesdeBD(archivo);
    }

    //Método para obtener la clave secreta mediante consulta a la base de datos
    private SecretKey obtenerClaveDesdeBD(File archivo) {
        //Si el archivo es nulo o no existe, se muestra por consola un mensaje de error
        if (archivo == null || !archivo.exists()) {
            System.out.println("El archivo proporcionado no es válido.");
            return null;
        }

        /*Se obtiene una instancia de archivo desde la base de datos
        con un método de consulta al que se le pasa la ruta absoluta del
        archivo por parámetro
        */
        String rutaArchivo = archivo.getAbsolutePath();
        Archivo archivoBD = gestorBDArchivos.obtenerArchivoPorRutaAbsoluta(rutaArchivo);

        //Si no se obtiene un registro o si este es nulo, se muestra un mensaje por consola
        if (archivoBD == null) {
            System.out.println("No se encontró el archivo en la base de datos.");
            return null;
        }

        //Se obtiene la clave en base64 del archivo en cuestión
        String claveBase64 = archivoBD.getClaveBase64();
        return convertirClaveDesdeBase64(claveBase64);
    }

    //Método para convertir una clave en Base64 a una instancia de SecretKey
    private SecretKey convertirClaveDesdeBase64(String claveBase64) {
        try {
            byte[] claveDesformateada = Base64.getDecoder().decode(claveBase64);
            return new SecretKeySpec(claveDesformateada, "AES");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al convertir clave desde Base64: " + e.getMessage());
            return null;
        }
    }

    //Método para verificar si un archivo está cifrado, mediante consulta a la base de datos 
    public boolean verificarArchivoCifrado() {
       
        return gestorBDArchivos.verificarEstadoCifrado(archivoEntrada);
    }

    //Método principal para descifrar un archivo(booleano)
    public boolean descifrarArchivo() {
        //Si la clave Secreta es nula se muestra un mensaje por consola
        if (claveSecreta == null) {
            System.out.println("No se pudo obtener la clave de descifrado.");
            return false;
        }

        //Si no se obtiene el estado cifrado de un archivo se muestra un mensaje por consola
        if (!verificarArchivoCifrado()) {
            System.out.println("El archivo no está cifrado o ya ha sido descifrado.");
            return false;
        }

        //Se intenta realizar el descifrado dentro de un bloque try-catch
        try {
            //Creamos una instacia del objeto Cipher en el modo ECB 
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, claveSecreta); //utilizamos el metodo init en modo DECRYPT y le pasamos la clave Secreta

            //Se crea el archivo temporal
            String archivoEntradaPath = archivoEntrada.getAbsolutePath();
            String archivoDescifradoPath = archivoEntradaPath + "_descifrado";  // Aquí agregamos "_descifrado" al final
            File archivoDescifrado = new File(archivoDescifradoPath);

            //Si el archivo temporal ya existe se sobrescribe
            if (archivoDescifrado.exists()) {
                archivoDescifrado.delete();
                System.out.println("El archivo descifrado ya existía y ha sido sobrescrito.");
            }

            //Se verifica que el archivo cifrado no esté vacío
            if (archivoEntrada.length() == 0) {
                System.out.println("El archivo de entrada está vacío.");
                return false;
            }

            //Se leen los datos cifrados del archivo y se escriben en el archivo temporal
            try (FileInputStream fis = new FileInputStream(archivoEntrada);
                 FileOutputStream fos = new FileOutputStream(archivoDescifrado)) {

                byte[] buffer = new byte[1024]; //Se crea un buffer
                int bytesLeidos;

                //Se leen todos los datos y se actualiza el objeto cipher
                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    byte[] output = cipher.update(buffer, 0, bytesLeidos);
                    if (output != null) {
                        fos.write(output);  //Se escriben los datos en el archivo de salida con el FileOutputStream
                    }
                }

                //Procesamos el padding añadido
                byte[] outputFinal = cipher.doFinal();
                if (outputFinal != null && outputFinal.length > 0) {
                    fos.write(outputFinal);
                }

            } catch (IOException e) {
                System.out.println("Error al leer o escribir los archivos: " + e.getMessage());
                return false;
            }

            //Se verifica que el archivo se ha creado correctamente
            if (archivoDescifrado.exists() && archivoDescifrado.length() > 0) {
                System.out.println("Archivo descifrado con éxito. El archivo descifrado se guardó como: " + archivoDescifrado.getAbsolutePath());
            } else {
                System.out.println("Hubo un problema al guardar el archivo descifrado.");
                return false;
            }

            //Se elimina el archivo original
            if (archivoEntrada.exists()) {
                boolean deleted = archivoEntrada.delete();
                if (deleted) {
                    System.out.println("El archivo original ha sido eliminado.");
                } else {
                    System.out.println("No se pudo eliminar el archivo original.");
                }
            }

            //Se renombra el archivo descifrado, teniendo el mismo nombre que el archivo original
            String archivoOriginalPath = archivoEntrada.getAbsolutePath();
            File archivoRenombrado = new File(archivoOriginalPath);

            boolean renombrado = archivoDescifrado.renameTo(archivoRenombrado);
            if (renombrado) {
                System.out.println("El archivo descifrado ha sido renombrado a: " + archivoRenombrado.getAbsolutePath());
            } else {
                System.out.println("No se pudo renombrar el archivo descifrado.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error al descifrar el archivo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
