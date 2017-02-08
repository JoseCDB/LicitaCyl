package es.jcyl.barquejo.app.licitacyl.backend.handler;

import java.util.Collection;

import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 27/09/2016.
 */
public class SaverBlock implements CollectionDataSaverBlock<LicitaCyLDTO> {

    LicitaCyLHandler lictaHandler = new LicitaCyLHandler();

    @Override
    public void storeData(Collection<LicitaCyLDTO> data) {
        lictaHandler.guardaLicitaciones(data);
    }
}
