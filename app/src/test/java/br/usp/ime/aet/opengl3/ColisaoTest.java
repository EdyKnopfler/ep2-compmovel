package br.usp.ime.aet.opengl3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static br.usp.ime.aet.opengl3.Colisao.*;

public class ColisaoTest {

    private Sprite bola = new Sprite(0f, 0f, 0.5f, 0.5f, 0);
    private Sprite tijolo = new Sprite(0f, 0f, 2.0f, 2.0f, 0);

    private float[][] coordenadas = new float[][] {
            // em cima, sem tocar
            {-0.1f, 1.0f}, {1.0f, 1.0f}, {1.6f, 1.0f},

            // em cima
            {-0.1f, 0.4f}, {1.0f, 0.4f}, {1.6f, 0.4f},

            // em cima, pegando mais dos lados
            {-0.4f, 0.1f}, {1.9f, 0.1f},

            // dos lados
            {-0.4f, -0.1f}, {1.6f, -0.1f},

            // embaixo, pegando mais dos lados
            {-0.4f, -1.6f}, {1.9f, -1.6f},

            // embaixo
            {-0.1f, -1.9f}, {1.0f, -1.9f}, {1.6f, -1.9f},

            // embaixo, sem tocar
            {-0.1f, -3.0f}, {1.0f, -3.0f}, {1.6f, -3.0f},
    };

    private int[] colisoes = new int[] {
            SEM_COLISAO, SEM_COLISAO, SEM_COLISAO,
            ACIMA, ACIMA, ACIMA,
            ESQUERDA, DIREITA,
            ESQUERDA, DIREITA,
            ESQUERDA, DIREITA,
            ABAIXO, ABAIXO, ABAIXO,
            SEM_COLISAO, SEM_COLISAO, SEM_COLISAO,
    };

    @Test
    public void casosDeColisao() {
        for (int i = 0; i < coordenadas.length; i++) {
            System.out.println(i);
            bola.x = coordenadas[i][0];
            bola.y = coordenadas[i][1];
            Colisao colisao = Colisao.entre(bola, tijolo);
            assertEquals(colisao.getPosicao(), colisoes[i]);
        }
    }

}