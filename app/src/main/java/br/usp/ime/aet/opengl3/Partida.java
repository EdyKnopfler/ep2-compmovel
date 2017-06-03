package br.usp.ime.aet.opengl3;

import android.util.Log;
import java.util.ArrayList;
import static br.usp.ime.aet.opengl3.Colisao.*;

/** Lógica de jogo */
public class Partida {

    public static int VIDAS_EXTRAS = 3;

    private static float POS_BOLA_X = -0.05f, POS_BOLA_Y = -0.3f;
    private static float TAM_BOLA = 0.1f;
    private static float POS_PAD_X = -0.15f, POS_PAD_Y = -0.8f;
    private static float LARG_PAD = 0.3f, ALT_PAD = 0.05f;

    private static float VEL_BOLA = 0.7f, VEL_PAD = 0.5f;
    //private static float VEL_BOLA = 0.05f, VEL_PAD = 0.03f;

    public boolean finalizada = true;
    public boolean rolando = false;
    public int vidas = 0;

    public ArrayList<Bloco> blocos;
    public ArrayList<Sprite> parede;
    public ArrayList<Bloco> blocosExcluir;
    public Sprite bola, pad;

    private double[] probabilidadesTijolos;
    private float velBolaX = 0f, velBolaY = 0f, velPadX = 0f;
    private double tempoAnterior;

    public Partida() {
        bola = new Sprite(POS_BOLA_X, POS_BOLA_Y, TAM_BOLA, TAM_BOLA, Texturas.BOLA);
        pad = new Sprite(POS_PAD_X, POS_PAD_Y, LARG_PAD, ALT_PAD, Texturas.PAD);
        blocos = new ArrayList<>();
        parede = new ArrayList<>();
        blocosExcluir = new ArrayList<>();
    }

    public void iniciar() {
        finalizada = false;
        rolando = true;
        vidas = VIDAS_EXTRAS;
        probabilidadesTijolos = new double[] {0.2, 0.1, 0.05};
        novaFase();
    }

    public void pausar() {
        rolando = false;
    }

    public void retomar() {
        tempoAnterior = System.currentTimeMillis();
        rolando = true;
    }

    public void processar() {
        if (finalizada || !rolando) return;

        // Calcular deslocamentos pelo tempo decorrido entre um frame e outro
        double agora = System.currentTimeMillis();
        double decorrido = agora - tempoAnterior;
        tempoAnterior = agora;

        bola.x += velBolaX * decorrido/1000.0;
        bola.y += velBolaY * decorrido/1000.0;
        //bola.x += velBolaX;
        //bola.y += velBolaY;

        float novaPos = pad.x + velPadX * (float) decorrido/1000.0f;
        if (novaPos < -0.6f)  // paredes!
            novaPos = -0.6f;
        else if (novaPos > 0.6f - pad.largura)
            novaPos = 0.6f - pad.largura;
        pad.x = novaPos;

        if (bola.y < -0.9f) {
            Sons.queda();
            vidas--;

            if (vidas < 0) {
                finalizada = true;
                rolando = false;
                Mensagens.mostrar("", "GAME OVER");
                return;
            }
            else
                posicoesIniciais();
        }

        tratarColisoes();
        processarExclusoes();
    }

    public void tocouTela(float x) {
        if (finalizada)
            iniciar();
        else if (!rolando)
            rolando = true;

        moveuDedo(x);
    }

    public void moveuDedo(float x) {
        if (x > pad.x + pad.largura/2f)
            velPadX = VEL_PAD;
        else if (x < pad.x + pad.largura/2f)
            velPadX = -VEL_PAD;
        else
            velPadX = 0f;
    }

    public void soltouDedo() {
        velPadX = 0f;
    }

