package br.usp.ime.aet.opengl3;

public class Bloco extends Sprite {

    public static final float LARG_BLOCO = 0.2f;
    public static final float ALT_BLOCO = 0.1f;
    public static final float PAD_RAPIDO = 1;
    public static final float RESET_EFEITO = 2;
    public static final float BOLA_LENTA = 3;

    private int vidas;
    private int efeito;

    public Bloco(float x, float y, int vidas, int efeito, int textura) {
        super(x, y, LARG_BLOCO, ALT_BLOCO, textura);
        this.vidas = vidas;
        this.efeito = efeito;
    }

    public int tratarColisao() {

        vidas--;
        return efeito;

    }

    public boolean morreu() {
        return vidas == 0;
    }

}
