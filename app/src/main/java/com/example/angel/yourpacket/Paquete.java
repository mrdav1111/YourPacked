package com.example.angel.yourpacket;

import android.icu.text.SimpleDateFormat;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.DateTimeKeyListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.out;

/**
 * Created by angel on 25/6/2017.
 */

public class Paquete implements Parcelable {
    String noGuia;
    ArrayList<String> estado = new ArrayList<>();
    Date fecha;

    public Paquete(String noGuia) {
        this.noGuia = noGuia;
        estado.add("Solicitud recibida");
        fecha = Calendar.getInstance().getTime();

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
        return "hoy";
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
        dest.writeSerializable(fecha);
    }

    public Paquete(Parcel in){
        this.noGuia = in.readString();
        this.estado = (ArrayList<String>) in.readSerializable();
        this.fecha = (Date) in.readSerializable();
    }


}
