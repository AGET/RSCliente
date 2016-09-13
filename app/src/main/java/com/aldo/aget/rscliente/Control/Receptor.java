package com.aldo.aget.rscliente.Control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Configuracion.GPS;
import com.aldo.aget.rscliente.Configuracion.Utilidades;
import com.aldo.aget.rscliente.MainActivity;
import com.aldo.aget.rscliente.Vistas.Inicio;

import java.util.ArrayList;

/**
 * Created by Work on 14/08/16.
 */
public class Receptor extends BroadcastReceiver {
    int cantidadIntentos = 0;
    ArrayList datos;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.v("AGET", "BROAD RECIBIDO LOGIN en Usuarios");
        //Log.v("AGET-inten",intent.getAction());


        Log.v("AGET-intenConf", Configuracion.INTENT_MAINACTIVITY_LOGIN);
        switch (intent.getAction()) {
            case Configuracion.INTENT_MAINACTIVITY_LOGIN:
                MainActivity.mostrarProgress(false);
                Log.v("AGET", "BROAD RECIBIDO LOGIN en Usuarios");
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    MainActivity.ocultarFormulario();
                    recuperarDatos(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                    guardarDatosUsuario();
                    mostrarInicio();
                } else if (mensaje.equalsIgnoreCase("La empresa no esta habilitada")) {
                    mostrarMensaje(mensaje,Configuracion.coordinatorLayout);
                } else if (mensaje.equalsIgnoreCase("Datos incorrectos")) {
                    cantidadIntentos++;
                    mostrarMensaje(mensaje,Configuracion.coordinatorLayout);
                    if (cantidadIntentos == 3) {
                        cantidadIntentos = 0;
                        MainActivity.mostrarRecuperarClave();
                    }
                }
                break;
            case Configuracion.INTENT_MAINACTIVITY_COMPROBAR_LOGIN:
                Log.v("AGET", "BROAD RECIBIDO COMPROVAR LOGIN en Usuarios");
                mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    MainActivity.ocultarFormulario();
                    //recuperarDatos(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
//                    guardarDatos();
                    recuperarDatos(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                    guardarDatosUsuario();
                    mostrarInicio();
                } else if (mensaje.equalsIgnoreCase("La empresa no esta habilitada") || mensaje.equalsIgnoreCase("Datos incorrectos")) {
                    cambiarEstado();
                    mostrarMensaje(mensaje,Configuracion.coordinatorLayout);

                }

                break;
            case Configuracion.INTENT_USUARIO_LISTA_GPS:
                Log.v("AGET", "BROAD RECIBIDO GPS de Usuarios");
                Inicio.mostrarProgreso(false);
                mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    ArrayList datosGPS = intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST);
                    guardarDatosGPS(datosGPS);
                    Inicio.actualizar(datosGPS);
                }
                break;
            case Configuracion.INTENT_USUARIO_ENVIAR_COORDENADAS:
                Log.v("AGET", "BROAD RECIBIDO GPS de Usuarios");
                mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                if(mensaje.equalsIgnoreCase("Registro con exito!")){
                    mostrarMensaje("Coordenadas enviadas",Configuracion.coordinatorLayout);
                }
                break;
            case Configuracion.INTENT_MAINACTIVITY_RECUPERAR:
                Log.v("AGET", "BROAD RECIBIDO de recuperacion");
                mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                if(mensaje.equalsIgnoreCase("correo enviado")){
                    mostrarMensaje("Un mensaje se ha enviado a su correo con la clave",Configuracion.coordinatorLayout);
                }else{
                    mostrarMensaje("Ha ocurrido un error, intentelo nuevamente",Configuracion.coordinatorLayout);
                }
                break;
            case Configuracion.INTENT_USUARIO_CAMBIAR_CLAVE:
                mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                if(mensaje.equalsIgnoreCase("Registro actualizado correctamente")){
                    actualizarClaveLocal();
                    mostrarMensaje("Se ha modificado la clave",Configuracion.coordinatorLayoutInicio);
                }else if(mensaje.equalsIgnoreCase("El usuario al que intentas acceder no existe")){
                    mostrarMensaje("No se modificado la clave",Configuracion.coordinatorLayoutInicio);
                }
                break;
            case Configuracion.INTENT_INICIO_RECUPERAR_CLAVE:
                Log.v("AGET", "BROAD RECIBIDO de recuperacion");
                mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                if(mensaje.equalsIgnoreCase("correo enviado")){
                    mostrarMensaje("Un mensaje se ha enviado a su correo con la clave",Configuracion.coordinatorLayoutInicio);
                }else{
                    mostrarMensaje("Ha ocurrido un error, intentelo nuevamente",Configuracion.coordinatorLayoutInicio);
                }
                break;
        }
    }

    public void recuperarDatos(ArrayList elementos) {
        //Log.v("AGET-LOGIN:", "" + datos.size());
        this.datos = new ArrayList();
        //Log.v("AGET-ItemLogin:", "" + ((ArrayList) datos.get(0)).size());
        for (int i = 0; i < elementos.size(); i++) {
            Log.v("AGET-include:", "" + i);
            datos.add(elementos.get(i));
        }
        for (int i = 0; i < elementos.size(); i++) {
            Log.v("AGET-DATO", "" + this.datos.get(i));
        }
    }

    public void mostrarMensaje(String mensaje,View vista) {
        //Snackbar.make(Configuracion.parentLayout, mensaje, Snackbar.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
        Log.v("AGET-MSN", mensaje);
    }

    public void guardarDatosUsuario() {
        ManipulacionBD managerBD = new ManipulacionBD(Configuracion.context);

        String[] elementos = new String[datos.size()];
        elementos = (String[]) datos.toArray(elementos);

        managerBD.eliminarTodo(SQLHelper.TABLA_USUARIOS);

        managerBD.insertar(SQLHelper.TABLA_USUARIOS, elementos);
    }


    public void guardarDatosGPS(ArrayList lista) {
        ManipulacionBD managerBD = new ManipulacionBD(Configuracion.context);
        ArrayList aux;

        managerBD.eliminarTodo(SQLHelper.TABLA_GPS);

        for (int i = 0; i < lista.size() - 1; i++) {
            aux = new ArrayList();
            //nombres.add((String) "   " + ((ArrayList) datosMultiples.get(i)).get(5) + " || " + ((ArrayList) datosMultiples.get(i)).get(6));
            for (int j = 0 ; j< ((ArrayList)lista.get(i)).size();j++){
                aux.add((String) ((ArrayList) lista.get(i)).get(j));
            }
            String[] elementos = new String[aux.size()];
            elementos = (String[]) aux.toArray(elementos);
            managerBD.insertar(SQLHelper.TABLA_GPS, elementos);
        }

    }

    public void mostrarInicio() {
        Intent inten = new Intent(Configuracion.context, Inicio.class);
        inten.putExtra(Configuracion.COLUMNA_USUARIO_ID, datos.get(0).toString());
        inten.putExtra(Configuracion.COLUMNA_USUARIO_NOMBRE, datos.get(1).toString());
        Configuracion.context.startActivity(inten);
    }

    public void cambiarEstado(){
        ManipulacionBD managerBD = new ManipulacionBD(Configuracion.context.getApplicationContext());
        String estado;
        String idSqlite;
        String[] datos = {SQLHelper.COLUMNA_USUARIO_EMPRESA_STATUS,SQLHelper.COLUMNA_GENERICA_ID};
        if( managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, datos, null, null) != null){
            estado = (String)(managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, datos, null, null)).get(0);
            idSqlite = (String)(managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, datos, null, null)).get(1);

            if(estado.equalsIgnoreCase("1")){
                estado = "0";
                managerBD.actualizarEstadoUsuario(SQLHelper.TABLA_USUARIOS,SQLHelper.COLUMNA_USUARIO_EMPRESA_STATUS,
                        estado,SQLHelper.COLUMNA_GENERICA_ID,idSqlite);
            }
            //return true;
        }else{
            //return false;
        }
    }

    public void actualizarClaveLocal(){
        ManipulacionBD managerBD = new ManipulacionBD(Configuracion.context.getApplicationContext());

        String[] valorFiltro = {SQLHelper.COLUMNA_USUARIO_ID};
        String idUsuario= (String) (managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, valorFiltro, null, null)).get(0);

        String[] valorFiltro2 = {SQLHelper.COLUMNA_USUARIO_ID};
        managerBD.actualizarUnDato(SQLHelper.TABLA_USUARIOS,SQLHelper.COLUMNA_USUARIO_CONTRASE_NA,Configuracion.claveAux,
                SQLHelper.COLUMNA_USUARIO_ID,idUsuario);
    }

}
