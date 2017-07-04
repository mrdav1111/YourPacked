package com.example.angel.yourpacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.math.BigDecimal;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.w3c.dom.Text;

import static com.example.angel.yourpacket.R.id.container;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnviarPaquetepago.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnviarPaquetepago#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnviarPaquetepago extends Fragment {

    //PayPalConfiguration m_configuration;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView m_response;
    PayPalConfiguration m_configuration;
    String m_paypalClientId;
    Intent m_service;
    int m_paypalRequestCode = 999;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    private static final String CONFIG_CLIENT_ID = "AWfB5He94tzPXwWqSlgWPEFW9ssWRK858J9G9tnE8Bc_VFXVMXiyrN-i1_CpGME_T6iVstME5X2rNCs3";
    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)

            // configuracion minima del ente
            .merchantName("YourPacked")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.mi_tienda.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.mi_tienda.com/legal"));

    PayPalPayment thingToBuy;


    private OnFragmentInteractionListener mListener;
    private Paquete paquete;


    public EnviarPaquetepago() {

        // Required empty public constructor
    }

    final String[] ultGuia = new String[1];

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnviarPaquetepago.
     */
    // TODO: Rename and change types and number of parameters
    public static EnviarPaquetepago newInstance(String param1, String param2) {


        EnviarPaquetepago fragment = new EnviarPaquetepago();
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enviar_paquetepago, container, false);
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
        view.findViewById(R.id.pagar).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thingToBuy = new PayPalPayment(new BigDecimal("50"), "USD",
                        "pelicula", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getActivity(),
                        PaymentActivity.class);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {

                    // informacion extra del pedido
                    System.out.println(confirm.toJSONObject().toString(4));
                    System.out.println(confirm.getPayment().toJSONObject()
                            .toString(4));

                    Toast.makeText(getContext(), "Orden procesada",
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            System.out.println("El usuario canceló el pago");
        }

        obtenerUltimoPaquete();

        }


    public void registrarPaquete(){


        int guia = Integer.parseInt(ultGuia[0].substring(2));
        String nextGuia = "YP" +String.format("%5s",guia+1).replace(' ','0');
        paquete = new Paquete(nextGuia);
        Location ubicacion = obtenerUbicacion();
        paquete.setUbicacion(ubicacion.getLatitude(),ubicacion.getLongitude());
        EnviarPaquete Actividad = (EnviarPaquete)getActivity();
        PlaceholderFragment fragment1 = Actividad.getPlaceholder();
        Marker marker = fragment1.getMarker();
        paquete.setDestino(marker.getPosition().latitude,marker.getPosition().longitude);

        DatabaseReference Paquetes = FirebaseDatabase.getInstance().getReference();
        Paquetes.child("Paquetes").child(paquete.getNoGuia()).setValue(paquete);
        Paquetes.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("paquetes").child(paquete.getNoGuia()).setValue(true);
        Paquetes.child("Usuarios").child("WPmhJUKcaFS4dgjokVju2AIS7O43").child("paquetes").child(paquete.getNoGuia()).setValue(true);





    }

    public Location obtenerUbicacion(){

         Location ubicacion;

        LocationManager locat = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        ubicacion = locat.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return ubicacion;



    }

    public void obtenerUltimoPaquete(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Paquetes");
        Query paquetes = databaseReference.orderByKey().limitToLast(1);



        paquetes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()){
                    ultGuia[0] = child.getKey();
                }

                registrarPaquete();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
