package cl.inacap.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import cl.inacap.agenda.modelo.Persona;

public class MainActivity extends AppCompatActivity {
    // Declaramos las variables
    private EditText etNombreP, etApellidoP, etCorreoP, etPasswordP;
    private ListView lvPersonas;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos las variables en el formulario
        etNombreP=(EditText) findViewById(R.id.txt_nombrePersona);
        etApellidoP=(EditText) findViewById(R.id.txt_apellidoPersona);
        etCorreoP=(EditText) findViewById(R.id.txt_correoPersona);
        etPasswordP=(EditText) findViewById(R.id.txt_passwordPersona);
        lvPersonas=(ListView) findViewById(R.id.lv_datosPersonas);

        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    // Agrega Menu creado
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Leemos el item seleccionado en el menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        // Evaluamos cual fue el boton seleccionado
        switch (item.getItemId()){
            case R.id.icon_add:

                // Recuperamos datos del formulario
                String nombre=etNombreP.getText().toString();
                String apellido=etApellidoP.getText().toString();
                String correo=etCorreoP.getText().toString();
                String password=etPasswordP.getText().toString();

                //validamos que los campos no esten vacios
                if(nombre.equals("") || apellido.equals("") || correo.equals("") || password.equals("")){
                    validacion(nombre, apellido, correo, password);
                }else{

                    // Rellenamos las variables con esos datos ingresados
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(apellido);
                    p.setCorreo(correo);
                    p.setPassword(password);

                    //Agregamos a base de datos
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);

                    // Limpiamos formulario
                    limpiar();

                    // Enviamos mensaje de que esta agregado
                    Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                    break;
                }


            case R.id.icon_delete:

                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.icon_save:
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;

    }

    // Valida que los datos esten completos
    private void validacion(String nombre, String apellido, String correo, String password){

        if(nombre.equals("")){
            etNombreP.setError("Required");
        }
        if(apellido.equals("")){
            etApellidoP.setError("Required");
        }
        if(correo.equals("")){
            etCorreoP.setError("Required");
        }
        if(password.equals("")){
            etPasswordP.setError("Required");
        }
    }

    // Limpia los campos del formulario
    private void limpiar(){
        etNombreP.setText("");
        etApellidoP.setText("");
        etCorreoP.setText("");
        etPasswordP.setText("");
    }
}