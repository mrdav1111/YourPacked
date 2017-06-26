package com.example.angel.yourpacket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by angel on 25/6/2017.
 */

public class PaqueteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Paquete> paquetes;

    public PaqueteAdapter(Context context, ArrayList<Paquete> p) {
        this.context = context;
        paquetes = p;
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
                    inflate(R.layout.item_row, parent, false);
        }

        //Obtener paquete actual
        Paquete actualPaquete = (Paquete) getItem(position);

        //Creando referencias del diseño en codigo
        TextView noGuia, estado, fecha;

        noGuia = (TextView)
                convertView.findViewById(R.id.NoGuia);
        estado = (TextView)
                convertView.findViewById(R.id.estadoPaquete);
        fecha = (TextView)
                convertView.findViewById(R.id.fechaPaquete);

        //asignando valores desde el objeto hacia la vista
        noGuia.setText(actualPaquete.getNoGuia());
        estado.setText(actualPaquete.getLastEstado());
        fecha.setText(actualPaquete.getFecha());


        return convertView;
    }
}
