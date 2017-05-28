package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
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

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        float x = conversaoX(evento.getX(), tela.getLargura());
        float y = conversaoY(evento.getY(), tela.getAltura());

        switch (evento.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                tela.getPartida().tocouTela(x);
                break;
            case MotionEvent.ACTION_MOVE:
                tela.getPartida().moveuDedo(x);
                Log.d("X", x + ", " + y);
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

    private float conversaoY(float coord, float altura) {
        return -(coord / (altura/2) - 1f);
    }

}
