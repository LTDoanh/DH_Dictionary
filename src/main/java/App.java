import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


public class App extends Application {
  private static final String FXML_PATH = "C:\\Users\\DELL\\IdeaProjects\\DH_Dictionary\\src\\main\\resources\\App.fxml";
  @FXML
  private TextField textField;
  @FXML
  private ListView<String> listView;
  @FXML
  private WebView webView;

  public static void runApplication(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Scene scene = new Scene(new FXMLLoader().load(new FileInputStream(FXML_PATH)));
    primaryStage.setScene(scene);
    primaryStage.setTitle("DH Dictionary");
    primaryStage.show();
    DatabaseConnection.readAllData();
    display(scene);

  }

  public void init(Scene scene) {
    this.textField = (TextField) scene.lookup("#textField");
    this.listView = (ListView<String>) scene.lookup("#listView");
    this.webView = (WebView) scene.lookup("#webView");
  }

  public void display(Scene scene) throws IOException {
    init(scene);
    textFieldAction();
    listViewAction();
    searchAction();
  }

  public void textFieldAction() {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      this.listView.getItems().clear();
      this.listView.getItems().addAll(Objects.requireNonNull(AppCommandline.dictionarySearcher(textField.getText().toLowerCase())));
      this.webView.getEngine().loadContent("");
    });
  }


  public void listViewAction() {
    this.listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> textField.setText(newValue.trim()));
  }

  @FXML
  public void searchAction() throws IOException {
    if (Dictionary.map.containsKey(textField.getText())) {
      this.webView.getEngine().loadContent(Dictionary.map.get(textField.getText()), "text/html");
    }
  }

  @FXML
  public void deleteAction() {
    //AlertBox.display1("Delete tool", "Enter a word you want to delete: ");
  }

  @FXML
  public void modifyAction() {
    //AlertBox.display2("Modify tool", "Enter target word", "Modify with explain word");
  }

  @FXML
  public void addAction() {
    //AlertBox.display2("Add tool", "Enter target word", "Enter explain word");
  }

  @FXML
  public void exitAction() {
    Platform.exit();
  }
}
