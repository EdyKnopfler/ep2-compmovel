package br.usp.ime.aet.opengl3;

import java.util.ArrayList;

public class Partida {

    public boolean finalizada = true;
    public boolean rolando = false;

    private ArrayList<Sprite> blocos;
    private ArrayList<Sprite> parede;
    private Sprite bola, pad;

    private float velocidadeX = 0f, velocidadeY = 0f;


    public void setParede(ArrayList<Sprite> parede) {
        this.parede = parede;
    }

    public void setBola(Sprite bola) {
        this.bola = bola;
    }

    public void setPad(Sprite pad) {
        this.pad = pad;
    }

    public void setBlocos(ArrayList<Sprite> blocos) {
        this.blocos = blocos;
    }

    public void iniciar() {
        finalizada = false;
        rolando = true;
        velocidadeX = 0.01f;
        velocidadeY = 0.01f;
    }

    public void processar() {
        if (finalizada || !rolando) return;

        bola.x += velocidadeX;
        bola.y += velocidadeY;
    }

    public void tocouTela(float x, float y) {
        if (finalizada || !rolando)
            iniciar();


    }

    public void moveuDedo(float x, float y) {
    }

    public void soltouDedo(float x, float y) {
    }
}
