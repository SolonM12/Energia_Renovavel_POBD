
package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import src.Banco.Conexao;
import src.Equipamentos.PlacaSolar;
import src.Equipamentos.TurbinaEolica;
import src.Entidades.UnidadeGeradora;
import src.Consumo.Consumidor;
import src.Consumo.EquipamentoConsumo;
import src.Simulacao.GeradorDadosSimulados;
import src.Simulacao.Simulador;
import src.Repository.MedicaoRepository;

public class App {

    public static void main(String[] args) {

        try (Connection conn = Conexao.conectar()) {

            // 1. Criar ou recuperar os cadastros
            long[] ids = criarCadastros(conn);

            long idUnidade = ids[1];
            long idConsumidor = ids[2];

            // 2. Criar gerador de dados simulados
            GeradorDadosSimulados dados = new GeradorDadosSimulados();

            // 3. Criar unidade geradora
            UnidadeGeradora unidade = new UnidadeGeradora("Residência");

            PlacaSolar placaSolar = new PlacaSolar(4000);
            TurbinaEolica turbina = new TurbinaEolica(3000);

            placaSolar.ligarDesligar();
            turbina.ligarDesligar();

            unidade.adicionarGerador(placaSolar);
            unidade.adicionarGerador(turbina);

            // 4. Criar consumidor residencial
            Consumidor consumidor = new Consumidor("Casa");

            consumidor.adicionarEquipamento(
                new EquipamentoConsumo("Geladeira", 500, 0.02)
            );

            consumidor.adicionarEquipamento(
                new EquipamentoConsumo("Televisão", 150, 0.10)
            );

            consumidor.adicionarEquipamento(
                new EquipamentoConsumo("Chuveiro", 5500, 0.05)
            );

            consumidor.adicionarEquipamento(
                new EquipamentoConsumo("Ar-condicionado", 1200, 0.08)
            );

            // 5. Inicializar simulador
            // A conexão e os IDs serão utilizados na próxima etapa
            MedicaoRepository repository = new MedicaoRepository(conn);

            Simulador simulador = new Simulador(
                unidade,
                consumidor,
                dados,
                5,
                idUnidade,
                idConsumidor,
                repository
            );

            simulador.executar();

            System.out.println("Unidade cadastrada: " + idUnidade);
            System.out.println("Consumidor cadastrado: " + idConsumidor);

            // 6. Executar simulação
            simulador.executar();

        } catch (SQLException e) {
            System.err.println("Erro no banco de dados:");
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("Erro na aplicação:");
            e.printStackTrace();
        }
    }

    private static long[] criarCadastros(Connection conn)
            throws SQLException {

        long idUsuario;
        long idUnidade;
        long idConsumidor;

        // =====================================
        // 1. USUÁRIO
        // =====================================

        String sqlUsuario = """
            INSERT INTO usuario (nome, email, senha)
            VALUES (?, ?, ?)
            ON CONFLICT (email) DO NOTHING
            """;

        try (PreparedStatement stmt =
                conn.prepareStatement(sqlUsuario)) {

            stmt.setString(1, "Usuario Simulador");
            stmt.setString(2, "simulador@exemplo.com");
            stmt.setString(3, "HASH_DE_TESTE");

            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM usuario WHERE email = ?")) {

            stmt.setString(1, "simulador@exemplo.com");

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Usuário não encontrado.");
                }

                idUsuario = rs.getLong("id");
            }
        }

        // =====================================
        // 2. UNIDADE GERADORA
        // =====================================

        String sqlUnidade = """
            INSERT INTO unidade_geradora (id_usuario, cnpj)
            VALUES (?, ?)
            ON CONFLICT (cnpj) DO NOTHING
            """;

        try (PreparedStatement stmt =
                conn.prepareStatement(sqlUnidade)) {

            stmt.setLong(1, idUsuario);
            stmt.setString(2, "00000000000191");

            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement("""
                SELECT id_unidade
                FROM unidade_geradora
                WHERE cnpj = ? AND id_usuario = ?
                """)) {

            stmt.setString(1, "00000000000191");
            stmt.setLong(2, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Unidade não encontrada.");
                }

                idUnidade = rs.getLong("id_unidade");
            }
        }

        // =====================================
        // 3. CONSUMIDOR
        // =====================================

        String sqlConsumidor = """
            INSERT INTO consumidor (id_unidade, nome)
            VALUES (?, ?)
            ON CONFLICT (id_unidade) DO NOTHING
            """;

        try (PreparedStatement stmt =
                conn.prepareStatement(sqlConsumidor)) {

            stmt.setLong(1, idUnidade);
            stmt.setString(2, "Residência");

            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement("""
                SELECT id_consumidor
                FROM consumidor
                WHERE id_unidade = ?
                """)) {

            stmt.setLong(1, idUnidade);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Consumidor não encontrado.");
                }

                idConsumidor = rs.getLong("id_consumidor");
            }
        }

        System.out.println("\n===== CADASTROS =====");
        System.out.println("Usuário: " + idUsuario);
        System.out.println("Unidade: " + idUnidade);
        System.out.println("Consumidor: " + idConsumidor);
        System.out.println("=====================\n");

        return new long[] {
            idUsuario,
            idUnidade,
            idConsumidor
        };
    }
}
