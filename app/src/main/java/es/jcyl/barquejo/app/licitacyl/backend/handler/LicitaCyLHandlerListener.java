package es.jcyl.barquejo.app.licitacyl.backend.handler;

import java.util.Collection;

import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 23/10/2015.
 */
public interface LicitaCyLHandlerListener<M extends LicitaCyLDTO> {
    public void onResults(String callId, Collection<M> res, boolean expectRefreshCall);
    public void onError(String callId, Exception e, boolean expectRefreshCall);
}