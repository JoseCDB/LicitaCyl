package es.jcyl.barquejo.app.licitacyl.backend.handler;

import android.database.SQLException;

import java.util.Collection;

import es.jcyl.barquejo.app.licitacyl.backend.App;
import es.jcyl.barquejo.app.licitacyl.backend.ws.LicitaCyLWSException;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 27/09/2016.
 */
public class RetrieverBlock implements CollectionDataRetrieverBlock<LicitaCyLDTO> {

    LicitaCyLHandler lictaHandler = new LicitaCyLHandler();

    private static final String REMOTERETRIEVE_TOKEN = "wholeActivities";

    @Override
    public Collection<LicitaCyLDTO> remoteRetrieve() throws LicitaCyLWSException {
        return lictaHandler.getLicitacionesWebservice();
    }

    @Override
    public Collection<LicitaCyLDTO> localRetrieve(String textDesc, String clasePrestacion, String tematica,
                                                  String tipoLici, String fechaPrimeraPub) throws SQLException {
        try {
            // Desde la base de datos se obtienen datos de actividades.
            Collection<LicitaCyLDTO> data = App.getDataSource().consultaLicitaciones(textDesc, clasePrestacion, tematica, tipoLici, fechaPrimeraPub);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Imposible obtener datos de la base de datos");
        }
    }

    @Override
    public Object getRemoteRetrieveToken() {
        return REMOTERETRIEVE_TOKEN;
    }
}
