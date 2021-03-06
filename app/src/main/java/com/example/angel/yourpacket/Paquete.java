package com.example.angel.yourpacket;

import android.icu.text.SimpleDateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.DateTimeKeyListener;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;
import static java.lang.System.runFinalization;

/**
 * Created by angel on 25/6/2017.
 */

public class Paquete implements Parcelable {
    String noGuia;
    ArrayList<String> estado = new ArrayList<>();
    String fecha;
    Double lat = new Double(0);
    Double lng = new Double(0);
    Double latDest = new Double(0);
    Double lngDest = new Double(0);

    public ArrayList<Double>  getUbicacion() {
        ArrayList<Double> ubicacion = new ArrayList<>();
        ubicacion.add(lat);
        ubicacion.add(lng);

        return ubicacion;

    }

    public ArrayList<Double> getDestino(){
        ArrayList<Double> destino = new ArrayList<>();
        destino.add(latDest);
        destino.add(lngDest);

        return destino;
    }

    public void setUbicacion(double v, double v1) {
        this.lat = v;
        this.lng = v1;
    }

    public void setDestino(double v, double v1) {
        this.latDest = v;
        this.lngDest = v1;
    }



    public Paquete(){}

    public Paquete(String noGuia) {
        this.noGuia = noGuia;
        estado.add("Solicitud recibida");
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM h:mm a");

        fecha = format.format(calendar.getTime());

    }

    public String getLastEstado(){
        return estado.get(estado.size() - 1);
    }

    public String getNoGuia() {
        return noGuia;
    }

    public ArrayList<String> getEstado() {
        return estado;
    }

    public String getFecha() {
        return fecha;
    }

    public static final Parcelable.Creator<Paquete> CREATOR = new Creator<Paquete>() {
        @Override
        public Paquete createFromParcel(Parcel source) {
            return new Paquete(source.readString());
        }

        @Override
        public Paquete[] newArray(int size) {
            return new Paquete[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noGuia);
        dest.writeSerializable(estado);
       // dest.writeDouble(lat);
        //dest.writeDouble(lng);
        dest.writeString(fecha);

    }

    public Paquete(Parcel in){
        this.noGuia = in.readString();
        this.estado = (ArrayList<String>) in.readSerializable();
        this.fecha = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        Paquete comp = (Paquete) obj;
        if (this.noGuia.equals((comp.getNoGuia())))
            return true;
        return false;
    }
}
