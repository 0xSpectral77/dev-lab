

package logica_comunicacion;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;

/**
 *
 * @author Antonio
 */

public class Cliente {
    private String servidorIP;
    private int puerto;

    //Constructor de la clase
    public Cliente(String servidorIP, int puerto) {
        this.servidorIP = servidorIP;
        this.puerto = puerto;
    }

    //Método para comprobar si existe una conexión activa a la que poder conectarse
    public boolean esConexionActiva() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(servidorIP, puerto), 5000); //establecemos un tiempo de espera de 5 segundos, si pasados 5 segundos no encuentra una conexión esta se cierra
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //Método para enviar los datos del archivo y el archivo en sí
    public boolean enviarArchivoyDatos(int tamanioArchivo, String nombreArchivoParaEnviar, String ExtArchivoParaEnviar,
                                    String HashArchivoBaseParaEnviar,
                                    String HashArchivoCifradoParaEnviar, String ClaveBase64ArchivoParaEnviar,
                                    boolean estaCifradoArchivoParaEnviar, String nombreEmisor, String nombreReceptor, File archivo) {
        
        boolean transferenciaCompletada = false; //booleano para controlar que una transferencia se ha completado
        try (Socket socket = new Socket(servidorIP, puerto); //Se crea una instancia de socket indicando la IP y el puerto
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream()); //Se crea un DataOutputStream para asociarlo al flujo de salida del socket
             FileInputStream fis = new FileInputStream(archivo)) { //Se crea un file inputstream que lee los datos del archivo

            //Los datos se leen utilizando el FileInputStream y se enviarán en loques a través del DataOutPutStream
            //Se envía los datos del archivo al servidor (receptor)
            salida.writeInt(tamanioArchivo);
            salida.writeUTF(nombreArchivoParaEnviar);
            salida.writeUTF(ExtArchivoParaEnviar);
            salida.writeUTF(HashArchivoBaseParaEnviar);
            salida.writeUTF(HashArchivoCifradoParaEnviar);
            salida.writeUTF(ClaveBase64ArchivoParaEnviar);
            salida.writeBoolean(estaCifradoArchivoParaEnviar);
            salida.writeUTF(nombreEmisor);
            salida.writeUTF(nombreReceptor);

            //El archivo se envía en bloques
            byte[] buffer = new byte[4096]; //Se crea un buffer
            int bytesLeidos;
            while ((bytesLeidos = fis.read(buffer)) != -1) {
                salida.write(buffer, 0, bytesLeidos);
            }

            transferenciaCompletada = true; //El estado de la transferencia pasa a completada
            System.out.println("Datos y archivo enviados correctamente.");
        } catch (IOException e) {
            System.err.println("Error al enviar datos o archivo: " + e.getMessage());
        }
        return transferenciaCompletada; 
    }
}
