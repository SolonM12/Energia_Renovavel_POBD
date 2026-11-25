
package src.Dados;

public class Balanco {

    private double potenciaGerada;
    private double potenciaConsumida;
    private double saldo;

    public Balanco(double potenciaGerada, double potenciaConsumida) {
        this.potenciaGerada = potenciaGerada;
        this.potenciaConsumida = potenciaConsumida;
        calcularSaldo();
    }

    private void calcularSaldo() {
        this.saldo = potenciaGerada - potenciaConsumida;
    }

    public double getPotenciaGerada() {
        return potenciaGerada;
    }

    public double getPotenciaConsumida() {
        return potenciaConsumida;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getExcedente() {
        return Math.max(0, saldo);
    }

    public double getDeficit() {
        return Math.max(0, -saldo);
    }
}
