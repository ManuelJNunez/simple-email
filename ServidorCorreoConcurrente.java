import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorCorreoConcurrente {

	public static void main(String[] args) {
	
		// Puerto de escucha
		int port=5555;
		ServerSocket socketServidor;
		Socket socketServicio = null;
		ProcesadorCorreo procesador = null;
		MensajesCorreo mensajes = new MensajesCorreo();
		UsuariosCorreo usuarios = new UsuariosCorreo();

		
		try {
			socketServidor = new ServerSocket(port);

			// Mientras ... siempre!
			do {
				socketServicio = socketServidor.accept();

				procesador = new ProcesadorCorreo(socketServicio, mensajes, usuarios);

				procesador.start();
				
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}