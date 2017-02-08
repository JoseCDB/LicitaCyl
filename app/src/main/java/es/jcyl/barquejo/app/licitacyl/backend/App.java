package es.jcyl.barquejo.app.licitacyl.backend;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import es.jcyl.barquejo.app.licitacyl.backend.db.DBDataSource;


/**
 * Created by josecarlos.delbarrio on 21/09/2016.
 */
public class App extends Application {

    private static Context mContext;
    private static DBDataSource ds = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        ds = new DBDataSource();
        //Se crea la DB
        ds.open();
    }

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static DBDataSource getDataSource() {
        return ds;
    }
    public static Context getContext(){
        return mContext;
    }
}
