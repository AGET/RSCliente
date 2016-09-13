package com.aldo.aget.rscliente;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aldo.aget.rscliente.Configuracion.Configuracion;
import com.aldo.aget.rscliente.ServicioWeb.Peticion;


/**
 * Created by kobe.gong on 2015/7/17.
 */
public class FormView extends LinearLayout {

    private EditText edit1, edit2;
    public Button btn_recuperar,btn_intentar_iniciar;
    public TextInputLayout mcrCampoCorreo,mcrCampoClave;
    public View viewClave;

    public FormView(Context context) {
        super(context);
        loadView();
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadView();
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadView();
    }

    private void loadView(){
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.form_view, this);
        edit1 = (EditText) findViewById(R.id.edt_correo);
        edit2 = (EditText) findViewById(R.id.edt_clave);

        //Borrar
//        edit1.setText("aldo_get@hotmail.com");
//        edit2.setText("123456");
        btn_recuperar = (Button) findViewById(R.id.btn_recuperar);
        btn_intentar_iniciar = (Button) findViewById(R.id.btn_intentar_iniciar);

        viewClave = (View)findViewById(R.id.viewClave);

        mcrCampoCorreo = (TextInputLayout) findViewById(R.id.mcr_edt_correo);
        mcrCampoClave = (TextInputLayout) findViewById(R.id.mcr_edt_clave);

    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        edit1.setFocusable(focusable);
        edit2.setFocusable(focusable);
    }

    public String getEdtCorreo(){
        return edit1.getText().toString();
    }

    public String getEdtClave(){
        return edit2.getText().toString();
    }

    public void setEdtClave(String s){
        edit2.setText(s);
    }

    public void setEdtCorreo(String s){
        edit1.setText(s);
    }



    public void setErrorMcrCompoCorreo(String s){
         mcrCampoCorreo.setError(s);
    }

    public void setErrorMcrCompoClave(String s){
        mcrCampoClave.setError(s);
    }


}
