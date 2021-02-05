package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WellcomeActivity extends AppCompatActivity {
    public static final String user="names";
    EditText txtTema;
    Spinner spinArea, spinSecciones;
    Button btnRegistrar;

    TextView txtUser;

    private DatabaseReference Clases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        Clases= FirebaseDatabase.getInstance().getReference("Clases");

        txtUser=(TextView)findViewById(R.id.textUser);
        String user=getIntent().getStringExtra("names");
        txtUser.setText("¡ Bienvenido "+ user + "!");

        txtTema=(EditText)findViewById(R.id.txt_tema);
        spinArea=(Spinner) findViewById(R.id.spin_area);
        spinSecciones=(Spinner) findViewById(R.id.spin_seccion);
        btnRegistrar=(Button)findViewById(R.id.btn_registrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarClase();
            }
        });

    }

    public void registrarClase(){
        String seccion=spinSecciones.getSelectedItem().toString();
        String area=spinArea.getSelectedItem().toString();
        String tema=txtTema.getText().toString();

        if (!TextUtils.isEmpty(tema)){
            String id=Clases.push().getKey();
            Clases leccion=new Clases(id,seccion,area,tema);
            Clases.child("Actividades").child(id).setValue(leccion);
            Toast.makeText(this,"Actividad registrada", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Introduzca un tema", Toast.LENGTH_LONG).show();
        }
    }
}