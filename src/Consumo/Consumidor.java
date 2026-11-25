
package src.Consumo;

import java.util.ArrayList;
import java.util.List;

public class Consumidor {

    private String nome;
    private List<EquipamentoConsumo> equipamentos;

    public Consumidor(String nome) {
        this.nome = nome;
        this.equipamentos = new ArrayList<>();
    }

    public void adicionarEquipamento(EquipamentoConsumo equipamento) {
        equipamentos.add(equipamento);
    }

    public double calcularConsumo() {

        double consumoTotal = 0;

        for (EquipamentoConsumo equipamento : equipamentos) {
            consumoTotal += equipamento.calcularConsumo();
        }

        return consumoTotal;
    }

    public String getNome() {
        return nome;
    }

    public List<EquipamentoConsumo> getEquipamentos() {
        return equipamentos;
    }
}
