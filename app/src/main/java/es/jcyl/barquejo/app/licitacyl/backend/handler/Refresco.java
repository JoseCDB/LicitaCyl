package es.jcyl.barquejo.app.licitacyl.backend.handler;

import android.os.AsyncTask;

import java.util.Collection;

import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 06/10/2016.
 */
    /*
    AsyncTask<Params, Progress, Result>
    AsyncTask enables proper and easy use of the UI thread.
    This class allows you to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.*/
public class Refresco extends AsyncTask<String, String, Refresco.Response<Collection<LicitaCyLDTO>>> {

    final CollectionDataRetrieverBlock<LicitaCyLDTO> retrieverBlock = new RetrieverBlock();
    final CollectionDataSaverBlock<LicitaCyLDTO> saverBlock = new SaverBlock();
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        // do something  // show some progress of loading images
    }

    @Override
    protected Refresco.Response<Collection<LicitaCyLDTO>> doInBackground(String... params) {
        Refresco.Response<Collection<LicitaCyLDTO>> resp = new Refresco.Response<Collection<LicitaCyLDTO>>();
        try {
            resp.data = retrieverBlock.remoteRetrieve();
            saverBlock.storeData(resp.data);
        } catch (Exception e) {
            System.out.println("Excepción refrescando actividades");
            e.printStackTrace();
            resp.error = e;
        }
        return resp;
    }

    @Override
    protected void onPostExecute(Refresco.Response<Collection<LicitaCyLDTO>> licis) {
        //use the result you returned from you doInBackground method
        System.out.println("Ejecución de Post...");

        if(licis.error == null){

        }
    }

    public static class Response<M> {
        public Exception error;
        public M data;
    }
}//Clase Refresh