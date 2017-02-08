package es.jcyl.barquejo.app.licitacyl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.jcyl.barquejo.app.licitacyl.Constants;
import es.jcyl.barquejo.app.licitacyl.MainActivity;
import es.jcyl.barquejo.app.licitacyl.R;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;
import es.jcyl.barquejo.app.licitacyl.utils.CustomExpandableListAdapter;
import es.jcyl.barquejo.app.licitacyl.utils.ExpandExpandableListView;

public class DetalleActivity extends MainActivity {

    private LicitaCyLDTO it;

    ExpandExpandableListView expandableListView;//Objeto ExpandableListView mostrado en content_detalle.xml
    HashMap<String, List<String>> expandableListDetalles = new HashMap<String, List<String>>(); //Mapa con titulo de apartado y lista de valores para el apartado
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitles;
    private SimpleDateFormat sdf = new SimpleDateFormat(" d 'de' MMMM 'de' yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Botón atrás en toolbar. Hay que decirle cual es su parent en el Manifest.xml. Y al parent -> android:launchMode="singleTop"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Enable up icon. the home icon should be used as "Up"
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Constants.TITULO_APP);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Intent intent = getIntent();
        it = (LicitaCyLDTO) intent.getSerializableExtra("SELECTED_ITEM");


        //Código para la ExpandableListView: INICIO
        expandableListView = (ExpandExpandableListView) findViewById(R.id.expandableListView);

