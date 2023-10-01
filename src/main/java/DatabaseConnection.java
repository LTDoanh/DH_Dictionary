import java.sql.*;

public class DatabaseConnection {


  /** Phương thức kết nối với cơ sở dữ liệu SQLite */
  public static Connection connect() throws ClassNotFoundException, SQLException {
    // Đường dẫn kết nối cơ sở dữ liệu
    String url = "jdbc:sqlite:database/Dictionary.db";
    Connection conn = DriverManager.getConnection(url);
    return conn;
  }

 /** Phương thức đọc dữ liệu từ bảng av trong cơ sở dữ liệu
   * sau đó lưu nó vàng một HashMap trong lớp Dictionary
   */
  public static void readAllData() throws ClassNotFoundException, SQLException {
    Connection conn = connect();
    String sql = "SELECT * FROM av";
    PreparedStatement statement = conn.prepareStatement(sql);
    ResultSet result = statement.executeQuery();
    while (result.next()) {
      String wordTarget = result.getString("word");
      String wordExplain = result.getString("html");
      Dictionary.map.put(wordTarget, wordExplain);
    }
    result.close();
    statement.close();
    conn.close();
  }

  /** Phương thức chèn một hàng mới vào bảng av */
  public static void insertData(String word_target, String word_explain)
      throws ClassNotFoundException, SQLException {
    Connection conn = connect();
    String sql = "INSERT INTO av VALUES(?,?)";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setString(1, word_target);
    statement.setString(2, word_explain);
    statement.execute();
    statement.close();
    conn.close();
  }

  /** Phương thức xóa một hàng vào bảng av */
  public static void deleteData(String word_target) throws ClassNotFoundException, SQLException {
    Connection conn = connect();
    String sql = "DELETE FROM av WHERE word == ?";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setString(1, word_target);
    statement.execute();
    statement.close();
    conn.close();
  }

  /** Cập nhập nghĩa của một từ */
  public static void updateData(String word_target, String word_explain)
      throws ClassNotFoundException, SQLException {
    Connection conn = connect();
    String sql = "UPDATE av SET html = ? WHERE word == ?";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setString(1, word_explain);
    statement.setString(2, word_target);
    statement.execute();
    statement.close();
    conn.close();
  }
}