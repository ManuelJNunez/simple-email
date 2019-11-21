import java.util.ArrayList;

public class UsuariosCorreo{
    private ArrayList<UsuarioCorreo> usuarios = new ArrayList<UsuarioCorreo>();

    boolean existeCorreo(String correo) {
        for(int i = 0; i < usuarios.size(); ++i){
            if(usuarios.get(i).getEmail().equals(correo)){
                return true;
            }
        }

        return false;
    }

    void aniadirUsuario(UsuarioCorreo usuario){
        this.usuarios.add(usuario);
    }

    boolean compruebaCombinacionEmailPass(String correo, String password){
        for(int i = 0; i < usuarios.size(); ++i){
            if(usuarios.get(i).getEmail().equals(correo)){
                if(usuarios.get(i).getPassword().equals(password)){
                    return true;
                }
            }
        }

        return false;
    }
}