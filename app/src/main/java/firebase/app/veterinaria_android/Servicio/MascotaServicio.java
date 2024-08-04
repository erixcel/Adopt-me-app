package firebase.app.veterinaria_android.Servicio;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MascotaServicio {
    private FirebaseFirestore firestore;
    private CollectionReference firestoreRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public MascotaServicio () {
        firestore = FirebaseFirestore.getInstance();
        firestoreRef = firestore.collection("mascotas");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("mascotas");
    }

    public void eliminarMascotaFirestore(String idMascota) {
        firestoreRef.document(idMascota).delete();
    }
    public void eliminarImagenMascota(String nombreImagen) {
        storageRef.child(nombreImagen).delete();
    }
}
