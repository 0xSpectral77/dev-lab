

package logica_comunicacion;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Antonio
 */

public class Servidor {
    private int puerto;
    private ServerSocket serverSocket;
    private String directorioDestino;
    private int tamanioArchivo;
    private String nombreArchivo;
    private String extArchivo;
    private String hashBase;
    private String hashCifrado;
    private String claveBase64;
    private boolean estaCifrado;
    private String nombreEmisor;
    private String usuarioReceptor;
    private String nombreReceptor;
    private File archivoRecibido;
    private Runnable archivoRecibidoCallback; 

    //Constructor de la clase
    public Servidor(int puerto, String directorioDestino, String usuarioReceptor) {
        this.puerto = puerto;
        this.directorioDestino = directorioDestino;
        this.usuarioReceptor = usuarioReceptor;
    }

    //Método para inciar el servidor
    public void iniciarServidor() {
        try {
            //Comprobar si el puerto que se le pase por parámetro al constructor ya se encuentra en uso
            if (!esPuertoUtilizable(puerto)) {
                throw new BindException("El puerto " + puerto + " ya está en uso.");
            }

            //Si el puerto no se encuentra en uso se crea una instancia de serverSocket pasando el puerto por parámetro
            serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) { //Bucle infinito del servidor para aceptar múltiples conexiones
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress()); //Obtenemos la IP del cliente por consola

                //La recepción del archivo ocurre en un hilo secundario, para no congelar la interfaz gráfica
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        manejarConexion(socket);
                    }
                }).start();
            }

        } catch (IOException e) {
            System.err.println("No se pueden recibir archivos actualmente, el servidor está cerrado");
        }
    }

    //Método para comprobar que un puerto que se le pasa por parámetro se puede utilizar
    private boolean esPuertoUtilizable(int puerto) {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            return true;  //Comprobación mediante la apertura del puerto, si se puede abrir es que se puede utilizar
        } catch (IOException e) {
             
                return false;  //El puerto ya se está utilizando o no se puede abrir
        }
    }

    //Método encargado de manejar la conexión de manera independiente, recibiendo una instancia de socket por parámetro
    private void manejarConexion(Socket socket) {
        try (DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
            //Se leen los datos recibidos del archivos y se almacenan en variables
            tamanioArchivo = entrada.readInt();
            nombreArchivo = entrada.readUTF();
            extArchivo = entrada.readUTF();
            hashBase = entrada.readUTF();
            hashCifrado = entrada.readUTF();
            claveBase64 = entrada.readUTF();
            estaCifrado = entrada.readBoolean();
            nombreEmisor = entrada.readUTF();
            nombreReceptor = entrada.readUTF();

            if (!nombreReceptor.equals(usuarioReceptor)) { //Si el nombre del receptor no coincide con el usuario para el que se supone que va dirigido el archivo se cerrará la conexión
                System.out.println("Archivo denegado: el receptor no coincide con el usuario");
                socket.close();
                return;
            }

            //Se guarda el archivo en la carpeta de destino
            archivoRecibido = new File(directorioDestino, nombreArchivo);
            try (FileOutputStream fos = new FileOutputStream(archivoRecibido)) {
                byte[] buffer = new byte[4096];
                int bytesLeidos;
                while ((bytesLeidos = entrada.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesLeidos);
                }
            }

            System.out.println("Archivo guardado en: " + archivoRecibido.getAbsolutePath());

            //Ejecutamos el callback para avisar al controlador que ha terminado la transferencia
            if (archivoRecibidoCallback != null) {
                archivoRecibidoCallback.run();  
            }

            //Controlamos las excepciones
        } catch (IOException e) {
            System.err.println("Error al manejar la conexión: " + e.getMessage());
        } finally {
            try {
                socket.close(); //Se cierra la conexión
            } catch (IOException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    //Método para cerrar el servidor
    public void cerrarServidor() {
        try {
            //Si el serverSocket no es nulo y no está cerrado
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); //Cerramos el serverSocket
                System.out.println("Servidor cerrado correctamente.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }

    //Getters
    public File obtenerArchivo() {
        return archivoRecibido;
    }

    public int getTamanioArchivo() {
        return tamanioArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getExtArchivo() {
        return extArchivo;
    }

    public String getHashBase() {
        return hashBase;
    }

    public String getHashCifrado() {
        return hashCifrado;
    }

    public String getClaveBase64() {
        return claveBase64;
    }

    public boolean getEstaCifrado() {
        return estaCifrado;
    }

    public String getNombreEmisor() {
        return nombreEmisor;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public String obtenerRutaArchivo() {
        if (archivoRecibido != null) {
            return archivoRecibido.getAbsolutePath();
        } else {
            return "Archivo no recibido";
        }
    }

    //Método para establecer el callback
    public void setArchivoRecibidoCallback(Runnable callback) {
        this.archivoRecibidoCallback = callback;
    }
}