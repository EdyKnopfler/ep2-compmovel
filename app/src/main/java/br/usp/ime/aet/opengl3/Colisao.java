package br.usp.ime.aet.opengl3;

public class Colisao {

    public static final int SEM_COLISAO = 0;
    public static final int MEIO = 1;
    public static final int ESQUERDA = 2;
    public static final int DIREITA = 3;
    public static final int ACIMA = 4;
    public static final int ABAIXO = 5;

    public int emX;
    public int emY;

    public Colisao(int emX, int emY) {
        this.emX = emX;
        this.emY = emY;
    }

    /**
     * Detecta uma colisão e devolve os dados.
     * Os atributos referem-se à posição de s1 em relação a s2.
     */
    public static Colisao entre(Sprite s1, Sprite s2) {
        int emX = colisaoEmX(s1, s2);
        int emY = colisaoEmY(s1, s2);
        return new Colisao(emX, emY);
    }

    private static int colisaoEmX(Sprite s1, Sprite s2) {
        if (s1.x + s1.largura < s2.x) return SEM_COLISAO;
        if (s2.x + s2.largura < s1.x) return SEM_COLISAO;

        if (s1.x < s2.x) {
            if (s1.x + s1.largura < s2.x + s2.largura) return ESQUERDA;
            return MEIO;
        }

        if (s2.x + s2.largura < s1.x + s1.largura) return DIREITA;
        return MEIO;
    }

    private static int colisaoEmY(Sprite s1, Sprite s2) {
        if (s1.y - s1.altura > s2.y) return SEM_COLISAO;
        if (s2.y - s2.altura > s1.y) return SEM_COLISAO;

        if (s1.y > s2.y) {
            if (s1.y - s1.altura > s2.y - s2.altura) return ACIMA;
            return MEIO;
        }

        if (s2.y - s2.altura > s1.y - s1.altura) return ABAIXO;
        return MEIO;
    }

}
