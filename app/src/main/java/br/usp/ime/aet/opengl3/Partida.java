package br.usp.ime.aet.opengl3;

import java.util.ArrayList;
import static br.usp.ime.aet.opengl3.Colisao.*;

/** Lógica de jogo */
public class Partida {

    public static int VIDAS_EXTRAS = 3;

    private static float POS_BOLA_X = -0.05f, POS_BOLA_Y = -0.3f;
    private static float TAM_BOLA = 0.1f;
    private static float POS_PAD_X = -0.15f, POS_PAD_Y = -0.8f;
    private static float LARG_PAD = 0.3f, ALT_PAD = 0.05f;

    private static float VEL_BOLA = 0.6f, VEL_PAD = 0.4f;
    //private static float VEL_BOLA = 0.05f, VEL_PAD = 0.03f;

    public boolean finalizada = true;
    public boolean rolando = false;
    public int vidas = 0;

    public ArrayList<Bloco> blocos;
    public ArrayList<Sprite> parede;
    public ArrayList<Bloco> blocosExcluir;
    public Sprite bola, pad;

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
        posicoesIniciais();
        criarNovaFase();
        tempoAnterior = System.currentTimeMillis();
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
            vidas--;

            if (vidas < 0) {
                finalizada = true;
                rolando = false;
                Mensagens.mostrar("GAME OVER", "TESTE");
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

    private void posicoesIniciais() {
        bola.x = POS_BOLA_X;
        bola.y = POS_BOLA_Y;
        pad.x = POS_PAD_X;
        pad.y = POS_PAD_Y;
        velBolaX = 0f;
        velBolaY = -VEL_BOLA;
    }

    private void criarNovaFase() {
        // TODO: lógica de criação aleatória de nível
        blocos = new ArrayList<>();
        blocos.add(new Bloco(-0.2f, 0f, 3, Texturas.TIJOLO3));
        blocos.add(new Bloco(-0.4f, 0f, 2, Texturas.TIJOLO2));
        blocos.add(new Bloco(-0.6f, 0f, 1, Texturas.TIJOLO1));
        blocos.add(new Bloco(0f, 0f, 1, Texturas.TIJOLO1));
        blocos.add(new Bloco(0.2f, 0f, 2, Texturas.TIJOLO2));
        blocos.add(new Bloco(0.4f, 0f, 3, Texturas.TIJOLO3));
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
    }

}
