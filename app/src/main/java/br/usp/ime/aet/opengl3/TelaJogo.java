package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TelaJogo implements GLSurfaceView.Renderer {

    private Context contexto;
    private ArrayList<Sprite> blocos = new ArrayList<>();
    private Sprite bola;
    private float[] camera = new float[16];
    private float[] projecao = new float[16];

    public TelaJogo(Context contexto) {
        this.contexto = contexto;

        // Carregará conforme a fase...
        blocos = new ArrayList<>();
        blocos.add(new Sprite(0.1f, 0.9f, 0.2f, 0.1f, Texturas.TIJOLO1));
        blocos.add(new Sprite(0.3f, 0.9f, 0.2f, 0.1f, Texturas.TIJOLO1));
        blocos.add(new Sprite(0.1f, 0.8f, 0.2f, 0.1f, Texturas.TIJOLO1));
        blocos.add(new Sprite(-0.6f, 0.6f, 0.2f, 0.1f, Texturas.TIJOLO2));
        blocos.add(new Sprite(-0.3f, 0.3f, 0.2f, 0.1f, Texturas.TIJOLO3));
        bola = new Sprite(0f, 0f, 0.1f, 0.1f, Texturas.BOLA);
    }

    @Override
    public void onSurfaceCreated(GL10 ignorado, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Matrix.setLookAtM(camera, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Programas.inicializar();
        Texturas.carregar(contexto);
    }

    @Override
    public void onSurfaceChanged(GL10 ignorado, int largura, int altura) {
        GLES20.glViewport(0, 0, largura, altura);
        float proporcao = (float) largura / altura;
        Matrix.frustumM(projecao, 0, -proporcao, proporcao, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 ignorado) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        float[] ajuste = new float[16];
        Matrix.multiplyMM(ajuste, 0, projecao, 0, camera, 0);

        for (Sprite b : blocos)
            b.desenhar(ajuste);

        bola.desenhar(ajuste);
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("x", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}
