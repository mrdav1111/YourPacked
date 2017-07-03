package com.example.angel.yourpacket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

/**
 * Created by Gilby_2 on 30/6/17.
 */

public class registerjava extends android.support.v4.app.Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText email,contra,contra2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register, container,false);

        final Button registrar = (Button) rootView.findViewById(R.id.registrar);
        email = (EditText) rootView.findViewById(R.id.email);
        contra = (EditText) rootView.findViewById(R.id.contra);
        contra2 = (EditText) rootView.findViewById(R.id.contra2);

        mAuth = FirebaseAuth.getInstance();


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sEmail,sContra1,sContra2;

                sEmail = email.getText().toString();
                sContra1 = contra.getText().toString();
                sContra2 = contra2.getText().toString();

                if (sEmail.length() != 0){
                    if (sContra1.length() != 0){
                        if (sContra2.length() != 0){
                            registrar(sEmail,sContra1,sContra2);
                        } else {
                            Toast.makeText(getActivity(),"Rellenar campo: Confirmar contraseña",Toast.LENGTH_SHORT);
                        }
                    }else {

                        Toast.makeText(getActivity(),"Rellenar campo: Contraseña",Toast.LENGTH_SHORT);
                    }
                } else {
                    Toast.makeText(getActivity(),"Rellenar campo: Email",Toast.LENGTH_SHORT);
                }
            }
        });

        return rootView;
    }

    public void registrar (String email, String contra1, String contra2){

        if (contra1.equals(contra2)){

            mAuth.createUserWithEmailAndPassword(email, contra1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Usuario usuario = new Usuario(user.getEmail(),1);

                    DatabaseReference usuarioReference = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Usuarios")
                            .child(user.getUid()).child("info");

                    usuarioReference.setValue(usuario);

                }
            });


        } else {
            Toast.makeText(getContext(),"La contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }
    }

}
