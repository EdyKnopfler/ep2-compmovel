package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/** Captura de toques */
public class ToqueTela extends GLSurfaceView {

    private TelaJogo tela;

    public ToqueTela(Context context) {
        super(context);
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        tela = (TelaJogo) renderer;
    }

    //Aqui ele soh pega onde foi clicado na tela
    //Passa para a partida fazer o processamento
    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        float x = conversaoX(evento.getX(), tela.getLargura());

        switch (evento.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                tela.getPartida().tocouTela(x);
                break;
            case MotionEvent.ACTION_MOVE:
                tela.getPartida().moveuDedo(x);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                tela.getPartida().soltouDedo();
                break;
        }

        return true;
    }

    private float conversaoX(float coord, float largura) {
        return coord / (largura/2) - 1f;
    }

}
