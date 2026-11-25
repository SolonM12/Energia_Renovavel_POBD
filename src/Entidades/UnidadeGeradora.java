package src.Entidades;

import src.Equipamentos.Gerador;
import src.Simulacao.GeradorDadosSimulados;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UnidadeGeradora {

    private String nome;
    private List<Gerador> geradores;

    public UnidadeGeradora(String nome) {
        this.nome = nome;
        this.geradores = new ArrayList<>();
    }

    public void adicionarGerador(Gerador gerador) {
        geradores.add(gerador);
    }

    public double calcularGeracao(
            GeradorDadosSimulados dados,
            LocalTime horario,
            double intervaloSegundos) {

        double geracaoTotal = 0;

        for (Gerador gerador : geradores) {

            double potencia = gerador.simularGeracao(
                dados,
                horario,
                intervaloSegundos
            );

            geracaoTotal += potencia;
        }

        return geracaoTotal;
    }

    public String getNome() {
        return nome;
    }

    public List<Gerador> getGeradores() {
        return geradores;
    }
}
