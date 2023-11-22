package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PracticeController {

  @FXML
  private Button changeDicSceneButton;
  @FXML
  private Button changeTransSceneButton;

  @FXML
  Button flashcard;

  @FXML
  private GridPane grid;

  private final String fileName = "C:\\Users\\DELL\\Downloads\\demo2\\src\\main\\resources\\dictionary\\data\\flashcards.txt";

  private int count = 0; // Biến đếm số button

  public void initialize() {
    // Đọc file text chứa thông tin các button đã được tạo sẵn
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      // Đọc từng dòng trong file
      String line;
      while ((line = br.readLine()) != null) {
        // Tách dòng thành các phần tử bằng dấu |
        String[] parts = line.split("\\|");
        // Lấy số lượng, tên và đường dẫn của button
        int number = Integer.parseInt(parts[0]);
        String name = parts[1];
        String link = parts[2];
        // Tạo button mới và thêm vào lưới
        Button button = new Button(name);
        button.setOnAction(e -> handleChoiceFlashcard(e, link));
        // Chia lưới thành 3 cột
        int col = count % 3;
        // Tính số hàng dựa trên số cột
        int row = count / 3;
        // Thêm button vào lưới ở vị trí tương ứng
        grid.add(button, col, row);
        // Đặt kích thước cho button
        button.setStyle("css/practice.css");
        button.getStyleClass().add("flash-card");
        // Tăng biến đếm lên 1
        count++;
      }
    } catch (IOException e) {
      // Xử lý ngoại lệ
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Phương thức xử lý sự kiện cho button thêm
  @FXML
  private void handleAddButton(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog();
    // Đặt tiêu đề cho thông báo
    dialog.setTitle("Thêm button");
    // Đặt nội dung cho thông báo
    dialog.setContentText("Nhập tên cho button:");
    // Hiển thị thông báo và lấy kết quả
    String name = dialog.showAndWait().orElse("");
    // Nếu không nhập gì, thì đặt tên mặc định cho button
    if (name.isBlank()) {
      name = "Flashcard #" + (count + 1);
    }
    // Tạo flashcard mới và thêm vào lưới
    Button button = new Button(name);
    // Chia lưới thành 3 cột
    int col = count % 3;
    // Tính số hàng dựa trên số cột
    int row = count / 3;
    // Thêm flashcard vào lưới ở vị trí tương ứng, bỏ qua vị trí của nút thêm
    grid.add(button, col, row);
    button.setStyle("css/practice.css");
    button.getStyleClass().add("flash-card");
    // Tạo đường dẫn cho button mới
    String link = "flashcard" + (count + 1) + ".txt";
    // Đặt sự kiện cho button
    button.setOnAction(e -> handleChoiceFlashcard(e, link));
    // Ghi thông tin của button mới vào file text
    try (FileWriter fw = new FileWriter(fileName, true)) {
      // Tạo dòng chứa số lượng, tên và đường dẫn của button mới
      String line = "0|" + name + "|" + link + "\n";
      // Ghi dòng vào file
      fw.write(line);
    } catch (IOException e) {
      // Xử lý ngoại lệ
      e.printStackTrace();
    }
    count++;
  }

  // Phương thức xử lý sự kiện khi chọn button bất kỳ trong lưới
  private void handleChoiceFlashcard(Event event, String link) {
    String fullLink =
        "C:\\Users\\DELL\\Downloads\\demo2\\src\\main\\resources\\dictionary\\data\\" + link;
    // Đọc dữ liệu từ file link và in ra text area
    try (BufferedReader br = new BufferedReader(new FileReader(fullLink))) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/flashcard.fxml"));
      Parent root = loader.load();
      FlashcardController controller = loader.getController();
      controller.setFileName(fullLink);
      controller.init();
      Node node = (Node) event.getSource();
      Stage window = (Stage) node.getScene().getWindow();
      Scene scene = new Scene(root);
      scene.setCamera(new PerspectiveCamera());
      window.setScene(scene);
    } catch (IOException e) {
      // Tạo file mới ở vị trí theo đường dẫn link
      try (FileWriter fw = new FileWriter(fullLink)) {
        // Ghi nội dung mặc định vào file
        fw.write("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/flashcard.fxml"));
        Parent root = loader.load();
        FlashcardController controller = loader.getController();
        controller.setFileName(fullLink);
        controller.init();
        Node node = (Node) event.getSource();
        Stage window = (Stage) node.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
        window.setScene(scene);
      } catch (IOException e1) {
        // Xử lý ngoại lệ
        e1.printStackTrace();
      }
    }
  }


  public void handleChangeDicScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/dictionary.fxml"));
    Stage window = (Stage) changeDicSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void handleChangeTransScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/translate.fxml"));
    Stage window = (Stage) changeTransSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }
}
