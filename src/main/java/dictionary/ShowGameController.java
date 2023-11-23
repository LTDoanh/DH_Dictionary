package dictionary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowGameController extends AppController {
  @FXML
  private Button gameButton1;
  @FXML
  private Button gameButton2;
  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  public void handleGame1() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("fxml/flipcard.fxml"));
      Stage window = (Stage) gameButton1.getScene().getWindow();
      window.setScene(new Scene(root));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void handleGame2() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("fxml/wordsearch.fxml"));
      Stage window = (Stage) gameButton2.getScene().getWindow();
      window.setScene(new Scene(root));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
