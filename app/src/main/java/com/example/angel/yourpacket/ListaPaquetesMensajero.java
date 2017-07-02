package com.example.angel.yourpacket;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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
 * {@link ListaPaquetesMensajero.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaPaquetesMensajero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaPaquetesMensajero extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //  private OnFragmentInteractionListener mListener;

    PaqueteAdapterMensajero adaptador;
    private GoogleApiClient mGoogleApiClient;
    Location ubicacion;
    private LocationManager locationManager;
    private android.location.LocationListener locationListener;

    public ListaPaquetesMensajero() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5f, locationListener);
                }
                return;

        }
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
        paqueteUbicado.setUbicacion(18.4873827, -69.9633925);

        getActivity().setTitle("YourPacked");

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Paquetes").child(paqueteUbicado.getNoGuia()).setValue(paqueteUbicado);

        final ListView paquete = (ListView) v.findViewById(R.id.listaPaquetes);

        final ArrayList<Paquete> paquetes = new ArrayList<Paquete>();

        DatabaseReference bd = FirebaseDatabase.getInstance()
                .getReference()
                .child("Usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("paquetes");


        for (Paquete p : paquetes) {
            bd.child(p.getNoGuia()).setValue(true);
            bd.getParent().getParent().getParent().child("Paquetes").child(p.getNoGuia()).setValue(p);
        }

        bd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> paquetes1 = dataSnapshot.getChildren();
                DatabaseReference paqueteRaiz = FirebaseDatabase.getInstance().getReference().child("Paquetes");
                for (DataSnapshot aBoolean : paquetes1) {

                    if ((Boolean) aBoolean.getValue()) {
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

        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

         locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        ubicacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5f, locationListener);
        }



        bd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getContext(),"Child added",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getContext(),dataSnapshot.getKey()+"1",Toast.LENGTH_SHORT).show();
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








        adaptador = new PaqueteAdapterMensajero(getContext(),paquetes,ubicacion);
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
