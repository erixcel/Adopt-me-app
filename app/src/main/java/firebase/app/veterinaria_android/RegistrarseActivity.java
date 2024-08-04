package firebase.app.veterinaria_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.app.veterinaria_android.modelo.Usuario;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtPais;
    private EditText txtApellido;
    private EditText txtCelular;
    private EditText txtCorreo;
    private EditText txtContraseña;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        txtNombre = findViewById(R.id.txtNombre);
        txtPais = findViewById(R.id.txtPais);
        txtApellido = findViewById(R.id.txtApellido);
        txtCelular = findViewById(R.id.txtCelular);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("usuarios");
    }

    public void irIniciarSesion(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void crearCuenta(View view){
        String nombre = txtNombre.getText().toString();
        String pais = txtPais.getText().toString();
        String apellido = txtApellido.getText().toString();
        String celular = txtCelular.getText().toString();
        String email = txtCorreo.getText().toString();
        String password = txtContraseña.getText().toString();

        if (!nombre.isEmpty() && !pais.isEmpty() && !apellido.isEmpty() && !celular.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( task -> {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    Usuario usuario = new Usuario(auth.getCurrentUser().getUid(),nombre,pais,apellido,celular);
                    databaseRef.child(auth.getCurrentUser().getUid()).setValue(usuario);
                    Toast.makeText(getApplicationContext(),"Usuario creado correctamente, Bienvenido", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Fallo en la creaccion de la cuenta", Toast.LENGTH_SHORT).show();
                }
            });
        } else Toast.makeText(getApplicationContext(),"Llena todos los campos por favor", Toast.LENGTH_SHORT).show();

    }
}