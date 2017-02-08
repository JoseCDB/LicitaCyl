package es.jcyl.barquejo.app.licitacyl.parser.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by josecarlos.delbarrio on 20/09/2016.
 */
public class LicitaCyLDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public long refreshedOn;

    public long identificador = -1;
    public String titulo = null;
    public String descripcion = null;
    public String tematica = null;
    //public Date fechaPrimeraPub = null;
    public String fechaPrimeraPub = null;
    public String convocanteAlt = null;

    //public Date fechaBocyl = null;
    //public Date fechaBoe = null;
    //public Date fechaDoue = null;

    public String fechaBocyl = null;
    public String fechaBoe = null;
    public String fechaDoue = null;

    public String tipoLicitacion = null;
    public String tipoLicitacionAlt = null;
    public String clasePrestacion = null;
    public String tipoContratacion = null;
    public String tipoTramitacion = null;

    public String formaAdjudicacion = null;

    //public Date fechaInicioComputoPlazos = null;
    //public Date fechaLimite = null;
    public String fechaInicioComputoPlazos = null;
    public String fechaLimite = null;
    public String horaLimite = null;

    public String lugarPresentacion = null;
    //public Date fechaAperturaOfertas = null;
    public String fechaAperturaOfertas = null;
    public String horaApertura = null;
    public String lugarApertura = null;
    //public Date fechaAperturaMulCrit = null;
    public String fechaAperturaMulCrit = null;
    public String horaAperturaMulCrit = null;
    public String lugarAperturaMulCrit = null;
    public String plazoEjecucion = null;

    public String informacionAdicional = null;
    public String docusAsociados = null;
    public String nombresDocus = null;
    public String recursosEnlazados = null;
    public String nombresEnlazados = null;

    //public Date fechaResolucion = null;
    //public Date fechaAdjudicacionProvi = null;
    //public Date fechaDelcaracionDes = null;
    public String fechaResolucion = null;
    public String fechaAdjudicacionProvi = null;
    public String fechaDelcaracionDes = null;
    public String incidencias = null;
    public String reclamacion = null;
    //public Date fechaModificacion = null;
    public String fechaModificacion = null;
    public String enlaceContenido = null;


}
