package dictionary;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DictionaryController implements Initializable {

  @FXML
  private Button changeParaSceneButton;
  @FXML
  private Button changeChallengeSceneButton;
  @FXML
  private TextField txtKeyword;
  @FXML
  private ListView<Word> lvWords;
  @FXML
  private ComboBox<String> cbLanguage;
  @FXML
  private TextArea taMeaning;
  @FXML
  private Button buttonPlay;
  @FXML
  private Button buttonAdd;
  @FXML
  private Button buttonUpdate;
  @FXML
  private Button buttonDelete;

  private MediaPlayer mediaPlayer;
  private String text;

  private DatabaseConnection connect;

  public DictionaryController() {
    connect = new DatabaseConnection();
    connect.getConnection();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Thiết lập các giá trị cho ComboBox chọn ngôn ngữ
    cbLanguage.getItems().addAll("Eng - Vie", "Vie - Eng");
    cbLanguage.getSelectionModel().select(0);

    // Thiết lập sự kiện cho ListView khi chọn một từ trong danh sách kết quả
    lvWords.getSelectionModel().selectedItemProperty()
            .addListener((observableValue, oldValue, newValue) -> {
              // Nếu có từ được chọn, hiển thị nghĩa của từ đó lên TextArea và Text Field
              if (newValue != null) {
                taMeaning.setText(newValue.getDefinition());
                text = newValue.getTarget();
                buttonDelete.setDisable(false);
              }
            });
    // Thiết lập sự kiện cho Text Field khi nhập một từ bất kỳ
    txtKeyword.textProperty().addListener((observableValue, oldValue, newValue) -> {
      // Kiểm tra nếu từ không rỗng
      if (!newValue.isEmpty()) {
        // Truy vấn dữ liệu từ điển theo từ và lấy về danh sách kết quả
        List<Word> words = connect.select(newValue);
        // Hiển thị danh sách kết quả lên ListView
        lvWords.getItems().setAll(words);
        // Xóa hết dữ liệu trên TextArea
        taMeaning.clear();
        text = "";
        if (text.equals("")) {
          // Kích hoạt Button thêm
          buttonAdd.setDisable(false);
        } else {
          // Kích hoạt Button thêm
          buttonAdd.setDisable(true);
        }
      } else {
        // Nếu từ rỗng, xóa hết dữ liệu trên ListView và TextArea
        lvWords.getItems().clear();
        taMeaning.clear();
        text = "";
        buttonAdd.setDisable(true);
        buttonUpdate.setDisable(true);
        buttonDelete.setDisable(true);
      }
    });

    // Thiết lập sự kiện cho TextArea khi nhập nghĩa của từ
    taMeaning.textProperty().addListener((observableValue, oldValue, newValue) -> {
      // Kiểm tra nếu nghĩa không rỗng
      if (!newValue.isEmpty() && !text.equals("")) {
        buttonDelete.setDisable(false);
        // Kiểm tra nếu nghĩa của từ được thay đổi
        if (!newValue.equals(oldValue)) {
          buttonUpdate.setDisable(false);
        } else {
          buttonUpdate.setDisable(true);
        }
      } else {
        buttonUpdate.setDisable(true);
        buttonDelete.setDisable(true);
      }
    });
  }

  @FXML
  private void handlePlay() {
    Voice.engTextToVoice(text);
    Media media = new Media(new File(Voice.audioFilePath).toURI().toString());
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
        // Tạo một đối tượng Word từ hai giá trị này
        Word w = new Word(word, meaning);
        // Thêm đối tượng Word vào file SQL và lấy về kết quả thành công hay thất bại
        boolean result = connect.insert(w);
        // Hiển thị thông báo kết quả cho người dùng
        if (result) {
          showAlert(Alert.AlertType.INFORMATION, "Add the word success!");
          // Cập nhật lại danh sách kết quả trên ListView
          lvWords.getItems().add(w);
        } else {
          showAlert(Alert.AlertType.ERROR, "Add the word failure!");
        }
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
        boolean result = connect.update(w);
        // Hiển thị thông báo kết quả cho người dùng
        if (result) {
          showAlert(AlertType.INFORMATION, "Update the word success!");
          // Cập nhật lại danh sách kết quả trên ListView
          lvWords.refresh();
        } else {
          showAlert(AlertType.ERROR, "Update the word failure!");
        }
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
        boolean result = connect.delete(w);
        // Hiển thị thông báo kết quả cho người dùng
        if (result) {
          showAlert(AlertType.INFORMATION, "Delete the word success!");
          // Cập nhật lại danh sách kết quả trên ListView
          lvWords.getItems().remove(w);
        } else {
          showAlert(AlertType.ERROR, "Delete the word failure!");
        }
      }
    }
  }

  // Phương thức để hiển thị hộp thoại xác nhận cho người dùng
  private boolean showAlertConfirm(String message) {
    // Tạo một đối tượng Alert với loại và nội dung được truyền vào
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
    // Thiết lập tiêu đề và nút cho hộp thoại
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
    // Hiển thị hộp thoại lên màn hình và lấy về nút được nhấn
    Optional<ButtonType> result = alert.showAndWait();
    // Nếu nút được nhấn là YES, trả về true, ngược lại trả về false
    return result.orElse(ButtonType.NO) == ButtonType.YES;
  }

  // Phương thức để hiển thị thông báo cho người dùng
  private void showAlert(Alert.AlertType type, String message) {
    // Tạo một đối tượng Alert với loại và nội dung được truyền vào
    Alert alert = new Alert(type, message);
    // Hiển thị đối tượng Alert lên màn hình
    alert.showAndWait();
  }

  public void handleChangeParaScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/translate.fxml"));
    Stage window = (Stage) changeParaSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void handleChangeChallengeScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/challenge.fxml"));
    Stage window = (Stage) changeChallengeSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }
}
