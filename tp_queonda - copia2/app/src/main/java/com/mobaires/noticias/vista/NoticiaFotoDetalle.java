package com.mobaires.noticias.vista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobaires.noticias.R;
import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.logica.ATFoto;
import com.mobaires.noticias.logica.CompartirNoticia;

/*
    Muestra la imagen de la noticia y una pequeña descripción en grande.
    Permite guardar la foto a celular y compartirla con otras aplicaciones.
*/

public class NoticiaFotoDetalle extends AppCompatActivity {

    ImageView foto;
    TextView titulo;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_foto_detalle);

        this.foto = (ImageView)findViewById(R.id.iv_foto_noticia_foto);
        this.titulo = (TextView)findViewById(R.id.tv_foto_noticia_titulo);
        this.pb = (ProgressBar)findViewById(R.id.pb_foto_grande_detalle);

        this.cargarBarra();
        this.recibirDatos();
    }

    private void recibirDatos(){

        // Recibe la foto y el título...

        Intent intent = getIntent();
        NoticiaWeb noticiaSeleccionada = (NoticiaWeb)intent.getParcelableExtra(getResources().getString(R.string.enviar_objeto_noticia));
        this.mostrarDatos(noticiaSeleccionada);
    }

    private void mostrarDatos(NoticiaWeb noticia){

        // Muestra la foto y el título...

        this.titulo.setText(noticia.getTitulo());

        if(!noticia.getRutaImagenGrande().toString().matches(getResources().getString(R.string.noticia_sin_foto)))
        {
            new ATFoto(noticia.getRutaImagenGrande(), this.pb).execute(this.foto);
            this.foto.setVisibility(View.VISIBLE);
        }
        else
        {
            this.foto.setImageResource(R.drawable.mundo);
            this.pb.setVisibility(View.GONE);
            this.foto.setVisibility(View.VISIBLE);
        }
    }

    private void cargarBarra(){

        // METODO PARA HACER OPERACIONES CON LA BARRA
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.banerbarra));

        getSupportActionBar().setTitle(getResources().getString(R.string.guardar_imagen_titulo));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.guardar_imagen_subtitulo));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_activity_detalle, menu);

        MenuItem item = menu.findItem(R.id.bajar_foto);
        item.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.action_share:

                try
                {
                    new CompartirNoticia().compartir(this, this.foto);
                }
                catch(Exception e)
                {
                    Toast.makeText(this, getResources().getString(R.string.apps_soportadas), Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.bajar_foto:

                try
                {
                    guardarFoto();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.guardar_imagen_incorrecto), Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardarFoto(){

        CompartirNoticia.guardarImagen(getApplicationContext(), foto, getResources().getString(R.string.guardar_imagen_nombre_foto));
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.guardar_imagen_correcto), Toast.LENGTH_LONG).show();
    }
}
