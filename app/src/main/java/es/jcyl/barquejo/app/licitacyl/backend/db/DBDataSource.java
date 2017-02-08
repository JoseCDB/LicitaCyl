package es.jcyl.barquejo.app.licitacyl.backend.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import es.jcyl.barquejo.app.licitacyl.backend.App;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;


public class DBDataSource {
    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper = null;

    /**
     * Constructor
     */
    public DBDataSource() {
        dbHelper = new DatabaseHelper(App.getContext());
    }

    public void open() {
        //Con método que DatabaseHelper hereda de SQLiteOpenHelper
        db = dbHelper.getWritableDatabase(); //Hasta que no se llama este método, la DB no se crea (no se llama al onCreate)
    }


    /* -------------------------------------------------- TABLA LICITACIONES ------------------------------------------------------------------- */
    /**
     * Elimina las licitaciones que hubiese en Base de datos e inserta las nuevas.
     *
     * @param licis Colección con Licitaciones
     * @return boolean
     */
    public boolean sustituyeActividades(Collection<LicitaCyLDTO> licis) {
        boolean success = true;
        // Inicia la transacción en modo EXCLUSIVO
        db.beginTransaction();
        try {
            // 1º Se eliminan todas las actividades.
            eliminaLicitaciones();
            // 2º Se insertan una a una las nuevas actividades.
            for (LicitaCyLDTO lic : licis) {
                if (!insertaLicitacion(lic)) {
                    success = false;
                    break;
                }
            }

            //En la original existe una tabla (TAB_REFRESHINFO) con nombre de resto de tablas y fecha de actualización.
            if (success) {
                success = insertOrUpdateRefreshDate(DBTableLicitaciones.NAME, System.currentTimeMillis());
            }

            if (success) {
                db.setTransactionSuccessful();// 3º Se marca la transacción como exitosa.
            }
            return success;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Elimina las filas existentes en la Tabla TAB_LICI.
     *
     * @return void
     */
    public void eliminaLicitaciones() {
        //Se realiza el borrado de la tabla.
        db.delete(DBTableLicitaciones.NAME, null, null);
    }

    /**
     * Inserta una licitación en la tabla TAB_LICI.
     * @param lic actividad a insertar.
     * @return boolean Éxito o no en la operación de inserción.
     */
    public boolean insertaLicitacion(LicitaCyLDTO lic) {
        ContentValues values = DBTableLicitaciones.pasaDatosModeloAValores(lic);
        try {
            db.insertOrThrow(DBTableLicitaciones.NAME, null, values);
        } catch (SQLException sqle) {
            System.out.println("Imposible insertar la licitación");
            sqle.printStackTrace();
            return false;
        }
        return true;
    }

     /* -------------------------------------------------- TABLA REFRESCOS ------------------------------------------------------------------- */

    /**
     * Obtiene el último refresco en la tabla "TAB_REFRESHINFO"  de la tabla "TAB_LICI" pasáda como parámetro.
     * @param table Nombre de tabla a refrescar
     * @return long con el tiempo de último refresco en milisegundos.
     */
    public long getLastRefreshFor(String table) {
        Cursor c = db.query(DBTableRefreshInfo.NAME, DBTableRefreshInfo.COLUMNS, DBTableRefreshInfo.COL_NAME + " = '" + table + "'", null, null, null, null);
        if (c != null && !c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            int refresco = -1;
            String nombre;
            if (c.getCount() > 0) {
                do {
                    if (!c.isNull(0))
                        nombre = c.getString(0);
                    if (!c.isNull(1))
                        refresco = c.getInt(1);
                } while (c.moveToNext());
            }
            return refresco;
        }
        try {
            if (c != null && c.isAfterLast()) {
                return -1;
            }
            long timestamp = c.getLong(1);

            return timestamp;
        } finally {
            c.close();
        }
    }

    /**
     * Método que llama a los tres siguientes.
     * Comprueba si ha habido un refresco de datos para actualizarlos o
     * si son nuevos para insertarlos.
     *
     * @param table Nombre de la tabla sobre la que se han actualizado datos o insertado.
     * @param when Tiempo actual en milisegundos para la inserción o actualización del refresco.
     * @return boolean Con el resultado de actualización o inserción.
     */
    public boolean insertOrUpdateRefreshDate(String table, long when) {
        if (checkExistsRefresh(table)) {
            return updateRefreshDate(table, when);
        } else {
            return insertRefreshDate(table, when);
        }
    }

    /**
     * Inserta en la tabla TAB_REFRESHINFO el nombre de
     * la tabla actualizada y le fecha en long de actualización
     *
     * @param table Nombre de la tabla sobre la que se han insertado datos.
     * @param when Tiempo actual en milisegundos para la inserción del refresco.
     * @return boolean Con el resultado de la inserción.
     */
    public boolean insertRefreshDate(String table, long when) {
        ContentValues values = new ContentValues();
        values.put(DBTableRefreshInfo.COL_NAME, table);
        values.put(DBTableRefreshInfo.COL_REFRESHDATE, when);

        try {
            db.insertOrThrow(DBTableRefreshInfo.NAME, null, values);
        } catch (SQLException sqle) {

            System.out.println("Unable to insert refresh info");
            sqle.printStackTrace();
            return false;
        }
        System.out.println("Inserted refresh for table " + table);
        return true;
    }

    /**
     * Actualiza en la tabla TAB_REFRESHINFO el nombre de
     * la tabla actualizada y le fecha en long de actualización
     *
     * @param table Nombre de la tabla sobre la que se han actualizado datos .
     * @param when Tiempo actual en milisegundos para la inserción del refresco.
     * @return boolean Con el resultado de la actualización.
     */
    public boolean updateRefreshDate(String table, long when) {
        ContentValues values = new ContentValues();
        values.put(DBTableRefreshInfo.COL_NAME, table);
        values.put(DBTableRefreshInfo.COL_REFRESHDATE, when);

        try {
            db.update(DBTableRefreshInfo.NAME, values, DBTableRefreshInfo.COL_NAME + " = '" + table + "'", null);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
        System.out.println("Updated refresh info for table " + table);
        return true;
    }

    public boolean checkExistsRefresh(String name) {
        Cursor c = db.query(DBTableRefreshInfo.NAME, new String[] {DBTableRefreshInfo.COL_REFRESHDATE}, DBTableRefreshInfo.COL_NAME + " = '" + name + "'", null, null, null, null );
        try {
            c.moveToFirst();
            if (!c.isAfterLast()) {
                return true;
            }
            return false;
        } finally {
            c.close();
        }
    }

    /**
     * Realiza la consulta (con los datos aportados) a la base de datos SQLite.
     *
     * @param textDesc          Texto de búsqueda en descripción
     * @param clasePrestacion   Tipo curso, taller o charla
     * @param tematica          Localidad donde se realiza (Presencial)
     * @param tipoLici          Localidad donde se realiza (Presencial)
     * @param fechaPrimeraPub   Localidad donde se realiza (Presencial)
     * @return Collection<LicitaCyLDTO> Listado de objetos licitación  recuperadas.
     */
    public Collection<LicitaCyLDTO> consultaLicitaciones(String textDesc, String clasePrestacion, String tematica,
                                                    String tipoLici, String fechaPrimeraPub ) throws SQLException {
        // StringBuffer de filtros para la query
        StringBuffer osConditions = new StringBuffer();

        // Filtro texto libre. Se compara el campo de texto libre con la descripción o el título de la licitación.
        if (textDesc != null && textDesc.length() > 0) {
            //Se realiza un normalizado Unicode del texto. Decomposición canónica "NFD".
            String normalized = Normalizer.normalize(textDesc, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

            osConditions.append("(").append(DBTableLicitaciones.COL_DESCRIPTION).append(" LIKE '%").append(normalized).append("%' ")
                    .append(" OR ").append(DBTableLicitaciones.COL_TITLE).append(" LIKE '%").append(normalized).append("%'")
                    .append(")");
        }
        //Filtro Clase de prestación
        if (clasePrestacion != null && clasePrestacion.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            osConditions.append(DBTableLicitaciones.COL_CLASS).append(" LIKE '%").append(clasePrestacion).append("%' ");
        }
        //Filtro Temática
        if (tematica != null && tematica.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            osConditions.append(DBTableLicitaciones.COL_MATTER).append(" LIKE '%").append(tematica).append("%' ");
        }
        //Filtro Tipo licitación
        if (tipoLici != null && tipoLici.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            osConditions.append(DBTableLicitaciones.COL_LICITYPE).append(" LIKE '%").append(tipoLici).append("%' ");
        }



        Date date;
        SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

        //Filtro Fecha de primera publicación
        if (fechaPrimeraPub != null && fechaPrimeraPub.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            String fechaIniAMD = "";
            try{
                date = dmyFormat.parse(fechaPrimeraPub);
                fechaIniAMD = ymdFormat.format(date);
            }catch (ParseException pe) { pe.printStackTrace();}
            osConditions.append(DBTableLicitaciones.COL_FIRSTPUBDATE).append(" >= '").append(fechaIniAMD).append("' ");
        }


        Cursor c = db.query(DBTableLicitaciones.NAME,  //TABLA
                DBTableLicitaciones.COLUMNS, // COLUMNAS SELECCIONADAS
                osConditions.toString(), //FILTRO
                null, // SELECTION ARGS
                null, //GROUP BY
                null, //HAVING
                //DBTableLicitaciones.COL_FIRSTPUBDATE + " DESC, " +
                DBTableLicitaciones.COL_FIRSTPUBDATE + " ASC");//ORDER BY
        //Mueve el cursor a la primera fila.
        c.moveToFirst();
        try {
            List<LicitaCyLDTO> lics = DBTableLicitaciones.pasaCursorAListaModelos(c);
            return lics;
        } finally {
            c.close();
        }
    }

}
