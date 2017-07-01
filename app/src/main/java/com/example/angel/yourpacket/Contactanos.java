package com.example.angel.yourpacket;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import static com.example.angel.yourpacket.R.id.textView3;


public class Contactanos extends Fragment{

    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_contactanos,container,false);

        Button button3 = (Button) v.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView4 = (TextView) v.findViewById(R.id.textView4);
                textView4.setText("Su Mensaje ha sido enviado!");
            }
        });

        return v;
    }



}
