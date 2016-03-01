package com.mobaires.noticias.vista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobaires.noticias.R;
import com.mobaires.noticias.datos.DatosTelefono;
import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.logica.ATNoticiasWeb;
import com.mobaires.noticias.logica.AdaptadorNoticiasWeb;
import com.mobaires.noticias.logica.CacheFotos;
import com.mobaires.noticias.logica.Variables;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

 /*
    Esta clase maneja el fragment "lista".
 */

public class NoticiasListaFragment extends Fragment implements ATNoticiasWeb.IListaCallbackWeb {

    public static final String BUSCAR = "buscar";
    private ListView lista;
    private List<NoticiaWeb> listaNoticiasWeb;
    private String idiomaBusqueda;
    private String buscar = "";

    String topico = "";

    Button botonAtras;
    Button botonAdelante;

    public static int posicionPagina = 0;
    public static int numeroPagina = 1;

    String cantidadResultados = "8";

    public void setBusqueda(String textoBusqueda, String topico){

        // Este método recibe el texto de búsqueda y carga los resultados en la lista...

        String busqueda = URLEncoder.encode(textoBusqueda); // esto reemplaza los espacios por %20 entre otras cosas....
        this.idiomaBusqueda = Variables.getCodigoIdiomaTelefono(getActivity());
        this.topico = topico;
        this.listaNoticiasWeb.clear();

        buscar = busqueda;

        posicionPagina = 0;
        numeroPagina = 1;

        this.LlenarListaAsyncTaskWeb(getContext(), busqueda, this.idiomaBusqueda, cantidadResultados, String.valueOf(posicionPagina), topico);

        CacheFotos.fotos.clear();
        botonAtras.setVisibility(View.GONE);
        botonAdelante.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noticias_list, container, false);

        if(savedInstanceState != null){

            // mantener el activity luego de cerrado
           this.buscar = savedInstanceState.getString(BUSCAR);
        }

        this.lista = (ListView)view.findViewById(R.id.listView_lista);

        this.idiomaBusqueda = Variables.getCodigoIdiomaTelefono(getActivity());

        // La primera vez que inicia la app buscar estará vacio,
        // entonces le cargo un valor inicial por default para popular
        // la listView.
        if(buscar == ""){buscar = Variables.getBusquedaInicial(getActivity());}

