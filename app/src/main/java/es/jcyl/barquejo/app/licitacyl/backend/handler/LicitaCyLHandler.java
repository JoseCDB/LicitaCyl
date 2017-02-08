package es.jcyl.barquejo.app.licitacyl.backend.handler;

import android.os.AsyncTask;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.jcyl.barquejo.app.licitacyl.backend.App;
import es.jcyl.barquejo.app.licitacyl.backend.db.DBTableLicitaciones;
import es.jcyl.barquejo.app.licitacyl.backend.ws.LicitaCyLWS;
import es.jcyl.barquejo.app.licitacyl.backend.ws.LicitaCyLWSException;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;
import es.jcyl.barquejo.app.licitacyl.ui.SplashActivity;

/**
 * Created by josecarlos.delbarrio on 21/09/2016.
 */
public class LicitaCyLHandler {

    private static Refresco refresco = new Refresco();

    private static long dataExpirationTime = 24 * 60 * 60 * 1000;//un día

    private static Set<String> llamadasPendientes = new java.util.HashSet<String>();
    //Mapa de (Objetos token) / (Lista de Listeners) con las llamadas al WS
    //Este objeto se sincrononiza mientras el objeto retrieverBlock de cada Listener que se recorre en la lista, hace consultas
    private static Map<Object, List<ListenerContainer<? extends LicitaCyLDTO>>> esperandoRefresco = new java.util.HashMap<Object, List<ListenerContainer<? extends LicitaCyLDTO>>>();

    /**
     * Agrega el actual callId a la lista de llamadas pendientes.
     *
     * @param callId Id de la llamada a añadir.
     * @return void
     */
    protected static void startCallId(String callId) {
        synchronized(LicitaCyLHandler.class) {
            llamadasPendientes.add(callId);
        }
    }

    /**
     * Comprueba si la llamada se ha realizado ya o no.
     *
     * @param callId
     * @return
     */
    protected static boolean checkCall(String callId) {
        synchronized (LicitaCyLHandler.class) {
            return llamadasPendientes.contains(callId);
        }
    }

    /**
     * Elimina el actual callId de la lista de llamadas pendientes
     *
     * @param callId
     * @return void
     */
    public static void cancelCall(String callId) {
        synchronized(LicitaCyLHandler.class) { //trozo de código sicnronizado para ser accedido por un solo Thread al tiempo.
            llamadasPendientes.remove(callId);
        }
    }

    /**
     * Metodo que es llamado desde la actividad BuscadorActivity al ser creada.
     * Comienza la comprobación de la necesidad de actualización de datos.
     *
     * @param textDesc
     * @param clasePrestacion
     * @param tematica
     * @param tipoLici
     * @param fechaPrimeraPub
     */
    public static void consultaLicitacionesGeneral(final String callId, String textDesc, String clasePrestacion,
                                                  String tematica, String tipoLici, String fechaPrimeraPub,
                                                   final SplashActivity listener){
        //Se añade la llamadaa actual a la lista "llamadasPendientes"
        startCallId(callId);

        CollectionDataRetrieverBlock<LicitaCyLDTO> retrieverBlock =  new RetrieverBlock();
        CollectionDataSaverBlock<LicitaCyLDTO> saverBlock = new SaverBlock();

        executeCollectionRetrieval(callId, textDesc, clasePrestacion, tematica, tipoLici, fechaPrimeraPub, retrieverBlock, saverBlock, listener);
    }

    /**
     * Retrieves information executing background refresh if required.
     *
     * @param callId
     * @param textDesc
     * @param clasePrestacion
     * @param tematica
     * @param tipoLici
     * @param fechaPrimeraPub
     * @param retrieverBlock
     * @param saverBlock
     */
    public static <T extends LicitaCyLDTO> void executeCollectionRetrieval(String callId,String textDesc, String clasePrestacion, String tematica, String tipoLici, String fechaPrimeraPub,
                                                  CollectionDataRetrieverBlock<LicitaCyLDTO>retrieverBlock,
                                                  CollectionDataSaverBlock<LicitaCyLDTO>saverBlock,
                                                  LicitaCyLHandlerListener<T> listener) {
        boolean requiereRefresco = false;
        Collection<T> data;
        //En CylDigital se creaba un objeto de clase anónima pq se implementaban de manera dsistinta los métodos remoteRetrieve y localRetrieve
        data = (Collection<T>)retrieverBlock.localRetrieve(textDesc, clasePrestacion, tematica, tipoLici, fechaPrimeraPub);

        requiereRefresco = seRequiereRefresco(data, dataExpirationTime, DBTableLicitaciones.NAME);

        try {
            synchronized (LicitaCyLHandler.class) {
                if (checkCall(callId)) {
                    listener.onResults(callId, data, requiereRefresco);
                }
            }
        } catch (Exception e) {
            synchronized (LicitaCyLHandler.class) {
                if (checkCall(callId)) {
                    listener.onError(callId, e, requiereRefresco);
                }
            }
        }

        if (requiereRefresco) {
            try{
                //TODO: completar método que cree objeto Refresh (tipo AsyncTask) y llame a execute()
                generalRefreshCollectionData(callId, retrieverBlock, saverBlock, (LicitaCyLHandlerListener<LicitaCyLDTO>)listener);

            } catch (Exception e) {
                System.out.println("Excepción refrescando licitaciones");
                e.printStackTrace();
            }
        }
    }


