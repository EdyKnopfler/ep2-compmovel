package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TelaJogo implements GLSurfaceView.Renderer {

    private static final float LARG_BLOCO = 0.2f;
    private static final float ALT_BLOCO = 0.1f;

    private Context contexto;
    private Partida partida;
    private ArrayList<Sprite> blocos;
    private ArrayList<Sprite> parede;
    private Sprite bola, pad;
    private float[] camera = new float[16];
    private float[] projecao = new float[16];
    private float largura, altura;

    public TelaJogo(Context contexto) {
        this.contexto = contexto;
        this.partida = new Partida();
        posicoesIniciais();
        criarParede();
        criarNovaFase();
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
        this.largura = largura;
        this.altura = altura;
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

        for (Sprite b : parede)
            b.desenhar(ajuste);

        bola.desenhar(ajuste);
        pad.desenhar(ajuste);

        if (partida.finalizada) return;

        if (!partida.rolando) {
            posicoesIniciais();
            partida.iniciar();
        }

        partida.processar();
    }

    private void criarParede() {
        parede = new ArrayList<>();

        // Esquerda e direita
        int i = 1;
        for (float y = -1.2f; y <= 1.2f; y += ALT_BLOCO) {
            parede.add(new Sprite(i, -0.8f, y, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO4));
            i++;
            parede.add(new Sprite(i, 0.6f, y, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO4));
            i++;
        }

        // Em cima
        for (float x = -0.8f; x <= 0.6f; x += LARG_BLOCO) {
            parede.add(new Sprite(i, x, 1.0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO4));
            i++;
        }

        partida.setParede(parede);
    }

    private void posicoesIniciais() {
        bola = new Sprite(-1, -0.05f, -0.3f, 0.1f, 0.1f, Texturas.BOLA);
        pad = new Sprite(-2, -0.15f, -0.8f, 0.3f, 0.05f, Texturas.PAD);
        partida.setBola(bola);
        partida.setPad(pad);
    }

    private void criarNovaFase() {
        blocos = new ArrayList<>();
        blocos.add(new Sprite(1, -0.2f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO1));
        blocos.add(new Sprite(2, -0.4f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO2));
        blocos.add(new Sprite(3, -0.6f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO3));
        blocos.add(new Sprite(4, 0f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO3));
        blocos.add(new Sprite(5, 0.2f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO2));
        blocos.add(new Sprite(6, 0.4f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO1));
        partida.setBlocos(blocos);
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("x", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public Partida getPartida() {
        return partida;
    }

    public float getLargura() {
        return largura;
    }

    public float getAltura() {
        return altura;
    }
}
