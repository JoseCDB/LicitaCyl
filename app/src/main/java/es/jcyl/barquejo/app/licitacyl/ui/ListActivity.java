package es.jcyl.barquejo.app.licitacyl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import es.jcyl.barquejo.app.licitacyl.Constants;
import es.jcyl.barquejo.app.licitacyl.MainActivity;
import es.jcyl.barquejo.app.licitacyl.R;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

public class ListActivity extends MainActivity {

    private BoardAdapter adapter;
    private PullToRefreshListView list;
    private byte[] bites;
    Collection<LicitaCyLDTO> datos;
    private ArrayList<LicitaCyLDTO> data = new ArrayList<LicitaCyLDTO>();
    private SimpleDateFormat sdf = new SimpleDateFormat(" d 'de' MMMM 'de' yyyy", Locale.getDefault());
    private ImageView icono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Botón atrás en toolbar. Hay que decirle cual es su parent en el Manifest.xml. Y al parent -> android:launchMode="singleTop"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Enable up icon. the home icon should be used as "Up"
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Constants.TITULO_APP);

        Intent inte = getIntent();
        bites = (byte[]) inte.getSerializableExtra("DATA");

        ByteArrayInputStream bais = new ByteArrayInputStream(bites);
        try {
            GZIPInputStream gzipIn = new GZIPInputStream(bais);
            ObjectInputStream objectIn = new ObjectInputStream(gzipIn);

            datos = (Collection<LicitaCyLDTO>) objectIn.readObject();
            objectIn.close();
        } catch (IOException ioe) {

        } catch (ClassNotFoundException ioe) {

        }

        data.addAll(datos);

        //LISTA ITEMS ACTIVIDADES
        list = (PullToRefreshListView) findViewById(R.id.list);
        //list.setOnRefreshListener(this);
        adapter = new BoardAdapter();
        list.setAdapter(adapter);
        //list.setOnScrollListener(adapter);
        list.setOnItemClickListener(adapter);
        //list.setPullToRefreshOverScrollEnabled(false);
        list.setMode(Mode.DISABLED); //Desactivo el actualizar al tirar
        //list.setScrollingWhileRefreshingEnabled(false);
        //list.setRefreshingLabel(getString(R.string.update));
    }




    /**
     * Extiende de AutoPagingListAdapter
     * Implementa interfaz OnItemClickListener: implementar onItemClick()
     * Extiende AutoPagingListAdapter: implementar getView()
     * Rellena la lista de items
     *
     */
    private class BoardAdapter extends AutoPagingListAdapter<LicitaCyLDTO>
            implements AdapterView.OnItemClickListener {

        public BoardAdapter() {
            super(ListActivity.this, R.layout.item_licitacion, data);
        }

        /**
         * Se van rellenando los valores de cada item de la lista
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            //pillo el ITEM
            if (convertView == null) {
                if (type == TYPE_ITEM) {
                    convertView = getLayoutInflater().inflate(
                            R.layout.item_licitacion, null); //Pillo el Layout item_licitacion
                } else if (type == TYPE_LOADING) {
                    convertView = getLayoutInflater().inflate(
                            R.layout.item_menu, null);
                }
            }
            // y se le pasan los datos al ITEM
            if (type == TYPE_ITEM) {
                LicitaCyLDTO it = getItem(position); //Objeto LicitaCyLDTO

                //TÍTULO
                TextView tv = (TextView) convertView.findViewById(R.id.titulo_licitacion);
                if (it.titulo != null && !it.titulo.equals("")) {
                    tv.setText(it.titulo);
                }

                //DESCRIPCIÓN
                tv = (TextView) convertView.findViewById(R.id.descripcion_licitacion);
                if (it.descripcion != null && !it.descripcion.equals("")) {
                    tv.setText(it.descripcion);
                }

                //FECHA PRIMERA PUBLICACIÓN
                tv = (TextView) convertView.findViewById(R.id.fecha_primera_publicacion);
                if (it.fechaPrimeraPub != null) {
                    try {
                        StringBuffer st = new StringBuffer();
                        st.append(it.fechaPrimeraPub);
                        st.replace(20, 24, " ");
                        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).parse(st.toString());;
                        tv.setText("Primera publicación: " + sdf.format(date));
                    }catch (ParseException pe) {
                        System.err.println("Problemas parseando fecha de primera publicación");
                    }
                } else {
                    tv.setText("");
                }

                //TEMÁTICA
                tv = (TextView) convertView.findViewById(R.id.tematica);
                if (it.tematica != null && !it.tematica.equals("")) {
                    tv.setText("Temática: " + it.tematica);
                }

                //IMÁGEN CLASE PRESTACIÓN
                ImageView iv = (ImageView) convertView.findViewById(R.id.imagen);
                if (it.clasePrestacion != null && !it.clasePrestacion.equals("")) {

                    switch(it.clasePrestacion) {
                        case "Suministros":
                            iv.setImageResource(R.drawable.suministros);
                                break;
                        case "Obras":
                            iv.setImageResource(R.drawable.obras);
                            break;
                        case "Suministros sanitarios":
                            iv.setImageResource(R.drawable.sanitarios);
                            break;
                        case "Gestión del servicio público":
                            iv.setImageResource(R.drawable.gest_serv_publico);
                            break;
                    }
                }

            } else if (type == TYPE_LOADING) {
                TextView tv = (TextView) convertView.findViewById(R.id.text);
                tv.setText("Cargando...");
            }
            return convertView;
        }


        @Override
        protected void onScrollNext() {

            //requestData(false, search.getText().toString());
        }

        /**
         * Evento que se ejecuta cuando se selecciona una de las actividades de la lista.
         *
         * @param arg0
         * @param arg1
         * @param position
         * @param arg3
         */
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            LicitaCyLDTO it = getItem(position - 1);
            if (it != null) {
                //Globals.selectedItem = it;
                Intent i = new Intent(ListActivity.this, DetalleActivity.class);
                i.putExtra("SELECTED_ITEM", it);
                startActivity(i);
            }
        }
    }//BoardAdapter

}
