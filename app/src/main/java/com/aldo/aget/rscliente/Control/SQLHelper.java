package com.aldo.aget.rscliente.Control;

/**
 * Created by workstation on 2/07/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    Context contexto = null;

    public static final String DATABASE_NAME = "Usuario.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TIPO_DATO_TEXT = "TEXT";
    public static final String TIPO_DATO_INTEGER= "INTEGER";


    public static final String COLUMNA_GENERICA_ID = "_id";

    public static final String TABLA_USUARIOS = "usuarios";
    public static final String COLUMNA_USUARIO_ID = "usuario_id";
    public static final String COLUMNA_USUARIO_NOMBRE = "nombre";
    public static final String COLUMNA_USUARIO_AP_PATERNO = "ap_paterno";
    public static final String COLUMNA_USUARIO_AP_MATERNO = "ap_materno";
    public static final String COLUMNA_USUARIO_TELEFONO = "telefono";
    public static final String COLUMNA_USUARIO_CORREO = "correo";
    public static final String COLUMNA_USUARIO_CONTRASE_NA = "contrase_na";
    public static final String COLUMNA_USUARIO_DEPARTAMENTO_ID = "departamento_id";
    public static final String COLUMNA_USUARIO_DEPARTAMENTO_NOMBRE = "departamento_nombre";
    public static final String COLUMNA_USUARIO_EMPRESA_ID = "empresa_id";
    public static final String COLUMNA_USUARIO_EMPRESA_NOMBRE = "empresa_nombre";
    public static final String COLUMNA_USUARIO_EMPRESA_STATUS = "status";

    public static final String TABLA_APP = "aplicacion";
    public static final String COLUMNA_PRIMER_USO = "Primer_uso INTEGER";
    public static final String COLUMNA_CONTRASE_NA = "Contrase_na TEXT";
    public static final String COLUMNA_CONTRASE_NA_NOMBRE = "Contrase_na";
    public static final String COLUMNA_ID_USUARIO = "idUsuario INTEGER";



    public static final String TABLA_GPS = "gps";
    public static final String COLUMNA_GPS_ENLACE_ID = "enlace_id";
    public static final String COLUMNA_GPS_ID = "gps_id";
    public static final String COLUMNA_GPS_IMEI = "imei";
    public static final String COLUMNA_GPS_NUMERO = "numero";
    public static final String COLUMNA_GPS_DESCRIPCION = "descripcion";
    public static final String COLUMNA_GPS_DEPARTAMENTO = "departamento_id";

//    private static final String SQL_CREAR_TABLA_DISPOSITIVO = "CREATE TABLE IF NOT EXISTS  "
//            + TABLA_DISPOSITIVO + " ("
//            + COLUMNA_ID + " PRIMARY KEY, "
//            + COLUMNA_GPS_PRIMERA_VEZ + ","
//            + COLUMNA_CONTRASE_NA + ", "
//            + COLUMNA_ESTADO_GPS + ", "
//            + COLUMNA_MODO_LINK + ", "
//            + COLUMNA_MODO_LLAMADA_GPS + ", "
//            + COLUMNA_TELEFONO_DISPOSITIVO + ")";

    private static final String SQL_CREAR_TABLA_USUARIOS = "CREATE TABLE IF NOT EXISTS "
            + TABLA_USUARIOS + " ("
            + COLUMNA_GENERICA_ID +" "+ TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_USUARIO_ID +" "+ TIPO_DATO_INTEGER + ", "
            + COLUMNA_USUARIO_NOMBRE +" "+ TIPO_DATO_TEXT + ", "
            + COLUMNA_USUARIO_AP_PATERNO +" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_USUARIO_AP_MATERNO +" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_USUARIO_TELEFONO +" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_USUARIO_CORREO +" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_USUARIO_CONTRASE_NA +" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_USUARIO_DEPARTAMENTO_ID +" "+ TIPO_DATO_INTEGER  + ", "
            + COLUMNA_USUARIO_DEPARTAMENTO_NOMBRE +" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_USUARIO_EMPRESA_ID +" "+ TIPO_DATO_INTEGER  + ", "
            + COLUMNA_USUARIO_EMPRESA_NOMBRE +" "+ TIPO_DATO_TEXT  + ", "
            +COLUMNA_USUARIO_EMPRESA_STATUS +" "+ TIPO_DATO_TEXT + ")";


    private static final String SQL_CREAR_TABLA_GPS = "CREATE TABLE IF NOT EXISTS "
            + TABLA_GPS + " ("
            + COLUMNA_GENERICA_ID +" "+ TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_GPS_ENLACE_ID+" "+ TIPO_DATO_INTEGER + ", "
            + COLUMNA_USUARIO_ID+" "+ TIPO_DATO_INTEGER + ", "
            + COLUMNA_USUARIO_NOMBRE+" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_GPS_ID+" "+ TIPO_DATO_INTEGER + ", "
            + COLUMNA_GPS_IMEI+" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_GPS_NUMERO+" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_GPS_DESCRIPCION+" "+ TIPO_DATO_TEXT  + ", "
            + COLUMNA_GPS_DEPARTAMENTO+" "+ TIPO_DATO_INTEGER  + ")";


    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contexto = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_TABLA_USUARIOS);
        db.execSQL(SQL_CREAR_TABLA_GPS);

        /*Toast t1 = Toast.makeText(contexto, SQL_CREAR_TABLA_AUXILIAR, Toast.LENGTH_LONG);
        t1.setGravity(Gravity.TOP | Gravity.LEFT, 60, 110);
        t1.show();
        Toast t2 = Toast.makeText(contexto,SQL_INSERTAR_DATOS_DEFAULT_AUXILIAR,Toast.LENGTH_LONG);
        t2.setGravity(Gravity.BOTTOM | Gravity.RIGHT,60,100);*/
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // se elimina la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //db.execSQL("DROP TABLE IF EXISTS TablaPrueba");
        //db.execSQL(codeSQL);
    }

}
