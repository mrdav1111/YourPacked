package com.example.angel.yourpacket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaPaquetes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaPaquetes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaPaquetes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

  //  private OnFragmentInteractionListener mListener;

    PaqueteAdapter adaptador;

    public ListaPaquetes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaPaquetes.
     */
    // TODO: Rename and change types and number of parameters
 /*   public static ListaPaquetes newInstance(String param1, String param2) {
        ListaPaquetes fragment = new ListaPaquetes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_paquetes, container, false);

        Paquete paqueteUbicado = new Paquete("YP00004");
        paqueteUbicado.setUbicacion(18.4873827,-69.9633925);

        getActivity().setTitle("YourPacked");

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
            //bd.child(p.getNoGuia()).setValue(true);
            //bd.getParent().getParent().getParent().child("Paquetes").child(p.getNoGuia()).setValue(p);
        }

        bd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> paquetes1 = dataSnapshot.getChildren();
                DatabaseReference paqueteRaiz = FirebaseDatabase.getInstance().getReference().child("Paquetes");
                for (DataSnapshot aBoolean : paquetes1) {

                    if ((Boolean) aBoolean.getValue()){
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

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

  /*  @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
