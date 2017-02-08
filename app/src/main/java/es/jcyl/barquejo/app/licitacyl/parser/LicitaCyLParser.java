package es.jcyl.barquejo.app.licitacyl.parser;

import android.text.Html;

import org.xml.sax.SAXException;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Map;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 20/09/2016.
 */
public class LicitaCyLParser extends GenericParser<LicitaCyLDTO> {

    private static final String ATTRVAL_IDENTIFIER = "Identificador";
    private static final String ATTRVAL_TITLE = "Titulo_es";
    private static final String ATTRVAL_DESCRIPTION = "DescripcionLicitacion_es";
    private static final String ATTRVAL_MATTER = "Tematica";
    private static final String ATTRVAL_FIRSTPUBDATE = "FechaPrimeraPublicacion";
    private static final String ATTRVAL_ALTCALLER = "ConvocanteAlternativo";
    private static final String ATTRVAL_BOCYLDATE = "FechaBocyl";
    private static final String ATTRVAL_BOEDATE = "FechaBoe";
    private static final String ATTRVAL_DOUEDATE = "FechaDoue";
    private static final String ATTRVAL_LICITYPE = "TipoLicitacion";
    private static final String ATTRVAL_ALTLICITYPE = "TipoLicitacionAlternativo_es";
    private static final String ATTRVAL_CLASS = "ClasePrestacion";
    private static final String ATTRVAL_HIRINGTYPE = "TipoContratacion";
    private static final String ATTRVAL_TRAMTYPE = "TipoTramitacion";
    private static final String ATTRVAL_ADJUDICATIONWAY = "FormaAdjudicacion";
    private static final String ATTRVAL_STARTERMSDATE = "FechaInicioComputoPlazos";
    private static final String ATTRVAL_DEADLINE = "FechaLimite";
    private static final String ATTRVAL_LIMITHOUR = "HoraLimite";
    private static final String ATTRVAL_PRESPLACE = "LugarPresentacion";
    private static final String ATTRVAL_OPENINGDATE = "FechaAperturaOfertas";
    private static final String ATTRVAL_OPENINGHOHUR = "HoraApertura";
    private static final String ATTRVAL_OPENINGPLACE = "LugarApertura";
    private static final String ATTRVAL_ENVELOPE2DATE = "FechaAperturaOfertasMultCriterios";
    private static final String ATTRVAL_ENVELOPE2HOUR = "HoraAperturaMultCriterios";
    private static final String ATTRVAL_ENVELOPE2PLACE = "LugarAperturaMultCriterios";
    private static final String ATTRVAL_EXECUTIONTIME = "PlazoEjecucion";
    private static final String ATTRVAL_ADDITIONALINFO = "InformacionAdicional_es";
    private static final String ATTRVAL_DOCUMENTS = "DocumentosAsociados_es";
    private static final String ATTRVAL_DOCUNAMES = "NombresDocumentos_es";
    private static final String ATTRVAL_RESOURCES = "RecursosEnlazados_es";
    private static final String ATTRVAL_RESONAMES = "NombresEnlazados_es";
    private static final String ATTRVAL_RESOLUTIONDATE = "FechaResolucion";
    private static final String ATTRVAL_PROVISIONALDATE = "FechaAdjudicacionProvisional";
    private static final String ATTRVAL_NULLDATE = "FechaDeclaracionDesierto";
    private static final String ATTRVAL_PROCISSUES = "IncidenciasProcedimiento";
    private static final String ATTRVAL_RECLAMATION = "ReclamacionLicitacion";
    private static final String ATTRVAL_MODDATE = "FechaModificacion";
    private static final String ATTRVAL_CONTENTSLINK = "Enlace al contenido";


