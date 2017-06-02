package br.usp.ime.aet.opengl3;

import android.app.Activity;
import android.app.AlertDialog;

public class Mensagens {

    private static Activity contexto;

    public static void setContexto(Activity activity) {
        contexto = activity;
    }

    public static void mostrar(final String titulo, final String texto) {
        contexto.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
                alert.setTitle(titulo);
                alert.setMessage(texto);
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        });

    }

}
