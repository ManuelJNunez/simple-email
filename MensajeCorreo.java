
public class MensajeCorreo {
    private String emisor;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private boolean leido;

    MensajeCorreo(String emisor, String destinatario, String asunto, String mensaje){
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.leido = false;
    }

    public String toString(){
        return "Emisor:" + this.emisor + "\nAsunto:" + this.asunto + "\nMensaje:\n" + this.mensaje + "\n";
    }
}