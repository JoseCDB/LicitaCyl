package es.jcyl.barquejo.app.licitacyl.backend.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 21/09/2016.
 */
public class DBTableLicitaciones extends DBTable {

    public static final String NAME = "TAB_LICI";

    public static final String METACOL_REFRESHEDON = "REFRESHEDON";

    public static final String COL_IDENTIFIER = "ID";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_MATTER = "MATTER";
    public static final String COL_FIRSTPUBDATE = "FIRSTPUBDATE";
    public static final String COL_ALTCALLER = "ALTCALLER";
    public static final String COL_BOCYLDATE = "BOCYLDATE";
    public static final String COL_BOEDATE = "BOEDATE";
    public static final String COL_DOUEDATE = "DOUEDATE";
    public static final String COL_LICITYPE = "LICITYPE";
    public static final String COL_ALTLICITYPE = "ALTLICITYPE";
    public static final String COL_CLASS = "CLASS";
    public static final String COL_HIRINGTYPE = "HIRINGTYPE";
    public static final String COL_TRAMTYPE = "TRAMTYPE";
    public static final String COL_ADJUDICATIONWAY = "ADJUDICATIONWAY";
    public static final String COL_STARTERMSDATE = "STARTERMSDATE";
    public static final String COL_DEADLINE = "DEADLINE";
    public static final String COL_LIMITHOUR = "LIMITHOUR";
    public static final String COL_PRESPLACE = "PRESPLACE";
    public static final String COL_OPENINGDATE = "OPENINGDATE";
    public static final String COL_OPENINGHOHUR = "OPENINGHOUR";
    public static final String COL_OPENINGPLACE = "OPENINGPLACE";
    public static final String COL_ENVELOPE2DATE = "ENVELOPE2DATE";
    public static final String COL_ENVELOPE2HOUR = "ENVELOPE2HOUR";
    public static final String COL_ENVELOPE2PLACE = "ENVELOPE2PLACE";
    public static final String COL_EXECUTIONTIME = "EXECUTIONTIME";
    public static final String COL_ADDITIONALINFO = "ADDITIONALINFO";
    public static final String COL_DOCUMENTS = "DOCUMENTS";
    public static final String COL_DOCUNAMES = "DOCUNAMES";
    public static final String COL_RESOURCES = "RESOURCES";
    public static final String COL_RESONAMES = "RESONAMES";
    public static final String COL_RESOLUTIONDATE = "RESOLUTIONDATE";
    public static final String COL_PROVISIONALDATE = "PROVISIONALDATE";
    public static final String COL_NULLDATE = "NULLDATE";
    public static final String COL_PROCISSUES = "PROCISSUES";
    public static final String COL_RECLAMATION = "RECLAMATION";
    public static final String COL_MODDATE = "MODDATE";
    public static final String COL_CONTENTSLINK = "CONTENTSLINK";

    public static String[] COLUMNS = {
            COL_IDENTIFIER,
            COL_TITLE,
            COL_DESCRIPTION,
            COL_MATTER,
            COL_FIRSTPUBDATE,
            COL_ALTCALLER,
            COL_BOCYLDATE,
            COL_BOEDATE,
            COL_DOUEDATE,
            COL_LICITYPE,
            COL_ALTLICITYPE,
            COL_CLASS,
            COL_HIRINGTYPE,
            COL_TRAMTYPE,
            COL_ADJUDICATIONWAY,
            COL_STARTERMSDATE,
            COL_DEADLINE,
            COL_LIMITHOUR,
            COL_PRESPLACE,
            COL_OPENINGDATE,
            COL_OPENINGHOHUR,
            COL_OPENINGPLACE,
            COL_ENVELOPE2DATE,
            COL_ENVELOPE2HOUR,
            COL_ENVELOPE2PLACE,
            COL_EXECUTIONTIME,
            COL_ADDITIONALINFO,
            COL_DOCUMENTS,
            COL_DOCUNAMES,
            COL_RESOURCES,
            COL_RESONAMES,
            COL_RESOLUTIONDATE,
            COL_PROVISIONALDATE,
            COL_NULLDATE,
            COL_PROCISSUES,
            COL_RECLAMATION,
            COL_MODDATE,
            COL_CONTENTSLINK,
            METACOL_REFRESHEDON
    };

