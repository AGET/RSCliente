package com.aldo.aget.rscliente.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by workstation on 10/11/15.
 */
public class ManipulacionBD {

    SQLHelper sqlhelper;
    SQLiteDatabase db;

    public ManipulacionBD(Context contexto) {
        sqlhelper = new SQLHelper(contexto);
    }

    public boolean eliminarDato(String tabla, String columna, String dato) {
        abrirEscrituraBD();
        db.delete(tabla, columna + "=" + dato, null);
        return true;
    }

    public boolean eliminarTodo(String tabla) {
        abrirEscrituraBD();
        db.delete(tabla, null, null);
        return true;
    }

    public void abrirEscrituraBD() {
        db = sqlhelper.getWritableDatabase();
    }

    public void cerrarBD() {
        db.close();
    }

    public boolean actualizarEstadoUsuario(String tabla, String columna, String dato, String condicion, String valorCondicion) {
        abrirEscrituraBD();
        ContentValues valores = new ContentValues();
        valores.put(columna, dato);
        db.update(tabla, valores, condicion+"="+valorCondicion, null);
        return true;
    }

    public ArrayList obtenerDatos(String tabla, String[] campos, String where, String[] datosWhere) {
        abrirEscrituraBD();
        ArrayList datosCursor = new ArrayList();
        Cursor c;
        if (where != null) {
            c = db.query(tabla, campos, where + "=?", datosWhere, null, null, null);
        } else {
            c = db.query(tabla, campos, null, null, null, null, null);
        }
        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    datosCursor.add(c.getString(i));
                }
            } while (c.moveToNext());
        } else {
            datosCursor = null;
        }
        return datosCursor;
    }

    public void obtenerDatosGPSNumero() {
        abrirEscrituraBD();
        ArrayList datosCursor = new ArrayList();
        String[] campos = new String[] {"enlace_id"};
        String[] args = new String[] {"7471212314"};

        Cursor c = db.query("gps", campos, "numero=?", args, null, null, null);
//Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    datosCursor.add(c.getString(i));
                    Log.v("AGET-bd","NUMERO:"+c.getString(i));
                }
            } while(c.moveToNext());
        }else{
            Log.v("AGET-bd","cantidad:"+c.getCount());
            Log.v("AGET-bd","cantidad columna:"+c.getColumnCount());
            Log.v("AGET-bd","NUMERO: vacio");
        }

     //   return datosCursor;
    }

    public void insertar(String tabla, String[] datosColumnas) {
        abrirEscrituraBD();
        ContentValues nuevoRegistro = new ContentValues();
        switch (tabla) {
            case SQLHelper.TABLA_USUARIOS:
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_ID, datosColumnas[0]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_NOMBRE, datosColumnas[1]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_AP_PATERNO, datosColumnas[2]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_AP_MATERNO, datosColumnas[3]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_TELEFONO, datosColumnas[4]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_CORREO, datosColumnas[5]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_CONTRASE_NA, datosColumnas[6]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_DEPARTAMENTO_ID, datosColumnas[7]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_DEPARTAMENTO_NOMBRE, datosColumnas[8]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_EMPRESA_ID, datosColumnas[9]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_EMPRESA_NOMBRE, datosColumnas[10]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_EMPRESA_STATUS, datosColumnas[11]);
                break;
            case SQLHelper.TABLA_GPS:
                nuevoRegistro.put(SQLHelper.COLUMNA_GPS_ENLACE_ID, datosColumnas[0]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_ID, datosColumnas[1]);
                nuevoRegistro.put(SQLHelper.COLUMNA_USUARIO_NOMBRE, datosColumnas[2]);
                nuevoRegistro.put(SQLHelper.COLUMNA_GPS_ID, datosColumnas[3]);
                nuevoRegistro.put(SQLHelper.COLUMNA_GPS_IMEI, datosColumnas[4]);
                nuevoRegistro.put(SQLHelper.COLUMNA_GPS_NUMERO, datosColumnas[5]);
                nuevoRegistro.put(SQLHelper.COLUMNA_GPS_DESCRIPCION, datosColumnas[6]);
                nuevoRegistro.put(SQLHelper.COLUMNA_GPS_DEPARTAMENTO, datosColumnas[7]);
                break;
        }
        db.insert(tabla, null, nuevoRegistro);
        cerrarBD();
    }
}
