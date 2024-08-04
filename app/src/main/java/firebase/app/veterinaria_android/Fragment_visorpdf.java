package firebase.app.veterinaria_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Fragment_visorpdf extends Fragment {

    public static final long ONE_MEGABYTE = 1024 * 1024*20;

    private String libroNombre;
    private PDFView pdfView;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visorpdf, container, false);
        libroNombre = getArguments().getString("TITULO LIBRO");
        pdfView = view.findViewById(R.id.pdfView);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("libros");

        storageRef.child(libroNombre).getBytes(ONE_MEGABYTE).addOnSuccessListener((OnSuccessListener) (bytes)->{
            pdfView.fromBytes((byte[]) bytes).load();
        });
        return view;
    }
}