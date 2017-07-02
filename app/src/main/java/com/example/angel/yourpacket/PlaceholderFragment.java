package com.example.angel.yourpacket;

/**
 * Created by angel on 1/7/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements OnMapReadyCallback {
    Button button5;

    ImageSwitcher imageSwitcher;
    Integer[] images = {R.drawable.icon, R.drawable.user};

    int i = 0;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    MapView map;
    GoogleMap mapa;
    MarkerOptions markerOptions;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enviar_paquete, container, false);
        map = (MapView) rootView.findViewById(R.id.mapView3);

        map.onCreate(savedInstanceState);
        map.getMapAsync(this);


        imageSwitcher = (ImageSwitcher) rootView.findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(imageView.getScaleType());
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.in);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.out);

        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);



        button5 = (Button) rootView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < images.length - 1) {
                    i++;
                    imageSwitcher.setImageResource(images[i]);
                }
                else {
                    i = 0;
                    imageSwitcher.setImageResource(images[i]);
                }


           /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/



                }
            });
        return rootView;
        }



    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        LatLng center = googleMap.getCameraPosition().target;

        markerOptions = new MarkerOptions()
                .title("Prueba")
                .snippet("Ubicacion actual de tu paquete")
                .position(center);
        final Marker m = googleMap.addMarker(markerOptions);


        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng center = googleMap.getCameraPosition().target;
                m.setPosition(center);

            }
        });

    }
}
