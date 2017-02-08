package es.jcyl.barquejo.app.licitacyl.backend.handler;

import android.database.SQLException;

import java.util.Collection;

import es.jcyl.barquejo.app.licitacyl.backend.ws.LicitaCyLWSException;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 27/09/2016.
 */
public interface CollectionDataRetrieverBlock <M extends LicitaCyLDTO>{
    /**
     * La implementación de este método obtendrá datos desde la BBDD de tipo LicitaCyLDTO.
     * @return
     * @throws SQLException
     */
    public Collection<M> localRetrieve(String textDesc, String clasePrestacion, String tematica,
                                       String tipoLici, String fechaPrimeraPub) throws SQLException;

    /**
     * La implementación de este método obtendrá datos del WS con información actualizada.
     * @return
     * @throws LicitaCyLWSException
     */
    public Collection<M> remoteRetrieve() throws LicitaCyLWSException;

    public Object getRemoteRetrieveToken();
}
