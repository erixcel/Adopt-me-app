package firebase.app.veterinaria_android.Servicio;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioServicio {
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public boolean estaAutenticado() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    public void cerrarSesion(){
        auth.signOut();
    }
}
