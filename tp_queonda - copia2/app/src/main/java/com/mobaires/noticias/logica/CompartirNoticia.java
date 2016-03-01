package com.mobaires.noticias.logica;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/*
    Clase para compartir una notica con otras
    apps mediante Intent Action_Send
*/

public class CompartirNoticia {

    public void compartir(Context con, TextView tit, TextView descrpcion, ImageView fot){

        String titulo = tit.getText().toString();
        String contenido = descrpcion.getText().toString();

        Bitmap bitmap = ((BitmapDrawable)fot.getDrawable()).getBitmap();
        Uri foto = this.getImageUri(con, bitmap);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulo);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, contenido);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, foto);
        sharingIntent.setType("image/jpeg");

        con.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void compartir(Context con, ImageView fot){

        Bitmap bitmap = ((BitmapDrawable)fot.getDrawable()).getBitmap();
        Uri foto = this.getImageUri(con, bitmap);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_STREAM, foto);
        sharingIntent.setType("image/jpeg");

        con.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

    public static void guardarImagen(Context context, ImageView iv, String titulo){

        iv.setDrawingCacheEnabled(true);
        Bitmap b = iv.getDrawingCache();
        MediaStore.Images.Media.insertImage(context.getContentResolver(), b, titulo, "descripcion");
    }
}
