package es.jcyl.barquejo.app.licitacyl.backend;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.jcyl.barquejo.app.licitacyl.R;

/**
 * Created by josecarlos.delbarrio on 21/09/2016.
 */
public class Configuration {

    private static Configuration _instance_ = null;

    public static final String KEY_LICI_CONNECTTIMEOUT = "licitaciones.connectTimeOut";
    public static final String KEY_LICI_READTIMEOUT = "licitaciones.readTimeOut";
    public static final String KEY_LICI_WSURL = "licitaciones.ws.url";
    private Properties props = null;

    private Configuration() {
        props = new Properties();
        InputStream is = App.getContext().getResources().openRawResource(R.raw.config);
        try {
            props.load(is);
        } catch (IOException ioe) {
            System.err.println("Problems loading properties");
            props = null;
        }
    }

    private String innerGetProperty(String prop) {
        if (props != null) {
            return props.getProperty(prop);
        } else {
            return null;
        }
    }

    public static String getProperty(String prop) {
        synchronized (Configuration.class) {
            if (_instance_ == null) {
                _instance_ = new Configuration();
            }
        }
        return _instance_.innerGetProperty(prop);
    }
}
