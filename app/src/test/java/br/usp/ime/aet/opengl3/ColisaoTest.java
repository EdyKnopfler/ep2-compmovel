package br.usp.ime.aet.opengl3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static br.usp.ime.aet.opengl3.Colisao.*;

public class ColisaoTest {

    private Sprite bola = new Sprite(0, 0f, 0f, 1.0f, 1.0f, 0);
    private Sprite tijolo = new Sprite(1, 0f, 0f, 2.0f, 2.0f, 0);
    private float[][] coordenadas = new float[][] {
            // Acima, sem tocar
            {-2f, 2f},    // Esquerda, sem tocar
            {-0.5f, 2f},  // Esquerda, tocando
            {0.5f, 2f},   // Meio
            {1.5f, 2f},   // Direita, tocando
            {2.5f, 2f},   // Direita, sem tocar

            // Acima, tocando
            {-2f, 0.5f},
            {-0.5f, 0.5f},
            {0.5f, 0.5f},
            {1.5f, 0.5f},
            {2.5f, 0.5f},

            // Meio
            {-2f, -0.5f},
            {-0.5f, -0.5f},
            {0.5f, -0.5f},
            {1.5f, -0.5f},
            {2.5f, -0.5f},

            // Abaixo, tocando
            {-2f, -1.5f},
            {-0.5f, -1.5f},
            {0.5f, -1.5f},
            {1.5f, -1.5f},
            {2.5f, -1.5f},

            // Abaixo, sem tocar
            {-2f, -2.5f},
            {-0.5f, -2.5f},
            {0.5f, -2.5f},
            {1.5f, -2.5f},
            {2.5f, -2.5f},
    };
    private int[][] colisoes = new int[][] {
            {SEM_COLISAO, SEM_COLISAO},
            {ESQUERDA, SEM_COLISAO},
            {MEIO, SEM_COLISAO},
            {DIREITA, SEM_COLISAO},
            {SEM_COLISAO, SEM_COLISAO},
            {SEM_COLISAO, ACIMA},
            {ESQUERDA, ACIMA},
            {MEIO, ACIMA},
            {DIREITA, ACIMA},
            {SEM_COLISAO, ACIMA},
            {SEM_COLISAO, MEIO},
            {ESQUERDA, MEIO},
            {MEIO, MEIO},
            {DIREITA, MEIO},
            {SEM_COLISAO, MEIO},
            {SEM_COLISAO, ABAIXO},
            {ESQUERDA, ABAIXO},
            {MEIO, ABAIXO},
            {DIREITA, ABAIXO},
            {SEM_COLISAO, ABAIXO},
            {SEM_COLISAO, SEM_COLISAO},
            {ESQUERDA, SEM_COLISAO},
            {MEIO, SEM_COLISAO},
            {DIREITA, SEM_COLISAO},
            {SEM_COLISAO, SEM_COLISAO},
    };

    @Test
    public void casosDeColisao() {
        for (int i = 0; i < coordenadas.length; i++) {
            bola.x = coordenadas[i][0];
            bola.y = coordenadas[i][1];
            Colisao c = Colisao.entre(bola, tijolo);
            assertEquals(c.emX, colisoes[i][0]);
            assertEquals(c.emY, colisoes[i][1]);
        }
    }

}