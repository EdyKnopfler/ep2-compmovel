package br.usp.ime.aet.opengl3;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sprite {

    protected static ShortBuffer ordemDesenho;

    private int id;
    public float x, y, largura, altura;
    private int textura;

    // Prealocamos para melhor desempenho
    private float[] coords = new float[12];
    private ByteBuffer bytes;
    private FloatBuffer coordenadas;

    public Sprite(int id, float x, float y, float largura, float altura, int textura) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.textura = textura;

        if (ordemDesenho == null) {
            short[] ordem = {0, 1, 2, 0, 2, 3};
            ByteBuffer bytes = ByteBuffer.allocateDirect(ordem.length * 2);
            bytes.order(ByteOrder.nativeOrder());
            ordemDesenho = bytes.asShortBuffer();
            ordemDesenho.put(ordem);
            ordemDesenho.position(0);
        }

        bytes = ByteBuffer.allocateDirect(48);  // 12 coordenadas * 4 bytes
        bytes.order(ByteOrder.nativeOrder());
        coordenadas = bytes.asFloatBuffer();
    }

    public void desenhar(float[] ajuste) {
        determinarCoordenadas();

        int programa = Programas.SPRITE;
        GLES20.glUseProgram(programa);

        // get handle to vertex shader's vPosition member
        int hPosicao = GLES20.glGetAttribLocation(programa, "vPosition");
        TelaJogo.checkGlError("glGetAttribLocation");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(hPosicao);
        TelaJogo.checkGlError("glEnableVertexAttribArray");

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(hPosicao, 3, GLES20.GL_FLOAT, false, 0, coordenadas);
        TelaJogo.checkGlError("glVertexAttribPointer");

        // Get handle to texture coordinates location
        int hRecorte = GLES20.glGetAttribLocation(programa, "a_texCoord");
        TelaJogo.checkGlError("glGetAttribLocation");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(hRecorte);
        TelaJogo.checkGlError("glEnableVertexAttribArray");

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(hRecorte, 2, GLES20.GL_FLOAT, false, 0, Texturas.recorte);
        TelaJogo.checkGlError("glVertexAttribPointer");

        // Get handle to shape's transformation matrix
        int hAjuste = GLES20.glGetUniformLocation(programa, "uMVPMatrix");
        TelaJogo.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(hAjuste, 1, false, ajuste, 0);
        TelaJogo.checkGlError("glUniformMatrix4fv");

        // Get handle to textures locations
        int hTextura = GLES20.glGetUniformLocation (programa, "s_texture");
        TelaJogo.checkGlError("glGetUniformLocation");

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(hTextura, textura);
        TelaJogo.checkGlError("glUniform1i");

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, ordemDesenho);
        TelaJogo.checkGlError("glDrawElements");

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(hPosicao);
        TelaJogo.checkGlError("glDisableVertexAttribArray");
        GLES20.glDisableVertexAttribArray(hRecorte);
        TelaJogo.checkGlError("glDisableVertexAttribArray");

    }

    private void determinarCoordenadas() {
        // De 3 em 3 (x, y, z), em sentido anti-horário:

        // Superior esquerdo
        coords[0] = x;
        coords[1] = y;
        coords[2] = 0.0f;

        // Inferior esquerdo
        coords[3] = x;
        coords[4] = y - altura;
        coords[5] = 0.0f;

        // Inferior direito
        coords[6] = x + largura;
        coords[7] = y - altura;
        coords[8] = 0.0f;

        // Superior direito
        coords[9] = x + largura;
        coords[10] = y;
        coords[11] = 0.0f;

        coordenadas.put(coords);
        coordenadas.position(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Sprite && ((Sprite) obj).id == this.id;
    }
}
