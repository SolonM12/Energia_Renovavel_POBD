
package src.Equipamentos;

import java.time.LocalTime;

import src.Simulacao.GeradorDadosSimulados;

public class PlacaSolar extends Gerador {

    private double potenciaAtual;
    private double energiaAcumulada;

    private static final double COEFICIENTE_TEMPERATURA = -0.004;

    public PlacaSolar(double potenciaNominal) {
        super("Placa Solar", potenciaNominal);
        this.potenciaAtual = 0;
        this.energiaAcumulada = 0;
    }

    public double gerarEnergia(
            double irradiancia,
            double temperatura,
            double intervaloSegundos) {

        if (intervaloSegundos <= 0) {
            throw new IllegalArgumentException("Intervalo inválido.");
        }

        if (!ligado || irradiancia <= 0) {
            potenciaAtual = 0;
            return 0;
        }

        double fatorIrradiancia = irradiancia / 1000.0;

        double fatorTemperatura =
                1 + COEFICIENTE_TEMPERATURA * (temperatura - 25);

        fatorTemperatura = Math.max(0, fatorTemperatura);

        potenciaAtual = potenciaNominal
                * fatorIrradiancia
                * fatorTemperatura;

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

    double irradiancia = dados.gerarIrradiancia(horario);
    double temperatura = dados.gerarTemperaturaPainel();

    return gerarEnergia(
        irradiancia,
        temperatura,
        intervaloSegundos
    );
}

    public double getPotenciaAtual() {
        return potenciaAtual;
    }

    public double getEnergiaAcumulada() {
        return energiaAcumulada;
    }
}
