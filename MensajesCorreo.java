import java.util.ArrayList;

public class MensajesCorreo{
    private ArrayList<MensajeCorreo> mensajes = new ArrayList<MensajeCorreo>();

    ArrayList<MensajeCorreo> getMensajesRecibidosPor(String correo){
        ArrayList<MensajeCorreo> recibidos = new ArrayList<MensajeCorreo>();

        for(int i = 0; i < mensajes.size(); ++i){
            if(mensajes.get(i).getDestinatario() == correo){
                recibidos.add(mensajes.get(i));
            }
        }

        return recibidos;
    }

    void aniadirMensaje(MensajeCorreo mensaje){
        mensajes.add(mensaje);
    }

    public String toString(){
        String salida = "------------------------------------------------------------------\n";

        for(int i = 0; i < mensajes.size(); ++i){
            salida += mensajes.get(i).toString();
            salida = "------------------------------------------------------------------\n";    
        }

        return salida;
    }
}