    private static int pos = 1;
    private static final int CONTEXT_IDENTIFIER = pos++;
    private static final int CONTEXT_TITLE = pos++;
    private static final int CONTEXT_DESCRIPTION = pos++;
    private static final int CONTEXT_MATTER= pos++;
    private static final int CONTEXT_FIRSTPUBDATE = pos++;
    private static final int CONTEXT_ALTCALLER = pos++;
    private static final int CONTEXT_BOCYLDATE = pos++;
    private static final int CONTEXT_BOEDATE = pos++;
    private static final int CONTEXT_DOUEDATE = pos++;
    private static final int CONTEXT_LICITYPE = pos++;
    private static final int CONTEXT_ALTLICITYPE = pos++;
    private static final int CONTEXT_CLASS= pos++;
    private static final int CONTEXT_HIRINGTYPE = pos++;
    private static final int CONTEXT_TRAMTYPE= pos++;
    private static final int CONTEXT_ADJUDICATIONWAY= pos++;
    private static final int CONTEXT_STARTERMSDATE = pos++;
    private static final int CONTEXT_DEADLINE = pos++;
    private static final int CONTEXT_LIMITHOUR = pos++;
    private static final int CONTEXT_PRESPLACE = pos++;
    private static final int CONTEXT_OPENINGDATE = pos++;
    private static final int CONTEXT_OPENINGHOUR = pos++;
    private static final int CONTEXT_OPENINGPLACE = pos++;
    private static final int CONTEXT_ENVELOPE2DATE = pos++;
    private static final int CONTEXT_ENVELOPE2HOUR = pos++;
    private static final int CONTEXT_ENVELOPE2PLACE = pos++;
    private static final int CONTEXT_EXECUTIONTIME = pos++;
    private static final int CONTEXT_ADDITIONALINFO = pos++;
    private static final int CONTEXT_DOCUMENTS = pos++;
    private static final int CONTEXT_DOCUNAMES = pos++;
    private static final int CONTEXT_RESOURCES = pos++;
    private static final int CONTEXT_RESONAMES = pos++;
    private static final int CONTEXT_RESOLUTIONDATE = pos++;
    private static final int CONTEXT_PROVISIONALDATE = pos++;
    private static final int CONTEXT_NULLDATE = pos++;
    private static final int CONTEXT_PROCISSUES = pos++;
    private static final int CONTEXT_RECLAMATION = pos++;
    private static final int CONTEXT_MODDATE = pos++;
    private static final int CONTEXT_CONTENTSLINK = pos++;

    private static Map<String, Integer> attributeToContextMap = new java.util.HashMap<String, Integer>();

    private SimpleDateFormat dtFmt = new SimpleDateFormat("yyyyMMdd");

