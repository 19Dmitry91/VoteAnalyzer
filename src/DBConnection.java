import java.sql.*;
import java.text.ParseException;
import java.util.List;

public class DBConnection {
    private static final String URL = "jdbc:mysql://<HOST>:<PORT>/<DB_NAME>";
    private static final String USER = "YOUR_LOGIN";
    private static final String PASSWORD = "YOUR_PASSWORD";
    private static final String INSERT_QUERY = "INSERT INTO voter_count (name, birthDate) VALUES (?, ?)";

    private static Connection connection;
    private static DBConnection instance;

    static {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void multiInsertVoters(List<Voter> voterList) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO voter_count (name, birthDate) VALUES ");

        for (int i = 0; i < voterList.size(); i++) {
            Voter voter = voterList.get(i);
            sql.append("(?, ?)");
            if (i < voterList.size() - 1) {
                sql.append(", ");
            }
        }

        try (PreparedStatement statement = getConnection().prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            for (Voter voter : voterList) {
                statement.setString(parameterIndex++, voter.getName());
                java.util.Date utilDate = Voter.birthDayFormat.parse(voter.getBirthDayString());
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                statement.setDate(parameterIndex++, sqlDate);
            }
            statement.execute();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printVoterCounts() throws SQLException {
        String query = "SELECT name, birthDate, COUNT(*) FROM voter_count GROUP BY name, birthDate HAVING COUNT(*) > 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " +
                        resultSet.getString(2) + " - " +
                        resultSet.getInt(3));
            }
        }
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
