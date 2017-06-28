package com.example.angel.yourpacket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.angel.yourpacket.R.id.editText;
import static com.example.angel.yourpacket.R.id.textView3;


public class Contactanos extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactanos);

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText("Su Mensaje ha sido enviado!");
        EditText editText = (EditText) findViewById(R.id.editText);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Mensaje").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(editText.getText().toString());

    }
}
