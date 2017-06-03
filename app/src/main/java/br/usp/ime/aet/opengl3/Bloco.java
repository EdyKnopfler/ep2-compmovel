package br.usp.ime.aet.opengl3;

public class Bloco extends Sprite {

    public static final float LARG_BLOCO = 0.2f;
    public static final float ALT_BLOCO = 0.1f;

    private int vidas;

    public Bloco(float x, float y, int vidas, int textura) {
        super(x, y, LARG_BLOCO, ALT_BLOCO, textura);
        this.vidas = vidas;
    }

    public void tratarColisao() {
        vidas--;
    }

    public boolean morreu() {
        return vidas == 0;
    }

}
