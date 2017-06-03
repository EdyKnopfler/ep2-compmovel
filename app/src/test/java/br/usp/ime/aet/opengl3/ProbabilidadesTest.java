package br.usp.ime.aet.opengl3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Teste do cálculo de probabilidades dos tijolos */
public class ProbabilidadesTest {

    public double[][] niveis = new double[][] {
            // fácil -> difícil
            // A probabilidade é dada por nivel[i] / total
            {0.05, 0, 0},
            {0.1, 0.05, 0},
            {0.15, 0.1, 0.05},
            {0.2, 0.15, 0.1},
            {0.25, 0.2, 0.15},
            {0.3, 0.25, 0.2},
            {0.35, 0.3, 0.25},
            {0.4, 0.35, 0.3},
            {0.45, 0.4, 0.35},
            {0.5, 0.45, 0.4},
    };

    @Test
    public void calculaNiveisDeDificuldade() {
        double[] probabs = {0, 0, 0};
        for (int i = 0; i < niveis.length; i++) {
            recalcularDificuldade(probabs);
            for (int j = 0; j < 3; j++)
                assertEquals(niveis[i][j], probabs[j], 0.001);
        }
    }

    private void recalcularDificuldade(double[] probabs) {
        for (int i = probabs.length - 1; i >= 1; i--)
            probabs[i] = probabs[i-1];
        probabs[0] += 0.05;
    }

}
