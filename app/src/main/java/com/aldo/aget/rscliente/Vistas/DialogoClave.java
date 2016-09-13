package com.aldo.aget.rscliente.Vistas;

/**
 * Created by Work on 13/09/16.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Control.ManipulacionBD;
import com.aldo.aget.rscliente.Control.SQLHelper;
import com.aldo.aget.rscliente.R;
import com.aldo.aget.rscliente.ServicioWeb.Peticion;

/**
 * Fragmento con diálogo básico
 */
public class DialogoClave extends DialogFragment {

    String claveAnterior = "", clave = "", confirmarClave = "", usuario_id;

    ManipulacionBD managerBD;

    @SuppressLint("ValidFragment")
    public DialogoClave() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogoClave();
    }


    /**
     * Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */
//    public AlertDialog createSimpleDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("Titulo")
//                .setMessage("El Mensaje para el usuario")
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Acciones
//                            }
//                        })
//                .setNegativeButton("CANCELAR",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Acciones
//                            }
//                        });
//
//        return builder.create();
//    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog crearDialogoClave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialogo_clave, null);

        builder.setView(v);

        Button btnCambiarClave = (Button) v.findViewById(R.id.btn_cambiar_clave);
        final EditText edtClaveAnterior = (EditText) v.findViewById(R.id.edt_clave_anterior);
        final EditText edtClaveNueva = (EditText) v.findViewById(R.id.edt_nueva_clave);
        final EditText edtConfirmarClave = (EditText) v.findViewById(R.id.edt_confirmar_clave);
        final TextView txtAviso = (TextView) v.findViewById(R.id.txt_aviso);
        final TextView txtOlvidoClave = (TextView) v.findViewById(R.id.txt_olvidar_clave);

        btnCambiarClave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        claveAnterior = edtClaveAnterior.getText().toString();
                        clave = edtClaveNueva.getText().toString();
                        confirmarClave = edtConfirmarClave.getText().toString();

                        if (comprobar_clave(claveAnterior)) {
                            if (clave.equalsIgnoreCase(confirmarClave)) {
                                peticionCambiarClave();
                            } else {
                                txtAviso.setText("La nueva contraseña no conincide");
                                edtClaveNueva.setHint("Estos campos no coinciden");
                                edtConfirmarClave.setHint("Estos campos no coinciden");

                            }
                        } else {
                            txtAviso.setText("Error en la contraseña actual");
                            edtClaveAnterior.setHint("Error en la contraseña");
                        }

//                        dismiss();
                    }
                }
        );

        txtOlvidoClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("AGET-CLAVE", "Olvido su clave");
                if (txtOlvidoClave.getText().toString().equalsIgnoreCase("Olvide mi contraseña")) {
                    txtOlvidoClave.setTextColor(Color.RED);
                    peticionRecuperarClave();
                } else {
                    txtOlvidoClave.setTextColor(Color.BLACK);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        txtOlvidoClave.setTextColor(Color.BLACK);
                        txtOlvidoClave.setText("Se ha enviado la clave a su correo");
                    }
                }, 1000);
            }
        });
        return builder.create();
    }

    public boolean comprobar_clave(String claveAnterior) {
        String claveActual = "";
        managerBD = new ManipulacionBD(Configuracion.contextInicio);

        String[] valorFiltro = {SQLHelper.COLUMNA_USUARIO_CONTRASE_NA};
        claveActual = (String) (managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, valorFiltro, null, null)).get(0);

        if (claveActual.equalsIgnoreCase(claveAnterior))
            return true;
        else
            return false;
    }

    public void peticionCambiarClave() {
        managerBD = new ManipulacionBD(Configuracion.contextInicio);
        String[] valorFiltro = {SQLHelper.COLUMNA_USUARIO_ID};
        String idUsuario = (String) (managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, valorFiltro, null, null)).get(0);

        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_USUARIO_CONTRASE_NA};
        String[] valorFiltroR = {idUsuario, clave};

        new Peticion(Configuracion.context, Configuracion.INTENT_USUARIO_CAMBIAR_CLAVE, columnasFiltro, valorFiltroR, Configuracion.TABLA_USUARIOS, null, false)
                .execute(Configuracion.PETICION_USUARIO_CAMBIAR_CLAVE, "post");

        Configuracion.claveAux = clave;
        dismiss();
    }

    public void peticionRecuperarClave() {
        managerBD = new ManipulacionBD(Configuracion.contextInicio);
        String[] valorFiltroLocalEmail = {SQLHelper.COLUMNA_USUARIO_CORREO};
        String email = (String) (managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, valorFiltroLocalEmail, null, null)).get(0);

        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_CORREO};

        String[] valorFiltro = {email};

        new Peticion(Configuracion.context, Configuracion.INTENT_MAINACTIVITY_RECUPERAR, columnasFiltro, valorFiltro, Configuracion.TABLA_CORREO, null, false)
                .execute(Configuracion.PETICION_RECUPERAR_CLAVE, "post");
    }
}