package src.Equipamentos;

import src.Simulacao.GeradorDadosSimulados;
import java.time.LocalTime;

public abstract class Gerador extends Equipamentos {

    public Gerador(String tipo, double potenciaNominal) {
        super(tipo, potenciaNominal, false);
    }

    public abstract double simularGeracao(
        GeradorDadosSimulados dados,
        LocalTime horario,
        double intervaloSegundos
    );
}
