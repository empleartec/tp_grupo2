package com.mobaires.noticias.datos;

import android.content.Context;

/*
    Clase con métodos para obtener datos del
    teléfono que seran consumidos por "lógica"...
*/

public class DatosTelefono {

    // Obtiene el codigo idioma del teléfono: ej: "es"
    public static String obtenerIdiomaTelefonoCodigo(Context c){
        return c.getResources().getConfiguration().locale.getLanguage();
    }

    // Obtiene el idioma del teléfono: ej: "español"
    public static String obtenerIdiomaTelefono(Context c){
        return c.getResources().getConfiguration().locale.getDisplayLanguage();
    }

    // Obtiene el codigo país del teléfono: ej: "it"
    public static String obtenerPaisTelefonoCodigo(Context c){
        return c.getResources().getConfiguration().locale.getCountry();
    }

    // Obtiene el país del teléfono: ej: "italia"
    public static String obtenerPaisTelefono(Context c){
        return c.getResources().getConfiguration().locale.getDisplayCountry();
    }
}
