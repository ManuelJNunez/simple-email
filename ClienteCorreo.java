//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteCorreo {

	public static void main(String[] args) {
		
		String buferEnvio;
		String buferRecepcion;
		int bytesLeidos=0;
		
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=5555;

		char accion;
		String gmail;
		String contraseña;		
		String []mensaje;
		bool conectado =true;
		bool logeado =false;

		// Socket para la conexión TCP
		Socket socketServicio=null;
		Scanner myObj = new Scanner(System.in);
		
		try {
			// Creamos un socket que se conecte a "host" y "port":
			//////////////////////////////////////////////////////
			socketServicio = new Socket(host, port);
			//////////////////////////////////////////////////////			
			
			PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
			
			while(conectado){
				buferEnvio.clear();
				while(!logeado){
					System.out.println("Quieres registrarte(1), logearte(2) o salir(3)");
					accion=myObj.nextInt();
					if(accion!=1 && accion!=2 && accion!=3 ){
						System.out.println("ACCION INVALIDA \nQuieres registrarte(1), logearte(2) o salir(3)");
						accion=myObj.nextInt();
					}
					switch (accion){
						case 1:
							buferEnvio="0 REGISTER ";
							System.out.println("Dime tu gmail");
							gmail=myObj.nextLine();
							buferEnvio=buferEnvio+gmail+" PASS";
							System.out.println("Dime tu contraseña");
							contraseña=myObj.nextLine();
							buferEnvio=buferEnvio+contraseña;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0]==200){
								logeado=true;
								System.out.println("Registrado con exito");
							}else{
								if(mensaje[0]==400){
									System.out.println("ERROR: Ese usuario ya está registrado");
								}
							}
						break;
						case 2:
							buferEnvio="1 LOGIN ";
							System.out.println("Dime tu gmail");
							gmail=myObj.nextLine();
							buferEnvio=buferEnvio+gmail+" PASS";
							System.out.println("Dime tu contraseña");
							contraseña=myObj.nextLine();
							buferEnvio=buferEnvio+contraseña;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0]==201){
								logeado=true;
								System.out.println("Logeado con exito");
							}else{
								if(mensaje[0]==401){
									System.out.println("ERROR: contraseña incorrecta o no existe el usuario");
								}
							}
						break;
						case 3:
							conectado=false;
							logeado=true;
						break;
					}
				}

				if(conectado){
					
				}
			}
			






			// Si queremos enviar una cadena de caracteres por un OutputStream, hay que pasarla primero
			// a un array de bytes:
			buferEnvio="Al monte del volcán debes ir sin demora";
			
			// Enviamos el array por el outputStream;
			outPrinter.println(buferEnvio);

			// Aunque le indiquemos a TCP que queremos enviar varios arrays de bytes, sólo
			// los enviará efectivamente cuando considere que tiene suficientes datos que enviar...
			// Podemos usar "flush()" para obligar a TCP a que no espere para hacer el envío:
			outPrinter.flush();
			
			// Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que intentará
			// rellenar.
			buferRecepcion = inReader.readLine();

			// MOstremos la cadena de caracteres recibidos:
			System.out.println("Recibido: " + buferRecepcion + "\n");
			
			// Una vez terminado el servicio, cerramos el socket (automáticamente se cierran
			// el inpuStream  y el outputStream)
			socketServicio.close();
			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
