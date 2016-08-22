package com.aldo.aget.rscliente.Vistas;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Mapa;
import com.aldo.aget.rscliente.R;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class DetalleGps extends AppCompatActivity {
    String enlaceId = "";
    String idGps = "";
    String imei = "";
    String numero = "";
    String descripcion = "";


    TextView txtImeiGps;
    EditText edtDescripcion;
    public FloatingActionButton fab_obtnerCooredenada,fab_mapa;

    MenuItem menuCambiarContrase_na;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_gps);

        Bundle bundle = getIntent().getExtras();
        enlaceId = bundle.getString(Configuracion.COLUMNA_ENLACE_ID);
        idGps = bundle.getString(Configuracion.COLUMNA_GPS_ID);
        imei = bundle.getString(Configuracion.COLUMNA_GPS_IMEI);
        numero = bundle.getString(Configuracion.COLUMNA_GPS_NUMERO);
        descripcion = bundle.getString(Configuracion.COLUMNA_GPS_DESCRIPCION);

        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dispositivo Gps");

        Log.v("AGET-Bienvenida", idGps + " " + imei + " " + numero + " " + descripcion);

        txtImeiGps = (TextView) findViewById(R.id.txtImeiGps);
        edtDescripcion = (EditText) findViewById(R.id.edtDescripcion);

        txtImeiGps.setText(imei);
        edtDescripcion.setText(descripcion);
        edtDescripcion.setEnabled(false);

        fab_obtnerCooredenada = (FloatingActionButton) findViewById(R.id.fab_obtnerCooredenada);
        fab_obtnerCooredenada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtener(numero);
            }
        });


        fab_mapa= (FloatingActionButton) findViewById(R.id.fab_mapa);
        fab_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarMapa();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        this.menuCambiarContrase_na = menu.findItem(R.id.cambiar_contrase_na);
        // Verificación de visibilidad acción eliminar
//        if (idEmpresa != null) {
        menuCambiarContrase_na.setVisible(true);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.cambiar_contrase_na:
                mostrarDialogoCambiarPass();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void obtener(String numero) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + numero));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            startActivity(callIntent);
            //cn = new ComponentName(getApplicationContext(), Receiver.class);
        } catch (ActivityNotFoundException activityException) {
            Log.e("dialing-example", "Call failed", activityException);
        }
    }

    public void mostrarDialogoCambiarPass(){

    }

    public void mostrarMapa(){
        Intent inten = new Intent(this, Mapa.class);
        inten.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        inten.putExtra(Configuracion.COLUMNA_GPS_NUMERO, numero);
        inten.putExtra(Configuracion.LATITUD, 17.531895);
        inten.putExtra(Configuracion.LONGITUD, -99.499745);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_STATUS, true);
        Configuracion.context.startActivity(inten);
    }

}
