
package src.Equipamentos;

public abstract class Equipamentos {

    protected String tipo;
    protected double potenciaNominal;
    protected boolean ligado;

    public Equipamentos(String tipo, double potenciaNominal, boolean ligado) {

        if (potenciaNominal <= 0) {
            throw new IllegalArgumentException(
                "A potência nominal deve ser maior que zero."
            );
        }

        this.potenciaNominal = potenciaNominal;
        this.tipo = tipo;
        this.ligado = ligado;
    }

    public void ligarDesligar() {

        this.ligado = !this.ligado;

        if (this.ligado) {
            System.out.println("O equipamento " + tipo + " foi ligado!");
        } else {
            System.out.println("O equipamento " + tipo + " foi desligado!");
        }
    }

    public boolean isLigado() {
        return this.ligado;
    }

    public String getTipo() {
        return this.tipo;
    }

    public double getPotenciaNominal() {
        return this.potenciaNominal;
    }
}
