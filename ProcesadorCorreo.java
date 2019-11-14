import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente! 
//
public class ProcesadorCorreo extends Thread {
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private Socket socketServicio;
	// stream de lectura (por aquí se recibe lo que envía el cliente)
	private InputStream inputStream;
	// stream de escritura (por aquí se envía los datos al cliente)
	private OutputStream outputStream;
	private PrintWriter outPrinter;
    private BufferedReader inReader;
    private ArrayList<UsuarioCorreo> usuarios = new ArrayList<UsuarioCorreo>();
	
	// Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
	private Random random;
	
	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public ProcesadorCorreo(Socket socketServicio) {
		this.socketServicio=socketServicio;
		random=new Random();
	}

	public void run(){
		procesa();
	}
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){
		
		// Como máximo leeremos un bloque de 1024 bytes. Esto se puede modificar.
        String datosRecibidos;
        String[] datosSeparados;
        String correoRecibido;
        String passwordRecibida;
        boolean usuarioYaRegistrado = false;
        boolean usuarioCorrecto = false;
		
		// Array de bytes para enviar la respuesta. Podemos reservar memoria cuando vayamos a enviarla:
		String respuesta = "500 ERROR Opción no correcta";
		
		
		try {
            // Creamos los flujos
			outputStream = socketServicio.getOutputStream();
            inputStream = socketServicio.getInputStream();
            outPrinter = new PrintWriter(outputStream, true);
            inReader = new BufferedReader(new InputStreamReader(inputStream));
            
            // Leemos una petición de entrada y generamos respuesta
			datosRecibidos = inReader.readLine();
            
            datosSeparados = datosRecibidos.split(" ");

            if(datosSeparados[0] == "0"){
                correoRecibido = datosSeparados[2];
                passwordRecibida = datosSeparados[4];

                for(int i = 0; i < usuarios.size() && !usuarioYaRegistrado; ++i){
                    if(usuarios.get(i).getEmail() == correoRecibido){
                        usuarioYaRegistrado = true;
                    }
                }
                
                if(!usuarioYaRegistrado){
                    usuarios.add(new UsuarioCorreo(correoRecibido, passwordRecibida));
                    respuesta = "200 OK";
                }else{
                    respuesta = "400 ERROR Este email ya ha sido registrado";
                }
            }else if(datosSeparados[0] == "1"){
                correoRecibido = datosSeparados[2];
                passwordRecibida = datosSeparados[4];

                for(int i = 0; i < usuarios.size() && !usuarioCorrecto; ++i){
                    if(usuarios.get(i).getEmail() == correoRecibido){
                        if(usuarios.get(i).getPassword() == passwordRecibida){
                            usuarioCorrecto = true;
                        }else{
                            usuarioCorrecto = false;
                        }
                    }

                    if(usuarioCorrecto){
                        respuesta = "201 OK";
                    }else{
                        respuesta = "401 ERROR Usuario o contraseña incorrectos";
                    }
                }
            }
            
            // Escribimos la respuesta
			outPrinter.println(respuesta);
			
			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujso de entrada/salida.");
		}

	}
}