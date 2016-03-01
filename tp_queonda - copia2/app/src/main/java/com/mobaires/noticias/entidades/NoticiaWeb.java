package com.mobaires.noticias.entidades;

import android.os.Parcel;
import android.os.Parcelable;

/*
    Clase que define una Noticia.
*/

public class NoticiaWeb implements Parcelable {

    private String titulo;
    private String descripcion;
    private String RutaImagen;
    private String editor;
    private String fecha;
    private String rutaImagenGrande;
    private String direccionWebOriginal;
    private String idioma;

    public NoticiaWeb(String titulo, String descripcion, String fecha, String editor, String RutaImagen, String rutaImagenGrande, String direccionWebOriginal, String idioma){

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.RutaImagen = RutaImagen;
        this.editor = editor;
        this.fecha = fecha;
        this.rutaImagenGrande = rutaImagenGrande;
        this.direccionWebOriginal = direccionWebOriginal;
        this.idioma = idioma;
    }

    protected NoticiaWeb(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        RutaImagen = in.readString();
        editor = in.readString();
        fecha = in.readString();
        rutaImagenGrande = in.readString();
        direccionWebOriginal = in.readString();
        idioma = in.readString();
    }

    public static final Creator<NoticiaWeb> CREATOR = new Creator<NoticiaWeb>() {
        @Override
        public NoticiaWeb createFromParcel(Parcel in) {
            return new NoticiaWeb(in);
        }

        @Override
        public NoticiaWeb[] newArray(int size) {
            return new NoticiaWeb[size];
        }
    };

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRutaImagen() {
        return RutaImagen;
    }

    public String getEditor() {
        return editor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getRutaImagenGrande() {
        return rutaImagenGrande;
    }

    public String getDireccionWebOriginal() {
        return direccionWebOriginal;
    }

    public String getIdioma() {
        return idioma;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(RutaImagen);
        dest.writeString(editor);
        dest.writeString(fecha);
        dest.writeString(rutaImagenGrande);
        dest.writeString(direccionWebOriginal);
        dest.writeString(idioma);
    }
}
