package com.mobaires.noticias.datos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mobaires.noticias.R;
import com.mobaires.noticias.entidades.NoticiaWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.text.Html;

/*
    Clase que se encarga de obtener datos de webservice y entregar objetos ya construídos.

    Los 4 métodos principales son:

    doGet(url) - devuelve el html de una web.
    readIt - genera el stream (se usa con la anterior)
    downloadImage(url) - entrega una imagen apartir de una web
    GenerarObjetoNoticia(String json) - entrega un objeto noticia apartir de un objeto json

*/

public class HttpClient {

    private static final int BUFFER_LENGTH = 4096;
    private static final String UTF8 = "UTF-8";
    private static final String TAG = "HttpClient";

    public static String doGet(URL url) throws IOException {
        Log.d(TAG, "Do GET");

        int responseCode = -1;

        String respuesta = null;
        HttpURLConnection urlConnection = null;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);

            // Configuro el request
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "Response code " + responseCode);
            InputStream is = urlConnection.getInputStream();
            // Convert the InputStream into a string
            respuesta = readIt(is);
            return respuesta;
        }
        catch (IOException e)
        {
            throw new IOException("no se conecto"); // propagar la excepcion (incompleto)
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
    }

    private static String readIt(InputStream stream) throws IOException {
        if (stream == null) return "";
        int readCount = 0;
        Reader reader = null;
        reader = new InputStreamReader(stream, UTF8);
        char[] buffer = new char[BUFFER_LENGTH];
        StringWriter writer = new StringWriter();
        //readCount is important, as due to this buffer for BUFFER_LENGTH was creating Non-Empty String for Empty Response.
        while (-1 != (readCount = reader.read(buffer))) {
            writer.write(buffer, 0, readCount);
        }
        return writer.toString();
    }

    public static Bitmap DownloadImage (String imageHttpAddress){

        // METODO PARA OBTENER UNA FOTO DE WEB

        URL imageUrl = null;
        Bitmap imagen = null;

        try
        {
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        return imagen;
    }

    public static List<NoticiaWeb> GenerarObjetoNoticia (Context c, String jsonAsString) throws JSONException {

        // {"responseData": null, "responseDetails": "out of range start", "responseStatus": 400}

        // METODO RECIBE STRING JSON Y GENERA  NOTICIA EN  OBJETO (DEVUELVE UN OBJETO JAVA NOTICIA)

        // DEBERIA VALIDARSE SI ES UN JSON CORRECTO O NO Y LANZAR O NO UNA EXEPTION ********

        List<NoticiaWeb> Listanoticias = new ArrayList<NoticiaWeb>();

        // DatosTelefono en donde se guardarán lo string provenientes del Json.
        String titulo;
        String editor;
        String contenido;
        String fecha;
        String fotoChica;
        String fotoGrande;
        String web;
        String idioma;

        String faltaFoto = c.getResources().getString(R.string.noticia_sin_foto);

        // Crear JsonObject y definirle los atributos.
        JSONObject jsonObject = new JSONObject(jsonAsString);

        JSONObject responseData = null; // = jsonObject.optJSONObject(c.getResources().getString(R.string.json_response_data));
        JSONArray jsonArray_results = null;// = responseData.getJSONArray(c.getResources().getString(R.string.json_results));

        responseData = jsonObject.optJSONObject(c.getResources().getString(R.string.json_response_data));

        if(responseData != null && !jsonAsString.isEmpty())
        {
            jsonArray_results = responseData.getJSONArray(c.getResources().getString(R.string.json_results));

            //Llenar de objetos Noticia Listanoticias.
            for (int i = 0; i < jsonArray_results.length(); i++) {
                JSONObject jsonObject_i = jsonArray_results.getJSONObject(i);

                titulo = jsonObject_i.getString(c.getResources().getString(R.string.json_titulo)); // Título de la noticia
                contenido = jsonObject_i.getString(c.getResources().getString(R.string.json_contenido)); // Contenido de la noticia
                fecha = jsonObject_i.getString(c.getResources().getString(R.string.json_fecha)); // Fecha
                editor = jsonObject_i.getString(c.getResources().getString(R.string.json_editor)); // Editor
                web = jsonObject_i.getString(c.getResources().getString(R.string.json_url_noticia)); // Direccion noticia
                idioma = jsonObject_i.getString(c.getResources().getString(R.string.json_idioma)); // Idioma

                // Estas dos líneas quitan código HTML y genera un string "limpio":
                titulo = Html.fromHtml(titulo).toString();
                contenido = Html.fromHtml(contenido).toString();
                // ----------------------------------------------------------------

                try {
                    // Puede que no haya imagenes en el json...
                    fotoChica = jsonObject_i.getJSONObject(c.getResources().getString(R.string.json_imagen)).getString(c.getResources().getString(R.string.json_imagen_chica)); // Fotos Chicas
                    fotoGrande = jsonObject_i.getJSONObject(c.getResources().getString(R.string.json_imagen)).getString(c.getResources().getString(R.string.json_imagen_grande)); // Fotod Grandes
                } catch (Exception e) {
                    // Si no existiera la imagen lanzo la excepcion, recolecto la información que no varía
                    // y corto el bucle...

                    Listanoticias.add(new NoticiaWeb(titulo,
                            contenido,
                            fecha,
                            editor,
                            faltaFoto,
                            faltaFoto,
                            web,
                            idioma));
                    continue;
                }

                Listanoticias.add(new NoticiaWeb(titulo,
                        contenido,
                        fecha,
                        editor,
                        fotoChica,
                        fotoGrande,
                        web,
                        idioma));
            }
        }
        else
        {
            try
            {
                String mensaje = jsonObject.getString("responseDetails");
                Log.d(TAG, "problema: " + mensaje);

                NoticiaWeb noticiaVacia = new NoticiaWeb(mensaje, "Si esto se ve, no hay más noticias","","","","","","");

                Listanoticias.add(noticiaVacia);
            }
            catch (Exception e)
            {
                Log.d(TAG, "problema al conectarse... X_X");
            }
        }

        return Listanoticias;
    }
}
