package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/** Lógica de OpenGL: texturas */
public class Texturas {

    public static FloatBuffer recorte;

    public static int BOLA = 0;
    public static int TIJOLO1 = 1;
    public static int TIJOLO2 = 2;
    public static int TIJOLO3 = 3;
    public static int TIJOLO4 = 4;
    public static int PAD = 5;

    private static int[] nomes = new int[6];

    public static void carregar(Context contexto) {
        float[] uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        recorte = bb.asFloatBuffer();
        recorte.put(uvs);
        recorte.position(0);

        GLES20.glGenTextures(nomes.length, nomes, 0);

        criarTextura(contexto, 0, R.drawable.bola, GLES20.GL_TEXTURE0);
        criarTextura(contexto, 1, R.drawable.tijolo1, GLES20.GL_TEXTURE1);
        criarTextura(contexto, 2, R.drawable.tijolo2, GLES20.GL_TEXTURE2);
        criarTextura(contexto, 3, R.drawable.tijolo3, GLES20.GL_TEXTURE3);
        criarTextura(contexto, 4, R.drawable.tijolo4, GLES20.GL_TEXTURE4);
        criarTextura(contexto, 5, R.drawable.pad, GLES20.GL_TEXTURE5);
    }

    private static void criarTextura(Context contexto, int indice, int id, int identificador) {
        Bitmap bitmap = BitmapFactory.decodeResource(contexto.getResources(), id);

        GLES20.glActiveTexture(identificador);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, nomes[indice]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

}