    static {
        attributeToContextMap.put(ATTRVAL_IDENTIFIER, CONTEXT_IDENTIFIER);
        attributeToContextMap.put(ATTRVAL_TITLE, CONTEXT_TITLE);
        attributeToContextMap.put(ATTRVAL_DESCRIPTION, CONTEXT_DESCRIPTION);
        attributeToContextMap.put(ATTRVAL_MATTER, CONTEXT_MATTER);
        attributeToContextMap.put(ATTRVAL_FIRSTPUBDATE, CONTEXT_FIRSTPUBDATE);
        attributeToContextMap.put(ATTRVAL_ALTCALLER, CONTEXT_ALTCALLER);
        attributeToContextMap.put(ATTRVAL_BOCYLDATE, CONTEXT_BOCYLDATE);
        attributeToContextMap.put(ATTRVAL_BOEDATE, CONTEXT_BOEDATE);
        attributeToContextMap.put(ATTRVAL_DOUEDATE, CONTEXT_DOUEDATE);
        attributeToContextMap.put(ATTRVAL_LICITYPE, CONTEXT_LICITYPE);
        attributeToContextMap.put(ATTRVAL_ALTLICITYPE, CONTEXT_ALTLICITYPE);
        attributeToContextMap.put(ATTRVAL_CLASS, CONTEXT_CLASS);
        attributeToContextMap.put(ATTRVAL_HIRINGTYPE, CONTEXT_HIRINGTYPE);
        attributeToContextMap.put(ATTRVAL_TRAMTYPE, CONTEXT_TRAMTYPE);
        attributeToContextMap.put(ATTRVAL_ADJUDICATIONWAY, CONTEXT_ADJUDICATIONWAY);
        attributeToContextMap.put(ATTRVAL_STARTERMSDATE, CONTEXT_STARTERMSDATE);
        attributeToContextMap.put(ATTRVAL_DEADLINE, CONTEXT_DEADLINE);
        attributeToContextMap.put(ATTRVAL_LIMITHOUR, CONTEXT_LIMITHOUR);
        attributeToContextMap.put(ATTRVAL_PRESPLACE, CONTEXT_PRESPLACE);
        attributeToContextMap.put(ATTRVAL_OPENINGDATE, CONTEXT_OPENINGDATE);
        attributeToContextMap.put(ATTRVAL_OPENINGHOHUR, CONTEXT_OPENINGHOUR);
        attributeToContextMap.put(ATTRVAL_OPENINGPLACE, CONTEXT_OPENINGPLACE);
        attributeToContextMap.put(ATTRVAL_ENVELOPE2DATE, CONTEXT_ENVELOPE2DATE);
        attributeToContextMap.put(ATTRVAL_ENVELOPE2HOUR, CONTEXT_ENVELOPE2HOUR);
        attributeToContextMap.put(ATTRVAL_ENVELOPE2PLACE, CONTEXT_ENVELOPE2PLACE);
        attributeToContextMap.put(ATTRVAL_EXECUTIONTIME, CONTEXT_EXECUTIONTIME);
        attributeToContextMap.put(ATTRVAL_ADDITIONALINFO, CONTEXT_ADDITIONALINFO);
        attributeToContextMap.put(ATTRVAL_DOCUMENTS, CONTEXT_DOCUMENTS);
        attributeToContextMap.put(ATTRVAL_DOCUNAMES, CONTEXT_DOCUNAMES);
        attributeToContextMap.put(ATTRVAL_RESOURCES, CONTEXT_RESOURCES);
        attributeToContextMap.put(ATTRVAL_RESONAMES, CONTEXT_RESONAMES);
        attributeToContextMap.put(ATTRVAL_RESOLUTIONDATE, CONTEXT_RESOLUTIONDATE);
        attributeToContextMap.put(ATTRVAL_PROVISIONALDATE, CONTEXT_PROVISIONALDATE);
        attributeToContextMap.put(ATTRVAL_NULLDATE, CONTEXT_NULLDATE);
        attributeToContextMap.put(ATTRVAL_PROCISSUES, CONTEXT_PROCISSUES);
        attributeToContextMap.put(ATTRVAL_RECLAMATION, CONTEXT_RECLAMATION);
        attributeToContextMap.put(ATTRVAL_MODDATE, CONTEXT_MODDATE);
        attributeToContextMap.put(ATTRVAL_CONTENTSLINK, CONTEXT_CONTENTSLINK);
    }



    @Override
    public String getAppendableOnlyUnderElement(int context) {
        if ( context == CONTEXT_IDENTIFIER
                ||context == CONTEXT_TITLE
                || context == CONTEXT_MATTER
                || context == CONTEXT_FIRSTPUBDATE
                || context == CONTEXT_ALTCALLER
                || context == CONTEXT_BOCYLDATE
                || context == CONTEXT_BOEDATE
                || context == CONTEXT_DOUEDATE
                || context == CONTEXT_ALTLICITYPE
                || context == CONTEXT_STARTERMSDATE
                || context == CONTEXT_DEADLINE
                || context == CONTEXT_LIMITHOUR
                || context == CONTEXT_PRESPLACE
                || context == CONTEXT_OPENINGDATE
                || context == CONTEXT_OPENINGHOUR
                || context == CONTEXT_OPENINGPLACE
                || context == CONTEXT_ENVELOPE2DATE
                || context == CONTEXT_ENVELOPE2HOUR
                || context == CONTEXT_ENVELOPE2PLACE
                || context == CONTEXT_EXECUTIONTIME
                || context == CONTEXT_ADDITIONALINFO
                || context == CONTEXT_DOCUMENTS
                || context == CONTEXT_DOCUNAMES
                || context == CONTEXT_RESOURCES
                || context == CONTEXT_RESONAMES
                || context == CONTEXT_RESOLUTIONDATE
                || context == CONTEXT_PROVISIONALDATE
                || context == CONTEXT_NULLDATE
                || context == CONTEXT_PROCISSUES
                || context == CONTEXT_RECLAMATION
                || context == CONTEXT_MODDATE
                || context == CONTEXT_CONTENTSLINK
                || context == CONTEXT_ADJUDICATIONWAY
                ) {
            return "string";
        } else if (context == CONTEXT_DESCRIPTION)  {
            return "text";
		} else if (context == CONTEXT_LICITYPE) {
			//return "TipoLicitacion";
            return "string";
		} else if (context == CONTEXT_CLASS) {
			//return "ClasePrestacion";
            return "string";
		} else if (context == CONTEXT_HIRINGTYPE) {
			//return "TipoContratacion";
            return "string";
        } else if (context == CONTEXT_TRAMTYPE) {
            //return "TipoTramitacion";
            return "string";
        } else {
            return null;
        }

    }


