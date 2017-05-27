package br.usp.ime.aet.opengl3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        recorte = bb.asFloatBuffer();
        recorte.put(uvs);
        recorte.position(0);

        // Generate Textures, if more needed, alter these numbers.
        GLES20.glGenTextures(nomes.length, nomes, 0);
        TelaJogo.checkGlError("glGenTextures");

        criarTextura(contexto, 0, R.drawable.bola, GLES20.GL_TEXTURE0);
        criarTextura(contexto, 1, R.drawable.tijolo1, GLES20.GL_TEXTURE1);
        criarTextura(contexto, 2, R.drawable.tijolo2, GLES20.GL_TEXTURE2);
        criarTextura(contexto, 3, R.drawable.tijolo3, GLES20.GL_TEXTURE3);
        criarTextura(contexto, 4, R.drawable.tijolo4, GLES20.GL_TEXTURE4);
        criarTextura(contexto, 5, R.drawable.pad, GLES20.GL_TEXTURE5);
    }

    private static void criarTextura(Context contexto, int indice, int id, int identificador) {
        // Temporary create a bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(contexto.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(identificador);
        TelaJogo.checkGlError("glActiveTexture");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, nomes[indice]);
        TelaJogo.checkGlError("glBindTexture");

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        TelaJogo.checkGlError("glTexParameteri");
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        TelaJogo.checkGlError("glTexParameteri");

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        TelaJogo.checkGlError("texImage2D");

        // We are done using the bitmap so we should recycle it.
        bitmap.recycle();
    }

}
