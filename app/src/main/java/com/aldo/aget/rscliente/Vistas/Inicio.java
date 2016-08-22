package com.aldo.aget.rscliente.Vistas;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Configuracion.GPS;
import com.aldo.aget.rscliente.Control.ManipulacionBD;
import com.aldo.aget.rscliente.Control.Receptor;
import com.aldo.aget.rscliente.Control.SQLHelper;
import com.aldo.aget.rscliente.R;
import com.aldo.aget.rscliente.ServicioWeb.Peticion;

import java.util.ArrayList;

public class Inicio extends AppCompatActivity implements AdapterView.OnItemClickListener {

    static ListView lista;
    static ArrayAdapter adaptador;
    private static ProgressBar progressBar;
    static ArrayList datos;
    final long DURACION = 1300;

    static ArrayList<GPS> datosGps;

    public ManipulacionBD managerBD;

    Receptor receptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bienvenido");

        Configuracion.contextInicio = this;

        lista = (ListView) findViewById(R.id.lista_gps);
        progressBar = (ProgressBar) findViewById(R.id.barra);
        lista.setOnItemClickListener(this);

        receptor = new Receptor();

        peticionLista();


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_USUARIO_LISTA_GPS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptor, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptor);
        //this.finish();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String marcado = (String) lista.getItemAtPosition(position);
//        Snackbar.make(view, "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//        marcado = (String) ((ArrayList) datos.get(position)).get(0);
//        Log.v("AGET-Enviado", marcado);
//        actividadGps(marcado);


//        if (position == 0) {
//            Log.v("AGET-NULO", "click en cabecera");
//        } else {
        //Alternativa 1:
        String opcionSeleccionada =
                (String)((GPS) parent.getItemAtPosition(position)).getId();
        Snackbar.make(view, "Ha marcado el item " + position + " " + opcionSeleccionada, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Log.v("AGET-idSeleccionado", opcionSeleccionada);
        mostrarDetalleGPs(
                (String)((GPS) parent.getItemAtPosition(position)).getEnlaceId(),
                (String)((GPS) parent.getItemAtPosition(position)).getId(),
                (String)((GPS) parent.getItemAtPosition(position)).getImei(),
                (String)((GPS) parent.getItemAtPosition(position)).getNumero(),
                (String)((GPS) parent.getItemAtPosition(position)).getDescripcion());

//        }
    }


    public void peticionLista() {

        mostrarProgreso(true);
        managerBD = new ManipulacionBD(this);


        final String tablaLista = Configuracion.TABLA_USUARIOS;

        final String columnasFiltroLista[] = {Configuracion.COLUMNA_USUARIO_ID};

        String[] valorFiltro = {SQLHelper.COLUMNA_USUARIO_ID};

        valorFiltro[0] = (String) (managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, valorFiltro, null, null)).get(0);

        Log.v("AGET-filtro", "valor:" + valorFiltro[0]);

        final String columnasLista[] = {Configuracion.COLUMNA_ENLACE_ID, Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_USUARIO_NOMBRE,
                Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION,
                Configuracion.COLUMNA_GPS_DEPARTAMENTO};

        new Peticion(Configuracion.context, Configuracion.INTENT_USUARIO_LISTA_GPS, columnasFiltroLista, valorFiltro, tablaLista, columnasLista, true)
                .execute(Configuracion.PETICION_USUARIO_LISTAR_GPS, "post");
    }

    public static void actualizar(ArrayList datosMultiples) {
        datosGps = new ArrayList<GPS>();

//        ArrayList nombres = new ArrayList();

        for (int i = 0; i < datosMultiples.size() - 1; i++) {
            //nombres.add((String) "   " + ((ArrayList) datosMultiples.get(i)).get(5) + " || " + ((ArrayList) datosMultiples.get(i)).get(6));
            datosGps.add(new GPS(
                    (String) ((ArrayList) datosMultiples.get(i)).get(0),
                    (String) ((ArrayList) datosMultiples.get(i)).get(3),
                    (String) ((ArrayList) datosMultiples.get(i)).get(4),
                    (String) ((ArrayList) datosMultiples.get(i)).get(5),
                    (String) ((ArrayList) datosMultiples.get(i)).get(6),
                    null));
        }

        AdaptadorGPS adaptador = new AdaptadorGPS(Configuracion.contextInicio, datosGps);

        //adaptador = new ArrayAdapter<String>(Configuracion.contextInicio, android.R.layout.simple_list_item_1, nombres);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //adaptador.insert(grupo, 0);
        adaptador.notifyDataSetChanged();
    }

    public static void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    public static void actividadGps(String imei) {
        Intent actividad = new Intent(Configuracion.context, DetalleGps.class);
        actividad.putExtra(Configuracion.COLUMNA_GPS_ID, imei);
        Configuracion.context.startActivity(actividad);
    }


    static class AdaptadorGPS extends ArrayAdapter<GPS> {
        ArrayList<GPS> elementos;

        public AdaptadorGPS(Context context, ArrayList<GPS> datos) {
            super(context, R.layout.listitem_gps, datos);
            elementos = datos;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            View item = inflater.inflate(R.layout.listitem_titular, null);
//
//            TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
//            lblTitulo.setText(datos[position].getTitulo());
//
//            TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
//            lblSubtitulo.setText(datos[position].getSubtitulo());
//
//            return(item);

            //Optimizado
            View item = convertView;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                item = inflater.inflate(R.layout.listitem_gps, null);

                holder = new ViewHolder();
                holder.imei = (TextView) item.findViewById(R.id.txtImei);
                holder.numero = (TextView) item.findViewById(R.id.txtNumero);
                holder.descripcion = (TextView) item.findViewById(R.id.txtDescripcion);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            holder.imei.setText(((GPS) elementos.get(position)).getImei());
            holder.numero.setText(((GPS) elementos.get(position)).getNumero());
            holder.descripcion.setText(((GPS) elementos.get(position)).getDescripcion());

            return (item);
        }
    }

    static class ViewHolder {
        TextView imei;
        TextView numero;
        TextView descripcion;
    }

    public void mostrarDetalleGPs(String enlaceId, String idGps, String imei, String numero, String descripcion) {
        Intent inten = new Intent(this, DetalleGps.class);
        inten.putExtra(Configuracion.COLUMNA_ENLACE_ID, enlaceId);
        inten.putExtra(Configuracion.COLUMNA_GPS_ID, idGps);
        inten.putExtra(Configuracion.COLUMNA_GPS_IMEI, imei);
        inten.putExtra(Configuracion.COLUMNA_GPS_NUMERO, numero);
        inten.putExtra(Configuracion.COLUMNA_GPS_DESCRIPCION, descripcion);
        Configuracion.context.startActivity(inten);
    }
}