package com.example.angel.yourpacket;

import android.app.Fragment;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by Gilby_2 on 30/6/17.
 */

public class loginjava extends android.support.v4.app.Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText email,contra;

    int loginType;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login, container,false);

        final Button login = (Button) rootView.findViewById(R.id.login);
        email = (EditText) rootView.findViewById(R.id.email);
        contra = (EditText) rootView.findViewById(R.id.contra);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailt = email.getText().toString();
                String contrat = contra.getText().toString();

               if (emailt.length()!= 0){

                   if (contrat.length()!= 0){

                       Toast.makeText(getActivity(),"Iniciando sesion",Toast.LENGTH_SHORT);
                       mAuth.signInWithEmailAndPassword(emailt, contrat).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                           // If sign in fails, display a message to the user. If sign in succeeds
                           // the auth state listener will be notified and logic to handle the
                           // signed in user can be handled in the listener.
                           if (!task.isSuccessful()) {
                               Log.w(TAG, "signInWithEmail:failed", task.getException());
                               Toast.makeText(getContext(), R.string.auth_failed,
                                       Toast.LENGTH_SHORT).show();
                           }
                       }
                   }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {

                           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                           DatabaseReference usuarioReference = FirebaseDatabase
                                   .getInstance()
                                   .getReference()
                                   .child("Usuarios")
                                   .child(user.getUid()).child("info");

                           usuarioReference.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   Usuario usuario = (Usuario) dataSnapshot.getValue(Usuario.class);
                                   loginType = usuario.getTipo();
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });}
                   });
                   }
               } else {
                   if (contrat.length() == 0){
                       Toast.makeText(getActivity(),"Llenar todos loc campos",Toast.LENGTH_SHORT);
                   }
               }

            }
        });


        return rootView;
    }

}
