package com.aldo.aget.rscliente;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.Control.ManipulacionBD;
import com.aldo.aget.rscliente.Control.Receptor;
import com.aldo.aget.rscliente.Control.SQLHelper;
import com.aldo.aget.rscliente.ServicioWeb.Peticion;
import com.aldo.aget.rscliente.Vistas.Inicio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VIDEO_NAME = "welcome_video.mp4";

    private VideoView mVideoView;

    private InputType inputType = InputType.NONE;

    public static Button buttonLeft, buttonRight;

    private static FormView formView;

    private ViewGroup contianer;

    public static TextView appName;

    String correo, clave;

    Receptor receptor = new Receptor();

    String tipoPeticion = "post";

    static int delta;

    ManipulacionBD managerBD;

    String[] columnasRetorar = {Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_USUARIO_NOMBRE,
            Configuracion.COLUMNA_USUARIO_AP_PATERNO, Configuracion.COLUMNA_USUARIO_AP_MATERNO, Configuracion.COLUMNA_USUARIO_TELEFONO,
            Configuracion.COLUMNA_USUARIO_CORREO, Configuracion.COLUMNA_USUARIO_CONTRASE_NA, Configuracion.COLUMNA_DEPARTAMENTO_ID, Configuracion.COLUMNA_USUARIO_NOMBRE_DEPARTAMENTO,
            Configuracion.COLUMNA_EMPRESA_ID, Configuracion.COLUMNA_USUARIO_NOMBRE_EMPRESA, Configuracion.COLUMNA_EMPRESA_STATUS
    };

    static ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //esto quita el título de la activity en la parte superior
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        }
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Configuracion.context = this;
        Configuracion.parentLayout = findViewById(R.id.root);
        Configuracion.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.root);

        ArrayList datosCursor = existenDatos();
        if (datosCursor == null || datosCursor.size() < 1) {
            cargarpantalla();
        } else {
            cargarpantalla();
            comprobarVigencia();
            //mostrarInicio(datosCursor);
        }
        formView.btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.peticionRecPass();
            }
        });

        formView.btn_intentar_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.intentarIniciarSesion();
            }
        });

        progress = (ProgressBar)findViewById(R.id.progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//******************************************************************

    private void findView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        contianer = (ViewGroup) findViewById(R.id.container);
        formView = (FormView) findViewById(R.id.formView);
        appName = (TextView) findViewById(R.id.appName);
        formView.post(new Runnable() {
            @Override
            public void run() {
                //int delta = formView.getTop()+formView.getHeight();
                delta = formView.getTop() + formView.getHeight();
                formView.setTranslationY(-1 * delta);
            }
        });

    }

    private void initView() {
        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
    }

    private void playVideo(File videoFile) {
        mVideoView.setVideoPath(videoFile.getPath());
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }

    static int num = 0;

    public static void playAnim() {
        Log.v("AGET-animacion", "" + num);
        num++;
        ObjectAnimator anim = ObjectAnimator.ofFloat(appName, "alpha", 0, 1);
        anim.setDuration(4000);
        anim.setRepeatCount(1);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                appName.setVisibility(View.INVISIBLE);
            }
        });
    }

    @NonNull
    private File copyVideoFile() {
        File videoFile;
        try {
            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
            InputStream in = getResources().openRawResource(R.raw.welcome_video);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists())
            throw new RuntimeException("problema en el archivo de video, en res/raw");
        return videoFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor datos
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_MAINACTIVITY_LOGIN);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptor, filtro);

        IntentFilter filtro2 = new IntentFilter(Configuracion.INTENT_MAINACTIVITY_COMPROBAR_LOGIN);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptor, filtro2);

        IntentFilter filtro3 = new IntentFilter(Configuracion.INTENT_MAINACTIVITY_RECUPERAR);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptor, filtro3);


    }

    @Override
    protected  void onRestart(){
        super.onRestart();
//        ArrayList datosCursor = existenDatos();
//        if (datosCursor == null || datosCursor.size() < 1) {
//            cargarpantalla();
//        } else {
//            mostrarInicio(datosCursor);
//        }
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptor);
        this.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }


    @Override
    public void onClick(View view) {
        //int delta = formView.getTop()+formView.getHeight();
        delta = formView.getTop() + formView.getHeight();
        switch (inputType) {
            case NONE:

                formView.animate().translationY(0).alpha(1).setDuration(500).start();
                formView.btn_recuperar.setVisibility(View.INVISIBLE);
                formView.btn_intentar_iniciar.setVisibility(View.INVISIBLE);

                if (view == buttonLeft) {
                    inputType = InputType.LOGIN;
                    buttonLeft.setText(R.string.button_confirm_login);
                    buttonRight.setText(R.string.button_cancel_login);
                } else if (view == buttonRight) {
                    finish();
                    inputType = InputType.SIGN_UP;
                    buttonLeft.setText(R.string.button_confirm_signup);
                    buttonRight.setText(R.string.button_cancel_signup);
                }

                break;
            case LOGIN:

                //formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
                if (view == buttonLeft) {
                    correo = formView.getEdtCorreo();
                    clave = formView.getEdtClave();
                    if (!esNombreValido(correo)) {
                        formView.setErrorMcrCompoCorreo("El correo es requerido");
                    } else if (!esNombreValido(clave)) {
                        formView.setErrorMcrCompoClave("La clave es requerida");
                    } else {
                        tipoPeticion = "post";
                        if(estaConectado()){
                            mostrarProgress(true);
                            peticionLogin(correo,clave);

                        }
                    }
                } else if (view == buttonRight) {
                    formView.setEdtCorreo("");
                    formView.setEdtClave("");

                    inputType = InputType.NONE;
                    buttonLeft.setText(R.string.button_login);
                    buttonRight.setText(R.string.button_signup);
                }
                break;
            case SIGN_UP:

                formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
                if (view == buttonLeft) {

                } else if (view == buttonRight) {

                }
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
        }
    }

    /*******************/

    protected Boolean estaConectado(){
        if(conectadoWifi()){
            //showAlertDialog(MainActivity.this, "Tu Dispositivo tiene Conexion a Wifi.", true);
            return true;
        }else{
            if(conectadoRedMovil()){
                //showAlertDialog(MainActivity.this, "Tu Dispositivo tiene Conexion Movil.", true);
                return true;
            }else{
                showAlertDialog(MainActivity.this,
                        "El dispositivo no tiene conexion a internet.", false);
                return false;
            }
        }
    }
    public void showAlertDialog(Context cnt,String mensaje, boolean x){
        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }
    /*public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }*/

    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /*******************/

    enum InputType {
        NONE, LOGIN, SIGN_UP;
    }

    public static void ocultarFormulario() {
        formView.animate().translationY(-1 * delta).alpha(0).setDuration(500).start();
    }

    public static void mostrarFormulario() {
        formView.animate().translationY(0).alpha(1).setDuration(500).start();
    }

    public static void mostrarRecuperarClave() {
        formView.mcrCampoClave.setVisibility(View.GONE);
        formView.btn_recuperar.setVisibility(View.VISIBLE);
        formView.btn_intentar_iniciar.setVisibility(View.VISIBLE);
        formView.viewClave.setVisibility(View.GONE);
        buttonLeft.setVisibility(View.GONE);
        buttonRight.setVisibility(View.GONE);
    }

    public void cargarpantalla(){
        findView();

        initView();

        File videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists()) {
            videoFile = copyVideoFile();
        }

        playVideo(videoFile);

        playAnim();
    }
    //******************************************************************


    //Fin
    public void peticionLogin(String correo, String clave) {
        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_CORREO, Configuracion.COLUMNA_USUARIO_CONTRASE_NA};
        String[] valorFiltro = {correo, clave};

        new Peticion(MainActivity.this, Configuracion.INTENT_MAINACTIVITY_LOGIN, columnasFiltro, valorFiltro, Configuracion.TABLA_USUARIOS, columnasRetorar, false)
                .execute(Configuracion.PETICION_MAINACTIVITY_LOGIN, tipoPeticion);
    }

    public void peticionLoginVigencia(String correo, String clave) {
        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_CORREO, Configuracion.COLUMNA_USUARIO_CONTRASE_NA};
        String[] valorFiltro = {correo, clave};

        new Peticion(MainActivity.this, Configuracion.INTENT_MAINACTIVITY_COMPROBAR_LOGIN, columnasFiltro, valorFiltro, Configuracion.TABLA_USUARIOS, columnasRetorar, false)
                .execute(Configuracion.PETICION_MAINACTIVITY_LOGIN, tipoPeticion);
    }


    private boolean esNombreValido(String nombre) {
        return !TextUtils.isEmpty(nombre);
    }

    public void mostrarInicio(ArrayList datos) {
        Intent inten = new Intent(Configuracion.context, Inicio.class);
        inten.putExtra(Configuracion.COLUMNA_USUARIO_ID, datos.get(0).toString());
        inten.putExtra(Configuracion.COLUMNA_USUARIO_NOMBRE, datos.get(1).toString());
        Configuracion.context.startActivity(inten);
    }

    public ArrayList existenDatos(){
        managerBD = new ManipulacionBD(this);
        String[] datos = {SQLHelper.COLUMNA_USUARIO_ID, SQLHelper.COLUMNA_USUARIO_NOMBRE,
                SQLHelper.COLUMNA_USUARIO_AP_PATERNO,SQLHelper.COLUMNA_USUARIO_CORREO,
                SQLHelper.COLUMNA_USUARIO_CONTRASE_NA};
        return managerBD.obtenerDatos(SQLHelper.TABLA_USUARIOS, datos, null, null);
    }

    public void comprobarVigencia(){
        ArrayList comprobandoUsuario = existenDatos();
        peticionLoginVigencia(comprobandoUsuario.get(3).toString(),comprobandoUsuario.get(4).toString());
    }

    public static void peticionRecPass(){

        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_CORREO};
        String[] valorFiltro = {formView.getEdtCorreo()};

        new Peticion(Configuracion.context, Configuracion.INTENT_MAINACTIVITY_RECUPERAR, columnasFiltro, valorFiltro, Configuracion.TABLA_CORREO, null, false)
                .execute(Configuracion.PETICION_RECUPERAR_CLAVE, "post");
    }

    public static void mostrarProgress(boolean mostrar) {
        progress.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    public static void intentarIniciarSesion(){
        formView.mcrCampoClave.setVisibility(View.VISIBLE);
        formView.btn_recuperar.setVisibility(View.GONE);
        formView.btn_intentar_iniciar.setVisibility(View.GONE);
        formView.viewClave.setVisibility(View.VISIBLE);
        buttonLeft.setVisibility(View.VISIBLE);
        buttonRight.setVisibility(View.VISIBLE);
    }


}
