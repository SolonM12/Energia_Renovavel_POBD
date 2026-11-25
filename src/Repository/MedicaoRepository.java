
package src.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class MedicaoRepository {

    private final Connection conn;

    public MedicaoRepository(Connection conn) {
        this.conn = conn;
    }

    public void salvarMedicao(
            long idUnidade,
            long idConsumidor,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            double potenciaGerada,
            double energiaGerada,
            double potenciaConsumida,
            double energiaConsumida
    ) throws SQLException {

        double saldoPotencia = potenciaGerada - potenciaConsumida;
        double excedentePotencia = Math.max(0, saldoPotencia);
        double deficitPotencia = Math.max(0, -saldoPotencia);

        double saldoEnergia = energiaGerada - energiaConsumida;
        double excedenteEnergia = Math.max(0, saldoEnergia);
        double deficitEnergia = Math.max(0, -saldoEnergia);

        boolean autoCommitOriginal = conn.getAutoCommit();

        try {
            conn.setAutoCommit(false);

            salvarEnergia(
                idUnidade, inicio, fim,
                potenciaGerada, energiaGerada
            );

            salvarConsumo(
                idConsumidor, inicio, fim,
                potenciaConsumida, energiaConsumida
            );

            salvarBalanco(
                idUnidade, inicio, fim,
                potenciaGerada, potenciaConsumida,
                saldoPotencia, excedentePotencia, deficitPotencia,
                energiaGerada, energiaConsumida,
                saldoEnergia, excedenteEnergia, deficitEnergia
            );

            conn.commit();

        } catch (SQLException e) {

            conn.rollback();
            throw e;

        } finally {
            conn.setAutoCommit(autoCommitOriginal);
        }
    }

    private void salvarEnergia(
            long idUnidade,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            double potencia,
            double energia
    ) throws SQLException {

        String sql = """
            INSERT INTO medicao_energia (
                id_unidade,
                data_inicio,
                data_hora,
                potencia_gerada_w,
                energia_gerada_wh
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUnidade);
            stmt.setObject(2, inicio);
            stmt.setObject(3, fim);
            stmt.setDouble(4, potencia);
            stmt.setDouble(5, energia);
            stmt.executeUpdate();
        }
    }

    private void salvarConsumo(
            long idConsumidor,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            double potencia,
            double energia
    ) throws SQLException {

        String sql = """
            INSERT INTO medicao_consumo (
                id_consumidor,
                data_inicio,
                data_hora,
                potencia_consumida_w,
                energia_consumida_wh
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idConsumidor);
            stmt.setObject(2, inicio);
            stmt.setObject(3, fim);
            stmt.setDouble(4, potencia);
            stmt.setDouble(5, energia);
            stmt.executeUpdate();
        }
    }

    private void salvarBalanco(
            long idUnidade,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            double potenciaGerada,
            double potenciaConsumida,
            double saldoPotencia,
            double excedentePotencia,
            double deficitPotencia,
            double energiaGerada,
            double energiaConsumida,
            double saldoEnergia,
            double excedenteEnergia,
            double deficitEnergia
    ) throws SQLException {

        String sql = """
            INSERT INTO balanco_energetico (
                id_unidade,
                data_inicio,
                data_hora,
                potencia_gerada_w,
                potencia_consumida_w,
                saldo_potencia_w,
                excedente_potencia_w,
                deficit_potencia_w,
                energia_gerada_wh,
                energia_consumida_wh,
                saldo_energia_wh,
                excedente_energia_wh,
                deficit_energia_wh
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUnidade);
            stmt.setObject(2, inicio);
            stmt.setObject(3, fim);
            stmt.setDouble(4, potenciaGerada);
            stmt.setDouble(5, potenciaConsumida);
            stmt.setDouble(6, saldoPotencia);
            stmt.setDouble(7, excedentePotencia);
            stmt.setDouble(8, deficitPotencia);
            stmt.setDouble(9, energiaGerada);
            stmt.setDouble(10, energiaConsumida);
            stmt.setDouble(11, saldoEnergia);
            stmt.setDouble(12, excedenteEnergia);
            stmt.setDouble(13, deficitEnergia);
            stmt.executeUpdate();
        }
    }
}
