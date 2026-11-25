
package src.Banco;

// improtando a biblioteca .env
import io.github.cdimascio.dotenv.Dotenv;
// importanto a biblioteca do 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final Dotenv  dotenv = Dotenv.load();

    private static final String NOME_BANCO = dotenv.get("NOME_DO_BANCO");
    
    //Conexão com o banco de dados em máquina externa através de um túnel de vpn.
    //AVISO! Colocar essas informações na .env antes de upar para o Github

    private static final String IP_BANCO = dotenv.get("IP_BANCO");
        
    private static final String URL ="jdbc:postgresql://"+IP_BANCO+"/" + NOME_BANCO;

    private static final String USER = dotenv.get("USUARIO");

    private static final String PASSWORD = dotenv.get("SENHA");

    public static Connection conectar() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                "A variável de ambiente DB_PASSWORD não foi configurada."
            );
        }

        Connection conn = DriverManager.getConnection(
            URL, USER, PASSWORD
        );

        System.out.println(
            "Conectado ao PostgreSQL! Banco: " + NOME_BANCO
        );

        return conn;
    }
}
