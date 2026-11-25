
package src.Consumo;

import java.util.Random;

public class EquipamentoConsumo {

    private String nome;
    private double potenciaNominal;
    private double probabilidadeUso;

    private boolean ligado;
    private Random random;

    public EquipamentoConsumo(
            String nome,
            double potenciaNominal,
            double probabilidadeUso) {

        this.nome = nome;
        this.potenciaNominal = potenciaNominal;
        this.probabilidadeUso = probabilidadeUso;

        this.random = new Random();
        this.ligado = random.nextBoolean();
    }

    public double calcularConsumo() {

        // Decide aleatoriamente se o equipamento liga ou desliga
        if (random.nextDouble() < probabilidadeUso) {
            ligado = !ligado;
        }

        if (!ligado) {
            return 0;
        }

        // Varia a potência entre 70% e 100% da potência nominal
        double variacao = 0.7 + (random.nextDouble() * 0.3);

        return potenciaNominal * variacao;
    }

    public String getNome() {
        return nome;
    }

    public boolean isLigado() {
        return ligado;
    }
}
