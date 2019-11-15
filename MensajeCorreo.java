
public class MensajeCorreo {
    private String emisor;
    private String destinatario;
    private String asunto;
    private String mensaje;

    MensajeCorreo(String emisor, String destinatario, String asunto, String mensaje){
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    String getEmisor(){
        return this.emisor;
    }

    String getDestinatario(){
        return this.destinatario;
    }

    String getAsunto(){
        return this.asunto;
    }

    String getMensaje(){
        return this.mensaje;
    }

    public String toString(){
        return "Enviado por: " + this.emisor + "\n" + "Para: " + this.destinatario + "\n" + "Asunto: "  + this.asunto + "\n" + "Mensaje:\n" + this.mensaje + "\n";
    }
}