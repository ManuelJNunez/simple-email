import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors; 


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
    private UsuariosCorreo usuarios;
    private MensajesCorreo mensajes;
	
	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public ProcesadorCorreo(Socket socketServicio, MensajesCorreo mensajes, UsuariosCorreo usuarios) {
        this.socketServicio=socketServicio;
        this.usuarios = usuarios;
        this.mensajes = mensajes;
	}

    @Override
	public void run(){
		procesa();
	}
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){
		
		// Como máximo leeremos un bloque de 1024 bytes. Esto se puede modificar.
        String datosRecibidos;
        String[] datosSeparados;
        String correoRecibido;
        String correoDestino;
        String passwordRecibida;
        boolean usuarioYaRegistrado = false;
        boolean usuarioCorrecto = false;
        String mensajeEnviado;
		
		// Array de bytes para enviar la respuesta. Podemos reservar memoria cuando vayamos a enviarla:
		String respuesta = "500 ERROR Opción no correcta";
		
		
		try {
            // Creamos los flujos
			outputStream = socketServicio.getOutputStream();
            inputStream = socketServicio.getInputStream();
            outPrinter = new PrintWriter(outputStream, true);
            inReader = new BufferedReader(new InputStreamReader(inputStream));

            while(true){
            
                // Leemos una petición de entrada y generamos respuesta
                datosRecibidos = inReader.readLine();
                
                datosSeparados = datosRecibidos.split(" ");

                if(datosSeparados[0].equals("0")){
                    correoRecibido = datosSeparados[2];
                    passwordRecibida = datosSeparados[4];

                    usuarioYaRegistrado = usuarios.existeCorreo(correoRecibido);
                    
                    if(!usuarioYaRegistrado){
                        usuarios.aniadirUsuario(new UsuarioCorreo(correoRecibido, passwordRecibida));
                        respuesta = "200 OK";
                    }else{
                        respuesta = "400 ERROR Este email ya ha sido registrado";
                    }
                }else if(datosSeparados[0].equals("1")){
                    correoRecibido = datosSeparados[2];
                    passwordRecibida = datosSeparados[4];

                    usuarioCorrecto = usuarios.compruebaCombinacionEmailPass(correoRecibido, passwordRecibida);

                    if(usuarioCorrecto){
                        respuesta = "201 OK";
                    }else{
                        respuesta = "401 ERROR Usuario o contraseña incorrectos";
                    }
                }else if (datosSeparados[0].equals("2")){
                    correoRecibido = datosSeparados[2];

                    MensajesCorreo recibidos = mensajes.getMensajesRecibidosPor(correoRecibido);

                    if(recibidos.getNumMensajes() > 0){
                        respuesta = "202 " + recibidos.toString();
                    }else{
                        respuesta = "402 El usuario no tiene mensajes";
                    }
                }else if(datosSeparados[0].equals("3")){
                    correoDestino = datosSeparados[2];
                    correoRecibido = datosSeparados[4];
                    String []mensaje = new String[datosSeparados.length - 6];

                    for(int i = 6; i < datosSeparados.length; ++i){
                        mensaje[i-6] = datosSeparados[i];
                    }

                    mensajeEnviado = Arrays.stream(mensaje).collect(Collectors.joining(" "));

                    usuarioYaRegistrado = usuarios.existeCorreo(correoDestino);

                    if(usuarioYaRegistrado){
                        mensajes.aniadirMensaje(new MensajeCorreo(correoRecibido, correoDestino, mensajeEnviado));
                        respuesta = "203 OK";
                    }else{
                        respuesta = "403 ERROR Usuario no existe";
                    }
                }else if(datosSeparados[0].equals("4")){
                    correoRecibido = datosSeparados[2];

                    MensajesCorreo recibidos = mensajes.getMensajesEnviadosPor(correoRecibido);

                    if(recibidos.getNumMensajes() > 0){
                        respuesta = "204 " + recibidos.toString();
                    }else{
                        respuesta = "404 ERROR Cliente no tiene mensajes enviados";
                    }
                }else if(datosSeparados[0].equals("10")){
                    respuesta = "1000 OK";
                }
            
                outPrinter.println(respuesta);

                if(respuesta.split(" ")[0].equals("1000")){
                    break;
                }
            }	
			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujso de entrada/salida.");
		}

	}
}