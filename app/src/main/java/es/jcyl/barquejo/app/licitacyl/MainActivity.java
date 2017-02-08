package es.jcyl.barquejo.app.licitacyl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

//En sustitución de la ActionBAr o AppBar (tras Android 5.0)
//Para el uso del nuevo componente ToolBar proporcionado por la librería appcompat.
//1.- Nuestro proyecto incluye la última versión de la librería de soporte appcompat-v7, en dependencias de build.gradle
    //Hasta la versión 21 de la librería appcompat-v7 la clase base de la que debían heredar nuestras actividades era ActionBarActivity
//2.- Extender nuestras actividades de AppCompatActivity (versión 22)
//3.-“desactivar” la funcionalidad por defecto configurando un tema para nuestra aplicación que no incluya la action bar.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }
}
