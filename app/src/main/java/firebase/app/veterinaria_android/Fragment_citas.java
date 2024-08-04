package firebase.app.veterinaria_android;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

public class Fragment_citas extends Fragment {

    Button btn_siguiente;
    Button btn_confirmar;

    CardView cv_confirmar;
    CardView cv_siguiente;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citas, container, false);

        btn_siguiente = view.findViewById(R.id.btn_siguiente);
        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        cv_confirmar = view.findViewById(R.id.cv_confirmar);
        cv_siguiente = view.findViewById(R.id.cv_siguiente);

        btn_siguiente.setOnClickListener(click -> {
            cv_siguiente.setVisibility(View.GONE);
            cv_confirmar.setVisibility(View.VISIBLE);
        });

        btn_confirmar.setOnClickListener(click -> {
            Toast.makeText(getContext(), "Cita Creada exitosamente", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame1, new Fragment_citas());
            transaction.addToBackStack(null);
            transaction.commit();

        });
        return view;
    }
}