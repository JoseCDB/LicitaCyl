package es.jcyl.barquejo.app.licitacyl.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

import es.jcyl.barquejo.app.licitacyl.Constants;
import es.jcyl.barquejo.app.licitacyl.MainActivity;
import es.jcyl.barquejo.app.licitacyl.R;
import es.jcyl.barquejo.app.licitacyl.backend.handler.LicitaCyLHandler;
import es.jcyl.barquejo.app.licitacyl.model.Family;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;
import es.jcyl.barquejo.app.licitacyl.utils.Utils;

public class BuscadorActivity extends MainActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Campo buscar
    private EditText search;
    //Botones.
    private Button botonBuscar, botonLimpiar, botonFechaPub;
    //Combos
    private Spinner clasePrestacion, tematica, tipoLicitacion;
    //Texto de valores seleccionados en los combos
    private String clasePrestacionStr, tematicaStr, tipoLicitacionStr = "";

    private TextView fechaPublicacion;
    //Fecha publicación
    private Calendar cal;

    private int day, month, year;

    Collection<LicitaCyLDTO> col;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //de aquí va a main
        setContentView(R.layout.activity_licitaciones);

        i = new Intent(this, ListActivity.class);
        search = (EditText) this.findViewById(R.id.search);
        //La acción de cuando se pulsa la lupa del teclado
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // String s = search.getText().toString();
                    // requestData(false, s, provinceVal, familyVal);
                    ///list.setRefreshing();
                    //i = new Intent(this, ListActivity.class);
                    new RetriveStock().execute(search.getText().toString());

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        clasePrestacion = (Spinner) this.findViewById(R.id.spnClasePrestación);
        //Para que cuando se selecciona un valor en el Spinner, el contexto recoja el escuchador. Tengo que implementar interfaz OnItemSelectedListener
        clasePrestacion.setOnItemSelectedListener(this);
        //Se crea el adaptador, para rellenar el combo
        FamilyAdapter adapterClase = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.clasePrestacion);
        adapterClase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clasePrestacion.setAdapter(adapterClase);

        tematica = (Spinner) this.findViewById(R.id.spnTematica);
        tematica.setOnItemSelectedListener(this);
        FamilyAdapter adapterTematica = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.tematica);
        adapterTematica.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tematica.setAdapter(adapterTematica);

        tipoLicitacion = (Spinner) this.findViewById(R.id.spnTipoLicitacion);
        tipoLicitacion.setOnItemSelectedListener(this);
        FamilyAdapter adapterTipoLicitacion = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.tipoLicitacion);
        adapterTipoLicitacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoLicitacion.setAdapter(adapterTipoLicitacion);


        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        fechaPublicacion = (TextView) findViewById(R.id.fechaPub);

        botonFechaPub = (Button) findViewById(R.id.btnFechaPub);
        botonFechaPub.setOnClickListener(this);

        botonBuscar = (Button) this.findViewById(R.id.btnBusqueda);
        //botonBuscar.setTextColor(getResources().getColor(R.color.background));
        //botonBuscar.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.board_icon_selected_selector, 0, 0);
        botonBuscar.setOnClickListener(this);
        botonLimpiar = (Button) this.findViewById(R.id.btnLimpiar);
        botonLimpiar.setOnClickListener(this);
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            fechaPublicacion.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
        }
    };

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == botonBuscar) {
            //Collection<LicitaCyLDTO> col =
            //LicitaCyLHandler.listaLicitacionesGeneral(search.getText().toString(), clasePrestacionStr, tematicaStr, tipoLicitacionStr, "");
            i = new Intent(this, ListActivity.class);
            new RetriveStock().execute(search.getText().toString());
        } else if (v == botonFechaPub) {
            showDialog(0);
        } else if (v == botonLimpiar) {
            clasePrestacion.setSelection(0);
            tematica.setSelection(0);
            tipoLicitacion.setSelection(0);
            search.setText("");
            fechaPublicacion.setText("");
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapter, View arg1, int index, long arg3) {
        if (adapter == clasePrestacion) {
            if (index == 0) {
                clasePrestacionStr = "";
            } else {
                // provinceVal = location.getSelectedItem().toString();
                clasePrestacionStr = ((Family) clasePrestacion.getSelectedItem()).getCode();
                //Change the selected item's text color
                ((TextView) arg1).setTextColor(Color.argb(100,15,77,0));
            }
        } else if (adapter == tematica) {
            if (index == 0) {
                tematicaStr = "";
            } else {
                tematicaStr = ((Family) tematica.getSelectedItem()).getCode();
                ((TextView) arg1).setTextColor(Color.argb(100,15,77,0));
            }
        } else if (adapter == tipoLicitacion) {
            if (index == 0) {
                tipoLicitacionStr = "";
            } else {
                tipoLicitacionStr = ((Family) tipoLicitacion.getSelectedItem()).getCode();
                ((TextView) arg1).setTextColor(Color.argb(100,15,77,0));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapter) {

    }

    /**
     * Clase que extiende de ArrayAdapter a la cual se le pasa en el constructor
     * Los objetos en un Array de tipo Family
     * El contexto (this)
     * La vista, un TextView para contener el valor del objeto de tipo Family
     * */
     private class FamilyAdapter extends ArrayAdapter<Family> {

        LayoutInflater inflater;

        /**
         *  Constructor con el que se crea un objeto LayoutInflater.
         */
        public FamilyAdapter(Context context, int textViewResourceId,  Family[] objects) {
            super(context, textViewResourceId, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        /*Devuelve/rellena en pantalla los elementos del combo*/
        public View getDropDownView(final int position, View convertView,
                                    ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);//con false no da error
            }

            TextView item = (TextView) convertView;
            item.setText(getItem(position).getName());
            final TextView finalItem = item;
            item.post(new Runnable() {
                @Override
                public void run() {
                    finalItem.setSingleLine(false);
                }
            });
            return item;
        }
    }// FamilyAdapter


    public class RetriveStock extends AsyncTask<String, Void, Void> {

        private ProgressDialog progress;

        byte[] bytes;

        @Override
        protected void onPreExecute() {
            //Create progress dialog here and show it
            progress = new ProgressDialog(BuscadorActivity.this);
            progress.setMessage("Buscando Licitaciones..");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            col = LicitaCyLHandler.listaLicitacionesGeneral(params[0],
                    clasePrestacionStr, tematicaStr, tipoLicitacionStr, "");

            if (col.size() > 0) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
                    ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
                    objectOut.writeObject(col);
                    objectOut.close();
                    bytes = baos.toByteArray();
                } catch (Exception ttle) {

                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        protected void onPostExecute(Void result) {
            if (col.size() > 0) {
                i.putExtra("DATA", (Serializable) bytes);
                startActivity(i);
            } else {
                Utils.showSimpleDialog(BuscadorActivity.this, null,
                        getString(R.string.no_results), null);
            }
            progress.dismiss(); // Dismissing my progress dialog
            // My UI updations and all using "message" string.
            super.onPostExecute(result);
        }

        //@Override
        //protected void onPreExecute() { super.onPreExecute();}

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