    private void novaFase() {
        pausar();
        posicoesIniciais();
        blocos.clear();

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 10; j++) {
                double sorteio = Math.random();
                float x = -0.6f + i*0.2f;
                float y = 0f + j*0.1f;

                Log.d("X", i + ", " + j + " ==> " + x + "," + y);

                if (sorteio < probabilidadesTijolos[2])
                    blocos.add(new Bloco(x, y, -1, Texturas.TIJOLO4));
                else if (sorteio < probabilidadesTijolos[1])
                    blocos.add(new Bloco(x, y, 3, Texturas.TIJOLO3));
                else if (sorteio < probabilidadesTijolos[0])
                    blocos.add(new Bloco(x, y, 2, Texturas.TIJOLO2));
                else
                    blocos.add(new Bloco(x, y, 1, Texturas.TIJOLO1));
            }
    }

    private void posicoesIniciais() {
        bola.x = POS_BOLA_X;
        bola.y = POS_BOLA_Y;
        pad.x = POS_PAD_X;
        pad.y = POS_PAD_Y;
        velBolaX = 0f;
        velBolaY = -VEL_BOLA;
    }

    private void tratarColisoes() {
        Colisao colisao = Colisao.entre(bola, pad);

        if (colisao.getPosicao() != SEM_COLISAO) {
            colisaoComPad(colisao);
            return;
        }

        Colisao maiorArea = new Colisao();  // área zerada
        Bloco blocoAtingido = null;

        for (Bloco bloco : blocos) {
            colisao = Colisao.entre(bola, bloco);
            if (colisao.getPosicao() != SEM_COLISAO &&
                colisao.getAreaInterseao() > maiorArea.getAreaInterseao()) {
                maiorArea = colisao;
                blocoAtingido = bloco;
            }
        }

        for (Sprite tijolo : parede) {
            colisao = Colisao.entre(bola, tijolo);
            if (colisao.getPosicao() != SEM_COLISAO &&
                colisao.getAreaInterseao() > maiorArea.getAreaInterseao())
                maiorArea = colisao;
        }

        if (maiorArea.getPosicao() != SEM_COLISAO)
            colisaoComTijolo(maiorArea);

        if (blocoAtingido != null) {
            blocoAtingido.tratarColisao();
            if (blocoAtingido.morreu())
                blocosExcluir.add(blocoAtingido);
        }
    }

    private void colisaoComPad(Colisao colisao) {
        Sons.pad();

        if (velBolaX == 0f)  // O x começa zerado
            velBolaX = VEL_BOLA;

        switch (colisao.getPosicao()) {
            case ACIMA:
                if (bola.x < pad.x + pad.largura/2f - bola.largura/2f)
                    velBolaX = -Math.abs(velBolaX);
                else
                    velBolaX = Math.abs(velBolaX);
                velBolaY = Math.abs(velBolaY);
                break;
            case ESQUERDA:
                velBolaX = -Math.abs(velBolaX);
                break;
            case DIREITA:
                velBolaX = Math.abs(velBolaX);
                break;
        }
    }

    private void colisaoComTijolo(Colisao colisao) {
        Sons.quebra();

        switch (colisao.getPosicao()) {
            case ACIMA:
                velBolaY = Math.abs(velBolaY);
                break;
            case ABAIXO:
                velBolaY = -Math.abs(velBolaY);
                break;
            case ESQUERDA:
                velBolaX = -Math.abs(velBolaX);
                break;
            case DIREITA:
                velBolaX = Math.abs(velBolaX);
                break;
        }
    }

    private void processarExclusoes() {
        for (Sprite bloco : blocosExcluir)
            blocos.remove(bloco);

        blocosExcluir.clear();

        if (blocos.isEmpty()) {  // Passou de fase :)
            recalcularDificuldade();
            novaFase();
        }
    }

    private void recalcularDificuldade() {
        if (probabilidadesTijolos[0] > 0.4) return;  // limite de dificuldade
        for (int i = probabilidadesTijolos.length - 1; i >= 1; i--)
            probabilidadesTijolos[i] = probabilidadesTijolos[i-1];
        probabilidadesTijolos[0] += 0.1;
    }

}
