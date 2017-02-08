package es.jcyl.barquejo.app.licitacyl.backend.handler;

import android.database.SQLException;

import java.util.Collection;

import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 27/09/2016.
 */
public interface CollectionDataSaverBlock <M extends LicitaCyLDTO>{
    public void storeData(Collection<M> data) throws SQLException;
}
