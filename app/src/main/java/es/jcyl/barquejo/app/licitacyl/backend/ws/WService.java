package es.jcyl.barquejo.app.licitacyl.backend.ws;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import android.net.NetworkInfo;

import es.jcyl.barquejo.app.licitacyl.backend.App;
import es.jcyl.barquejo.app.licitacyl.parser.dto.LicitaCyLDTO;

/**
 * Created by josecarlos.delbarrio on 21/09/2016.
 */
public abstract class WService {

    protected static boolean isNetworkAccessible() {
        NetworkInfo ni = App.getConnectivityManager().getActiveNetworkInfo();
        return ni != null && ni.isConnected() && ni.isAvailable();
    }

    protected static HttpURLConnection sendRequestTo(String sUrl, int connectTimeOut, int readTimeOut) throws IOException {
        //URL url = null;
        HttpURLConnection cnt = null;

        //url = new URL(sUrl);
        //cnt = (HttpURLConnection) url.openConnection();
        //cnt.setRequestMethod("GET");
        //cnt.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:26.0) Gecko/20100101 Firefox/26.0");
        //cnt.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        //cnt.setConnectTimeout(connectTimeOut);
        //cnt.setReadTimeout(readTimeOut);
        //Collection<LicitaCyLDTO> c = new Refresh().execute(sUrl, Integer.toString(connectTimeOut), Integer.toString(readTimeOut));
        return cnt;
    }

    protected static String interpretError(HttpURLConnection cnt) {
        try {
            InputStream es = cnt.getErrorStream();
            StringBuffer response = new StringBuffer("[" + cnt.getResponseCode() + "]\n\n");
            if (es != null) {
                InputStreamReader isr = new InputStreamReader(es);
                char[] c = new char[256];

                int length = 0;
                while ((length = isr.read(c)) != -1) {
                    response.append(new String(c, 0, length));
                }
            } else {
                response.append(" No description");
            }
            return response.toString();

        } catch (IOException ioe) {
            return "!!Unable to interpret error. IOException";
        }
    }
}
