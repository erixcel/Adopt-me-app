package firebase.app.veterinaria_android;

import android.os.Bundle;

import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import firebase.app.veterinaria_android.adapter.MascotaAdapter;
import firebase.app.veterinaria_android.modelo.Mascota;


public class Fragment_mascotas extends Fragment {

    SearchView txtBuscarMascota;
    Query query;

    FloatingActionButton btnAgregarMascota;
    RecyclerView recyclerMascotas;
    MascotaAdapter mascotaAdapter;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    // Creamos una variable mHandler para manejar la ejecución en segundo plano
    private Handler mHandler = new Handler();
    // Creamos un runnable para filtrar la lista después de un retraso determinado
    private Runnable mFilterRunnable = new Runnable() {
        @Override
        public void run() {}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mascotas, container, false);

        txtBuscarMascota = view.findViewById(R.id.txtBuscarMascota);
        eventoCambioDeTexto();

        // configuracion firestore y autenticacion
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("mascotas").whereEqualTo("propietario", auth.getCurrentUser().getUid());
        FirestoreRecyclerOptions<Mascota> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Mascota>().setQuery(query,Mascota.class).build();

        // configuracion del adapter
        mascotaAdapter = new MascotaAdapter(firestoreRecyclerOptions, this);
        mascotaAdapter.notifyDataSetChanged();

        // configuracion del recycleMascotas
        recyclerMascotas = view.findViewById(R.id.recyclerMascotas);
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMascotas.setAdapter(mascotaAdapter);

        // configuracion boton agregar
        btnAgregarMascota = view.findViewById(R.id.btnAgregarMascota);
        btnAgregarMascota.setBackgroundTintMode(null);
        btnAgregarMascota.setOnClickListener( click -> {
            Dialog_crearmascota modal = new Dialog_crearmascota(getContext());
            modal.show(getParentFragmentManager(),"Navegar");
        });
        return view;
    }

    public void buscarPorNombre(String nombre){
        FirestoreRecyclerOptions<Mascota> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Mascota>().setQuery(query.orderBy("nombre")
                        .startAt(nombre).endAt(nombre+"\uf8ff"),Mascota.class).build();
        mascotaAdapter = new MascotaAdapter(firestoreRecyclerOptions,this);
        mascotaAdapter.startListening();
        recyclerMascotas.setAdapter(mascotaAdapter);
    }

    public void eventoCambioDeTexto(){
        txtBuscarMascota.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarPorNombre(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                buscarPorNombre(query);
                return false;
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        mascotaAdapter.startListening();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mascotaAdapter.stopListening();
    }
}