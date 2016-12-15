package com.aldo.aget.rscliente;

/**
 * Created by workstation on 26/10/15.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Control.ManipulacionBD;
import com.aldo.aget.rscliente.Control.SQLHelper;

import java.util.ArrayList;

public class RecibirSMS extends BroadcastReceiver {
    int latitug = 0, logintud = 0;
    Context contexto;
    ManipulacionBD managerBD;
    String numero;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] smss = null;
        String cadenaMensaje = "";
        String mensaje = "";
        String numeroRemitente = null;

        contexto = context;
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            smss = new SmsMessage[pdus.length];
            for (int i = 0; i < smss.length; i++) {
                smss[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                ///mensaje +="SMS de: " + smss[i].getOriginatingAddress();

                numeroRemitente = smss[i].getOriginatingAddress();

                Log.v("AGET-SMS", "de: " + smss[i].getOriginatingAddress());
                Log.v("AGET-SMS", "de: " + smss[i].getDisplayOriginatingAddress());
                Log.v("AGET-SMS", "txt:" + smss[i].getMessageBody().toString());

                cadenaMensaje += "SMS de GPS";
                cadenaMensaje += ": ";
                cadenaMensaje += smss[i].getMessageBody().toString();
                mensaje += smss[i].getMessageBody().toString();
                cadenaMensaje += "\n";
            }
            numeroRemitente = numeroRemitente.replaceAll("\\s", "");
            numeroRemitente = numeroRemitente.replaceAll("-", "");
            if (numeroRemitente.substring(0, 1).equalsIgnoreCase("+")) {
                numeroRemitente = numeroRemitente.substring(3);
            }
            if (buscarNumeroEnBD(numeroRemitente)) {
                char caracteres[] = mensaje.toCharArray();
                double lat = 0;
                lat = getLatitud(caracteres);
                double lon = 0;
                lon = getLongitud(caracteres);
                if ((lat != 0 || lat != 0.0) && lon != 0 || lon != 0.0) {
                    Intent i = new Intent().setClass(contexto, Mapa.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Configuracion.COLUMNA_GPS_NUMERO, numero);
                    i.putExtra(Configuracion.LATITUD, lat);
                    i.putExtra(Configuracion.LONGITUD, lon);
                    i.putExtra(Configuracion.COLUMNA_EMPRESA_STATUS, comporbarStatus());
//                public void mostrarMapa(){
//                    Intent inten = new Intent(this, Mapa.class);
//                    inten.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    inten.putExtra(Configuracion.COLUMNA_GPS_NUMERO, numero);
//                    inten.putExtra(Configuracion.LATITUD, 17.531895);
//                    inten.putExtra(Configuracion.LONGITUD, -99.499745);
//                    inten.putExtra(Configuracion.COLUMNA_EMPRESA_STATUS, true);
//                    Configuracion.context.startActivity(inten);
//                }

// Launch the new activity and add the additional flags to the intent
                    contexto.getApplicationContext().startActivity(i);
                    Log.v("AGET-MAPA", "MAPA LANZADO");

                }
            }
            /*Toast aviso = Toast.makeText(context.getApplicationContext(), lat + " - " + lon, Toast.LENGTH_LONG);
            aviso.setGravity(Gravity.TOP | Gravity.LEFT, 10, 100);
            aviso.show();*/
        }
    }

    public double[] getLocalizacion(char[] caracteres) {
        double coordenadas[] = new double[2];
        char auxiliar = 0;
        String cadenaLatitud = "", cadenaLongitud = "";
        boolean primerDato = true;

        for (int i = 0; i < caracteres.length; i++) {
            auxiliar = caracteres[i];
            if (auxiliar == ':') {
                Log.v("punto", "puntos entontrado");
                if (primerDato) {
                    if (Character.isDigit(auxiliar)) {
                        Log.v("punto", "num entontrado");
                        cadenaLatitud += auxiliar;
                    } else if (auxiliar == '.') {
                        cadenaLatitud += auxiliar;
                        Log.v("punto", "punto entontrado");
                    }
                    primerDato = false;
                } else {
                    if (Character.isDigit(auxiliar)) {
                        Log.v("punto", "num entontrado");
                        cadenaLongitud += auxiliar;
                    } else if (auxiliar == '.') {
                        cadenaLongitud += auxiliar;
                        Log.v("punto", "punto entontrado");
                    }
                }
            }
            Log.v("cadenaLatitud", "cadena: " + cadenaLatitud);
            Log.v("cadenaLongitud", "cadena: " + cadenaLongitud);

        }

        Log.v("cadenaLatitud", "cadena: " + cadenaLatitud);
        Log.v("cadenaLongitud", "cadena: " + cadenaLongitud);
        return coordenadas;
    }

    public double getLatitud(char[] caracteres) {
        char auxiliar = 0;
        String suma = "";
        double latid = 0;
        for (int i = 0; i < caracteres.length; i++) {
            auxiliar = caracteres[i];
            if (auxiliar == ':') {
                if (caracteres[i - 1] == 't' && caracteres[i - 2] == 'a') {
                    suma += caracteres[i + 1];
                    for (int j = i + 2; j < caracteres.length; j++) {
                        if (!esCoordenada(caracteres[j])) {
                            Log.v("NoCordenada", "" + caracteres[j]);
                            j = caracteres.length;
                        } else {
                            suma += caracteres[j];
                        }
                    }
                    i = caracteres.length;
                }
            }
        }
        Log.v("suma", "" + suma);
        try {
            latid = Double.parseDouble(suma);

        } catch (NumberFormatException ex) {
            latid = 0.0;
        }
        return latid;
    }

    public double getLongitud(char[] caracteres) {
        char auxiliar = 0;
        String suma = "";
        double longitud = 0;
        for (int i = 0; i < caracteres.length; i++) {
            auxiliar = caracteres[i];
            if (auxiliar == ':') {
                if (caracteres[i - 1] == 'n' && caracteres[i - 2] == 'o') {
                    suma += caracteres[i + 1];
                    for (int j = i + 2; j < caracteres.length; j++) {
                        if (!esCoordenada(caracteres[j])) {
                            Log.v("NoCordenada", "" + caracteres[j]);
                            j = caracteres.length;
                        } else {
                            suma += caracteres[j];
                        }
                    }
                    i = caracteres.length;
                }
            }
        }
        Log.v("suma", "" + suma);
        try {
            longitud = Double.parseDouble(suma);
        } catch (NumberFormatException ex) {
            longitud = 0.0;
        }

        return longitud;
    }

    public boolean esCoordenada(int caracter) {
        boolean coordenada = false;
        if (caracter == 46 || (caracter > 47 && caracter < 58)) {
            coordenada = true;
            Log.v("numero", "esNumero: " + caracter);
        } else {
            coordenada = false;
            Log.v("numero", "noEsNumero: " + caracter);
        }
        return coordenada;
    }

    public boolean comporbarStatus() {
        ArrayList dato;
        managerBD = new ManipulacionBD(contexto.getApplicationContext());
        String[] datos = {SQLHelper.COLUMNA_USUARIO_ID, SQLHelper.COLUMNA_USUARIO_EMPRESA_STATUS};
        dato = managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, datos, null, null);
        if (dato.get(1) != null) {
            if (((String) dato.get(1)).equalsIgnoreCase("1"))
                return true;
            else
                return false;
        }
        return false;
    }


    public boolean buscarNumeroEnBD(String numRemit) {
        managerBD = new ManipulacionBD(contexto.getApplicationContext());
        String[] datos = {SQLHelper.COLUMNA_GPS_NUMERO};
        String[] valor = {numRemit};
        if (managerBD.obtenerDatos(SQLHelper.TABLA_GPS, datos, SQLHelper.COLUMNA_GPS_NUMERO, valor) != null) {
            numero = (String) (managerBD.obtenerDatos(SQLHelper.TABLA_GPS, datos, SQLHelper.COLUMNA_GPS_NUMERO, valor)).get(0);
            Log.v("AGET-NUMERO", "ENCONTRADO");
            return true;
        } else {
            Log.v("AGET-NUMERO", "NO ENCONTRADO");
            String numtel = numRemit.substring(3, 13);
            String[] valor2 = {numtel};
            Log.v("AGET-NUMERO", "SUBSTRING" + valor2[0]);
            if (managerBD.obtenerDatos(SQLHelper.TABLA_GPS, datos, SQLHelper.COLUMNA_GPS_NUMERO, valor2) != null) {
                numero = (String) (managerBD.obtenerDatos(SQLHelper.TABLA_GPS, datos, SQLHelper.COLUMNA_GPS_NUMERO, valor2)).get(0);
                Log.v("AGET-NUMERO", "ENCONTRADO");
                return true;
            } else {
                Log.v("AGET-NUMERO", "NO ENCONTRADO en intento 2");
                return false;
            }
        }
    }

}


/*
Mensaje recibido:


de:7471421423
de:7471421423
txt:lat:17.556173 lon:-99.523757
speed:5.54
T:16/08/18 22:01
bat:50%
http://maps.google.com/maps?f=q&q=17.556173,-99.523757&z=16


*/