package br.usp.ime.aet.opengl3;

import java.util.ArrayList;
import static br.usp.ime.aet.opengl3.Sprite.ALT_BLOCO;
import static br.usp.ime.aet.opengl3.Sprite.LARG_BLOCO;

/** Lógica de jogo */
public class Partida {

    public static int VIDAS_EXTRAS = 3;

    public boolean finalizada = true;
    public boolean rolando = false;
    public int vidas = 0;

    public ArrayList<Sprite> blocos;
    public ArrayList<Sprite> parede;
    public Sprite bola, pad;

    private float velBolaX = 0f, velBolaY = 0f, velPadX = 0f;
    private double tempoAnterior;

    public Partida() {
        bola = new Sprite(-1, -0.05f, -0.3f, 0.1f, 0.1f, Texturas.BOLA);
        pad = new Sprite(-2, -0.15f, -0.8f, 0.3f, 0.05f, Texturas.PAD);
        blocos = new ArrayList<>();
        parede = new ArrayList<>();
    }

    public void iniciar() {
        finalizada = false;
        rolando = true;
        vidas = VIDAS_EXTRAS;
        posicoesIniciais();
        criarNovaFase();
        tempoAnterior = System.currentTimeMillis();
    }

    public void processar() {
        if (finalizada || !rolando) return;

        // Calcular deslocamentos pelo tempo decorrido entre um frame e outro
        double agora = System.currentTimeMillis();
        double decorrido = agora - tempoAnterior;
        tempoAnterior = agora;

        bola.x += velBolaX * decorrido/1000.0;
        bola.y += velBolaY * decorrido/1000.0;

        float novaPos = pad.x + velPadX * (float) decorrido/1000.0f;
        if (novaPos < -0.6f)  // paredes!
            novaPos = -0.6f;
        else if (novaPos > 0.6f - pad.largura)
            novaPos = 0.6f - pad.largura;
        pad.x = novaPos;

        // TODO testar colisões aqui

        if (bola.y < -0.9f) {
            vidas--;

            if (vidas < 0) {
                // TODO mostrar um alerta
                finalizada = true;
                rolando = false;
            }
            else
                posicoesIniciais();
        }
    }

    public void tocouTela(float x) {
        if (finalizada || !rolando)
            iniciar();

        moveuDedo(x);
    }

    public void moveuDedo(float x) {
        if (x > pad.x + pad.largura/2f)
            velPadX = 0.1f;
        else if (x < pad.x + pad.largura/2f)
            velPadX = -0.1f;
        else
            velPadX = 0f;
    }

    public void soltouDedo() {
        velPadX = 0f;
    }

    private void posicoesIniciais() {
        bola.x = -0.05f;
        bola.y = -0.3f;
        pad.x = -0.15f;
        pad.y = -0.8f;
        velBolaX = 0f;
        velBolaY = -0.2f;
    }

    private void criarNovaFase() {
        // TODO: lógica de criação aleatória de nível
        blocos = new ArrayList<>();
        blocos.add(new Sprite(1, -0.2f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO3));
        blocos.add(new Sprite(2, -0.4f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO2));
        blocos.add(new Sprite(3, -0.6f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO1));
        blocos.add(new Sprite(4, 0f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO1));
        blocos.add(new Sprite(5, 0.2f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO2));
        blocos.add(new Sprite(6, 0.4f, 0f, LARG_BLOCO, ALT_BLOCO, Texturas.TIJOLO3));
    }

}