    //Horas y Fechas como text
    private static final StringBuffer CREATE_DB = new StringBuffer()
            .append("CREATE TABLE ").append(NAME).append("(")
            .append(COL_IDENTIFIER).append(" INTEGER PRIMARY KEY, ")
            .append(COL_TITLE).append(" TEXT COLLATE NOCASE, ")
            .append(COL_DESCRIPTION).append(" TEXT COLLATE NOCASE, ")
            .append(COL_MATTER).append(" TEXT, ")//Temática combo multiples valores
            .append(COL_FIRSTPUBDATE).append(" INTEGER, ")//F
            .append(COL_ALTCALLER).append(" TEXT, ")
            .append(COL_BOCYLDATE).append(" INTEGER, ")//F
            .append(COL_BOEDATE).append(" INTEGER, ")//F
            .append(COL_DOUEDATE).append(" INTEGER, ")//F
            .append(COL_LICITYPE).append(" TEXT, ") //combo
            .append(COL_ALTLICITYPE).append(" TEXT COLLATE NOCASE, ")
            .append(COL_CLASS).append(" TEXT, ") //C
            .append(COL_HIRINGTYPE).append(" TEXT, ")//C
            .append(COL_TRAMTYPE).append(" TEXT, ")
            .append(COL_ADJUDICATIONWAY).append(" TEXT, ")
            .append(COL_STARTERMSDATE).append(" INTEGER, ")//F
            .append(COL_DEADLINE).append(" INTEGER, ")//F
            .append(COL_LIMITHOUR).append(" TEXT, ")
            .append(COL_PRESPLACE).append(" TEXT, ")
            .append(COL_OPENINGDATE).append(" INTEGER, ")//F
            .append(COL_OPENINGHOHUR).append(" TEXT, ")
            .append(COL_OPENINGPLACE).append(" TEXT, ")
            .append(COL_ENVELOPE2DATE).append(" INTEGER, ")//F
            .append(COL_ENVELOPE2HOUR).append(" TEXT, ")
            .append(COL_ENVELOPE2PLACE).append(" TEXT, ")
            .append(COL_EXECUTIONTIME).append(" TEXT, ")
            .append(COL_ADDITIONALINFO).append(" TEXT, ")
            .append(COL_DOCUMENTS).append(" TEXT, ")
            .append(COL_DOCUNAMES).append(" TEXT, ")
            .append(COL_RESOURCES).append(" TEXT, ")
            .append(COL_RESONAMES).append(" TEXT, ")
            .append(COL_RESOLUTIONDATE).append(" INTEGER, ")//F
            .append(COL_PROVISIONALDATE).append(" INTEGER, ")//F
            .append(COL_NULLDATE).append(" INTEGER, ")//F
            .append(COL_PROCISSUES).append(" TEXT, ")
            .append(COL_RECLAMATION).append(" TEXT, ")
            .append(COL_MODDATE).append(" INTEGER, ")//F
            .append(COL_CONTENTSLINK).append(" TEXT, ")
            .append(METACOL_REFRESHEDON).append(" INTEGER")
            .append(")");

    private void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_DB.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        System.out.println("Created table " + NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }


    /**
     * Rellena el objeto LicitaCyLDTO con los valores del Cursor tras la consulta local.
     *
     * @param c cursor con los valores para rellenar el objeto LicitaCyLDTO.
     * @return LicitaCyLDTO objeto tipo LicitaCyLDTO con los valores del de la licitación del cursor.
     */
    public static LicitaCyLDTO pasaCursorAModeloInividual(Cursor c) {
        LicitaCyLDTO lic = new LicitaCyLDTO();
        //posicion de la columna del cursor
        int pos = 0;

        lic.identificador = c.getInt(pos++);;
        lic.titulo = c.getString(pos++);
        lic.descripcion = c.getString(pos++);
        lic.tematica = c.getString(pos++);
        lic.fechaPrimeraPub = c.getString(pos++);
        lic.convocanteAlt = c.getString(pos++);

        lic.fechaBocyl = c.getString(pos++);
        lic.fechaBoe = c.getString(pos++);
        lic.fechaDoue = c.getString(pos++);

        lic.tipoLicitacion = c.getString(pos++);
        lic.tipoLicitacionAlt = c.getString(pos++);
        lic.clasePrestacion = c.getString(pos++);
        lic.tipoContratacion = c.getString(pos++);
        lic.tipoTramitacion = c.getString(pos++);

        lic.formaAdjudicacion = c.getString(pos++);
        lic.fechaInicioComputoPlazos = c.getString(pos++);
        lic.fechaLimite = c.getString(pos++);
        lic.horaLimite = c.getString(pos++);

        lic.lugarPresentacion = c.getString(pos++);
        lic.fechaAperturaOfertas = c.getString(pos++);
        lic.horaApertura = c.getString(pos++);
        lic.lugarApertura = c.getString(pos++);
        lic.fechaAperturaMulCrit = c.getString(pos++);
        lic.horaAperturaMulCrit = c.getString(pos++);
        lic.lugarAperturaMulCrit = c.getString(pos++);
        lic.plazoEjecucion = c.getString(pos++);

        lic.informacionAdicional = c.getString(pos++);
        lic.docusAsociados = c.getString(pos++);
        lic.nombresDocus = c.getString(pos++);
        lic.recursosEnlazados = c.getString(pos++);
        lic.nombresEnlazados = c.getString(pos++);

        lic.fechaResolucion = c.getString(pos++);
        lic.fechaAdjudicacionProvi = c.getString(pos++);
        lic.fechaDelcaracionDes = c.getString(pos++);
        lic.incidencias = c.getString(pos++);
        lic.reclamacion = c.getString(pos++);
        lic.fechaModificacion = c.getString(pos++);
        lic.enlaceContenido = c.getString(pos++);
        lic.refreshedOn = c.getLong(pos++);

        return lic;
    }

