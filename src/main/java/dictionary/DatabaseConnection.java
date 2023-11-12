package dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

  // Đối tượng Connection để kết nối với cơ sở dữ liệu
  public Connection connection;

  // Phương thức khởi tạo để khởi tạo đối tượng Connection
  public Connection getConnection() {
    String databaseName = "example";
    String username = "root";
    String password = "doanh2004";
    String url = "jdbc:mysql://localhost/" + databaseName;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      // Tạo đối tượng Connection bằng cách sử dụng URL, username và password của cơ sở dữ liệu
      connection = DriverManager.getConnection(url, username, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return connection;
  }

  // Phương thức để truy vấn dữ liệu từ điển theo từ khóa
  public List<Word> select(String keyword) {
    // Tạo một danh sách để lưu trữ kết quả truy vấn
    List<Word> words = new ArrayList<>();
    try {
      // Tạo một đối tượng PreparedStatement để thực thi câu lệnh SQL
      PreparedStatement ps = connection.prepareStatement(
          "SELECT * FROM dictionary WHERE target LIKE ?");
      // Thiết lập tham số cho câu lệnh SQL
      ps.setString(1, keyword + "%");
      // Thực thi câu lệnh SQL và lấy về đối tượng ResultSet chứa kết quả truy vấn
      ResultSet rs = ps.executeQuery();
      // Duyệt qua các bản ghi trong ResultSet và thêm vào danh sách words
      while (rs.next()) {
        // Lấy giá trị của các cột trong bản ghi hiện tại
        int id = rs.getInt("id");
        String target = rs.getString("target");
        String definition = rs.getString("definition");
        // Tạo một đối tượng Word từ các giá trị này
        Word w = new Word(id, target, definition);
        // Thêm đối tượng Word vào danh sách words
        words.add(w);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // Trả về danh sách words
    return words;
  }

  // Phương thức để truy vấn 5 dữ liệu từ điển một cách ngẫu nhiên
  public List<Word> random() {
    // Tạo một danh sách để lưu trữ kết quả truy vấn
    List<Word> words = new ArrayList<>();
    try {
      // Tạo một đối tượng PreparedStatement để thực thi câu lệnh SQL
      PreparedStatement ps = connection.prepareStatement("SELECT * FROM dictionary ORDER BY RAND() LIMIT ?");
      // Thiết lập tham số cho câu lệnh SQL
      ps.setInt(1, 5);
      // Thực thi câu lệnh SQL và lấy về đối tượng ResultSet chứa kết quả truy vấn
      ResultSet rs = ps.executeQuery();
      // Duyệt qua các bản ghi trong ResultSet và thêm vào danh sách words
      while (rs.next()) {
        // Lấy giá trị của các cột trong bản ghi hiện tại
        int id = rs.getInt("id");
        String target = rs.getString("target");
        String definition = rs.getString("definition");
        // Tạo một đối tượng Word từ các giá trị này
        Word w = new Word(id, target, definition);
        // Thêm đối tượng Word vào danh sách words
        words.add(w);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // Trả về danh sách words
    return words;
  }


  public List<Word> selectRandom() {
    // Tạo một danh sách để lưu trữ kết quả truy vấn
    List<Word> words = new ArrayList<>();
    try {
      // Tạo một đối tượng PreparedStatement để thực thi câu lệnh SQL
      PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM dictionary ORDER BY RAND() LIMIT ? ;");
      ps.setInt(1, 8);
      ResultSet rs = ps.executeQuery();
      // Duyệt qua các bản ghi trong ResultSet và thêm vào danh sách words
      while (rs.next()) {
        // Lấy giá trị của các cột trong bản ghi hiện tại
        int id = rs.getInt("id");
        String target = rs.getString("target");
        String definition = rs.getString("definition");
        // Tạo một đối tượng Word từ các giá trị này
        Word w = new Word(id, target, definition);
        // Thêm đối tượng Word vào danh sách words
        words.add(w);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // Trả về danh sách words
    return words;
  }

  // Phương thức để thêm dữ liệu từ điển vào file SQL
  public boolean insert(Word word) {
    try {
      // Tạo một đối tượng PreparedStatement để thực thi câu lệnh SQL
      PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO dictionary(target, definition) VALUES(?, ?)");
      // Thiết lập tham số cho câu lệnh SQL
      ps.setString(1, word.getTarget());
      ps.setString(2, word.getDefinition());
      // Thực thi câu lệnh SQL và trả về số bản ghi bị ảnh hưởng
      int result = ps.executeUpdate();
      // Nếu số bản ghi bị ảnh hưởng là 1, trả về true, ngược lại trả về false
      return result == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  // Phương thức để cập nhật dữ liệu từ điển trong file SQL
  public boolean update(Word word) {
    try {
      // Tạo một đối tượng PreparedStatement để thực thi câu lệnh SQL
      PreparedStatement ps = connection.prepareStatement(
          "UPDATE dictionary SET target = ?, definition = ? WHERE id = ?");
      // Thiết lập tham số cho câu lệnh SQL
      ps.setString(1, word.getTarget());
      ps.setString(2, word.getDefinition());
      ps.setInt(3, word.getId());
      // Thực thi câu lệnh SQL và trả về số bản ghi bị ảnh hưởng
      int result = ps.executeUpdate();
      // Nếu số bản ghi bị ảnh hưởng là 1, trả về true, ngược lại trả về false
      return result == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  // Phương thức để xóa dữ liệu từ điển trong file SQL
  public boolean delete(Word word) {
    try {
      // Tạo một đối tượng PreparedStatement để thực thi câu lệnh SQL
      PreparedStatement ps = connection.prepareStatement("DELETE FROM dictionary WHERE id = ?");
      // Thiết lập tham số cho câu lệnh SQL
      ps.setInt(1, word.getId());
      // Thực thi câu lệnh SQL và trả về số bản ghi bị ảnh hưởng
      int result = ps.executeUpdate();
      // Nếu số bản ghi bị ảnh hưởng là 1, trả về true, ngược lại trả về false
      return result == 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
