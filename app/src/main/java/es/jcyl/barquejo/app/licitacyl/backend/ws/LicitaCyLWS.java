package es.jcyl.barquejo.app.licitacyl.backend.ws;


import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;

import es.jcyl.barquejo.app.licitacyl.backend.Configuration;
import es.jcyl.barquejo.app.licitacyl.backend.handler.CollectionDataRetrieverBlock;
import es.jcyl.barquejo.app.licitacyl.backend.handler.CollectionDataSaverBlock;
import es.jcyl.barquejo.app.licitacyl.backend.handler.RetrieverBlock;
import es.jcyl.barquejo.app.licitacyl.backend.handler.SaverBlock;
import es.jcyl.barquejo.app.licitacyl.parser.LicitaCyLParser;
import es.jcyl.barquejo.app.licitacyl.parser.ParserException;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 21/09/2016.
 */
public class LicitaCyLWS extends WService {
    //Tiempo de conexión
    private static int connectTimeOut = -1;
    //Tiempo de lectura de datos
    private static int readTimeOut = -1;

    static Collection<LicitaCyLDTO> licitaciones;

    private static HttpURLConnection cnt = null;

    private static InputStream is = null;

    private static  String sUrl = Configuration.getProperty(Configuration.KEY_LICI_WSURL);

    //Iniciamos los valores
    static {
        String sValue = Configuration.getProperty(Configuration.KEY_LICI_CONNECTTIMEOUT);
        connectTimeOut = Integer.parseInt(sValue) * 1000;
        sValue = Configuration.getProperty(Configuration.KEY_LICI_READTIMEOUT);
        readTimeOut = Integer.parseInt(sValue) * 1000;
    }

    public Collection<LicitaCyLDTO> getLicitaciones() throws LicitaCyLWSException {

        if (!isNetworkAccessible()) {
            throw new LicitaCyLWSException("Network is unreachable");
        }

        try {
           return enviarRequest();
            //new Refresh().execute(sUrl, Integer.toString(connectTimeOut), Integer.toString(readTimeOut));
            //return licitaciones;
        } catch (Exception pe) {
            System.out.println("Unable to read education courses");
            pe.printStackTrace();
            throw new LicitaCyLWSException("Unable to read education courses");
        }
    }

    protected static Collection<LicitaCyLDTO> enviarRequest() {
        URL url = null;
        Collection<LicitaCyLDTO> licitaciones = null;
        try {
            url = new URL(sUrl);
            cnt = (HttpURLConnection) url.openConnection();
            cnt.setRequestMethod("GET");
            //cnt.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:26.0) Gecko/20100101 Firefox/26.0");
            cnt.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            cnt.setConnectTimeout(connectTimeOut);
            cnt.setReadTimeout(readTimeOut);

            LicitaCyLParser parser = new LicitaCyLParser();
            System.out.println("Licitaciones: Parsing response from " + url);
            is = new BufferedInputStream(cnt.getInputStream());
            licitaciones = parser.parse(is);
        }catch (MalformedURLException mue){

        }catch (IOException ioe){
            System.out.println("Unable to read stream: " + interpretError(cnt));
            ioe.printStackTrace();
            //throw new LicitaCyLWSException("Unable to read stream");
        }catch (ParserException pe){
            System.out.println("Unable to read education courses");
            pe.printStackTrace();
            //throw new LicitaCyLWSException("Unable to read education courses");
        } finally {
            if (is != null) {try { is.close(); } catch (IOException ioe) {}}
            if (cnt != null) { cnt.disconnect(); }
        }
        return licitaciones;
    }




}