    /**
     * Pasa los valores contenidos en el objeto de tipo LicitaCyLDTO para la inserción en Local.
     *
     * @param lic objeto de tipo LicitaCyLDTO.
     * @return ContentValues
     */
    public static ContentValues pasaDatosModeloAValores(LicitaCyLDTO lic) {

        ContentValues values = new ContentValues();

        values.put(COL_IDENTIFIER, lic.identificador);
        values.put(COL_TITLE, lic.titulo);
        values.put(COL_DESCRIPTION, lic.descripcion);
        values.put(COL_MATTER, lic.tematica);
        values.put(COL_FIRSTPUBDATE, lic.fechaPrimeraPub);
        values.put(COL_ALTCALLER, lic.convocanteAlt);
        values.put(COL_BOCYLDATE, lic.fechaBocyl);
        values.put(COL_BOEDATE, lic.fechaBoe );
        values.put(COL_DOUEDATE, lic.fechaDoue );
        values.put(COL_LICITYPE, lic.tipoLicitacion);
        values.put(COL_ALTLICITYPE, lic.tipoLicitacionAlt);
        values.put(COL_CLASS, lic.clasePrestacion);
        values.put(COL_HIRINGTYPE, lic.tipoContratacion);
        values.put(COL_TRAMTYPE, lic.tipoTramitacion);
        values.put(COL_ADJUDICATIONWAY, lic.formaAdjudicacion);
        values.put(COL_STARTERMSDATE, lic.fechaInicioComputoPlazos);
        values.put(COL_DEADLINE, lic.fechaLimite);
        values.put(COL_LIMITHOUR, lic.horaLimite);
        values.put(COL_PRESPLACE, lic.lugarPresentacion);
        values.put(COL_OPENINGDATE, lic.fechaAperturaOfertas);
        values.put(COL_OPENINGHOHUR, lic.horaApertura);
        values.put(COL_OPENINGPLACE, lic.lugarApertura );
        values.put(COL_ENVELOPE2DATE, lic.fechaAperturaMulCrit);
        values.put(COL_ENVELOPE2HOUR, lic.horaAperturaMulCrit);
        values.put(COL_ENVELOPE2PLACE, lic.lugarAperturaMulCrit);
        values.put(COL_EXECUTIONTIME, lic.plazoEjecucion);
        values.put(COL_ADDITIONALINFO, lic.informacionAdicional);
        values.put(COL_DOCUMENTS, lic.docusAsociados );
        values.put(COL_DOCUNAMES, lic.nombresDocus);
        values.put(COL_RESOURCES, lic.recursosEnlazados);
        values.put(COL_RESONAMES, lic.nombresEnlazados);
        values.put(COL_RESOLUTIONDATE, lic.fechaResolucion);
        values.put(COL_PROVISIONALDATE, lic.fechaAdjudicacionProvi);
        values.put(COL_NULLDATE, lic.fechaDelcaracionDes);
        values.put(COL_PROCISSUES, lic.incidencias);
        values.put(COL_RECLAMATION, lic.reclamacion);
        values.put(COL_MODDATE, lic.fechaModificacion);
        values.put(COL_CONTENTSLINK,lic.enlaceContenido );
        values.put(METACOL_REFRESHEDON, System.currentTimeMillis());

        return values;
    }

    /**
     * Recorre el cursor añadiendo valores a objetos LicitaCyLDTO creando una Lista.
     * @param c
     * @return List<LicitaCyLDTO> con
     */
    public static List<LicitaCyLDTO> pasaCursorAListaModelos(Cursor c) {
        List<LicitaCyLDTO> lst = new java.util.LinkedList<LicitaCyLDTO>();
        //Si hay otra fila en el cursor..
        while (!c.isAfterLast()) {
            LicitaCyLDTO licis = pasaCursorAModeloInividual(c);
            lst.add(licis); //la añadimos a nuestra lista.
            c.moveToNext();
        }
        return lst;
    }
}
