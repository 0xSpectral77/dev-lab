/**
 *
 * @author Antonio
 */
package logica_cifradodes;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.Base64;
import javax.swing.JOptionPane;
import logica_bd.GestorBDArchivos;
import entidades.Archivo;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Antonio
 */

public class Cifrado {

    private File archivoEntrada;
    private GestorBDArchivos gestorBD;

    //Constructor de la clase cifrado
    public Cifrado(File archivoEntrada, GestorBDArchivos gestorBD) {
        this.archivoEntrada = archivoEntrada;
        this.gestorBD = gestorBD;
    }

    //Método para generar una clave secreta aleatoria de 256 bits siguiendo el algoritmo AES
    public SecretKey generarClaveAES() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES"); //Se crea una instancia del objeto KeyGenerator
            keyGen.init(256); //Se utiliza el método init pasándole por parámetro el número de bits
            return keyGen.generateKey();//se genera la clave secreta
        } catch (NoSuchAlgorithmException e) { //Se controla la excepción
            System.out.println("Error al generar la clave AES: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al generar la clave AES: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    //Método para cifrar un archivo que recibe un objeto file y una clave secreta por parámetro
    public void cifrarArchivo(File archivoEntrada, SecretKey claveSecreta) {
        //En primer lugar se comprueba si el archivo está cifrado, consultando a la base de datos
        Archivo archivoBD = gestorBD.obtenerArchivoPorRutaAbsoluta(archivoEntrada.getAbsolutePath());

        //Si no se encuentra un registro (se obtiene null) o si el archivo ya está cifrado, se mostrará un mensaje de error
        if (archivoBD != null && archivoBD.getEstaCifrado()) {
            JOptionPane.showMessageDialog(null, "El archivo ya está cifrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return; //No se hará nada más
        }

        try {
            //Si el archivo no existe crear una excepción
            if (archivoEntrada == null || !archivoEntrada.exists()) {
                throw new Exception("El archivo no existe.");
            }

            //Se inicializa el cifrador creando una instancia de la clase Cipher y con el método getInstance indicarle el método de cifrado, en este caso utilizamos ECB, ya que es más sencillo de implementar
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, claveSecreta); //utilizamos el método init y le pasamos la constante ENCRYPT_MODE y la clave secreta generada

            //Se crea un fichero temporal
            File archivoTemporal = new File(archivoEntrada.getAbsolutePath() + ".aes");

            //Se utiliza un FileInputStream y un OutputStream para leer los datos del archivo de entrada y escribirlos de manera cifrada en el archivo temporal
            try (FileInputStream fis = new FileInputStream(archivoEntrada);
                 FileOutputStream fos = new FileOutputStream(archivoTemporal)) {

                //Se crea un buffer para almacenar los bytes
                byte[] buffer = new byte[1024];
                int bytesLeidos;

                //Mientras queden bytes por leer, se escribirán en el archivo temporal
                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    byte[] output = cipher.update(buffer, 0, bytesLeidos);
                    if (output != null) {
                        fos.write(output);
                    }
                }

                //Se aplica padding al final de los datos del archivo para garantizar que el tamaño del archivo sea múltiplo de 16 bytes
                byte[] outputFinal = cipher.doFinal();
                if (outputFinal != null) {
                    fos.write(outputFinal);
                }
            }

            //Se reemplaza el archivo original por el archivo temporal
            Files.delete(archivoEntrada.toPath());
            archivoTemporal.renameTo(archivoEntrada);

            //Se actualiza el estado del archivo en la base de datos
            if (archivoBD != null) {
                archivoBD.setEstaCifrado(true);  //Se establece el estado de cifrado
                gestorBD.actualizarEstadoCifrado(archivoBD); //Se actualiza en la base de datos
            }

        } catch (Exception e) {
            System.out.println("Error al cifrar el archivo: " + e.getMessage());
           
        }
    }

    //Método para obtener la clave en formato Base64 pasándole una instancia de SecretKey por parámetro
    public String obtenerClaveSecretaBase64(SecretKey claveSecreta) {
        return Base64.getEncoder().encodeToString(claveSecreta.getEncoded());  // Convertimos la clave secreta a Base64
    }

    //Método para obtener el hash del archivo (SHA-256)
    public String obtenerHashArchivo(File archivo) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");  //Se obtiene una instacia de  MessageDigest a la cual se le pasa por parámetro el algoritmo SHA-256

            //Se procede a leer el contenido del archivo utilizando un FileStream
            try (FileInputStream fis = new FileInputStream(archivo)) {
                byte[] buffer = new byte[1024]; //Se crea un buffer
                int bytesLeidos;
                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesLeidos);  //Se actualiza la instancia con los datos leídos
                }
            }

            byte[] digest = md.digest();  //Se obtiene el hash en un array de bytes
            StringBuilder sbHexadecimal = new StringBuilder(); //Creamos un StringBuilder para añadir caracteres
            for (byte byteATransformar : digest) { //recorremos el array de bytes
                sbHexadecimal.append(String.format("%02x", byteATransformar));  
                /*
                Convertimos cada byte (tomando su valor decimnal) en su formato hexadecimal (dos dígitos) dividiendolo por 16, si el valor del byte es menor que
                16, entonces se añade un 0 delante, para el resto de números:
                
                -El resultado de la división corresponderá al primer dígito.
                -El resto de la división corresponderá al segundo dígito.
                
                Por ejemplo si tenemos el valor 3: el formato hexadecimal será 03.
                */
               
            }
            return sbHexadecimal.toString();  //retornamos el hash como un String
        } catch (Exception e) {
            System.out.println("Error al obtener el hash del archivo: " + e.getMessage());
            return null;
        }
    }
}
