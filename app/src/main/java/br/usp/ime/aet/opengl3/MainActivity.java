package br.usp.ime.aet.opengl3;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/** Tela do jogo */
public class MainActivity extends Activity {

    private TelaJogo tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tela = new TelaJogo(this);
        ToqueTela sv = new ToqueTela(this);
        sv.setEGLContextClientVersion(2);
        sv.setRenderer(tela);
        setContentView(sv);
    }

}
