
package src.Simulacao;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.LocalTime;

import src.Entidades.UnidadeGeradora;
import src.Consumo.Consumidor;
import src.Dados.Balanco;
import src.Repository.MedicaoRepository;

public class Simulador {

    private UnidadeGeradora unidade;
    private Consumidor consumidor;
    private GeradorDadosSimulados dados;
    private MedicaoRepository repository;

    private long idUnidade;
    private long idConsumidor;
    private double intervaloSegundos;

    public Simulador(
            UnidadeGeradora unidade,
            Consumidor consumidor,
            GeradorDadosSimulados dados,
            double intervaloSegundos,
            long idUnidade,
            long idConsumidor,
            MedicaoRepository repository) {

        if (intervaloSegundos <= 0) {
            throw new IllegalArgumentException("Intervalo inválido.");
        }

        this.unidade = unidade;
        this.consumidor = consumidor;
        this.dados = dados;
        this.intervaloSegundos = intervaloSegundos;
        this.idUnidade = idUnidade;
        this.idConsumidor = idConsumidor;
        this.repository = repository;
    }

    public void executar() throws SQLException {

        long intervaloMillis = (long) (intervaloSegundos * 1000);

        System.out.println("Iniciando simulação energética...");

        while (!Thread.currentThread().isInterrupted()) {

            OffsetDateTime fim = OffsetDateTime.now();

            OffsetDateTime inicio = fim.minusNanos(
                (long) (intervaloSegundos * 1_000_000_000L)
            );

            LocalTime horario = fim.toLocalTime();

            double potenciaGerada = unidade.calcularGeracao(
                dados, horario, intervaloSegundos
            );

            double potenciaConsumida = consumidor.calcularConsumo();

            double energiaGerada =
                potenciaGerada * intervaloSegundos / 3600.0;

            double energiaConsumida =
                potenciaConsumida * intervaloSegundos / 3600.0;

            Balanco balanco = new Balanco(
                potenciaGerada,
                potenciaConsumida
            );

            // Persistência das três medições em uma transação
            repository.salvarMedicao(
                idUnidade,
                idConsumidor,
                inicio,
                fim,
                potenciaGerada,
                energiaGerada,
                potenciaConsumida,
                energiaConsumida
            );

            double saldoEnergia = energiaGerada - energiaConsumida;

            System.out.println("\n========== MEDIÇÃO ==========");
            System.out.println("Data/Hora: " + fim);

            System.out.printf(
                "Geração: %.2f W | %.4f Wh%n",
                potenciaGerada, energiaGerada
            );

            System.out.printf(
                "Consumo: %.2f W | %.4f Wh%n",
                potenciaConsumida, energiaConsumida
            );

            System.out.printf(
                "Saldo: %.2f W | %.4f Wh%n",
                balanco.getSaldo(), saldoEnergia
            );

            System.out.printf(
                "Excedente: %.4f Wh | Déficit: %.4f Wh%n",
                Math.max(0, saldoEnergia),
                Math.max(0, -saldoEnergia)
            );

            System.out.println("Medições gravadas no PostgreSQL.");
            System.out.println("=============================");

            try {
                Thread.sleep(intervaloMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Simulação encerrada.");
    }
}
