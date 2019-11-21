import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorCorreoConcurrente {

	public static void main(String[] args) {
	
		// Puerto de escucha
		int port=5555;
		// array de bytes auxiliar para recibir o enviar datos.
		byte []buffer=new byte[256];
		// Número de bytes leídos
		int bytesLeidos=0;
		ServerSocket socketServidor;
		Socket socketServicio = null;
		ProcesadorCorreo procesador = null;

		
		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			//////////////////////////////////////////////////
			// ...serverSocket=... (completar)
			//////////////////////////////////////////////////
			socketServidor = new ServerSocket(port);

			// Mientras ... siempre!
			do {
				
				// Aceptamos una nueva conexión con accept()
				/////////////////////////////////////////////////
				// socketServicio=... (completar)
				//////////////////////////////////////////////////
				socketServicio = socketServidor.accept();
				

				// Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
				// argumento el nuevo socket, para que realice el procesamiento
				// Este esquema permite que se puedan usar hebras más fácilmente.
				procesador = new ProcesadorCorreo(socketServicio);
								
				procesador.run();
				
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}