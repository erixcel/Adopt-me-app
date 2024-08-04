package firebase.app.veterinaria_android;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import firebase.app.veterinaria_android.Servicio.UsuarioServicio;

public class MenuActivity extends AppCompatActivity {

    private BottomNavigationView btnNavigationMenu;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout=findViewById(R.id.drawerlayout1);
        navigationView=findViewById(R.id.navigationview1);
        toolbar=findViewById(R.id.toolbar1);
        btnNavigationMenu = findViewById(R.id.btnNavigationMenu);

        // mostrar colores de botones de abajo y laterales
        btnNavigationMenu.setItemIconTintList(null);
        navigationView.setItemIconTintList(null);

        // establecer boton seleccionado de abajo
        btnNavigationMenu.setSelectedItemId(R.id.interfaz_adoptar);

        // configuracion del toolbar
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // estableciendo ventana por defecto
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, new Fragment_adoptar()).commit();

        // configuracion menu lateral
        navigationView.setNavigationItemSelectedListener( item -> {
            switch (item.getItemId()){
                case R.id.menu_inicio:getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new Fragment_adoptar()).commit();
                    btnNavigationMenu.setSelectedItemId(R.id.interfaz_adoptar);
                    btnNavigationMenu.setVisibility(View.VISIBLE);
                    drawerLayout.closeDrawers();
                    return false;
                case R.id.menu_mascotas:getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new Fragment_mascotas()).commit();
                    drawerLayout.closeDrawers();
                    break;
                case R.id.menu_web:getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new Fragment_web()).commit();
                    drawerLayout.closeDrawers();
                    break;
                case R.id.menu_libros:getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new Fragment_libros()).commit();
                    drawerLayout.closeDrawers();
                    break;
                case R.id.menu_configuracion:getSupportFragmentManager().beginTransaction().replace(R.id.frame1,new Fragment_configuracion()).commit();
                    drawerLayout.closeDrawers();
                    break;
                case R.id.menu_cerrar_sesion:
                    cerrarSesion();
                    break;
            }
            btnNavigationMenu.setVisibility(View.GONE);

            return false;


        });

        // configuracion botones de abajo
        btnNavigationMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.interfaz_citas:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new Fragment_citas()).commit();
                    return true;
                case R.id.interfaz_dar_adopcion:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new Fragment_adopcion()).commit();
                    return true;
                case R.id.interfaz_adoptar:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new Fragment_adoptar()).commit();
                    return true;
                case R.id.interfaz_pendientes:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new Fragment_pendientes()).commit();
                    return true;
            }
            return false;
        });
    }

    public void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que quieres Cerrar Sesion?");
        builder.setPositiveButton("Sí", (dialogInterface, i) ->  {
            new UsuarioServicio().cerrarSesion();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.desplazar_izquierda_entra, R.anim.desplazar_izquierda_sale);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}