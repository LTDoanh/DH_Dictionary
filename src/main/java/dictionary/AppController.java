package dictionary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AppController extends Controller {
  @FXML
  private Button changeDicSceneButton;
  @FXML
  private Button changeTransSceneButton;
  @FXML
  private Button changePracSceneButton;
  @FXML
  private Button changeGameSceneButton;

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

  public void handleChangePracScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/practice.fxml"));
    Stage window = (Stage) changePracSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void handleChangeGameScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/game.fxml"));
    Stage window = (Stage) changeGameSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.initialize(url, resourceBundle);
  }
}
