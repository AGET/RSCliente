package com.aldo.aget.rscliente.Configuracion;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.aldo.aget.rscliente.MainActivity;
import com.aldo.aget.rscliente.Vistas.Inicio;

/**
 * Created by Work on 22/05/16.
 */
public class Configuracion extends Activity {

    //Herramientas
    //https://materialdesignicons.com/icon/account-plus

    public static MainActivity context = null;
    public static Inicio contextInicio = null;
    public static View parentLayout = null;
    public static CoordinatorLayout coordinatorLayout = null;
    public static final String SERVIDOR = "http://192.168.0.100";
    //public static final String SERVIDOR = "http://192.168.2.140";
    //public static final String SERVIDOR = " http://aldogamaliel.byethost16.com";
    //http://aldogamaliel.byethost16.com/api.rs.com/v1/gps/listarVarios


    //PETICION ENLACE

    public static final String PETICION_MAINACTIVITY_LOGIN = SERVIDOR + "/api.rs.com/v1/usuarios/login";
    public static final String PETICION_ENLACE_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/enlace/";

    //PETICION COORDENADAS
    public static final String PETICION_COORDENADAS_REGISTRO = SERVIDOR + "/api.rs.com/v1/coordenadas/registro";

    //USUARIO
    public static final String PETICION_USUARIO_LISTAR_GPS = SERVIDOR + "/api.rs.com/v1/usuarios/listarGpsDeUsuario";//gps enlazados de un usuario


    //RECEPTORES
    public static final String INTENT_MAINACTIVITY_LOGIN = "com.aldo.aget.rscliente.Vistas.Login";
    public static final String INTENT_MAINACTIVITY_COMPROBAR_LOGIN = "com.aldo.aget.rscliente.Vistas.Comprobar.Login";
    public static final String INTENT_USUARIO_LISTA_GPS = "com.aldo.aget.rscliente.Vistas.Usuario.ListaGPS";
    public static final String INTENT_GESTION_USUARIO_REGISTRO_ENLACE = "com.aldo.aget.rscliente.Vistas.GestionUsuario.Registro.Enlace";

    public static final String INTENT_USUARIO_ENVIAR_COORDENADAS = "com.aldo.aget.rscliente.Mapa.EnviarCoordenadas";




    //    Tablas
    public static final String TABLA_GPS = "gps";
    public static final String TABLA_EMPRESA_CLIENTE = "empresa_cliente";
    public static final String TABLA_ENLACE = "enlace";
    public static final String TABLA_DEPARTAMENTO = "departamento";
    public static final String TABLA_USUARIOS = "usuarios";

    //    Columnas
//GPS
    public static final String COLUMNA_GPS_ID = "gps_id";
    public static final String COLUMNA_GPS_IMEI = "imei";
    public static final String COLUMNA_GPS_NUMERO = "numero";
    public static final String COLUMNA_GPS_DESCRIPCION = "descripcion";
    public static final String COLUMNA_GPS_AUTORASTREO= "autorastreo";
    public static final String COLUMNA_GPS_DEPARTAMENTO = "departamento_id";


    //    EMPRESA CLIETNE
    public static final String COLUMNA_EMPRESA_ID = "empresa_id";
    public static final String COLUMNA_EMPRESA_NOMBRE = "nombre";
    public static final String COLUMNA_EMPRESA_TELEFONO = "telefono";
    public static final String COLUMNA_EMPRESA_CORREO = "correo";
    public static final String COLUMNA_EMPRESA_STATUS = "status";

    //DEPARTAMENTOS
    public static final String COLUMNA_DEPARTAMENTO_ID = "departamento_id";
    public static final String COLUMNA_DEPARTAMENTO_NOMBRE = "nombre";
    public static final String COLUMNA_DEPARTAMENTO_TELEFONO = "telefono";
    public static final String COLUMNA_DEPARTAMENTO_CORREO = "correo";
    public static final String COLUMNA_DEPARTAMENTO_DIRECCION = "direccion";
    public static final String COLUMNA_DEPARTAMENTO_EMPRESA_ID = "empresa_id";

    //USUARIOS
    public static final String COLUMNA_USUARIO_ID = "usuario_id";
    public static final String COLUMNA_USUARIO_NOMBRE = "nombre";
    public static final String COLUMNA_USUARIO_AP_PATERNO = "ap_paterno";
    public static final String COLUMNA_USUARIO_AP_MATERNO = "ap_materno";
    public static final String COLUMNA_USUARIO_TELEFONO = "telefono";
    public static final String COLUMNA_USUARIO_CORREO = "correo";
    public static final String COLUMNA_USUARIO_CONTRASE_NA = "contrase_na";
    public static final String COLUMNA_USUARIO_DEPARTAMENTO_ID = "departamento_id";

    //login
    public static final String COLUMNA_USUARIO_NOMBRE_DEPARTAMENTO = "nombre_departamento";
    public static final String COLUMNA_USUARIO_NOMBRE_EMPRESA = "nombre_empresa";

    //ENLACE
    public static final String COLUMNA_ENLACE_ID = "enlace_id";
    public static final String COLUMNA_ENLACE_USUARIO = "usuario_id";
    public static final String COLUMNA_ENLACE_GPS = "gps_id";

    public static final String COLUMNA_COORDENADA_LONGITUD = "longitud";
    public static final String COLUMNA_COORDENADA_LATITUD = "latitud";

    public static final String LONGITUD = "LONGITUD";
    public static final String LATITUD = "LATITUD";

    public static Boolean cambio = false;
}