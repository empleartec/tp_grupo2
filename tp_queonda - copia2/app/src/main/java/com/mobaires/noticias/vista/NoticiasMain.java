package com.mobaires.noticias.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mobaires.noticias.R;
import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.logica.CompartirNoticia;

import java.sql.Time;



 /*
    Principal
    El programa empieza aquí...

 */

public class NoticiasMain extends AppCompatActivity implements NoticiasListaFragment.OnNoticeSelectedListener {

    TabHost tabs;

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
        else
        {
            // FALTA TERMINAR TEXTO HARDCODEADO !!!
            String fecha = "12/12/2015";
            TextView marquesina = (TextView)findViewById(R.id.marquesina);
            marquesina.setText("Hoy es: " + fecha + ". Aquí podrían visualizarse noticias, cotizaciones bursátiles o datos de teléfono como una agenda o algún tipo de mensaje...");
            marquesina.setSelected(true);
        }

        cargarTab();
    }

    private void cargarTab(){

        // forma de poner una foto: spec.setIndicator("", getResources().getDrawable(R.drawable.bajar_foto));

        tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec spec;

        spec = tabs.newTabSpec("tabGeneral");
        spec.setContent(R.id.tab_noticias_general);
        spec.setIndicator(getResources().getString(R.string.tabhost_personalizado));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tabEconomia");
        spec.setContent(R.id.tab_noticias_economia);
        spec.setIndicator(getResources().getString(R.string.tabhost_economia));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tabDeportes");
        spec.setContent(R.id.tab_noticias_deportes);
        spec.setIndicator(getResources().getString(R.string.tabhost_depoertes));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tabEntretenimiento");
        spec.setContent(R.id.tab_noticias_entretenimiento);
        spec.setIndicator(getResources().getString(R.string.tabhost_entretenimiento));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tabSalud");
        spec.setContent(R.id.tab_noticias_salud);
        spec.setIndicator(getResources().getString(R.string.tabhost_salud));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tabCiencia");
        spec.setContent(R.id.tab_noticias_ciencia);
        spec.setIndicator(getResources().getString(R.string.tabhost_ciencia));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {

                switch (tabId){

                    case "tabGeneral":

                        searchItem.setVisible(true);
                        searchView.setVisibility(View.VISIBLE);
                        // llenarLista("", "", R.id.listFragment);
                        NoticiasListaFragment.numeroPagina = 1;
                        NoticiasListaFragment.posicionPagina = 0;
                        break;

                    case "tabEconomia":

                        searchItem.setVisible(false);
                        searchView.setVisibility(View.GONE);
                        llenarLista("", "b", R.id.listFragment_economia);
                        NoticiasListaFragment.numeroPagina = 1;
                        NoticiasListaFragment.posicionPagina = 0;
                        break;

                    case "tabDeportes":

                        searchItem.setVisible(false);
                        searchView.setVisibility(View.GONE);
                        llenarLista("", "s", R.id.listFragment_deportes);
                        NoticiasListaFragment.numeroPagina = 1;
                        NoticiasListaFragment.posicionPagina = 0;
                        break;

                    case "tabEntretenimiento":

                        searchItem.setVisible(false);
                        searchView.setVisibility(View.GONE);
                        llenarLista("", "e", R.id.listFragment_entretenimiento);
                        NoticiasListaFragment.numeroPagina = 1;
                        NoticiasListaFragment.posicionPagina = 0;
                        break;

                    case "tabSalud":

                        searchItem.setVisible(false);
                        searchView.setVisibility(View.GONE);
                        llenarLista("", "m", R.id.listFragment_salud);
                        NoticiasListaFragment.numeroPagina = 1;
                        NoticiasListaFragment.posicionPagina = 0;
                        break;

                    case "tabCiencia":

                        searchItem.setVisible(false);
                        searchView.setVisibility(View.GONE);
                        llenarLista("", "t", R.id.listFragment_ciencia);
                        NoticiasListaFragment.numeroPagina = 1;
                        NoticiasListaFragment.posicionPagina = 0;
                        break;

                    default:
                        break;
                }
            }});
    }

    Menu menu;
    MenuItem searchItem;
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu; // preciso el menu para mas adelante agrgar o no el boton share si esta seleccionada la noticia

        searchItem = menu.findItem(R.id.action_search);

        // Búsqueda en la barra .....
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint(getResources().getString(R.string.hint_busqueda_toolbar)); // Hint buscar

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {

                String query = pQuery; // aquí lo que tipeo el usuario

                llenarLista(query, "", R.id.listFragment);

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

    private void llenarLista(String query, String topico, int listFrag){

        // Mando y recargo fragment lista...
        NoticiasListaFragment nlf;
        nlf = (NoticiasListaFragment) getSupportFragmentManager().findFragmentById(listFrag);

        nlf.setBusqueda(query, topico); // Consulta y Topico
    }

    private void mostrarBotonShare(){

        MenuItem share = menu.findItem(R.id.action_share);
        share.setVisible(true);
    }

    private void cargarFragment(NoticiaWeb noticia){

        NoticiaDetalleFragment ddf;

        switch (this.tabs.getCurrentTabTag()){

            case "tabGeneral":
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment);
                ddf.setNoticia(noticia);
                break;

            case "tabEconomia":
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment_economia);
                ddf.setNoticia(noticia);
                break;

            case "tabDeportes":
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment_deportes);
                ddf.setNoticia(noticia);
                break;

            case "tabEntretenimiento":
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment_entretenimiento);
                ddf.setNoticia(noticia);
                break;

            case "tabSalud":
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment_salud);
                ddf.setNoticia(noticia);
                break;

            case "tabCiencia":
                ddf = (NoticiaDetalleFragment)getSupportFragmentManager().findFragmentById(R.id.detailFragment_ciencia);
                ddf.setNoticia(noticia);
                break;
        }
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
