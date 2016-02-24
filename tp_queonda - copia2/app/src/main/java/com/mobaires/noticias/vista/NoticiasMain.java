package com.mobaires.noticias.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobaires.noticias.R;
import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.logica.CompartirNoticia;

 /*
    Principal
    El programa empieza aquí...

 */

public class NoticiasMain extends AppCompatActivity implements NoticiasListaFragment.OnNoticeSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setSubtitle(getResources().getString(R.string.subtitulo_barra));


        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.banerbarra));
        if (getResources().getBoolean(R.bool.dual_pane)) {

            // Si estoy en landscape muestro el mundito.
             getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.mundochico));
             getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.tp_final));
        }
    }

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu; // preciso el menu para mas adelante agrgar o no el boton share si esta seleccionada la noticia

        MenuItem searchItem = menu.findItem(R.id.action_search);

        // Búsqueda en la barra .....
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint(getResources().getString(R.string.hint_busqueda_toolbar)); // Hint buscar

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {

                String query = pQuery; // aquí lo que tipeo el usuario

                // Mando y recargo fragment lista...
                NoticiasListaFragment nlf;
                nlf = (NoticiasListaFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);

                nlf.setBusqueda(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    // METODO IMPLEMENTADO DE LA INTERFACE OnNoticeSelectedListener
    @Override
    public void onNoticeSelected(NoticiaWeb noticia) {

        boolean dual_pane = getResources().getBoolean(R.bool.dual_pane);

        // SI LA PANTALLA ESTA LANDSCAPE MANDO A DETAILFRAGMENT, SINO ABRO ACTITIVYDETAIL

        if (dual_pane) {

           // MOSTRAR EL OBJETO (NOTICIA) EN EL FRAGMENT DETALLE CONTIGUO
            cargarFragment(noticia);
            this.mostrarBotonShare();

        } else {

            // MOSTRAR EL OBJETO (NOTICIA) EN EL FRAGMENT DETALLE EN UN ACTIVITY
            cargarActivity(noticia);
        }
    }

    private void mostrarBotonShare(){

        MenuItem share = menu.findItem(R.id.action_share);
        share.setVisible(true);
    }

    private void cargarFragment(NoticiaWeb noticia){

        NoticiaDetalleFragment ddf;
        ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        ddf.setNoticia(noticia);
    }

    private void cargarActivity(NoticiaWeb noticia){

        Intent intent;
        intent = new Intent(getApplicationContext(), NoticiaDetalleActivity.class);
        intent.putExtra(getResources().getString(R.string.enviar_objeto_noticia), noticia);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            // BOTON "SHARE":
            case R.id.action_share:

                NoticiaDetalleFragment ddf;
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment);

                try
                {
                    new CompartirNoticia().compartir(this, ddf.getTitulo(), ddf.getDescripcion(), ddf.getFoto());
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
