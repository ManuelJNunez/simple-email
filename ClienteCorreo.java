import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.stream.Collectors; 
import java.util.stream.Stream;
import java.util.Arrays;

public class ClienteCorreo {

	public static void main(String[] args) {
		
		String buferEnvio;
		String buferRecepcion;
		
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=5555;

		int accion;
		String gmail="";
		String gmaildestino="";
		String contrasena="";		
		String []mensaje;
		String mensajeresultado="";
		boolean conectado =true;
		boolean logeado =false;

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
				while(!logeado){
					System.out.println("Quieres registrarte(1), logearte(2) o salir(3)?");
					accion=myObj.nextInt();
					while(accion!=1 && accion!=2 && accion!=3 ){
						System.out.println("ACCION INVALIDA \nQuieres registrarte(1), logearte(2) o salir(3)?");
						accion=myObj.nextInt();
					}

					myObj.nextLine();
					switch (accion){
						case 1:
							buferEnvio="0 REGISTER ";


							while(gmail.equals("")){
								System.out.println("Dime tu gmail");
								gmail=myObj.nextLine();
							}
							buferEnvio=buferEnvio+gmail+" PASS ";

							while(contrasena.equals("")){
								System.out.println("Dime tu contrasena");
								contrasena=myObj.nextLine();
							}
							buferEnvio=buferEnvio+contrasena;

							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0].equals("200")){
								logeado=true;
								System.out.println("Registrado con exito");
							}else{
								if(mensaje[0].equals("400")){
									System.out.println("ERROR: Ese usuario ya está registrado");
									gmail="";
									contrasena="";
								}
							}
						break;
						case 2:
							buferEnvio="1 LOGIN ";
							while(gmail.equals("")){
								System.out.println("Dime tu gmail");
								gmail=myObj.nextLine();
							}
							buferEnvio=buferEnvio+gmail+" PASS ";

							while(contrasena.equals("")){
								System.out.println("Dime tu contrasena");
								contrasena=myObj.nextLine();
							}
							buferEnvio=buferEnvio+contrasena;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0].equals("201")){
								logeado=true;
								System.out.println("Logeado con exito");
							}else{
								if(mensaje[0].equals("401")){
									System.out.println("ERROR: contrasena incorrecta o no existe el usuario");
									gmail="";
									contrasena="";
								}
							}
						break;
						case 3:
							conectado=false;
							logeado=true;
							buferEnvio="1";
							outPrinter.println(buferEnvio);
							outPrinter.flush();
						break;
					}
				}

				if(conectado){		//El usuario ya va ha estar identificado el gmail tiene que estar bien
					System.out.println("Quieres ver tu bandeja de entrada(1), enviar un mensaje(2), ver tu bandeja de salida(3) o salir(4)");
					accion=myObj.nextInt();
					while(accion!=1 && accion!=2 && accion!=3 && accion !=4 ){
						System.out.println("ACCION INVALIDA \nQuieres ver tu bandeja de entrada(1), enviar un mensaje(2), ver tu bandeja de salida(3) o salir(4)");
						accion=myObj.nextInt();
					}

					switch (accion){
						case 1:
							buferEnvio="2 INBOX ";
							buferEnvio=buferEnvio+gmail;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							System.out.println("hola");
							System.out.println(buferEnvio);

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");

							System.out.println("hola");

							if(mensaje[0].equals("202")){
								mensaje[0]="";
								mensajeresultado=Arrays.stream(mensaje).collect(Collectors.joining(" "));
								System.out.println(mensajeresultado);
							}else{
								if(mensaje[0].equals("402")){		//No va a pasar porque obligo a autenticarse antes pero por si cambia la interfaz
									System.out.println("ERROR: No hay mensajes");
								}
							}
						break;
						case 2:
							buferEnvio="3 SENDTO ";
							while(gmaildestino==""){
								System.out.println("Dime el gmail del destinatario");
								gmaildestino=myObj.nextLine();
							}
							buferEnvio=buferEnvio+ gmaildestino + " FROM "+gmail+" SUBJECT ";

							while(mensajeresultado==""){
								System.out.println("Dime el asunto del mensaje");
								mensajeresultado=myObj.nextLine();
							}
							buferEnvio=buferEnvio + mensajeresultado;
							mensajeresultado="";

							while(mensajeresultado==""){
								System.out.println("Dime el mensaje");
								mensajeresultado=myObj.nextLine();
							}
							buferEnvio=buferEnvio + " MESSAGE " + mensajeresultado;

							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0].equals("203")){
								System.out.println("Mensaje enviado con éxito");
							}else{
								if(mensaje[0].equals("403")){
									System.out.println("ERROR: destinatario incorrecto");
									gmaildestino="";
								}
							}
						break;
						case 3:
							buferEnvio="4 OUTBOX "+ gmail;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0].equals("204")){
								mensaje[0]="";
								mensajeresultado=Arrays.stream(mensaje).collect(Collectors.joining(" "));
								System.out.println(mensajeresultado);
							}else{
								if(mensaje[0].equals("404")){		//No va a pasar porque obligo a autenticarse antes pero por si cambia la interfaz
									System.out.println("ERROR: gmail incorrecto");
								}
							}
						break;
						case 4:
							conectado=false;
							buferEnvio="1";
							outPrinter.println(buferEnvio);
							outPrinter.flush();
						break;
						}

						accion = 0;
					}
				}
			System.out.println("Adios!!");

			socketServicio.close();
			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
