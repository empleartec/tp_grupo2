package com.mobaires.noticias.logica;

import android.content.Context;

import com.mobaires.noticias.R;
import com.mobaires.noticias.datos.DatosTelefono;

import java.net.URLDecoder;
import java.net.URLEncoder;

/*
    Contiene Métodos para obtener variables
*/
public class Variables {

    // CANTIDAD DE RESULTADOS
    public static String getCantidadResultados(Context c) {
        return c.getResources().getString(R.string.cantidad_resultados);
    }

    // BUSQUEDA INICIAL
    public static String getBusquedaInicial(Context c){
        return c.getResources().getString(R.string.string_inicial);
    }

    // TRAEN DATOS DE TELEFONO
    public static String getCodigoIdiomaTelefono(Context c) {
        return DatosTelefono.obtenerIdiomaTelefonoCodigo(c);
    }

    public static String getCodigoPaisTelefono(Context c) {
        return DatosTelefono.obtenerPaisTelefonoCodigo(c);
    }

    public static String getIdiomaTelefono(Context c) {
        return DatosTelefono.obtenerIdiomaTelefono(c);
    }

    public static String getPaisTelefono(Context c) {
        return DatosTelefono.obtenerPaisTelefono(c);
    }
}
