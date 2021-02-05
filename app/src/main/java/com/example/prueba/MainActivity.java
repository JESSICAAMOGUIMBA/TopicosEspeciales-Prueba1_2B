package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //definiendo objetos view
    private EditText text_email;
    private EditText text_password;
    private Button btn_registrar, btn_login;
    private ProgressDialog progressDialog;

    //Declara objeto FirebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializamos el objeto firebaseAuth
        firebaseAuth=FirebaseAuth.getInstance();

        //Referenciamos los views
        text_email=(EditText)findViewById(R.id.txt_email);
        text_password=(EditText)findViewById(R.id.txt_password);

        btn_registrar=(Button)findViewById(R.id.botonRegistrar);
        btn_login=(Button)findViewById(R.id.botonLogin);
        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        btn_registrar.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    private void registrarUsuario(){

        //obteniendo el email y la constraseña desde los campos
        String email=text_email.getText().toString().trim();
        String password=text_password.getText().toString().trim();

        //verifica que los campos de texto noo se encuentren vacios
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Ingrese un email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingrese una constraseña",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Procesando registro...");
        progressDialog.show();

        //create a new user
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if sucess
                        if (task.isSuccessful()){

                            Toast.makeText(MainActivity.this,"Se ha registrado el usuario: "+text_email.getText(),Toast.LENGTH_LONG).show();
                        }else {
                            if (task.getException()instanceof FirebaseAuthUserCollisionException){//si se presenta una colision
                                Toast.makeText(MainActivity.this,"El usuario ya existe",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this,"No se puedo registrar el usuario",Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }
    private void loginUsuario(){
        //obteniendo el email y la constraseña desde los campos
        String email=text_email.getText().toString().trim();
        String password=text_password.getText().toString().trim();

        //verifica que los campos de texto noo se encuentren vacios
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Ingrese un email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingrese una constraseña",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Buscando usuario...");
        progressDialog.show();

        //verify that user exist
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if sucess
                        if (task.isSuccessful()){
                            int pos=email.indexOf("@");
                            String user=email.substring(0,pos);
                            Toast.makeText(MainActivity.this," Bienvenido  "+text_email.getText(),Toast.LENGTH_LONG).show();
                            Intent intencion=new Intent(getApplication(),WellcomeActivity.class);
                            intencion.putExtra(WellcomeActivity.user,user);
                            startActivity(intencion);

                        }else {
                                Toast.makeText(MainActivity.this,"Usuario no registrado",Toast.LENGTH_LONG).show();

                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonRegistrar:
                registrarUsuario();
                break;
            case R.id.botonLogin:
                loginUsuario();
                break;
        }
    }
}