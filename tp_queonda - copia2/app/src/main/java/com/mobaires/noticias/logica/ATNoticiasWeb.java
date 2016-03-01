package com.mobaires.noticias.logica;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.datos.DatosTelefono;
import com.mobaires.noticias.datos.HttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
    Clase que se encarga de obtener los objetos noticias que se usaran en la aplicación
*/

public class ATNoticiasWeb extends AsyncTask<Void, Void, List<NoticiaWeb> > {

    Context con;
    List<NoticiaWeb> listadenoticias = new ArrayList<NoticiaWeb>();
    String busqueda = "";
    String idioma = "";
    String cantidadResultados;
    String paginador;
    String topico;

    public ATNoticiasWeb(Context ctx, IListaCallbackWeb callback, String busqueda, String idioma, String cantidadResultados, String paginador, String topico) {

        this.callback = callback;
        this.busqueda = busqueda;
        this.idioma = idioma;
        this.con = ctx;
        this.cantidadResultados = cantidadResultados;
        this.paginador = paginador;
        this.topico = topico;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) {
            callback.prepareUIWeb();
        }
    }

    @Override
    protected List<NoticiaWeb> doInBackground(Void... params) {

        List<NoticiaWeb> listaNoticias = new ArrayList<NoticiaWeb>();

        URL u = null;
        String pagina = "";

        try
        {
            u = new URL("https://ajax.googleapis.com/ajax/services/search/news?"
                        + "v=1.0"
                        + "&topic=" + this.topico // Topicos (deportes, política, etc)
                        + "&start=" + this.paginador // Apartir de que noticia traer noticias
                        + "&q=" + this.busqueda // String a buscar
                        + "&rsz=" + this.cantidadResultados //Cantidad de resultados
                        + "&hl=" + this.idioma); // idioma de la búsqueda )

            pagina = HttpClient.doGet(u);

            listaNoticias = HttpClient.GenerarObjetoNoticia(this.con, pagina);

            //demorarCarga(2000); // Para hacer tests... (dejar comentado)

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return listaNoticias;
    }

    @Override
    protected void onPostExecute(List<NoticiaWeb> noticias) {
        super.onPostExecute(noticias);
        if (callback != null) {
            this.callback.taskFinishedWeb(noticias);
        }
        this.listadenoticias = noticias;

    }

    private final IListaCallbackWeb callback;
    // INTERFACE
    public interface IListaCallbackWeb {
        void prepareUIWeb();
        void taskFinishedWeb(List<NoticiaWeb> lista);
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
