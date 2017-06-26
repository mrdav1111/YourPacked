package com.example.angel.yourpacket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView email;
    PaqueteAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


      View navheaderview = navigationView.getHeaderView(0);
        TextView email = (TextView) navheaderview.findViewById(R.id.textEmailView);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        final ListView paquete = (ListView) findViewById(R.id.listaPaquetes);

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
                Toast.makeText(getApplicationContext(),"Child added",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getApplicationContext(),dataSnapshot.getKey()+"1",Toast.LENGTH_SHORT).show();
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



        adaptador = new PaqueteAdapter(Main2Activity.this,paquetes);
        paquete.setAdapter(adaptador);


        paquete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paquete seleccionado = paquetes.get((int)id);
                Intent detallePaquete = new Intent(Main2Activity.this,DetallePaquete.class);
                detallePaquete.putExtra("paquete", seleccionado);
                startActivity(detallePaquete);

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(Main2Activity.this,LoginActivity.class);
            //Log.i("i", user.getEmail());
            Main2Activity.this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
