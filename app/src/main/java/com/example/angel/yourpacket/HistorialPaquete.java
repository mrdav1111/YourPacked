package com.example.angel.yourpacket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by angel on 30/6/2017.
 */

public class HistorialPaquete extends Fragment {

    PaqueteAdapter adaptador;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_historial, container, false);

        getActivity().setTitle("Historial");

        //Paquete paqueteUbicado = new Paquete("YP00005");
        //paqueteUbicado.setUbicacion(18.4873827,-68.9633925);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        //db.child("Paquetes").child(paqueteUbicado.getNoGuia()).setValue(paqueteUbicado);

        final ListView paquete = (ListView) v.findViewById(R.id.listaPaquetes);

        final ArrayList<Paquete> paquetes = new  ArrayList<Paquete>();

        DatabaseReference bd = FirebaseDatabase.getInstance()
                .getReference()
                .child("Usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("paquetes");


        for (Paquete p : paquetes){
            bd.child(p.getNoGuia()).setValue(true);
            bd.getParent().getParent().getParent().child("Paquetes").child(p.getNoGuia()).setValue(p);
        }

        bd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> paquetes1 = dataSnapshot.getChildren();
                DatabaseReference paqueteRaiz = FirebaseDatabase.getInstance().getReference().child("Paquetes");
                for (DataSnapshot aBoolean : paquetes1) {

                    if (!(Boolean) aBoolean.getValue()){
                        paqueteRaiz.child(aBoolean.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Paquete paquete1 = dataSnapshot.getValue(Paquete.class);
                                paquetes.add(paquete1);
                                adaptador.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getContext(),"Child added",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getContext(),dataSnapshot.getKey()+"1",Toast.LENGTH_SHORT).show();
                DatabaseReference paqueteDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Paquetes").child(dataSnapshot.getKey());

                if ((Boolean) dataSnapshot.getValue()){
                    paqueteDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Paquete paquete = dataSnapshot.getValue(Paquete.class);
                            paquetes.add(paquete);
                            adaptador.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });} else {

                    paqueteDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Paquete paquete = dataSnapshot.getValue(Paquete.class);
                            paquetes.remove(paquete);
                            adaptador.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        adaptador = new PaqueteAdapter(getContext(),paquetes);
        paquete.setAdapter(adaptador);


        paquete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paquete seleccionado = paquetes.get((int)id);
                Intent detallePaquete = new Intent(getContext(),DetallePaquete.class);
                detallePaquete.putExtra("paquete", seleccionado);
                startActivity(detallePaquete);

            }
        });
        return v;
    }
}
