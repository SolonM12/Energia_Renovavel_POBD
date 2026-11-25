
package src.Simulacao;

import java.time.LocalTime;
import java.util.Random;

public class GeradorDadosSimulados {

    private final Random random;

    public GeradorDadosSimulados() {
        this.random = new Random();
    }

    // Permite repetir os mesmos resultados durante testes
    public GeradorDadosSimulados(long seed) {
        this.random = new Random(seed);
    }

    // Irradiância solar simulada em W/m²
    public double gerarIrradiancia(LocalTime horario) {

        int hora = horario.getHour();

        // Sem geração solar durante a noite
        if (hora < 6 || hora >= 18) {
            return 0;
        }

        // Curva simplificada: pico ao meio-dia
        double fatorSolar = Math.sin(
            Math.PI * (hora - 6) / 12.0
        );

        // Variação aleatória de 80% a 100%
        double variacao = 0.8 + random.nextDouble() * 0.2;

        return 1000 * fatorSolar * variacao;
    }

    // Velocidade do vento simulada em m/s
    public double gerarVelocidadeVento() {
        return 2 + random.nextDouble() * 16;
    }

    // Decide se um equipamento deve mudar seu estado
    public boolean deveAlternarEstado(double probabilidade) {
        return random.nextDouble() < probabilidade;
    }

    // Variação aleatória da potência de um equipamento
    public double gerarVariacaoPotencia() {
        return 0.7 + random.nextDouble() * 0.3;
    }

        // Temperatura simulada do painel solar em °C
    public double gerarTemperaturaPainel() {
        return gerarValor(25, 55);
    }

    // Gera um valor aleatório entre mínimo e máximo
    public double gerarValor(double minimo, double maximo) {

        if (maximo < minimo) {
            throw new IllegalArgumentException("Intervalo inválido.");
        }

        return minimo + random.nextDouble() * (maximo - minimo);
    }
}
