package com.example.pm01restapi24.Config;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.pm01restapi24.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditarContactoActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etApellido;
    private EditText etTelefono;
    private EditText etFechaNacimiento;
    private Button btnGuardarCambios;
    private DatabaseReference databaseReference;
    private String nombrePersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto);

        // Inicializar las vistas
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Obtener la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("personas");

        // Obtener los detalles del registro seleccionado de la actividad anterior
        // Obtener los detalles del registro seleccionado de la actividad anterior
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nombre")) {
            nombrePersona = intent.getStringExtra("nombre");
            String telefono = intent.getStringExtra("telefono");
            String fechaNacimiento = intent.getStringExtra("fechaNacimiento");

            // Mostrar los detalles en los EditText
            etNombre.setText(nombrePersona);
            etTelefono.setText(telefono);
            etFechaNacimiento.setText(fechaNacimiento);
        }


        // Listener para el botón "Guardar cambios"
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar los cambios en la base de datos
                guardarCambios();
            }
        });
    }

    private void guardarCambios() {
        // Obtener los nuevos valores de los EditText
        String nuevoNombre = etNombre.getText().toString().trim();
        String nuevoTelefono = etTelefono.getText().toString().trim();
        String nuevaFechaNacimiento = etFechaNacimiento.getText().toString().trim();
s
        // Actualizar los datos en la base de datos
        if (!TextUtils.isEmpty(nuevoNombre)) {
            // Crear un nuevo objeto Personas con los nuevos valores
            Personas persona = new Personas(nuevoNombre, nuevoTelefono, nuevaFechaNacimiento);

            // Actualizar los datos en la base de datos usando la clave del registro seleccionado
            databaseReference.child(nombrePersona).setValue(persona)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Cambios guardados exitosamente
                            Toast.makeText(EditarContactoActivity.this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                            finish(); // Regresar a la actividad anterior
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error al guardar los cambios
                            Toast.makeText(EditarContactoActivity.this, "Error al guardar los cambios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
        }
    }
}
