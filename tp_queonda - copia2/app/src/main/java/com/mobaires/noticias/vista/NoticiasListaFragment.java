package com.mobaires.noticias.vista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobaires.noticias.R;
import com.mobaires.noticias.datos.DatosTelefono;
import com.mobaires.noticias.entidades.NoticiaWeb;
import com.mobaires.noticias.logica.ATNoticiasWeb;
import com.mobaires.noticias.logica.AdaptadorNoticiasWeb;
import com.mobaires.noticias.logica.Variables;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

 /*
    Esta clase maneja el fragment "lista".
 */

public class NoticiasListaFragment extends Fragment implements ATNoticiasWeb.IListaCallbackWeb {

    private ListView lista;
    private List<NoticiaWeb> listaNoticiasWeb;
    private String idiomaBusqueda;
    private static String buscar = ""; // static no pierde dato de busqueda al salir de activity


    public void setBusqueda(String textoBusqueda){

        // Este método recibe el texto de búsqueda y carga los resultados en la lista...

        String busqueda = URLEncoder.encode(textoBusqueda); // esto reemplaza los espacios por %20 entre otras cosas....
        this.idiomaBusqueda = Variables.getCodigoIdiomaTelefono(getActivity());

        buscar = busqueda;

        // en este punto si la app tuviera una configuración de idioma que el usuario puede cambiar
        // tomaría el idioma de ese combobox o configuración y se la asignaría a variable idioma.
        // sino se usa la default

        this.LlenarListaAsyncTaskWeb(getContext(), busqueda, this.idiomaBusqueda);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noticias_list, container, false);

        this.lista = (ListView)view.findViewById(R.id.listView_lista);

        this.idiomaBusqueda = Variables.getCodigoIdiomaTelefono(getActivity());

        // La primera vez que inicia la app buscar estará vacio,
        // entonces le cargo un valor inicial por default para popular
        // la listView.
        if(buscar == ""){buscar = Variables.getBusquedaInicial(getActivity());}

        this.LlenarListaAsyncTaskWeb(getContext(), buscar, this.idiomaBusqueda); // AT con noticias de la web.


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast para hacer test
                //Toast.makeText(getActivity(), "noticias: " + position + " " + noticia.getName(), Toast.LENGTH_LONG).show();

                // CAPTURAR EL OBJETO DE LA LISTA SELECCIONADO Y MANDA

                NoticiaWeb noticiaWeb = listaNoticiasWeb.get(position);
                AbrirNoticia(noticiaWeb);
            }
        });

        return view;
    }

    private void LlenarListaAsyncTaskWeb(Context c, String textoBusqueda, String idioma){
        // Método que popula la listview de noticias de la web mediante AsyncTask
        // El resultado de este procedmiento sale por TaskFinished() ()Ver abajo...

        new ATNoticiasWeb(c, this, textoBusqueda, idioma).execute();
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

        // Este if es para que al realizar búsqueda no quede cargado el dialog...
        if(dialog != null) {
            dialog.dismiss();//setReatainInstance(true)
            dialog = null;
        }

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getResources().getString(R.string.buscando) + ": " + URLDecoder.decode(buscar));
        dialog.show();
    }

    @Override
    public void taskFinishedWeb(List<NoticiaWeb> lista) {

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        this.lista.setVisibility(View.VISIBLE);
        this.listaNoticiasWeb = lista;
        this.lista.setAdapter(new AdaptadorNoticiasWeb(getActivity(), this.listaNoticiasWeb));

        // Si la búsqueda no arrojó resultados:
        if(this.listaNoticiasWeb.isEmpty())
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
    }
    // ----------------------------------------------------------------

    // INTERF.. -----------------------------------------
    private OnNoticeSelectedListener listener;

    public interface OnNoticeSelectedListener {
        void onNoticeSelected(NoticiaWeb noticia);
    }
    // --------------------------------------------------
}
