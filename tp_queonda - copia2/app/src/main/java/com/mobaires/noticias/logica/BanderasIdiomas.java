package com.mobaires.noticias.logica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;

import com.mobaires.noticias.R;

/*
    Contiene una método que convierte un string en una imagen
    de una bandera según nacionalidad para ser mostrada en la lista.
*/

public class BanderasIdiomas {

    public static Drawable seleccionarIdioma(String idioma, Context c){

        //getresources.getidentifier("filex","drawable","package")

        Drawable bandera = null;

        switch (idioma) {
            case "es":
                bandera = c.getResources().getDrawable(R.drawable.espaniol);
                break;

            case "en":
                bandera = c.getResources().getDrawable(R.drawable.ingles);
                break;

            case "pt":
                bandera = c.getResources().getDrawable(R.drawable.portbras);
                break;

            case "it":
                bandera = c.getResources().getDrawable(R.drawable.italiano);
                break;

            case "de":
                bandera = c.getResources().getDrawable(R.drawable.alemania);
                break;

            default:
                bandera = c.getResources().getDrawable(R.drawable.mundochico);
                break;
        }

        return bandera;
    }

}
