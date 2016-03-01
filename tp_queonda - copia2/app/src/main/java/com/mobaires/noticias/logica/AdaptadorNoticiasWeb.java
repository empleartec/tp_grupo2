package com.mobaires.noticias.logica;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobaires.noticias.R;

import com.mobaires.noticias.entidades.NoticiaWeb;

import java.util.List;

/*
    Clase adapter de la lista
*/

public class AdaptadorNoticiasWeb extends BaseAdapter {

    protected Activity activity;
    protected List<NoticiaWeb> items;


    public AdaptadorNoticiasWeb(Activity activity, List<NoticiaWeb> items) {

        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_noticia_web, null);

            holder = new ViewHolder();
            holder.setPbFoto((ProgressBar) v.findViewById(R.id.pb_foto_lista));
            holder.setFoto((ImageView) v.findViewById(R.id.imageView_foto));
            holder.setTitulo((TextView) v.findViewById(R.id.textView_nombre));
            holder.setEditor((TextView) v.findViewById(R.id.editor));
            holder.setInfo((TextView) v.findViewById(R.id.textView_info));
            holder.setFecha((TextView) v.findViewById(R.id.fecha_item_lista));
            holder.setIdioma((ImageView) v.findViewById(R.id.iv_foto_idioma));
            v.setTag(holder);
        }
        else
        {
            v = convertView;
            holder = (ViewHolder) v.getTag();
        }

        NoticiaWeb noticia = items.get(position);

        holder.getTitulo().setText(noticia.getTitulo());
        holder.getInfo().setText(noticia.getDescripcion());
        holder.getEditor().setText(noticia.getEditor());
        holder.getFecha().setText(noticia.getFecha());
        holder.getIdioma().setImageDrawable(BanderasIdiomas.seleccionarIdioma(noticia.getIdioma(), activity));

        holder.setPosition(position);

        this.limpiarImageView(holder); // Limpiar la ImageView

        if(!noticia.getRutaImagen().matches(this.activity.getResources().getString(R.string.noticia_sin_foto)))
        {
             new ATFoto(noticia.getRutaImagen(), holder.getPbFoto(),holder, position).execute(holder.getFoto());
        }
        else
        {
            // FOTO POR DEFAULT (mundo)
            holder.getFoto().setImageResource(R.drawable.mundochico);
            holder.getPbFoto().setVisibility(View.GONE);
            holder.getFoto().setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void limpiarImageView(ViewHolder holder){

        holder.getPbFoto().setVisibility(View.VISIBLE);
        holder.getFoto().setVisibility(View.GONE);
    }

    public static  class ViewHolder{

        private ProgressBar pbFoto;
        private ImageView foto;
        private TextView titulo;
        private TextView info;
        private TextView editor;
        private TextView fecha;
        private ImageView idioma;
        private int position;

        public ProgressBar getPbFoto() {
            return pbFoto;
        }

        public void setPbFoto(ProgressBar pbFoto) {
            this.pbFoto = pbFoto;
        }

        public ImageView getFoto() {
            return foto;
        }

        public void setFoto(ImageView foto) {
            this.foto = foto;
        }

        public TextView getTitulo() {
            return titulo;
        }

        public void setTitulo(TextView titulo) {
            this.titulo = titulo;
        }

        public TextView getInfo() {
            return info;
        }

        public void setInfo(TextView info) {
            this.info = info;
        }

        public TextView getEditor() {
            return editor;
        }

        public void setEditor(TextView editor) {
            this.editor = editor;
        }

        public TextView getFecha() {
            return fecha;
        }

        public void setFecha(TextView fecha) {
            this.fecha = fecha;
        }

        public ImageView getIdioma() {
            return idioma;
        }

        public void setIdioma(ImageView idioma) {
            this.idioma = idioma;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
