package dictionary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DictionaryController extends AppController {

  @FXML
  private TextField txtKeyword;
  @FXML
  private ListView<Word> lvWords;
  @FXML
  private TextArea taMeaning;
  @FXML
  private Button buttonSpeak;
  @FXML
  private Button buttonAdd;
  @FXML
  private Button buttonUpdate;
  @FXML
  private Button buttonDelete;
  @FXML
  private ImageView imageSpeak;
  @FXML
  private ImageView imageDelete;

  private List<Word> history = new ArrayList<>();

  private MediaPlayer mediaPlayer;
  private String text;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Thiết lập sự kiện cho ListView khi chọn một từ trong danh sách kết quả
    lvWords.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          // Nếu có từ được chọn, hiển thị nghĩa của từ đó lên TextArea và Text Field
          if (newValue != null) {
            taMeaning.setText(newValue.getDefinition());
            history.add(new Word("FIND", newValue));
            text = newValue.getTarget();
            buttonDelete.setDisable(false);
            imageDelete.getStyleClass().add("image-view-button");
            buttonSpeak.setDisable(false);
            imageSpeak.getStyleClass().add("image-view-button");
          }
        });
    // Thiết lập sự kiện cho Text Field khi nhập một từ bất kỳ
    txtKeyword.textProperty().addListener((observableValue, oldValue, newValue) -> {
      // Kiểm tra nếu từ không rỗng
      if (!newValue.isEmpty()) {
        // Truy vấn dữ liệu từ điển theo từ và lấy về danh sách kết quả
        List<Word> words = DatabaseConnection.select(newValue);
        // Hiển thị danh sách kết quả lên ListView
        lvWords.getItems().setAll(words);
        // Xóa hết dữ liệu trên TextArea
        taMeaning.clear();
        text = "";
      } else {
        // Nếu từ rỗng, xóa hết dữ liệu trên ListView và TextArea
        lvWords.getItems().clear();
        taMeaning.clear();
        text = "";
        buttonDelete.setDisable(true);
        imageDelete.setOpacity(0.2);
        imageDelete.getStyleClass().removeAll("image-view-button");
        buttonSpeak.setDisable(true);
        imageSpeak.setOpacity(0.2);
        imageSpeak.getStyleClass().removeAll("image-view-button");
      }
    });

    // Thiết lập sự kiện cho TextArea khi nhập nghĩa của từ
    taMeaning.textProperty().addListener((observableValue, oldValue, newValue) -> {
      // Kiểm tra nếu nghĩa không rỗng
      if (!newValue.isEmpty() && !text.equals("")) {
        buttonDelete.setDisable(false);
        imageDelete.getStyleClass().add("image-view-button");
        buttonSpeak.setDisable(false);
        imageSpeak.getStyleClass().add("image-view-button");
      } else {
        buttonDelete.setDisable(true);
        imageDelete.setOpacity(0.2);
        imageDelete.getStyleClass().removeAll("image-view-button");
        buttonSpeak.setDisable(true);
        imageSpeak.setOpacity(0.2);
        imageSpeak.getStyleClass().removeAll("image-view-button");
      }
    });
  }

  @FXML
  private void handleSpeak() {
    VoiceAPI v = new VoiceAPI();
    v.useAPI(text);
    Media media = new Media(new File(VoiceAPI.audioFilePath).toURI().toString());
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();
  }

  // Phương thức để xử lý sự kiện khi nhấn Button thêm từ
  @FXML
  private void handleAdd() {
    // Hiển thị hộp thoại xác nhận cho người dùng
    boolean confirm = showAlertConfirm("Are you sure you want to add this word?");
    if (confirm) {
      // Lấy giá trị của Text Field nhập từ khóa và TextArea nhập nghĩa của từ
      String word = txtKeyword.getText();
      String meaning = taMeaning.getText();
      // Kiểm tra nếu cả hai giá trị không rỗng
      if (!word.isEmpty() && !meaning.isEmpty()) {
        Word w = new Word(word, meaning);
        DatabaseConnection.add(w);
        history.add(new Word("ADD", w));
        // Hiển thị thông báo kết quả cho người dùng
        showAlert(AlertType.INFORMATION, "Add the word success!");
        // Cập nhật lại danh sách kết quả trên ListView
        lvWords.getItems().add(w);
      }
    }
  }

  // Phương thức để xử lý sự kiện khi nhấn Button cập nhật từ
  @FXML
  private void handleUpdate() {
    // Hiển thị hộp thoại xác nhận cho người dùng
    boolean confirm = showAlertConfirm("Are you sure you want to update this word?");
    if (confirm) {
      // Lấy giá trị của Text Field nhập từ khóa và TextArea nhập nghĩa của từ
      String word = txtKeyword.getText();
      String meaning = taMeaning.getText();
      // Lấy đối tượng Word được chọn trong ListView
      Word w = lvWords.getSelectionModel().getSelectedItem();
      // Kiểm tra nếu cả ba giá trị không rỗng
      if (!word.isEmpty() && !meaning.isEmpty() && w != null) {
        // Cập nhật giá trị của đối tượng Word
        w.setTarget(word);
        w.setDefinition(meaning);
        // Cập nhật đối tượng Word trong file SQL và lấy về kết quả thành công hay thất bại
        DatabaseConnection.update(w);
        history.add(new Word("UPDATE", w));
        // Hiển thị thông báo kết quả cho người dùng
        showAlert(AlertType.INFORMATION, "Update the word success!");
        // Cập nhật lại danh sách kết quả trên ListView
        lvWords.refresh();
      }
    }
  }

  // Phương thức để xử lý sự kiện khi nhấn Button xóa từ
  @FXML
  private void handleDelete() {
    // Hiển thị hộp thoại xác nhận cho người dùng
    boolean confirm = showAlertConfirm("Are you sure you want to delete this word?");
    if (confirm) {
      // Lấy đối tượng Word được chọn trong ListView
      Word w = lvWords.getSelectionModel().getSelectedItem();
      // Kiểm tra nếu có đối tượng Word được chọn
      if (w != null) {
        // Xóa đối tượng Word trong file SQL và lấy về kết quả thành công hay thất bại
        DatabaseConnection.delete(w);
        history.add(new Word("DELETE", w));
        // Hiển thị thông báo kết quả cho người dùng
        showAlert(AlertType.INFORMATION, "Delete the word success!");
        // Cập nhật lại danh sách kết quả trên ListView
        lvWords.getItems().remove(w);
      }
    }
  }

  // Phương thức để hiển thị hộp thoại xác nhận cho người dùng
  private boolean showAlertConfirm(String message) {
    // Tạo một đối tượng Alert với loại và nội dung được truyền vào
    Alert alert = new Alert(AlertType.CONFIRMATION, message);
    // Thiết lập tiêu đề và nút cho hộp thoại
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
    // Hiển thị hộp thoại lên màn hình và lấy về nút được nhấn
    Optional<ButtonType> result = alert.showAndWait();
    // Nếu nút được nhấn là YES, trả về true, ngược lại trả về false
    return result.orElse(ButtonType.NO) == ButtonType.YES;
  }

  public void showHistory() {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("History");
    String s = "";
    int j = 1;
    for (int i = history.size() - 1; i >= 0; i --) {
      s = s + j + ". " + history.get(i).getType() + " Word: " + history.get(i).getTarget() + "\n";
      j ++;
      if (j >= 15) {
        break;
      }
    }
    alert.setHeaderText(s);
    alert.setContentText("Đây là lịch sử hoạt động sử dụng gần đây của bạn.");
    alert.showAndWait();
  }
  // Phương thức để hiển thị thông báo cho người dùng
  private void showAlert(AlertType type, String message) {
    // Tạo một đối tượng Alert với loại và nội dung được truyền vào
    Alert alert = new Alert(type, message);
    // Hiển thị đối tượng Alert lên màn hình
    alert.showAndWait();
  }
}
