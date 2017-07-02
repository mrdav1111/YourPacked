package com.example.angel.yourpacket;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by angel on 25/6/2017.
 */

public class PaqueteAdapterMensajero extends BaseAdapter {

    private Context context;
    private ArrayList<Paquete> paquetes;
    private Location ubicacion;

    public PaqueteAdapterMensajero(Context context, ArrayList<Paquete> p, Location ubicacion) {
        this.context = context;
        paquetes = p;
        this.ubicacion = ubicacion;
    }

    public void actualizar(){
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return paquetes.size();
    }

    @Override
    public Object getItem(int position) {
        return paquetes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_row_mensajero, parent, false);
        }

        //Obtener paquete actual
        Paquete actualPaquete = (Paquete) getItem(position);

        //Creando referencias del diseño en codigo
        TextView noGuia, estado, fecha, distancia;

        noGuia = (TextView)
                convertView.findViewById(R.id.NoGuia);
        estado = (TextView)
                convertView.findViewById(R.id.estadoPaquete);
        fecha = (TextView)
                convertView.findViewById(R.id.fechaPaquete);
        distancia = (TextView)
                convertView.findViewById(R.id.distancia);

        //asignando valores desde el objeto hacia la vista
        noGuia.setText(actualPaquete.getNoGuia());
        estado.setText(actualPaquete.getLastEstado());
        fecha.setText(actualPaquete.getFecha());



        float[] results = new float[1];
        Location.distanceBetween(ubicacion.getLatitude(),ubicacion.getLongitude(),actualPaquete.lat,actualPaquete.lng,results);

        distancia.setText(Float.toString(results[0]));




        return convertView;
    }
}
