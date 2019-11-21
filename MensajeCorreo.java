
public class MensajeCorreo {
    private String emisor;
    private String destinatario;
    private String mensaje;

    MensajeCorreo(String emisor, String destinatario, String mensaje){
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }

    String getEmisor(){
        return this.emisor;
    }

    String getDestinatario(){
        return this.destinatario;
    }

    String getMensaje(){
        return this.mensaje;
    }

    public String toString(){
        return "Enviado por: " + this.emisor + ";" + "Para: " + this.destinatario + ";" + "Mensaje:;" + this.mensaje + ";";
    }
}