package com.aldo.aget.rscliente.Configuracion;

/**
 * Created by Work on 15/08/16.
 */
public class GPS {

        private String enlaceId;
        private String id;
        private String imei;
        private String numero;
        private String descripcion;
        private String img;

        public GPS (String enlaceId, String id, String imei, String numero, String descripcion,String img){
            this.enlaceId = enlaceId;
            this.id = id;
            this.imei = imei;
            this.numero = numero;
            this.descripcion = descripcion;
            this.img = img;
        }

        public String getEnlaceId(){
        return enlaceId;
    }

        public String getId(){
            return id;
        }

        public String getImei(){
            return imei;
        }

        public String getNumero(){
            return numero;
        }

        public String getDescripcion(){
            return descripcion;
        }

        public String getImg(){
        return img;
    }


}
