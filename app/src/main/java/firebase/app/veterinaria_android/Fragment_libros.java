package firebase.app.veterinaria_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Fragment_libros extends Fragment {

    private ListView lsvLibros;
    private List<String> libros;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private InterstitialAd interstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libros, container, false);
        lsvLibros = view.findViewById(R.id.lsvLibros);
        libros = new ArrayList<String>();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("libros");
        cargarNombreLibros();
        return view;
    }

    public void mostrarAnuncio(){
        MobileAds.initialize(getContext(), initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),"ca-app-pub-7674989853938944/3101543504", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitial) {
                        System.out.println("-------------------");
                        interstitialAd = interstitial;
                        interstitialAd.show(getActivity());
                        System.out.println("Se ejecuto correctamente");
                        System.out.println("-------------------");
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        System.out.println("-------------------");
                        System.out.println("Ha ocurrido un error");
                        System.out.println(loadAdError.getMessage());
                        System.out.println("-------------------");
                        interstitialAd = null;
                    }
                }
        );
    }

    private void cargarNombreLibros(){
        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()){
                libros.add(item.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, libros);
            lsvLibros.setAdapter(adapter);

            lsvLibros.setOnItemClickListener((parent,view,indice,id)->{
                String tituloLibroSeleccionado = lsvLibros.getItemAtPosition(indice).toString();
                Fragment_visorpdf fragment_visorpdf = new Fragment_visorpdf();

                Bundle bundle = new Bundle();
                bundle.putString("TITULO LIBRO", tituloLibroSeleccionado);
                fragment_visorpdf.setArguments(bundle);

                mostrarAnuncio();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame1, fragment_visorpdf);
                transaction.addToBackStack(null);
                transaction.commit();
            });
        });
    }
}