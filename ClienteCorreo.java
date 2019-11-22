import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
		String correo="";
		String correodestino="";
		String contrasena="";		
		String []mensaje;
		String mensajeresultado="";
		boolean conectado =true;
		boolean logeado =false;

		// Socket para la conexión TCP
		Socket socketServicio=null;
		Scanner myObj = new Scanner(System.in);

		//Encontrada en https://howtodoinjava.com/regex/java-regex-validate-email-address/
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(correo);
		
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


							while(correo.equals("") || !matcher.matches()){
								System.out.println("Dime tu correo");
								correo=myObj.nextLine();
								matcher = pattern.matcher(correo);
							}
							buferEnvio=buferEnvio+correo+" PASS ";

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
									correo="";
									contrasena="";
								}
							}
						break;
						case 2:
							buferEnvio="1 LOGIN ";
							while(correo.equals("")){
								System.out.println("Dime tu correo");
								correo=myObj.nextLine();
							}
							buferEnvio=buferEnvio+correo+" PASS ";

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
									correo="";
									contrasena="";
								}
							}
						break;
						case 3:
							conectado=false;
							logeado=true;
							buferEnvio="10";

							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");

							if(!mensaje[0].equals("1000")){
								System.out.println("La conexión no se cerró con éxito");
							}

							break;
					}
				}

				if(conectado){		//El usuario ya va ha estar identificado el correo tiene que estar bien
					System.out.println("Quieres ver tu bandeja de entrada(1), enviar un mensaje(2), ver tu bandeja de salida(3) o salir(4)");
					accion=myObj.nextInt();
					while(accion!=1 && accion!=2 && accion!=3 && accion !=4 ){
						System.out.println("ACCION INVALIDA \nQuieres ver tu bandeja de entrada(1), enviar un mensaje(2), ver tu bandeja de salida(3) o salir(4)");
						accion=myObj.nextInt();
					}

					myObj.nextLine();
					switch (accion){
						case 1:
							buferEnvio="2 INBOX ";
							buferEnvio=buferEnvio+correo;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");

							if(mensaje[0].equals("202")){
								mensaje[0]="";
								mensajeresultado=Arrays.stream(mensaje).collect(Collectors.joining(" "));
								mensajeresultado = mensajeresultado.substring(1);
								mensajeresultado = mensajeresultado.replace(';', '\n');
								System.out.println(mensajeresultado);
							}else{
								if(mensaje[0].equals("402")){		//No va a pasar porque obligo a autenticarse antes pero por si cambia la interfaz
									System.out.println("ERROR: No hay mensajes");
								}
							}

							mensajeresultado = "";
						break;
						case 2:
							buferEnvio="3 SENDTO ";
							while(correodestino.equals("")){
								System.out.println("Dime el correo del destinatario");
								correodestino=myObj.nextLine();
							}
							buferEnvio=buferEnvio+ correodestino + " FROM "+correo;

							while(mensajeresultado.equals("")){
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
									correodestino="";
								}
							}

							correodestino = "";
							mensajeresultado = "";
						break;
						case 3:
							buferEnvio="4 OUTBOX "+ correo;
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");
							if(mensaje[0].equals("204")){
								mensaje[0]="";
								mensajeresultado=Arrays.stream(mensaje).collect(Collectors.joining(" "));
								mensajeresultado = mensajeresultado.substring(1);
								mensajeresultado = mensajeresultado.replace(';', '\n');
								System.out.println(mensajeresultado);
							}else if(mensaje[0].equals("404")){
								System.out.println("ERROR: No hay mensajes");
							}

							mensajeresultado = "";
						break;
						case 4:
							conectado=false;
							buferEnvio="10";
							outPrinter.println(buferEnvio);
							outPrinter.flush();

							buferRecepcion = inReader.readLine();
							mensaje=buferRecepcion.split(" ");

							if(!mensaje[0].equals("1000")){
								System.out.println("La conexión no se cerró con éxito");
							}

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
