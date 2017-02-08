package es.jcyl.barquejo.app.licitacyl.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Utils {
    public static void showSimpleDialog(Context ctx, String title, String text,
                                        DialogInterface.OnClickListener listener) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage(text).setTitle(title);
            builder.setPositiveButton(android.R.string.ok, listener);
            builder.setCancelable(false);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
