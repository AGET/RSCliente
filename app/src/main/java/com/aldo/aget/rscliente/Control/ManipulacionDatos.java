package com.aldo.aget.rscliente.Control;

import java.util.ArrayList;

/**
 * Created by Work on 22/05/16.
 */
public class ManipulacionDatos {
    ArrayList datosJsonUno;
    ArrayList datosJsonDos;
    ArrayList datosJsonTres;
    ArrayList datosJson;

    public void setDatoUno(ArrayList datos){
        this.datosJsonUno = datos;
    }

    public void setDatoDos(ArrayList datos){
        this.datosJsonDos = datos;
    }

    public void setDatoTres(ArrayList datos){
        this.datosJsonTres = datos;
    }


    public void setDatosJson(ArrayList datos){
        this.datosJson = datos;
    }

    public  ArrayList getDatosJson(){
        return this.datosJson;
    }

    public  ArrayList getDatosJsonUno(){
        return this.datosJsonUno;
    }
    public  ArrayList getDatosJsonDos(){
        return this.datosJsonDos;
    }
    public  ArrayList getDatosJsonTres(){
        return this.datosJsonTres;
    }
}
