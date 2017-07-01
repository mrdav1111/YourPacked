package com.example.angel.yourpacket;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class DetallePaquete extends AppCompatActivity implements OnMapReadyCallback {
    TextView noGuia, mensajero, fecha;
    MapView mapView;
    GoogleMap map;
    Paquete paqueteSeleccionado;
    DatabaseReference objPaquete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        paqueteSeleccionado = (Paquete) getIntent().getParcelableExtra("paquete");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paquete);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        noGuia = (TextView) findViewById(R.id.NoGuia);
        fecha = (TextView) findViewById(R.id.horaSalida);
        mensajero = (TextView) findViewById(R.id.mensajero);

        objPaquete = FirebaseDatabase.getInstance().getReference().child("Paquetes").child(paqueteSeleccionado.getNoGuia());

        objPaquete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paqueteSeleccionado = dataSnapshot.getValue(Paquete.class);
                mapView.getMapAsync(DetallePaquete.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        noGuia.setText(paqueteSeleccionado.getNoGuia());
        fecha.setText(paqueteSeleccionado.getFecha());


    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {


        map = googleMap;
        final LatLng sydney = new LatLng(paqueteSeleccionado.getUbicacion().get(0), paqueteSeleccionado.getUbicacion().get(1));

        objPaquete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map.clear();
                Paquete paqueteActualizado = dataSnapshot.getValue(Paquete.class);
                LatLng mov = new LatLng(paqueteActualizado.getUbicacion().get(0), paqueteActualizado.getUbicacion().get(1));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mov, 15));

                map.addMarker(new MarkerOptions()
                        .title(paqueteSeleccionado.getNoGuia())
                        .snippet("Ubicacion actual de tu paquete")
                        .position(mov));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        map.addMarker(new MarkerOptions()
                .title(paqueteSeleccionado.getNoGuia())
                .snippet("Ubicacion actual de tu paquete")
                .position(sydney));

    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);

    }
}
