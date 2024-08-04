package firebase.app.veterinaria_android.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import firebase.app.veterinaria_android.Dialog_crearmascota;
import firebase.app.veterinaria_android.Fragment_mascotas;
import firebase.app.veterinaria_android.R;
import firebase.app.veterinaria_android.Servicio.MascotaServicio;
import firebase.app.veterinaria_android.modelo.Mascota;

public class MascotaAdapter extends FirestoreRecyclerAdapter<Mascota, MascotaAdapter.ViewHolder>{

    private Fragment_mascotas fragment_mascotas;
    public List<Mascota> mascotasFiltradas;


    public MascotaAdapter(@NonNull FirestoreRecyclerOptions<Mascota> options,Fragment_mascotas fragment_mascotas) {
        super(options);
        this.fragment_mascotas = fragment_mascotas;
        mascotasFiltradas = new ArrayList<>();
        mascotasFiltradas.addAll(options.getSnapshots());
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull Mascota mascota) {
        viewHolder.copyIcon.setPressed(true);
        viewHolder.txtNombre.setText(mascota.getNombre());
        viewHolder.txtSexo.setText(mascota.getSexo());
        viewHolder.txtEdad.setText(mascota.getEdad());
        viewHolder.txtEspecie.setText(mascota.getEspecie());
        viewHolder.txtRaza.setText(mascota.getRaza());
        viewHolder.copyIcon.setOnClickListener(click -> {
            ClipboardManager clipboard = (ClipboardManager) fragment_mascotas.getContext().getSystemService(fragment_mascotas.getContext().CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", mascota.getId());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(click.getContext(), "ID de la mascota copiada", Toast.LENGTH_SHORT).show();

        });
        viewHolder.btnEditar.setOnClickListener( click -> {
            Dialog_crearmascota modal = new Dialog_crearmascota(click.getContext(), mascota.getId());
            modal.show(fragment_mascotas.getFragmentManager(),"Navegar");
        });
        viewHolder.btnBorrar.setOnClickListener( click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(click.getContext());
            builder.setMessage("¿Desea eliminar la mascota?");
            builder.setPositiveButton("Sí", (dialogInterface, i) ->  {
                MascotaServicio mascotaServicio = new MascotaServicio();
                mascotaServicio.eliminarMascotaFirestore(mascota.getId());
                mascotaServicio.eliminarImagenMascota(mascota.getId());
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        Picasso.get().load(mascota.getImagen()).into(viewHolder.imgFotoMascota);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mascota, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtSexo, txtEdad, txtEspecie, txtRaza;
        Button btnEditar, btnBorrar;
        ImageView imgFotoMascota;
        ImageView copyIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            copyIcon = itemView.findViewById(R.id.copyIcon);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtSexo = itemView.findViewById(R.id.txtSexo);
            txtEdad = itemView.findViewById(R.id.txtEdad);
            txtEspecie = itemView.findViewById(R.id.txtEspecie);
            txtRaza = itemView.findViewById(R.id.txtRaza);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            imgFotoMascota = itemView.findViewById(R.id.imgFotoMascota);
        }
    }

//    public boolean interruptor = false;
//    public void setearMascotas(String text) {
//        interruptor = true;
//        mascotasFiltradas.clear();
//        if (text.trim().isEmpty()) {
//            mascotasFiltradas.addAll(getSnapshots());
//        } else {
//            text = text.toLowerCase();
//            for (Mascota mascota : getSnapshots()) {
//                if (mascota.getNombre().toLowerCase().contains(text)) {
//                    mascotasFiltradas.add(mascota);
//                }
//            }
//            notifyDataSetChanged();
//        }
//    }
//    @Override
//    public int getItemCount() {
//        if(interruptor) return mascotasFiltradas.size();
//        else return getSnapshots().size();
//    }
//
//    @Override
//    public Mascota getItem(int position) {
//        if(interruptor) return mascotasFiltradas.get(position);
//        else return getSnapshots().get(position);
//    }

}
