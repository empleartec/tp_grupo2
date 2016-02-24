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

   SE USA PARA LAS FOTOS DE LA LISTVIEW EXCLUSIVAMENTE
*/

public class ATFotoLista extends AsyncTask<ImageView, Void, Bitmap> {

    private static Map<ImageView,ATFotoLista> currentTasks = new HashMap<>();
    ImageView img;
    String ruta ="";
    ProgressBar pbFoto;
    AdaptadorNoticiasWeb.ViewHolder v;
    int position;
    boolean lista = false;

    public ATFotoLista(String ruta, ProgressBar pb, AdaptadorNoticiasWeb.ViewHolder v, int position){

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

        // Solucionar problemas con carga de imágenes-----------
        if(currentTasks.containsKey(img)){
            //Cancelar el ATFoto que está corriendo

            ATFotoLista atf = currentTasks.remove(img);
            atf.cancel(true);
        }

        currentTasks.put(img, this); // Agrego la tarea que estaré realizando...
        // -----------------------------------------------------
        Bitmap img = null;

        try
        {
            img = HttpClient.DownloadImage(this.ruta);
        }
        catch (Exception e)
        {
            throw e;
        }

        currentTasks.remove(img); // saco la tarea actual.

        return img;
    }

    @Override
    protected void onPostExecute(Bitmap imagen) {
        super.onPostExecute(imagen);

        //demorarCarga(1000);

        if(v.getPosition()==position && this.lista){

            img.setImageBitmap(imagen);
            img.setVisibility(View.VISIBLE);
            pbFoto.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        this.img = null;
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
