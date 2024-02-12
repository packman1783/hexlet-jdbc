import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {
        // Соединение с базой данных тоже нужно отслеживать
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Tommy");
                preparedStatement.setString(2, "33333333");
                preparedStatement.executeUpdate();
                var generatedKeys1 = preparedStatement.getGeneratedKeys();
                if (generatedKeys1.next()) {
                    System.out.println(generatedKeys1.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }

                preparedStatement.setString(1, "Maria");
                preparedStatement.setString(2, "44444444");
                preparedStatement.executeUpdate();
                var generatedKeys2 = preparedStatement.getGeneratedKeys();
                if (generatedKeys2.next()) {
                    System.out.println(generatedKeys2.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }

            var sql3 = "SELECT * FROM users";
            try (var statement = conn.createStatement()) {
                var resultSet = statement.executeQuery(sql3);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }

            String deleteSql = "DELETE FROM users WHERE username = ?";
            try (var statement = conn.prepareStatement(deleteSql)) {
                statement.setString(1, "Maria");
                statement.executeUpdate();
            }
        }
    }
}