        this.LlenarListaAsyncTaskWeb(getContext(), buscar, this.idiomaBusqueda, cantidadResultados,  String.valueOf(posicionPagina), topico); // AT con noticias de la web.


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // CAPTURAR EL OBJETO DE LA LISTA SELECCIONADO Y MANDA
                NoticiaWeb noticiaWeb = listaNoticiasWeb.get(position);
                AbrirNoticia(noticiaWeb);
            }
        });

        // BOTON ADELANTE
        botonAdelante = new Button(getContext());
        botonAdelante.setText(getResources().getString(R.string.proxima_pagina));
        lista.addFooterView(botonAdelante);

        botonAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ES LA CANTIDAD DE NOTICIAS QUE SE DEJAN ATRAS O BIEN DESDE DONDE SE CARGARAN LAS NUEVAS:
                int apartirDe = posicionPagina("adelante");

                // ---------------------------------------------------------------------------------------

                LlenarListaAsyncTaskWeb(getContext(),
                                        buscar,
                                        idiomaBusqueda,
                                        cantidadResultados,
                                        String.valueOf(apartirDe),
                                        topico);

                lista.setAdapter(new AdaptadorNoticiasWeb(getActivity(), listaNoticiasWeb));

                if(numeroPagina == 8){
                    botonAdelante.setVisibility(View.GONE);
                }
            }
        });

        // BOTON ATRAS
        botonAtras = new Button(getContext());
        botonAtras.setText(getResources().getString(R.string.anterior_pagina));
        lista.addFooterView(botonAtras);
        if(posicionPagina == 0){botonAtras.setVisibility(View.GONE);}

        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int apartirDe = posicionPagina("atras");

                LlenarListaAsyncTaskWeb(getContext(),
                                        buscar,
                                        idiomaBusqueda,
                                        cantidadResultados,
                                        String.valueOf(apartirDe),
                                        topico);

                lista.setAdapter(new AdaptadorNoticiasWeb(getActivity(), listaNoticiasWeb));
                botonAdelante.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public int posicionPagina(String modo){
        int salida = 0;

        if(modo == "adelante") {
            posicionPagina = posicionPagina + Integer.valueOf(cantidadResultados);
            salida = posicionPagina;
            numeroPagina++;
        }

        if(modo == "atras"){
            posicionPagina = posicionPagina - Integer.valueOf(cantidadResultados);

            if(posicionPagina > 0)
            {
                salida = posicionPagina;
                numeroPagina--;
            }
            else
            {
                posicionPagina = 0;
                salida = 0;
                numeroPagina = 1;
            }
        }

        if(posicionPagina == 0)
        {
            botonAtras.setVisibility(View.GONE);
        }
        else
        {
            botonAtras.setVisibility(View.VISIBLE);
        }
        Toast.makeText(getContext(), getResources().getString(R.string.pagina_numero) + ": " + numeroPagina, Toast.LENGTH_SHORT).show();

        return salida;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // mantener el activity en estado luego de destruido

        outState.putString(BUSCAR, this.buscar);
    }

    private void LlenarListaAsyncTaskWeb(Context c, String textoBusqueda, String idioma, String cantidadResultados, String paginador, String topico){
        // Método que popula la listview de noticias de la web mediante AsyncTask
        // El resultado de este procedmiento sale por TaskFinished() ()Ver abajo...

        new ATNoticiasWeb(c, this, textoBusqueda, idioma, cantidadResultados, paginador, topico).execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNoticeSelectedListener)
        {
            listener = (OnNoticeSelectedListener) context;
        }
        else
        {
            throw new ClassCastException(context.toString() + getResources().getString(R.string.error_noticeselected));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void AbrirNoticia(NoticiaWeb noticia) {

        listener.onNoticeSelected(noticia);
    }

    // ASYNCTASK WEB ---------------------------------------------------
    private ProgressDialog dialog;
    @Override
    public void prepareUIWeb() {

        lista.setVisibility(View.GONE);

        // Este if es para que al realizar búsqueda no quede cargado el dialog...
        if(dialog != null) {
            dialog.dismiss();//setReatainInstance(true)
            dialog = null;
        }

        String bus = URLDecoder.decode(buscar);
        if(this.topico != "")
        {
            switch (this.topico)
            {
                case "b":
                    bus = getResources().getString(R.string.tabhost_economia);
                    break;

                case "t":
                    bus = getResources().getString(R.string.tabhost_ciencia);
                    break;

                case "m":
                    bus = getResources().getString(R.string.tabhost_salud);
                    break;

                case "s":
                    bus = getResources().getString(R.string.tabhost_depoertes);
                    break;

                case "e":
                    bus = getResources().getString(R.string.tabhost_entretenimiento);
                    break;

                default:
                    break;
            }
        }

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getResources().getString(R.string.buscando) + ": " + bus);
        dialog.show();
    }

    @Override
    public void taskFinishedWeb(List<NoticiaWeb> lista) {

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        if(lista.isEmpty())
        {
            this.lista.setVisibility(View.GONE);

            if(getResources().getBoolean(R.bool.dual_pane))
            {
                Toast.makeText(getActivity(), getResources().getString(R.string.sin_resultado), Toast.LENGTH_SHORT).show();
            }
            else
            {
                TextView sinResultados = (TextView)getView().findViewById(R.id.tv_sin_resultados);
                sinResultados.setVisibility(View.VISIBLE);
                sinResultados.setText(getResources().getString(R.string.sin_resultado));
            }
        }
        else
        {
            this.listaNoticiasWeb = lista;
            this.lista.setVisibility(View.VISIBLE);
            this.lista.setAdapter(new AdaptadorNoticiasWeb(getActivity(), this.listaNoticiasWeb));
        }
    }
    // ----------------------------------------------------------------

    // INTERF.. -----------------------------------------
    private OnNoticeSelectedListener listener;

    public interface OnNoticeSelectedListener {
        void onNoticeSelected(NoticiaWeb noticia);
    }
    // --------------------------------------------------
}
