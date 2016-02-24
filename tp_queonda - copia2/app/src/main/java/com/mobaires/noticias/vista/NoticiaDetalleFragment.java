package com.mobaires.noticias.vista;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobaires.noticias.R;
import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.logica.ATFoto;

/*
    Clase que genera el detalle de la noticia al clickear en la lista contigua.
*/

public class NoticiaDetalleFragment extends Fragment {

    TextView titulo;
    TextView descripcion;
    TextView fecha;
    ImageView foto;
    Button botonVerWEB;
    ProgressBar pbFoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_noticias_detail, container, false);
        return view;
    }

    public void setNoticia(NoticiaWeb noticia) {

        // Título Noticia
        this.titulo = (TextView)getView().findViewById(R.id.textView_titulo);
        titulo.setText(noticia.getTitulo());

        // Descrición Noticia
        this.descripcion = (TextView)getView().findViewById(R.id.textView_descripcion);
        descripcion.setText(noticia.getDescripcion());

        // Fecha Noticia
        this.fecha = (TextView)getView().findViewById(R.id.textView_fecha);
        fecha.setText(noticia.getFecha());

        //Foto Noticia
        this.foto = (ImageView)getView().findViewById(R.id.imageView_foto);

        this.pbFoto = (ProgressBar)getView().findViewById(R.id.pb_foto_det_activ);
        this.pbFoto.setVisibility(View.VISIBLE);
        this.foto.setVisibility(View.GONE);

        if(noticia.getRutaImagen().matches(getResources().getString(R.string.noticia_sin_foto)))
        {
            foto.setImageResource(R.drawable.mundo);
            this.pbFoto.setVisibility(View.GONE);
            this.foto.setVisibility(View.VISIBLE);
        }
        else
        {
            new ATFoto(noticia.getRutaImagenGrande(), this.pbFoto).execute(foto);
        }

        this.botonVerWEB = (Button)getView().findViewById(R.id.button_ver_navegador);
        botonVerWEB.setVisibility(View.VISIBLE);
        final String direccion = noticia.getDireccionWebOriginal();
        botonVerWEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(direccion)); // DIRECCION WE QUE VIENE DEL OBJETO NOTICIA...
                startActivity(intent);
            }
        });
    }

    public TextView getTitulo(){
        return this.titulo;
    }

    public TextView getDescripcion(){
        return this.descripcion;
    }

    public ImageView getFoto(){
        return this.foto;
    }
}
