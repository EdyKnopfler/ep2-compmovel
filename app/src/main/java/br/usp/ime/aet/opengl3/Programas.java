package br.usp.ime.aet.opengl3;

import android.opengl.GLES20;

public class Programas {

    public static int SPRITE;

    private static final String codigoVertice =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "  v_texCoord = a_texCoord;" +
            "}";

    private static final String codigoPreenchimento =
            "precision mediump float;" +
            "varying vec2 v_texCoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
            "}";

    public static void inicializar() {
        int vertice = compilar(GLES20.GL_VERTEX_SHADER, codigoVertice);
        int preenchimento = compilar(GLES20.GL_FRAGMENT_SHADER, codigoPreenchimento);
        SPRITE = GLES20.glCreateProgram();
        GLES20.glAttachShader(SPRITE, vertice);
        GLES20.glAttachShader(SPRITE, preenchimento);
        GLES20.glLinkProgram(SPRITE);
    }

    private static int compilar(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

}