    /**
     * Refreshes information from remote endpoint and calls the block after refreshing
     *
     * @param callId
     * @param retrieverBlock
     * @param saverBlock
     * @param listener
     * @param <T>
     */
    protected static <T extends LicitaCyLDTO> void generalRefreshCollectionData(String callId,
                                                                                CollectionDataRetrieverBlock<T> retrieverBlock,
                                                                                final CollectionDataSaverBlock<T> saverBlock,
                                                                                LicitaCyLHandlerListener<T> listener) {

        final Object refreshToken = retrieverBlock.getRemoteRetrieveToken();
        //final CollectionDataRetrieverBlock<T> commonRemoteRetrieverBlock = retrieverBlock;


        synchronized (esperandoRefresco) {

            List<ListenerContainer<? extends LicitaCyLDTO>> listeners = esperandoRefresco.get(refreshToken);

            if (listeners == null) {
                listeners = new java.util.LinkedList<ListenerContainer<? extends LicitaCyLDTO>>();
                esperandoRefresco.put(refreshToken, listeners);
            } else {
                System.out.println("Se ha detectado otra instancia esperando para refresco remoto: \"" + refreshToken + "\"");
                // Replace the listener with this one
                // Delete repeated callIds
                ListenerContainer<? extends LicitaCyLDTO> toDelete = null;
                for (ListenerContainer<? extends LicitaCyLDTO> l : listeners) {
                    if (l.callId.equals(callId)) {
                        toDelete = l;
                        break;
                    }
                }
                if (toDelete != null) {
                    listeners.remove(toDelete);
                }
            }
            ListenerContainer<T> lc = new ListenerContainer<T>();
            lc.listener = listener;
            lc.callId = callId;
            lc.retrieverBlock = retrieverBlock;
            listeners.add(lc);
            if (listeners.size() > 1) { // If there is already a refresh under progress just exit waiting the refresh
                return;
            }
        }
        refresco.execute();
        //execute
    }















    //TRAS LA ACTUALIZACIÓN

    /**
     *
     * @param textDesc
     * @param clasePrestacion
     * @param tematica
     * @param tipoLici
     * @param fechaPrimeraPub
     * @return
     */
    public static Collection<LicitaCyLDTO> listaLicitacionesGeneral(String textDesc, String clasePrestacion, String tematica,
                                                String tipoLici, String fechaPrimeraPub) {
        return new RetrieverBlock().localRetrieve(textDesc, clasePrestacion, tematica, tipoLici, fechaPrimeraPub);
    }

    /**
     *
     * @return
     */
    public Collection<LicitaCyLDTO> getLicitacionesWebservice(){
        try {
            return new LicitaCyLWS().getLicitaciones();
        } catch (LicitaCyLWSException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método utilizado por el atributo de clase de tipo CollectionDataSaverBlock para
     * a través de objeto DBDataSource de App, eliminar las licitaciones existentes
     * en local y sustituirlas por las recuperadas de servicio web.
     *
     * @param licitaciones
     */
    public static void guardaLicitaciones(Collection<LicitaCyLDTO> licitaciones) {
        App.getDataSource().sustituyeActividades(licitaciones);
    }

    /**
     * Comprueba el último refresco/actualización de cualquier tabla que se le pase.
     *
     * @param dtos
     * @param expirationTime declarada en
     * @param nombreTablaPrincipal Nombre de la tabla a consultar su fecha de último refresco.
     * @return true si hay necesidad de refresco de datos.
     */
    protected static boolean seRequiereRefresco(Collection<? extends LicitaCyLDTO> dtos, long expirationTime, String nombreTablaPrincipal) {
        long lastRefresh = Long.MAX_VALUE;

        long now = System.currentTimeMillis(); //Returns the current time in milliseconds.

        if (dtos != null || dtos.size() > 0) {
            //Consulta el registro en  TAB_REFRESHINFO de TAB_LICI cual fue su última actualización (REFRESHDATE).
            lastRefresh = App.getDataSource().getLastRefreshFor(nombreTablaPrincipal);
        }

        if ((now - lastRefresh) > (expirationTime)) {
            return true;
        }
        return false;
    }


    /*****************************************************************************/
    /*****************************CLASES PRIVADAS*********************************/
    /*****************************************************************************/


    /**
     * Clase con 3 atributos
     * @param <T>
     */
    private static class ListenerContainer<T extends LicitaCyLDTO> {
        public LicitaCyLHandlerListener<T> listener;
        public String callId;
        public CollectionDataRetrieverBlock<T> retrieverBlock;
    }



}
