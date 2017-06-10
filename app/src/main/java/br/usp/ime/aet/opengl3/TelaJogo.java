package br.usp.ime.aet.opengl3;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static br.usp.ime.aet.opengl3.Bloco.ALT_BLOCO;
import static br.usp.ime.aet.opengl3.Bloco.LARG_BLOCO;

/** Lógica de renderização */
public class TelaJogo implements GLSurfaceView.Renderer {

    private Activity contexto;
    private Partida partida;
    private float[] camera = new float[16];
    private float[] projecao = new float[16];
    private float largura, altura;
    private Sprite[] vidas = new Sprite[Partida.VIDAS_EXTRAS];
    private Sprite splash;

    public TelaJogo(Activity contexto) {
        this.contexto = contexto;
        partida = new Partida();
        splash = new Sprite(-0.6f, 0.9f, 1.2f, 0.9f, Texturas.SPLASH);
        criarParede();
        faseAmostra();
        criarSpritesVidas();
    }

    @Override
    public void onSurfaceCreated(GL10 ignorado, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Matrix.setLookAtM(camera, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Programas.inicializar();
        Texturas.carregar(contexto);
        Sons.carregar(contexto);
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

        partida.processar();

        for (Sprite b : partida.blocos)
            b.desenhar(ajuste);

        for (Sprite b : partida.parede)
            b.desenhar(ajuste);

        partida.bola.desenhar(ajuste);
        partida.pad.desenhar(ajuste);

        desenharVidas(ajuste);

        if (partida.finalizada)
            splash.desenhar(ajuste);
    }

    public void pausar() {
        partida.pausar();
    }

    public void retomar() {
        partida.retomar();
    }

    private void criarParede() {
        ArrayList<Sprite> parede = new ArrayList<>();

        // Esquerda e direita
        int i = 1;
        for (float y = -1.2f; y <= 1.2f; y += ALT_BLOCO) {
            parede.add(new Sprite(-0.8f, y, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO4));
            i++;
            parede.add(new Sprite(0.6f, y, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO4));
            i++;
        }

        // Em cima
        for (float x = -0.8f; x <= 0.6f; x += LARG_BLOCO) {
            parede.add(new Sprite(x, 1.0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO4));
            i++;
        }

        partida.parede = parede;
    }

    private void faseAmostra() {
        ArrayList<Bloco> blocos = new ArrayList<>();
        blocos.add(new Bloco(-0.2f, 0f, 1, 0, Texturas.TIJOLO1));
        blocos.add(new Bloco(-0.4f, 0f, 1, 0, Texturas.TIJOLO2));
        blocos.add(new Bloco(-0.6f, 0f, 1, 0, Texturas.TIJOLO3));
        blocos.add(new Bloco(0f, 0f, 1, 0, Texturas.TIJOLO3));
        blocos.add(new Bloco(0.2f, 0f, 1, 0, Texturas.TIJOLO2));
        blocos.add(new Bloco(0.4f, 0f, 1, 0, Texturas.TIJOLO1));
        partida.blocos = blocos;
    }

    //Calcula as bolinhas que ainda restam
    private void criarSpritesVidas() {
        for (int i = 0; i < Partida.VIDAS_EXTRAS; i++)
            vidas[i] = new Sprite(-0.55f + i*0.1f, -0.9f, 0.05f, 0.05f, Texturas.BOLA);
    }

    private void desenharVidas(float[] ajuste) {
        for (int i = 0; i < partida.vidas; i++)
            vidas[i].desenhar(ajuste);
    }

    /*
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("x", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    */

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
