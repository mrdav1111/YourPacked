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

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contra.getText().toString().equals(contra2.getText().toString())){

                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), contra.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Usuario usuario = new Usuario(user.getEmail(),2);

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
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
