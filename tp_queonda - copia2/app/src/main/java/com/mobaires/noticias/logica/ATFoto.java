package com.mobaires.noticias.logica;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mobaires.noticias.datos.HttpClient;

import java.util.HashMap;
import java.util.Map;

/*
    Clase que maneja la tarea asíncrona de descargar una foto desde web...
    Recibe el ImageView en el cual aparecerá la foto...
    Recibe como parámetro en el constructor la ruta donde debe buscar la foto...
*/

public class ATFoto extends AsyncTask<ImageView, Void, Bitmap> {

    ImageView img;
    String ruta ="";
    ProgressBar pbFoto;
    AdaptadorNoticiasWeb.ViewHolder v;
    int position;
    boolean lista;

    public ATFoto(String ruta, ProgressBar pb){

        this.lista = false;
        this.ruta = ruta;
        this.pbFoto = pb;
    }

    // NOTA
    // SOBRECARGA EN CASO DE QUERER USAR SOLO UNA ATFOTO CON ATFOTOLISTA
    public ATFoto(String ruta, ProgressBar pb, AdaptadorNoticiasWeb.ViewHolder v, int position){

        this.lista = true;
        this.ruta = ruta;
        this.pbFoto = pb;
        this.position = position;
        this.v = v;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(ImageView... params) {

        this.img = params[0];
        Bitmap img = null;

        try
        {
            // Si la foto está cacheada la tomo, sino descargo y coloco en caché.
            if(CacheFotos.fotos.containsKey(ruta))
            {
                img = CacheFotos.fotos.get(ruta);
            }
            else
            {
                img = HttpClient.DownloadImage(this.ruta);
                CacheFotos.fotos.put(ruta, img);
            }
        }
        catch (Exception e)
        {
            throw e;
        }

        return img;
    }

    @Override
    protected void onPostExecute(Bitmap imagen) {
        super.onPostExecute(imagen);

        //demorarCarga(1000);

        // lista es un boolean que sirve para identificar si entre por el contructor de lista
        // o el de fotos. El de lista tiene el parametro viewholder y position y el de foto no.
        // de esta forma si el resultado es para la lista se ejecuta el if con comparacion entre
        // getPosition y postion, de lo contrario sólo saco la imagen pedida.
        if(this.lista)
        {
            if(v.getPosition() == position){

                img.setImageBitmap(imagen);
                img.setVisibility(View.VISIBLE);
                pbFoto.setVisibility(View.GONE);
            }
        }

        if(!this.lista)
        {
            img.setImageBitmap(imagen);
            img.setVisibility(View.VISIBLE);
            pbFoto.setVisibility(View.GONE);
        }
    }

    private void demorarCarga(int tiempoDemora){

        try
        {
            Thread.sleep(tiempoDemora);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

