package es.jcyl.barquejo.app.licitacyl.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import es.jcyl.barquejo.app.licitacyl.MainActivity;
import es.jcyl.barquejo.app.licitacyl.R;
import es.jcyl.barquejo.app.licitacyl.backend.handler.LicitaCyLHandler;
import es.jcyl.barquejo.app.licitacyl.backend.handler.LicitaCyLHandlerListener;

public class SplashActivity extends MainActivity implements LicitaCyLHandlerListener{

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    private String currentCallId = "";
    long currentCallTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// Set portrait orientation
        setContentView(R.layout.activity_splash);


        //ACTUALIZACIÓN DE DATOS
        //Comenzamos con la llamada a la posible actualización de datos en bbdd a través del WS
        if (currentCallId != null) {
            LicitaCyLHandler.cancelCall(currentCallId);
        }
        currentCallTime = new Date().getTime();
        currentCallId = "licitaciones" + currentCallTime;
        LicitaCyLHandler.consultaLicitacionesGeneral(currentCallId, "", "", "", "", "", SplashActivity.this);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity. En nuestro caso arrancamos desde aquí la MenuFormacionActivity
                Intent mainIntent = new Intent().setClass(SplashActivity.this, BuscadorActivity.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    public void onResults(String callId, Collection res, final boolean expectRefresh){

    }

    @Override
    public void onError(String callId, Exception e, boolean expectRefresh) {
        e.printStackTrace();
    }

}
