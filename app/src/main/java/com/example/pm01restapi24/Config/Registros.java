package com.example.pm01restapi24.Config;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class Registros extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        listView = findViewById(R.id.listView);

        databaseReference = FirebaseDatabase.getInstance().getReference("personas");

        // Crear el ArrayAdapter y establecerlo en el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Cargar los datos de la base de datos
        cargarDatos();

        // Establecer el clic corto en el ListView para mostrar el cuadro de diálogo
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String personaSeleccionada = adapter.getItem(position);
                mostrarOpcionesDialogo(personaSeleccionada);
            }
        });
    }

    private void cargarDatos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Personas persona = snapshot.getValue(Personas.class);
                    if (persona != null) {
                        String personaInfo = persona.getNombres() + " " + persona.getApellidos() +
                                "\nTeléfono: " + persona.getTelefono() +
                                "\nFecha de Nacimiento: " + persona.getFechanac();
                        adapter.add(personaInfo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Registros.this, "Error al cargar los datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para mostrar un cuadro de diálogo con las opciones "Editar" y "Eliminar"
    private void mostrarOpcionesDialogo(final String personaSeleccionada) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una opción")
                .setItems(new CharSequence[]{"Editar", "Eliminar"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Editar
                                // Dividir la cadena personaSeleccionada para obtener los detalles
                                String[] detalles = personaSeleccionada.split("\n");
                                String nombre = detalles[0].trim(); // Nombre y Apellido
                                String telefono = detalles[1].substring(detalles[1].indexOf(":") + 1).trim(); // Teléfono
                                String fechaNacimiento = detalles[2].substring(detalles[2].indexOf(":") + 1).trim(); // Fecha de Nacimiento

                                // Pasar los detalles a la actividad de edición
                                Intent intent = new Intent(Registros.this, EditarContactoActivity.class);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("telefono", telefono);
                                intent.putExtra("fechaNacimiento", fechaNacimiento);
                                startActivity(intent);
                                break;
                            case 1:
                                // Eliminar
                                eliminarPersona(personaSeleccionada);
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void editarPersona(String personaSeleccionada) {
        // Implementa la lógica para editar la persona seleccionada
        // Por ejemplo, puedes iniciar una actividad de edición con los datos de la persona
        Intent intent = new Intent(Registros.this, EditarContactoActivity.class);
        // Pasa los datos de la persona seleccionada a la actividad de edición
        intent.putExtra("personaSeleccionada", personaSeleccionada);
        startActivity(intent);
    }

    private void eliminarPersona(final String personaSeleccionada) {
        // Implementa la lógica para eliminar la persona seleccionada
        // Por ejemplo, puedes mostrar un cuadro de diálogo de confirmación antes de eliminar
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el nombre de la persona seleccionada
                        String nombrePersona = personaSeleccionada.split(" ")[0]; // Suponiendo que el nombre es la primera parte de la cadena

                        // Obtener la referencia al nodo 'personas' en la base de datos
                        DatabaseReference referenciaPersona = databaseReference.child(nombrePersona);

                        // Eliminar el registro de la base de datos
                        referenciaPersona.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Registro eliminado con éxito
                                        Toast.makeText(Registros.this, "Registro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error al eliminar el registro
                                        Toast.makeText(Registros.this, "Error al eliminar el registro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
