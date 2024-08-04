package firebase.app.veterinaria_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import firebase.app.veterinaria_android.Servicio.UsuarioServicio;

public class MainActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contraseña;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(new UsuarioServicio().estaAutenticado()) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return;
        }
        setContentView(R.layout.activity_main);
        correo = findViewById(R.id.txtCorreo);
        contraseña = findViewById(R.id.txtContraseña);
        auth = FirebaseAuth.getInstance();
    }

    public void irRegistrarse(View view){
        Intent intent = new Intent(this, RegistrarseActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void ingresar(View view){
        String email = correo.getText().toString().equals("") ? " " : correo.getText().toString();
        String password = contraseña.getText().toString().equals("") ? " " : contraseña.getText().toString();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Sesion Iniciada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}