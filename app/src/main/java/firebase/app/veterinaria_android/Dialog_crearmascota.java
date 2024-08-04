package firebase.app.veterinaria_android;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.activity.result.contract.ActivityResultContracts;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import firebase.app.veterinaria_android.modelo.Mascota;


public class Dialog_crearmascota extends DialogFragment {

    // CONTROLES
    TextView lblEditarMascota, lblRegistrarMascota;
    MaterialButton btnRegistrar, btnActualizar, btnAbrirGaleria, btnAbrirCamara;
    TextInputEditText txtNombre,txtEdad, txtRaza;
    AutoCompleteTextView txtSexo, txtEspecie;
    ImageView imgFoto;

    // FOTO
    String rutaFotoTomada;
    Uri imagenCargada;

    private ActivityResultLauncher<Uri> ejecutorResultadosCamara =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
                if (result) {
                    Uri foto = Uri.parse(rutaFotoTomada);
                    imgFoto.setImageURI(foto);
                    imagenCargada = Uri.fromFile(new File(foto.getPath()));
                }
            });

    // ACTUALIZAR
    String idMascota = "";
    Boolean isActualizar = false;

    // FIREBASE

    private Context context;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CollectionReference firestoreRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public Dialog_crearmascota(Context context) {
        this.context = context;
    }

    public Dialog_crearmascota(Context context, String idMascota) {
        this.context = context;
        this.idMascota = idMascota;
        this.isActualizar = true;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_crearmascota, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestoreRef = firestore.collection("mascotas");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("mascotas");

        lblRegistrarMascota = view.findViewById(R.id.lblRegistrarMascota);
        lblEditarMascota = view.findViewById(R.id.lblEditarMascota);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtEdad = view.findViewById(R.id.txtEdad);
        txtRaza = view.findViewById(R.id.txtRaza);
        txtSexo = view.findViewById(R.id.txtSexo);
        txtEspecie = view.findViewById(R.id.txtEspecie);
        imgFoto = view.findViewById(R.id.imgFoto);
        btnAbrirGaleria = view.findViewById(R.id.btnAbrirGaleria);
        btnAbrirCamara = view.findViewById(R.id.btnAbrirCamara);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnActualizar = view.findViewById(R.id.btnActualizar);

        // establecer seleccion del campo sexo
        String[] itemsSexo = getResources().getStringArray(R.array.arraySexo);
        if (itemsSexo.length > 0) txtSexo.setText(itemsSexo[0], false);

        // establecer seleccion del campo Especie
        String[] itemsEspecie = getResources().getStringArray(R.array.arrayEspecie);
        if (itemsEspecie.length > 0) txtEspecie.setText(itemsEspecie[0], false);

        // setear mascota si fuera necesario
        if (isActualizar)  setearMascota();
        else verRegistrar();

        abrirGaleria();
        abrirCamara();
        establecerClickBtnRegistrar();
        establecerClickBtnActualizar();

        return view;
    }

    private void verRegistrar(){
        btnRegistrar.setVisibility(View.VISIBLE);
        btnActualizar.setVisibility(View.GONE);
        lblRegistrarMascota.setVisibility(View.VISIBLE);
        lblEditarMascota.setVisibility(View.GONE);
    }
    private void verEditar(){
        btnRegistrar.setVisibility(View.GONE);
        btnActualizar.setVisibility(View.VISIBLE);
        lblRegistrarMascota.setVisibility(View.GONE);
        lblEditarMascota.setVisibility(View.VISIBLE);
    }

    private void setearMascota(){
        verEditar();
        DocumentReference documentoReferencia = firestoreRef.document(idMascota);
        StorageReference imageRef = storageRef.child(idMascota);
        documentoReferencia.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Mascota mascota = documentSnapshot.toObject(Mascota.class);
                txtNombre.setText(mascota.getNombre());
                txtEdad.setText(mascota.getEdad());
                String[] itemsSexo = getResources().getStringArray(R.array.arraySexo);
                txtSexo.setText(itemsSexo[Arrays.asList(itemsSexo).indexOf(mascota.getSexo())], false);
                String[] itemsEspecie = getResources().getStringArray(R.array.arrayEspecie);
                txtEspecie.setText(itemsEspecie[Arrays.asList(itemsEspecie).indexOf(mascota.getEspecie())], false);
                txtRaza.setText(mascota.getRaza());
            }
        });
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(imgFoto);
        });
    }

    private boolean camposLLenos(){
        String nombremascota = "" + txtNombre.getText().toString().trim();
        String edadmascota = "" + txtEdad.getText().toString().trim();
        String razamascota = "" + txtRaza.getText().toString().trim();
        if(nombremascota.isEmpty() && edadmascota.isEmpty() && razamascota.isEmpty()) return false;
        return true;
    }

    private void establecerClickBtnActualizar(){
        btnActualizar.setOnClickListener( click -> {
            String nombremascota = "" + txtNombre.getText().toString().trim();
            String edadmascota = "" + txtEdad.getText().toString().trim();
            String razamascota = "" + txtRaza.getText().toString().trim();
            String sexomascota = "" + txtSexo.getText().toString().trim();
            String especiemascota = "" + txtEspecie.getText().toString().trim();
            if(!camposLLenos()){
                Toast.makeText(getContext(), "Llenar todos los datos", Toast.LENGTH_SHORT).show();
            }else{
                actualizarEnFirebase(idMascota, nombremascota, edadmascota, razamascota, sexomascota, especiemascota, imagenCargada);
            }
        });
    }

    private void establecerClickBtnRegistrar(){
        btnRegistrar.setOnClickListener( click -> {
            String nombremascota = "" + txtNombre.getText().toString().trim();
            String edadmascota = "" + txtEdad.getText().toString().trim();
            String razamascota = "" + txtRaza.getText().toString().trim();
            String sexomascota = "" + txtSexo.getText().toString().trim();
            String especiemascota = "" + txtEspecie.getText().toString().trim();
            if(!camposLLenos() && imagenCargada != null){
                Toast.makeText(getContext(), "Llenar todos los datos", Toast.LENGTH_SHORT).show();
            }else{
                registrarEnFirebase(nombremascota, edadmascota, razamascota, sexomascota, especiemascota, imagenCargada);
            }
        });
    }

    private void actualizarEnFirebase(String idMascota, String nombremascota, String edadmascota, String razamascota, String sexomascota, String especiemascota, Uri imagenURI){

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("nombre",nombremascota);
        mascota.put("edad",edadmascota);
        mascota.put("raza",razamascota);
        mascota.put("sexo",sexomascota);
        mascota.put("especie",especiemascota);

        if (imagenURI != null){
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Actualizando imagen...");
            progressDialog.show();

            String nombreImagen = idMascota;
            getDialog().dismiss();

            guardarImagenEnStorage(imagenURI, nombreImagen, uri -> {
                mascota.put("imagen", uri.toString());
                firestoreRef.document(idMascota).update(mascota).addOnFailureListener(exception -> {
                    Toast.makeText(context, "Ocurrio un error al actualizar", Toast.LENGTH_SHORT).show();
                });
                progressDialog.dismiss();
                Toast.makeText(context, nombremascota + " actualizad" + (sexomascota.equals("Macho") ? "o":"a")  + " correctamente", Toast.LENGTH_SHORT).show();
            }, fail -> {
                progressDialog.dismiss();
                Toast.makeText(context, "Ocurrió un error al cargar la imagen", Toast.LENGTH_SHORT).show();
            });
        }
        else {
            firestoreRef.document(idMascota).update(mascota).addOnFailureListener(exception -> {
                Toast.makeText(context, "Ocurrio un error al actualizar", Toast.LENGTH_SHORT).show();
            });
            getDialog().dismiss();
            Toast.makeText(context, nombremascota + " actualizad" + (sexomascota.equals("Macho") ? "o":"a")  + " correctamente", Toast.LENGTH_SHORT).show();
        }
    }
    private void registrarEnFirebase(String nombremascota, String edadmascota, String razamascota, String sexomascota, String especiemascota, Uri imagenURI){

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("nombre",nombremascota);
        mascota.put("edad",edadmascota);
        mascota.put("raza",razamascota);
        mascota.put("sexo",sexomascota);
        mascota.put("especie",especiemascota);

        firestoreRef.add(mascota).addOnSuccessListener(documentReference -> {
            String mascotaId = documentReference.getId();
            String nombreImagen = mascotaId;

            getDialog().dismiss();
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Guardando imagen...");
            progressDialog.show();

            guardarImagenEnStorage(imagenURI, nombreImagen, uri -> {
                documentReference.update("id",mascotaId);
                documentReference.update("imagen", uri.toString());
                documentReference.update("propietario", auth.getCurrentUser().getUid());
                progressDialog.dismiss();
                Toast.makeText(context, nombremascota + " registrad" + (sexomascota.equals("Macho") ? "o":"a")  + " exitosamente", Toast.LENGTH_SHORT).show();
            }, fail -> {
                progressDialog.dismiss();
                Toast.makeText(context, "Ocurrió un error al cargar la imagen", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception -> {
            Toast.makeText(context, "Ocurrio un error al registrar", Toast.LENGTH_SHORT).show();
        });
    }


    private void guardarImagenEnStorage(Uri uri, String id_mascota, OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener){
        String nombreImagen = id_mascota;
        StorageReference imagenRef = storageRef.child(nombreImagen);
        try {
            InputStream stream = getContext().getContentResolver().openInputStream(uri);
            UploadTask uploadTask = imagenRef.putStream(stream);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imagenRef.getDownloadUrl().addOnSuccessListener(url -> {
                    onSuccessListener.onSuccess(url.toString());
                }).addOnFailureListener(onFailureListener);
            }).addOnFailureListener(onFailureListener);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    //CONFIGURACION GALERIA

    private void abrirGaleria(){
        ActivityResultLauncher<Intent> imagenSeleccionada = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), activityResult -> {
                    if (activityResult.getResultCode() == Activity.RESULT_OK && activityResult.getData() != null) {
                        imagenCargada = activityResult.getData().getData();
                        imgFoto.setImageURI(imagenCargada);
                    }
                });
        btnAbrirGaleria.setOnClickListener( click -> {
            Intent instanciaGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagenSeleccionada.launch(instanciaGaleria);
        });
    }

    //CONFIGURACION CAMARA

    private void abrirCamara(){
        btnAbrirCamara.setOnClickListener( click -> {
            tomarFoto();
        });
    }

    private File generarImagen() throws IOException {
        String fechaCreacion = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "imagen_" + fechaCreacion;
        File almacenamientoDeIamgenes = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen,".jpg",almacenamientoDeIamgenes);
        rutaFotoTomada = imagen.getAbsolutePath();
        return imagen;
    }

    private void tomarFoto() {
        Intent instanciaAbrirCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (instanciaAbrirCamara.resolveActivity(context.getPackageManager()) != null) {
            File imagenCapturada = null;
            try {
                imagenCapturada = generarImagen();
            } catch (IOException ex) {
            }
            if (imagenCapturada != null) {
                Uri imagenUri = FileProvider.getUriForFile(getContext(),"firebase.app.veterinaria_android.fileproviders", imagenCapturada);
                instanciaAbrirCamara.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
                ejecutorResultadosCamara.launch(imagenUri);
            }
        }
    }

}

