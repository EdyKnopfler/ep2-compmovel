package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.media.MediaPlayer;

public class Sons {

    private static MediaPlayer pad, quebra, queda;

    public static void carregar(Context contexto) {
        pad = MediaPlayer.create(contexto, R.raw.pad);
        quebra = MediaPlayer.create(contexto, R.raw.quebra);
        queda = MediaPlayer.create(contexto, R.raw.queda);
    }

    public static void pad() {
        pad.start();
    }

    public static void quebra() {
        quebra.start();
    }

    public static void queda() {
        queda.start();
    }

}
