package com.aldo.aget.rscliente.Configuracion;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.aldo.aget.rscliente.R;

/**
 * Created by Work on 22/05/16.
 */
public class Utilidades extends Activity {
    public void mensaje(String msn) {
        //Snackbar.make(findViewById(R.id.main), msn, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
    public static final String EXTRA_RESULTADO = "extra.resultado";
    public static final String EXTRA_MENSAJE = "extra.mensaje";
    public static final String EXTRA_DATOS_ALIST = "extra.datosalist";
}