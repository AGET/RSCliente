package com.aldo.aget.rscliente;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Control.ManipulacionBD;
import com.aldo.aget.rscliente.Control.Receptor;
import com.aldo.aget.rscliente.Control.SQLHelper;
import com.aldo.aget.rscliente.ServicioWeb.Peticion;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by workstation on 16/11/15.
 */
public class Mapa extends FragmentActivity implements OnMapReadyCallback {
    double latitud,longitud;
    Boolean status;
    TextView txtLblMapa;
    String numero;

    public FloatingActionButton fab_mapa;

    ManipulacionBD manejadorBD;

    Receptor receptor;

    String pack = "com.aldo.aget.rscliente";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Bundle parametros = getIntent().getExtras();
        numero = parametros.getString(Configuracion.COLUMNA_GPS_NUMERO);
        latitud = parametros.getDouble(Configuracion.LATITUD);
        longitud = parametros.getDouble(Configuracion.LONGITUD);
        status = parametros.getBoolean(Configuracion.COLUMNA_EMPRESA_STATUS);

        receptor = new Receptor();

        txtLblMapa = (TextView) findViewById(R.id.txtLblMapa);
        if( status ) {
            Configuracion.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.xml_mapa);
            txtLblMapa.setVisibility(View.INVISIBLE);

            peticionRegistroCoordenadas();

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }else{
            txtLblMapa.setVisibility(View.VISIBLE);
        }

        Log.v("AGET-"+pack, String.valueOf(latitud));
        Log.v("AGET-"+pack, String.valueOf(longitud));


        fab_mapa= (FloatingActionButton) findViewById(R.id.fab_mapa);
        fab_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {

        String[] filtroLocal = {SQLHelper.COLUMNA_GPS_DESCRIPCION};
        String[] datosWhere = {numero};
        String descirpcion= (String) manejadorBD.obtenerDatos(SQLHelper.TABLA_GPS, filtroLocal,
                SQLHelper.COLUMNA_GPS_NUMERO,datosWhere).get(0);

        LatLng geo = new LatLng(latitud ,longitud);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(geo)
                .zoom(17)
                // stablecemos el zoom en 19
                .bearing(45)      //Establecemos la orientación con el noreste arriba
                .tilt(50)         //Bajamos el punto de vista de la cámara 70 grados
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        map.addMarker(new MarkerOptions().position(geo).title( descirpcion ));
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.animateCamera(camUpd3);

        /*LatLng sydney = new LatLng(latitud, longitud);
        map.addMarker(new MarkerOptions().position(sydney).title("Ubicacion del GPS"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_USUARIO_ENVIAR_COORDENADAS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptor, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptor);
        //this.finish();

    }


    public void peticionRegistroCoordenadas() {
        manejadorBD = new ManipulacionBD(this);
        String valorEnlace;
        String[] datosWhere = {numero};

        Log.v("AGET","VERIGICAR NUMERO:" + numero);

        final String columnasFiltroLista[] = {Configuracion.COLUMNA_ENLACE_ID,
                Configuracion.COLUMNA_COORDENADA_LONGITUD,Configuracion.COLUMNA_COORDENADA_LATITUD};

        String[] valorFiltroLocal = {SQLHelper.COLUMNA_GPS_ENLACE_ID};

        valorEnlace = (String) manejadorBD.obtenerDatos(SQLHelper.TABLA_GPS, valorFiltroLocal,
                SQLHelper.COLUMNA_GPS_NUMERO,datosWhere).get(0);

        //String [] rec = {"enlace_id"};
        //valorEnlace = (String) (manejadorBD.obtenerDatos("gps", rec, "numero",datosWhere)).get(0);

        //manejadorBD.obtenerDatosGPSNumero();

//        Log.v("AGET-SQLITE","NUMERO:"+x);

        String[] valorFiltroRemoto = {valorEnlace, String.valueOf(longitud), String.valueOf(latitud)};

        new Peticion(Configuracion.context, Configuracion.INTENT_USUARIO_ENVIAR_COORDENADAS, columnasFiltroLista,
                valorFiltroRemoto, Configuracion.TABLA_USUARIOS, null, false)
                .execute(Configuracion.PETICION_COORDENADAS_REGISTRO, "post");
    }



}