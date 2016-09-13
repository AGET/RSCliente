package com.aldo.aget.rscliente.ServicioWeb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Configuracion.Utilidades;
import com.aldo.aget.rscliente.Control.Convertidor;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Work on 23/05/16.
 */
public class Peticion extends AsyncTask<String, Void, JSONObject> {
    private ProgressDialog progressDialog;

    String[] columnasFiltro, valorFiltro, columnas_a_recuperar;
    ArrayList datos;

    private HttpClient httpclient;

    String tabla;
    String intent = "";

    boolean sonTablas, obtener = true,login = false;

    public Peticion(Context context, String receptor, String[] nombresColumnasFiltro, String[] datosColumnasFiltro, String tabla, String[] columnas_a_recuperar, boolean sonTabla) {
        this.columnasFiltro = nombresColumnasFiltro;
        this.valorFiltro = datosColumnasFiltro;
        this.tabla = tabla;
        this.sonTablas = sonTabla;
        if (columnas_a_recuperar != null) {
            this.columnas_a_recuperar = columnas_a_recuperar;
            obtener = true;
        } else {
            obtener = false;
        }

        datos = new ArrayList();
        intent = receptor;
        if (receptor.equalsIgnoreCase(Configuracion.INTENT_MAINACTIVITY_COMPROBAR_LOGIN)){
            login = true;
        }else{
            login = false;
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.v("AGET-URL", params[0] + " tipo: " + params[1]);
        try {
            return procesarPeticion(params[0], params[1]);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject resul) {
        try {
            parserJson(resul);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject procesarPeticion(String peticion, String tipoPeticion) throws ClientProtocolException, IOException, JSONException {
        httpclient = new DefaultHttpClient();
        JSONObject jsonObject = new JSONObject();
        StringEntity stringEntity;
        HttpResponse response = null;
        //url y tipo de contenido
        if (tipoPeticion.equalsIgnoreCase("post")) {

            HttpPost httppost = new HttpPost(peticion);
            httppost.addHeader("Content-Type", "application/json");
            //forma el JSON y tipo de contenido
            for (int i = 0; i < columnasFiltro.length; i++) {
                jsonObject.put(columnasFiltro[i], valorFiltro[i]);
            }
            stringEntity = new StringEntity(jsonObject.toString());
            stringEntity.setContentType((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(stringEntity);
            Log.v("AGET-ver", stringEntity.toString());
            //ejecuta
            response = httpclient.execute(httppost);
        }
        if (tipoPeticion.equalsIgnoreCase("put")) {
            Log.v("AGET-URL", peticion);
            HttpPut httpput = new HttpPut(peticion);
            httpput.addHeader("Content-Type", "application/json");
            //forma el JSON y tipo de contenido
            for (int i = 0; i < columnasFiltro.length; i++) {
                jsonObject.put(columnasFiltro[i], valorFiltro[i]);
            }
            stringEntity = new StringEntity(jsonObject.toString());
            stringEntity.setContentType((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpput.setEntity(stringEntity);
            response = httpclient.execute(httpput);
        }
        if (tipoPeticion.equalsIgnoreCase("delete")) {
            Log.v("AGET-URL", peticion);
            HttpDelete httpdlete = new HttpDelete(peticion);
            response = httpclient.execute(httpdlete);
        }

        //obtiene la respuesta y transorma a objeto JSON
        String jsonResult = new Convertidor().inputStreamToString(response.getEntity().getContent()).toString();
        Log.v("AGET-CHECAR", jsonResult);
        JSONObject object = new JSONObject(jsonResult);
        Log.i("AGET-JSONResult", jsonResult);
        return object;
    }

    public void parserJson(JSONObject result) throws JSONException {
        if (result != null) {
            if (obtener) {
                if (!result.get("estado").toString().equalsIgnoreCase("5")) {
                    if (!sonTablas) {
                        Log.v("AGET", "TablaFalse");
//                        Log.v("AGET","Tamanio OBJ:" + result.getJSONObject(tabla).length());
                        Log.v("AGET","Tamanio Array:" + columnas_a_recuperar.length);

//                        for (int i = 0; i < result.getJSONObject(tabla).length(); i++) {
                        for (int i = 0; i < columnas_a_recuperar.length; i++) {
                            datos.add(result.getJSONObject(tabla).getString(columnas_a_recuperar[i]));
                        }
                        enviarBroadcast(true, "Cargado", datos);
                        Log.v("AGET-ENVIADOS", "true");
                    } else {
                        Log.v("AGET", "TablaTrue");
                        datos.add(new ArrayList());
                        JSONArray obj = result.getJSONArray(tabla);
                        if (result.getJSONArray(tabla).length() > 0) {
                            for (int i = 0; i < result.getJSONArray(tabla).length(); i++) {
                                for (int j = 0; j < columnas_a_recuperar.length; j++)
                                    ((ArrayList) datos.get(i)).add(obj.getJSONObject(i).getString(columnas_a_recuperar[j]));
                                datos.add(new ArrayList());
                            }
                            enviarBroadcast(true, "Cargado", datos);
                        } else {
                            enviarBroadcast(false, "Sin datos", datos);
                        }
                        // enviarBroadcast(true, "Cargado", datos);
                    }
                } else {
                    enviarBroadcast(false, result.getJSONObject("usuarios").getString("mensaje"), datos);
                }
            } else {
                Log.v("AGET-ENVIADOS", "false mensaje");
                enviarBroadcast(false, result.getString("mensaje").toString(), datos);
            }
        } else {
            enviarBroadcast(false, "Ha ocurrido un error, con los datos", datos);
            Log.v("AGET-ENVIADOS", "false por error");
        }
    }

    private void enviarBroadcast(boolean estado, String mensaje, ArrayList datos) {
        Intent intentLocal = new Intent(intent);
        intentLocal.putExtra(Utilidades.EXTRA_RESULTADO, estado);
        intentLocal.putExtra(Utilidades.EXTRA_MENSAJE, mensaje);
        intentLocal.putExtra(Utilidades.EXTRA_DATOS_ALIST, datos);
        LocalBroadcastManager.getInstance(Configuracion.context).sendBroadcast(intentLocal);

        Log.v("AGET", "BROAD ENVIADO");
    }
}

