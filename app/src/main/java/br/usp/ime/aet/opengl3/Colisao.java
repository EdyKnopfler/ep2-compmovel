package br.usp.ime.aet.opengl3;

public class Colisao {

    public static final int
            SEM_COLISAO = 1,
            ESQUERDA = 2,
            DIREITA = 3,
            ACIMA = 4,
            ABAIXO = 5;

    private float intersecaoX = 0f, intersecaoY = 0f;
    private int posicao = SEM_COLISAO;

    public int getPosicao() {
        return posicao;
    }

    public float getAreaInterseao() {
        return intersecaoX * intersecaoY;
    }

    /**
     * Detecta uma colisão e devolve o indicativo da posição de s1
     * em relação a s2.
     */
    public static Colisao entre(Sprite s1, Sprite s2) {
        Colisao colisao = new Colisao();

        if (s1.x + s1.largura < s2.x || s2.x + s2.largura < s1.x ||
            s1.y - s1.altura > s2.y || s2.y - s2.altura > s1.y) {
            colisao.posicao = SEM_COLISAO;
            return colisao;
        }

        float intersecaoX, intersecaoY;

        if (s1.x < s2.x) {
            if (s1.x + s1.largura < s2.x + s2.largura)
                intersecaoX = s1.x + s1.largura - s2.x;
            else
                intersecaoX = s2.largura;
        }
        else {
            if (s2.x + s2.largura < s1.x + s1.largura)
                intersecaoX = s2.x + s2.largura - s1.x;
            else
                intersecaoX = s1.largura;
        }

        if (s1.y < s2.y) {
            if (s1.y - s1.altura < s2.y - s2.altura)
                intersecaoY = s1.y - s2.y + s2.altura;
            else
                intersecaoY = s1.altura;
        }
        else {
            if (s2.y - s2.altura < s1.y - s1.altura)
                intersecaoY = s2.y - s1.y + s1.altura;
            else
                intersecaoY = s2.altura;
        }

        if (intersecaoY < intersecaoX) {
            if (s1.y < s2.y) colisao.posicao = ABAIXO;
            else             colisao.posicao = ACIMA;
        }
        else {
            if (s1.x < s2.x) colisao.posicao = ESQUERDA;
            else             colisao.posicao = DIREITA;
        }

        colisao.intersecaoX = intersecaoX;
        colisao.intersecaoY = intersecaoY;

        return colisao;
    }

}