    @Override
    public void setAttributeValue(int context, String value, LicitaCyLDTO dto) {
        try {
            if (context == CONTEXT_IDENTIFIER) {
                dto.identificador = Long.parseLong(value);
            } else if (context == CONTEXT_TITLE) {
                dto.titulo = value;
            } else if (context == CONTEXT_DESCRIPTION) {
                dto.descripcion = Html.fromHtml(value).toString();
            } else if (context == CONTEXT_MATTER) {
                dto.tematica = value;
            } else if (context == CONTEXT_ALTCALLER) {
                dto.convocanteAlt = value;
            } else if (context == CONTEXT_LICITYPE) {
                dto.tipoLicitacion = value;
            } else if (context == CONTEXT_ALTLICITYPE) {
                dto.tipoLicitacionAlt = value;
            } else if (context == CONTEXT_CLASS) {
                dto.clasePrestacion = value;
            } else if (context == CONTEXT_HIRINGTYPE) {
                dto.tipoContratacion = value;
            } else if (context == CONTEXT_TRAMTYPE) {
                dto.tipoTramitacion = value;
            } else if (context == CONTEXT_ADJUDICATIONWAY) {
                dto.formaAdjudicacion = value;
            } else if (context == CONTEXT_LIMITHOUR) {
                dto.horaLimite = value;
            } else if (context == CONTEXT_PRESPLACE) {
                dto.lugarPresentacion = value;
            } else if (context == CONTEXT_OPENINGHOUR) {
                dto.horaApertura = value;
            } else if (context == CONTEXT_OPENINGPLACE) {
                dto.lugarApertura = value;
            } else if (context == CONTEXT_ENVELOPE2HOUR) {
                dto.horaAperturaMulCrit = value;
            } else if (context == CONTEXT_ENVELOPE2PLACE) {
                dto.lugarAperturaMulCrit = value;
            } else if (context == CONTEXT_FIRSTPUBDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaPrimeraPub = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_BOCYLDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaBocyl = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_BOEDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaBoe = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_DOUEDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaDoue = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_STARTERMSDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaInicioComputoPlazos = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_OPENINGDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaAperturaOfertas = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_ENVELOPE2DATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaAperturaMulCrit = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_RESOLUTIONDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaResolucion = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_PROVISIONALDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaAdjudicacionProvi = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_NULLDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaDelcaracionDes = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_MODDATE) {
                if (value != null && value.length() > 0) {
                    dto.fechaModificacion = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_DEADLINE) {
                if (value != null && value.length() > 0) {
                    dto.fechaLimite = dtFmt.parse(value).toString();
                }
            } else if (context == CONTEXT_EXECUTIONTIME) {
                dto.plazoEjecucion = value;
            } else if (context == CONTEXT_ADDITIONALINFO) {
                dto.informacionAdicional = value;
            } else if (context == CONTEXT_DOCUMENTS) {
                dto.docusAsociados = value;
            } else if (context == CONTEXT_DOCUNAMES) {
                dto.nombresDocus = value;
            } else if (context == CONTEXT_RESOURCES) {
                dto.recursosEnlazados = value;
            } else if (context == CONTEXT_RESONAMES) {
                dto.nombresEnlazados = value;
            } else if (context == CONTEXT_PROCISSUES) {
                dto.incidencias = value;
            } else if (context == CONTEXT_CONTENTSLINK) {
                dto.enlaceContenido = value;
            } else if (context == CONTEXT_RECLAMATION) {
                dto.reclamacion = value;
            }
        } catch (ParseException pe) {
            System.err.println("Unable to set date " + value + " on context " + context);
        }
    }

    @Override
    public LicitaCyLDTO createNewDTO() {
        return new LicitaCyLDTO();
    }

    @Override
    public int getContextForAttribute(String attr) {
        if (attributeToContextMap.containsKey(attr)) {
            return attributeToContextMap.get(attr);
        }
        return 0;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
    }

}
