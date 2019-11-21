import java.util.ArrayList;

public class MensajesCorreo{
    private ArrayList<MensajeCorreo> mensajes = new ArrayList<MensajeCorreo>();

    MensajesCorreo getMensajesRecibidosPor(String correo){
        MensajesCorreo recibidos = new MensajesCorreo();

        for(int i = 0; i < mensajes.size(); ++i){
            if(mensajes.get(i).getDestinatario().equals(correo)){
                recibidos.aniadirMensaje(mensajes.get(i));
            }
        }

        return recibidos;
    }

    void aniadirMensaje(MensajeCorreo mensaje){
        mensajes.add(mensaje);
    }

    int getNumMensajes(){
        return mensajes.size();
    }

    public String toString(){
        String salida = "------------------------------------------------------------------;";

        for(int i = 0; i < mensajes.size(); ++i){
            salida += mensajes.get(i).toString();
            salida += "------------------------------------------------------------------;";    
        }

        return salida;
    }
}