        //HashMap con Título mas Datos de "Publicidad en el perfil del contratante"
        List<String> valoresPublicidadPerfil = new ArrayList<String>();
        if(it.fechaPrimeraPub != null && !it.fechaPrimeraPub.equals("")) {
            try{
                StringBuffer st = new StringBuffer();
                st.append(it.fechaPrimeraPub);
                st.replace(20, 24, " ");
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).parse(st.toString());
                valoresPublicidadPerfil.add("Inicio de la publicación: " + sdf.format(date));
            }catch (ParseException pe) {
                System.err.println("Problemas parseando fecha de inicio de publicación");
            }
        }
        //if(it.ultimaActualizacion != null && !it.ultimaActualizacion.equals("")) valoresPublicidadPerfil.add("Última actualización: " + it.ultimaActualizacion);
        if(valoresPublicidadPerfil.size()>0) expandableListDetalles.put(Constants.TIT_PUB_PERF_CONTRATANTE, valoresPublicidadPerfil);


        //HashMap con Título mas Datos de Presentación de Oferta
        List<String> valoresPresentacion = new ArrayList<String>();
        if(it.fechaLimite != null && !it.fechaLimite.equals("")) {
            try{
                StringBuffer st = new StringBuffer();
                st.append(it.fechaLimite);
                st.replace(20, 24, " ");
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).parse(st.toString());
                valoresPresentacion.add("Fecha limite de presentación de ofertas: " + sdf.format(date));
            }catch (ParseException pe) {
                System.err.println("Problemas parseando la fecha de limite de presentación de ofertas");
            }
        }
        if(it.horaLimite != null && !it.horaLimite.equals("")) valoresPresentacion.add("Hora limite de presentación de ofertas: " + it.horaLimite);
        if(it.lugarPresentacion != null && !it.lugarPresentacion.equals("")) valoresPresentacion.add("Lugar de presentación: " + it.lugarPresentacion);
        if(valoresPresentacion.size()>0) expandableListDetalles.put(Constants.TIT_LISTA_EXP_PESENTACION, valoresPresentacion);

        //HashMap con Título mas Datos de "Apertura de los criterios no evaluables mediante fórmulas(SOBRE 2)"
        List<String> valoresApertura2 = new ArrayList<String>();
        if(it.fechaAperturaMulCrit != null && !it.fechaAperturaMulCrit.equals("")) {
            try{
                StringBuffer st = new StringBuffer();
                st.append(it.fechaAperturaMulCrit);
                st.replace(20, 24, " ");
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).parse(st.toString());
                valoresApertura2.add("Fecha de apertura de ofertas: " + sdf.format(date));
            }catch (ParseException pe) {
                System.err.println("Problemas parseando fecha de apertura de criterios no evaluables");
            }
        }
        if(it.horaAperturaMulCrit != null && !it.horaAperturaMulCrit.equals("")) valoresApertura2.add("Hora de apertura de ofertas: " + it.horaAperturaMulCrit);
        if(it.lugarAperturaMulCrit != null && !it.lugarAperturaMulCrit.equals("")) valoresApertura2.add("Lugar de apertura de ofertas: " + it.lugarAperturaMulCrit);
        if(valoresApertura2.size()>0) expandableListDetalles.put(Constants.TIT_LISTA_EXP_APE_SOBRE_2, valoresApertura2);

        //HashMap con Título mas Datos de "Apertura de ofertas (sobre 3)(SOBRE 3)"
        List<String> valoresApertura3 = new ArrayList<String>();
        if(it.fechaAperturaOfertas != null && !it.fechaAperturaOfertas.equals("")) {
            try{
                StringBuffer st = new StringBuffer();
                st.append(it.fechaAperturaOfertas);
                st.replace(20, 24, " ");
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).parse(st.toString());
                valoresApertura3.add("Fecha de apertura de ofertas: " + sdf.format(date));
            }catch (ParseException pe) {
                System.err.println("Problemas parseando fecha de apertura de ofertas");
            }
        }
        if(it.horaApertura != null && !it.horaApertura.equals("")) valoresApertura3.add("Hora de apertura de ofertas: " + it.horaApertura);
        if(it.lugarApertura != null && !it.lugarApertura.equals("")) valoresApertura3.add("Lugar de apertura de ofertas: " + it.lugarApertura);
        if(valoresApertura3.size()>0) expandableListDetalles.put(Constants.TIT_LISTA_EXP_APE_SOBRE_3, valoresApertura3);

        //Se obtiene el listado con los títulos.
        expandableListTitles = new ArrayList<String>(expandableListDetalles.keySet());
        //Se añade al adapter la lista con títulos y el hashMap con Detalles
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitles, expandableListDetalles);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            /**
             * Se lanza cuando se intenta cerrar el grupo de datos de la ExpandableListView
             * @param groupPosition
             */
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), expandableListTitles.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            /**
             * Se lanza cuando se intenta expandir el grupo de datos de la ExpandableListView
             * @param groupPosition
            */
            @Override
            public void onGroupExpand(int groupPosition) {
                //Muestra un mensaje
                //Toast.makeText(getApplicationContext(), expandableListTitles.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
                //Se contraen todas, menos la que expando.
                for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*
                Toast.makeText(getApplicationContext(),expandableListTitles.get(groupPosition) + " -> " + expandableListDetalles.get(expandableListTitles.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
                        .show();
                        */
                return false;
            }
        });
        //Código para la ExpandableListView: FIN

        //Título de la licitación
        TextView tv = (TextView) this.findViewById(R.id.titulo_licitacion);
        if(it.titulo != null && !it.titulo.equals("")) {
            tv.setText(it.titulo);
        }

        //Descripción de la licitación
        tv = (TextView) findViewById(R.id.descripcion_licitacion);
        if(it.descripcion != null && !it.descripcion.equals("")){
            tv.setText(Html.fromHtml(it.descripcion));
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.titulo_descripción);
            tv.setVisibility(View.GONE);
        }
        //Tipo de contratación / Procedimiento de adjudicación
        tv = (TextView) findViewById(R.id.tipoContratacion);
        if(it.tipoContratacion != null && !it.tipoContratacion.equals("")){
            tv.setText(it.tipoContratacion);
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.titulo_tipoContratacion);
            tv.setVisibility(View.GONE);
        }

        //Tipo de contrato
        tv = (TextView) findViewById(R.id.tipoLicitacion);
        if(it.tipoLicitacion != null && !it.tipoLicitacion.equals("")){
            tv.setText(it.tipoLicitacion);
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.titulo_tipoLicitacion);
            tv.setVisibility(View.GONE);
        }

        //Forma de adjudicación
        tv = (TextView) findViewById(R.id.formaAdjudicacion);
        if(it.formaAdjudicacion != null && !it.formaAdjudicacion.equals("")){
            tv.setText(it.formaAdjudicacion);
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.titulo_formaAdjudicación);
            tv.setVisibility(View.GONE);
        }

        //Tipo de tramitación
        tv = (TextView) findViewById(R.id.tipoTramitación);
        if(it.tipoTramitacion != null && !it.tipoTramitacion.equals("")){
            tv.setText(it.tipoTramitacion);
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.titulo_tipoTramitacion);
            tv.setVisibility(View.GONE);
        }

        //Plazo ejecución
        tv = (TextView) findViewById(R.id.plazoEjecucion);
        if(it.plazoEjecucion != null && !it.plazoEjecucion.equals("")){
            tv.setText(it.plazoEjecucion);
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.titulo_plazoEjecucion);
            tv.setVisibility(View.GONE);
        }

        //Fecha de resolución
        tv = (TextView) findViewById(R.id.fechaResolucion);
        if(it.fechaResolucion != null && !it.fechaResolucion.equals("")) {
            try {
                StringBuffer st = new StringBuffer();
                st.append(it.fechaResolucion);
                st.replace(20, 24, " ");
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US).parse(st.toString());//M may. para obtener el mes correcto
                tv.setText(sdf.format(date));
            }catch (ParseException pe) {
                System.err.println("Problemas parseando fecha de resolución");
            }
        } else {
            tv.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.fechaResolucion);
            tv.setVisibility(View.GONE);
        }

    }//onCreate
}
