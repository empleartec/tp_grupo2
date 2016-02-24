package com.mobaires.noticias.vista;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
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

import java.io.ByteArrayOutputStream;
import java.net.URI;

 /*
    Activity de detalle de la noticia (es el activity que viene luego del click en la lista portrait).
    Esta Activity recibe un objeto noticia por parcelable, lo crea y luego lo muestra.
 */

public class NoticiaDetalleActivity extends AppCompatActivity {

    TextView descripcion;
    TextView titulo;
    ImageView imagenNoticia;

    Button botonVerWEB;
    ProgressBar pbFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias_detail);

        // Si usuario gira el telefono dual_pane se hace true y se ejecuta el finish.
        if (getResources().getBoolean(R.bool.dual_pane)) {
            finish();
            return;
        }

        // RECIBIR EL OBJETO (NOTICIA) QUE PROVIENE DE LA LISTA
        NoticiaWeb noticiaRecibida = this.RecibirNoticia();

        // MOSTRAR EL OBJETO RECIBIDO
        this.MostrarDatos(noticiaRecibida);

        // EVENTO AL CLICKEAR LA IMAGEN
        this.clickImagen(noticiaRecibida);
    }

    private void clickImagen(final NoticiaWeb noticia){


        this.imagenNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NoticiaFotoDetalle.class);
                intent.putExtra(getResources().getString(R.string.enviar_objeto_noticia), noticia);
                startActivity(intent);
            }
        });
    }

    private NoticiaWeb RecibirNoticia(){

        // METODO QUE RECIBE LA NOTICIA Y LA RETORNA.

        Intent intent = getIntent();

        NoticiaWeb noticiaSeleccionada = (NoticiaWeb)intent.getParcelableExtra(getResources().getString(R.string.enviar_objeto_noticia)); // Viene bajo el nombre "objeto"

        return noticiaSeleccionada;
    }

    private void MostrarDatos(NoticiaWeb noticia){

        this.descripcion = (TextView)findViewById(R.id.textView_descripcion_activity);
        descripcion.setText(noticia.getDescripcion());

        this.titulo = (TextView)findViewById(R.id.textView_titulo_activity_detalle);
        titulo.setText(noticia.getTitulo());


        this.botonVerWEB = (Button)findViewById(R.id.button_ver_navegador);
        final String direccion = noticia.getDireccionWebOriginal();
        botonVerWEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(direccion)); // DIRECCION WEB QUE VIENE DEL OBJETO NOTICIA...
                startActivity(intent);
            }
        });


        this.pbFoto = (ProgressBar)findViewById(R.id.pb_foto_det_activ);


        this.imagenNoticia = (ImageView)findViewById(R.id.imageView_foto_port);

        if(noticia.getRutaImagenGrande().matches(getResources().getString(R.string.noticia_sin_foto)))
        {
            imagenNoticia.setImageResource(R.drawable.mundo);
            this.pbFoto.setVisibility(View.GONE);
            this.imagenNoticia.setVisibility(View.VISIBLE);

        }
        else
        {
            // cambiar a noticia.getRutaImagen() para obtener la foto chica,
            // en caso de problemas o cargas muy lentas !!!!
            new ATFoto(noticia.getRutaImagenGrande(), this.pbFoto).execute(imagenNoticia);

        }

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.editor) + ": " + noticia.getEditor(), Toast.LENGTH_SHORT).show();

        this.CargarBarra(noticia.getEditor(), noticia.getFecha());
    }

    private void CargarBarra(String editor, String fecha){

        // METODO PARA HACER OPERACIONES CON LA BARRA
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.banerbarra));
        getSupportActionBar().setTitle(editor); // muestro editor en la barra
        getSupportActionBar().setSubtitle(fecha); // muestro fecha en la barra
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_activity_detalle, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.action_share:

                try
                {
                    new CompartirNoticia().compartir(this, this.titulo, this.descripcion, this.imagenNoticia);
                }
                catch(Exception e)
                {
                    Toast.makeText(this, getResources().getString(R.string.apps_soportadas),Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
