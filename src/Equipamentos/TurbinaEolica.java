
package src.Equipamentos;

import java.time.LocalTime;

import src.Simulacao.GeradorDadosSimulados;

public class TurbinaEolica extends Gerador {

    private double potenciaAtual;
    private double energiaAcumulada;

    private static final double VELOCIDADE_MINIMA = 3.0;
    private static final double VELOCIDADE_NOMINAL = 12.0;
    private static final double VELOCIDADE_MAXIMA = 25.0;

    public TurbinaEolica(double potenciaNominal) {
        super("Turbina Eólica", potenciaNominal);
        this.potenciaAtual = 0;
        this.energiaAcumulada = 0;
    }

    public double gerarEnergia(
            double velocidadeVento,
            double intervaloSegundos) {

        if (intervaloSegundos <= 0) {
            throw new IllegalArgumentException("Intervalo inválido.");
        }

        if (!ligado ||
                velocidadeVento < VELOCIDADE_MINIMA ||
                velocidadeVento >= VELOCIDADE_MAXIMA) {

            potenciaAtual = 0;
            return 0;
        }

        if (velocidadeVento >= VELOCIDADE_NOMINAL) {
            potenciaAtual = potenciaNominal;
        } else {
            double proporcao = velocidadeVento / VELOCIDADE_NOMINAL;

            potenciaAtual = potenciaNominal * Math.pow(proporcao, 3);
        }

        double energiaGerada =
                potenciaAtual * intervaloSegundos / 3600.0;

        energiaAcumulada += energiaGerada;

        return potenciaAtual;
    }
    @Override
public double simularGeracao(
        GeradorDadosSimulados dados,
        LocalTime horario,
        double intervaloSegundos) {

    double vento = dados.gerarVelocidadeVento();

    return gerarEnergia(vento, intervaloSegundos);
}

    public double getPotenciaAtual() {
        return potenciaAtual;
    }

    public double getEnergiaAcumulada() {
        return energiaAcumulada;
    }
}
