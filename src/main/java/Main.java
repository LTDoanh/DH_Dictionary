import java.sql.SQLException;

public class Main {
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    DatabaseConnection.connect();
    DatabaseConnection.readAllData();
  }
}
